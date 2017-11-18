package cn.jishiyu11.xjsjd.activity.IdentityCheck;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage.OperatorActivity;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.control.MyTextView;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.PublicPhoneDialog;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/27.
 * <p>
 * 运营商验证   芝麻认证
 */

public class PersonalData3 extends BaseActivity implements View.OnClickListener, DataCallBack {

    private TextView operator_no_authorization;

    private MyTextView operator_no_zhimafen;

    private String code = "";

    private String operator = "";

    private CreditApp creditApp;

    private Intent intent = null;

    private String username;

    private boolean zmfGetAuth = false;
    private boolean yysGetAuth = false;
    public static Activity PERSONALDATAS3 = null;
    private String IDCardNo = "";
    private String ReadName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_data_operator);
//        operator = getIntent().getExtras().getString("operator");
        findViewById();
        PERSONALDATAS3 = this;
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
        creditApp = CreditApp.getOrCreateInstance(this.getApplicationContext());
        username = SharedPreferencesUtils.get(this, "username", "").toString();
        initView();
//        getAuthStates();//从后台获取运营商和芝麻分授权结果
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.operator_no_authorization_linear:
            case R.id.operator_no_authorization:
                if (!yysGetAuth) {//运营商
                    startActivityForResult(new Intent(PersonalData3.this,
                            OperatorActivity.class), 1001);
                } else {
                    ToatUtils.showShort1(this, "已获得授权!");
                }
                break;
            case R.id.title_image:
                intent = new Intent();
                intent.putExtra("complete", "2");
                setResult(106, intent);
                finish();
                break;
            case R.id.title_complete:
                intent = new Intent();
                intent.putExtra("complete", "1");
                setResult(106, intent);
                finish();
                break;
            case R.id.operator_no_zhimafen_line://芝麻分
                if (!zmfGetAuth) {
                    getSesameCredit();
                } else {
                    ToatUtils.showShort1(this, "已获得授权!");
                }
                break;
            case R.id.personal3_data_next://下一步
                if (yysGetAuth && zmfGetAuth) {
                    Intent intent = new Intent(this, PersonalData4.class);
                    this.startActivity(intent);
//                    finish();
                }
                break;
        }

    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
//        findViewById(R.id.title_complete).setVisibility(View.VISIBLE);
//        findViewById(R.id.title_complete).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_loan_personal_data_operator));

        operator_no_authorization = findViewById(R.id.operator_no_authorization);
        operator_no_authorization.setOnClickListener(this);
        if (operator.equals(this.getString(R.string.name_loan_personal_data_complete))) {
            operator_no_authorization.setText(this.getString(R.string.name_loan_operator_authorization));
        } else {
            operator_no_authorization.setText(this.getString(R.string.name_loan_operator_no_authorization));
        }

        operator_no_zhimafen = findViewById(R.id.operator_no_zhimafen);

        findViewById(R.id.operator_no_zhimafen_line).setOnClickListener(this);
        findViewById(R.id.personal3_data_next).setOnClickListener(this);
        findViewById(R.id.operator_no_authorization_linear).setOnClickListener(this);
    }

    @Override
    protected void initView() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
//        getAuthStates();
        getAuthStates();//从后台获取运营商和芝麻分授权结果
    }



    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            String datastring = data.getExtras().getString("operator");
            if (!TextUtils.isEmpty(datastring)) {
                if (datastring.equals("1")) {
                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_authorization));
                    postYYSAuthStatus();
                } else {
                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_no_authorization));
                }
            }
        }else {
            String datastring = data.getExtras().getString("operator");
            Bundle bundle = data.getExtras();


            if (!TextUtils.isEmpty(datastring)) {
                if (datastring.equals("1")) {
                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_authorization));
                    postYYSAuthStatus();
                } else {
                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_no_authorization));
                }
            }
        }
    }

//    //获取用户活体信息
//    private void getUSERDATAFORLIVE(){
//        Map map = new HashMap();
//        map.put("uid",SharedPreferencesUtils.get(this,"uid","")+"");
//        OkHttpManager.postAsync(HttpURL.getInstance().DATA_FOR_LIVE,"getUSERDATAFORLIVE",map,this);
//
//    }
    private void getSesameCredit() {//芝麻信用
        Map<String, Object> map = new HashMap<>();
        map.put("identity_type", "1");
        JSONObject id = new JSONObject();
        try {
            id.put("mobileNo", username);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        map.put("identity_param", id.toString());
        OkHttpManager.postAsync(HttpURL.getInstance().SESAMECREDIT, "SesameCredit", map, this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        switch (name) {
            case "getAuthStates":
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
            case "authorize":
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
        }

    }

    private void getAuthStates() {//从后台获取授权结果

        Map<String, Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(this, "uid", ""));
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL3_AUTHSTATUS, "getAuthStates",
                map, this);
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name) {
            case "SesameCredit":
                JSONObject object1 = new JSONObject(result);
                JSONObject jsonObject = new JSONObject(object1.optString("data"));
                String params = "";
                String sign = "";
                if (jsonObject.has("params")) {
                    params = jsonObject.optString("params");
                }
                if (jsonObject.has("sign")) {
                    sign = jsonObject.optString("sign");
                }
                Map extParams = new HashMap<>();
                creditApp.authenticate(this, "1002755", "", params, sign, extParams, iCreditListener);
                break;
            case "authorize":
                if (result.contains("成功")) {
                    zmfGetAuth = true;
                    operator_no_zhimafen.setText(this.getString(R.string.name_loan_operator_authorization));
                } else {
                    zmfGetAuth = false;
                    operator_no_zhimafen.setText(this.getString(R.string.name_loan_operator_no_authorization));
                }
                break;
//            case "getUSERDATAFORLIVE":
//
//                JSONObject object = new JSONObject(result);
//
//                String code = object.getString("code")+"";
//                if (!TextUtils.isEmpty(code) && code.equals("0000")) {
//                    JSONObject object2 = object.getJSONObject("data");
//                    String card_no = object2.getString("card_no")+"";
//                    String real_name = object2.getString("real_name")+"";
//                    IDCardNo = card_no;
//                    ReadName = real_name;
//                }
//
//                break;
            case "getAuthStates"://获取授信状态

                JSONObject jsonObject1 = new JSONObject(result);
                JSONObject getresult = jsonObject1.getJSONObject("data");
                String zm_status = getresult.getString("zm_status");//0 未完成  1完成

                if (Integer.parseInt(zm_status) == 0) {
                    zmfGetAuth = false;
                    operator_no_zhimafen.setText(this.getString(R.string.name_loan_operator_no_authorization));
                } else {
                    operator_no_zhimafen.setText(this.getString(R.string.name_loan_operator_authorization));
                    zmfGetAuth = true;
                }

                String operator_status = getresult.getString("operator_status");//0 未完成  1完成

                if (Integer.parseInt(operator_status) == 0) {
                    yysGetAuth = false;
                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_no_authorization));
                } else {
//                    yysGetAuth = true;
//                    operator_no_authorization.setText(this.getString(R.string.name_loan_operator_authorization));
                }

                break;

            case "postYYSAuthStatus":
                if (result.contains("0000")) {
                    Log.i("postYYSAuthStatus", "上传运营商授权成功");
                }
                break;
        }
    }


    ICreditListener iCreditListener = new ICreditListener() {
        @Override
        public void onComplete(Bundle result) {
            zmfGetAuth = true;
            Set keys = result.keySet();
            Map<String, Object> map = new HashMap<>();
            for (Object key : keys) {
                map.put(key.toString(), result.getString(key.toString()));
            }
            map.put("uid", SharedPreferencesUtils.get(PersonalData3.this, "uid", "").toString());
            OkHttpManager.postAsync(HttpURL.getInstance().AUTHORIZE, "authorize", map, PersonalData3.this);

        }


        @Override
        public void onError(Bundle error) {
            zmfGetAuth = false;
            Set keys = error.keySet();
            Map<String, Object> map = new HashMap<>();
            for (Object key : keys) {
                map.put(key.toString(), error.getString(key.toString()));
            }
            map.put("uid", SharedPreferencesUtils.get(PersonalData3.this, "uid", "").toString());
            OkHttpManager.postAsync(HttpURL.getInstance().AUTHORIZE, "authorize", map, PersonalData3.this);
        }

        @Override
        public void onCancel() {
            Log.d("", "");
        }
    };

    private void postYYSAuthStatus() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(PersonalData3.this, "uid", "") + "");
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL_DATA_STATUS, "postYYSAuthStatus", map, PersonalData3.this);
    }

    private void getPhone(String title, String msg) {
        PublicPhoneDialog.Builder builder = new PublicPhoneDialog.Builder(this);
        builder.setTitle(title);
        builder.setTiltleMsg(msg);
        builder.setContentViewDetermine("我知道了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent();
            intent.putExtra("complete", "2");
            setResult(106, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
