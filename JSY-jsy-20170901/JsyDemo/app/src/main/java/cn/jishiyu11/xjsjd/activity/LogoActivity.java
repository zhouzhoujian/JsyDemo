package cn.jishiyu11.xjsjd.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.EntityClass.RegisterSignCodeModify;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.SetUp.SetUPActivity;
import cn.jishiyu11.xjsjd.activity.SetUp.SetUpPasswordActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CTelephoneInfo;
import cn.jishiyu11.xjsjd.utils.DisplayUtils;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.JsonData;
import cn.jishiyu11.xjsjd.utils.NetWorkUtils;
import cn.jishiyu11.xjsjd.utils.PublicClass.CountDownTimerUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/28.
 * 登录页面
 */
@SuppressLint("ResourceType")
public class LogoActivity extends FragmentActivity implements View.OnClickListener, DataCallBack {

    private LinearLayout tab_activity_lin;

    private ImageView title_image;

    private TextView title_complete;

    private EditText loan_logo_edittext_phone;

    private EditText loan_logo_edittext_code;

    private EditText loan_logo_edittext_password_code;

    private Button loan_logo_button_code;

    private TextView loan_logo_account_number;

    private LinearLayout loan_logo_Re_code;

    private RelativeLayout loan_logo_Re_password;

    private TextView loan_logo_no_password;

    private int logoCodeLogo = R.string.name_loan_logo_code_logo;

    private int logoAccountNumber = R.string.name_loan_logo_account_number;

    private String phone = "";

    private CountDownTimerUtils mCountDownTimerUtils;

    private int logintype = 0;

    private Intent intent;

    private Map<String,Object> map =new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_logo);
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            setTranslucentStatus(true);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tab_activity_lin.getLayoutParams();
            lp.gravity = Gravity.CENTER;
            lp.height = DisplayUtils.px2dip(this, 48 * 10) + 120;
            ImmersiveUtils.stateBarTint(this, "#00000000", true, false);
            //设置状态栏白色字体
            ImmersiveUtils.StatusFragmentBarDarkMode(this);
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loan_logo_account_number://账号登入 切换
                if (loan_logo_account_number.getText().toString().equals(LogoActivity.this.getString(logoAccountNumber))) {
                    loan_logo_Re_code.setVisibility(View.GONE);
                    loan_logo_Re_password.setVisibility(View.VISIBLE);
                    loan_logo_account_number.setText(LogoActivity.this.getString(logoCodeLogo));
                    loan_logo_no_password.setVisibility(View.VISIBLE);
                } else {
                    loan_logo_Re_code.setVisibility(View.VISIBLE);
                    loan_logo_Re_password.setVisibility(View.GONE);
                    loan_logo_account_number.setText(LogoActivity.this.getString(logoAccountNumber));
                    loan_logo_no_password.setVisibility(View.GONE);
                }
                break;
            case R.id.loan_logo_button_code://验证码
                if (StringUtil.isNullOrEmpty(phone)) {
                    ToatUtils.showShort1(this, "您输入的手机号码不能为空");
                } else if (!StringUtil.isMobileNO(phone)) {
                    ToatUtils.showShort1(this, "您输入的手机号码不正确,请重新输入");
                    loan_logo_edittext_phone.setText("");
                } else {
                    mCountDownTimerUtils = new CountDownTimerUtils(loan_logo_button_code, 60 * 1000, 1000);
                    mCountDownTimerUtils.start();
                    Map<String, Object> map = new HashMap<>();
                    map.put("mobile", Long.parseLong(phone));
                    map.put("password", "");
                    map.put("code", 0);
                    OkHttpManager.postAsync(HttpURL.getInstance().CODE, "code", map, this);
                }
                break;
            case R.id.loan_logo_button_logo://登入按钮
                if (StringUtil.isNullOrEmpty(phone)) {
                    ToatUtils.showShort1(this, "您输入的手机号码不能为空");
                    return;
                } else if (!StringUtil.isMobileNO(phone)) {
                    ToatUtils.showShort1(this, "您输入的手机号码不正确,请重新输入");
                    loan_logo_edittext_phone.setText("");
                    return;
                }
                Map<String, Object> map = new HashMap<>();
                map.put("username", Long.parseLong(phone));
                String no = AppUtil.getInstance().getChannel(LogoActivity.this, 2)+"";
                map.put("no", no);//渠道号
                if (loan_logo_account_number.getText().toString().equals(LogoActivity.this.getString(logoCodeLogo))) {
                    logintype = 1;
                    if (StringUtil.isNullOrEmpty(loan_logo_edittext_password_code.getText().toString())) {
                        ToatUtils.showShort1(this, "请输入密码");
                        return;
                    }
                    map.put("code", 0);
                    map.put("logintype", logintype);
                    map.put("password", loan_logo_edittext_password_code.getText().toString());
                } else {
                    logintype = 2;
                    if (StringUtil.isNullOrEmpty(loan_logo_edittext_code.getText().toString())) {
                        ToatUtils.showShort1(this, "请输入验证码");
                        return;
                    }
                    map.put("code", Long.parseLong(loan_logo_edittext_code.getText().toString()));
                    map.put("password", "");
                    map.put("logintype", logintype);
                }
                map.put("version","0.0.4");//后台区分标识符
                OkHttpManager.postAsync(HttpURL.getInstance().LOGO, "logo_code", map, this);
                Log.e("version",map.toString()+"============");
//                OkHttpManager.postAsync(HttpURL.getInstance().logoinString, "logo_code", map, this);
                break;
            case R.id.title_complete://注册
                intent = new Intent(LogoActivity.this, SetUpPasswordActivity.class);
                intent.putExtra("name", "1");
                startActivity(intent);
                finish();
                break;
            case R.id.title_image://返回键
                finish();
                break;
            case R.id.loan_logo_no_password://忘记密码
                intent = new Intent(LogoActivity.this, SetUpPasswordActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
                break;
        }

    }

    protected void findViewById() {
        tab_activity_lin = findViewById(R.id.tab_activity_lin);
        tab_activity_lin.setBackgroundResource(R.color.transparent);
        title_image = findViewById(R.id.title_image);
        title_image.setVisibility(View.VISIBLE);
        title_image.setOnClickListener(this);

        title_complete = findViewById(R.id.title_complete);
//        title_complete.setVisibility(View.VISIBLE);
        title_complete.setOnClickListener(this);
        title_complete.setText(this.getString(R.string.name_loan_logo_register));

        TextView title_view = findViewById(R.id.title_view);
        title_view.setVisibility(View.INVISIBLE);

        loan_logo_edittext_phone = findViewById(R.id.loan_logo_edittext_phone);
        loan_logo_edittext_code = findViewById(R.id.loan_logo_edittext_code);

        loan_logo_button_code = findViewById(R.id.loan_logo_button_code);
        loan_logo_button_code.setOnClickListener(this);

        findViewById(R.id.loan_logo_button_logo).setOnClickListener(this);

        loan_logo_account_number = findViewById(R.id.loan_logo_account_number);
        loan_logo_account_number.setOnClickListener(this);
        loan_logo_Re_code = findViewById(R.id.loan_logo_Re_code);
        loan_logo_Re_password = findViewById(R.id.loan_logo_Re_password);

        loan_logo_no_password = findViewById(R.id.loan_logo_no_password);
        loan_logo_no_password.setOnClickListener(this);

        loan_logo_edittext_password_code = findViewById(R.id.loan_logo_edittext_password_code);

    }

    protected void initView() {

        loan_logo_edittext_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                phone = s.toString();
            }
        });

    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        switch (name) {
            case "code":
                mCountDownTimerUtils.onFinish();
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
            case "logo_code"://登录返回值
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
                case "getUserLoginAuthorStatus"://登录返回值
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
        }

    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name) {
            case "code":
                RegisterSignCodeModify registerSignCodeModify = JsonData.getInstance().getJsonLogoCode(result);
                if (registerSignCodeModify.getStatus() == 0) {
                    ToatUtils.showShort1(this, registerSignCodeModify.getInfo());
                } else {
                    if (registerSignCodeModify.getStatus() == 1 && registerSignCodeModify.getState().equals("success")) {
                        ToatUtils.showShort1(this, registerSignCodeModify.getInfo());
                    }
                }
                break;
            case "logo_code":
                if (logintype == 1) {
                    RegisterSignCodeModify registerSignCodeModify1 = JsonData.getInstance().getJsonLogoCode(result);
                    if (registerSignCodeModify1.getStatus() == 0) {
                        ToatUtils.showShort1(this, registerSignCodeModify1.getInfo());
                    } else {
                        if (registerSignCodeModify1.getStatus() == 1 && registerSignCodeModify1.getState().equals("success")) {
//                            SharedPreferencesUtils.put(this, "username", registerSignCodeModify1.getUsername());
//                            SharedPreferencesUtils.put(this, "uid", registerSignCodeModify1.getUid());
//                            SharedPreferencesUtils.put(this, "password", loan_logo_edittext_password_code.getText().toString());
//                            finish();
                            map.put("username", registerSignCodeModify1.getUsername());
                            map.put("uid", registerSignCodeModify1.getUid());
                            map.put("password", loan_logo_edittext_password_code.getText().toString());
                            getUserLoginAuthorStatus(phone);//判断是否允许登录
                        } else {
                            ToatUtils.showShort1(this, registerSignCodeModify1.getInfo());
                        }
                    }
                } else {
                    RegisterSignCodeModify registerSignCodeModify1 = JsonData.getInstance().getJsonLogoCode(result);
                    if (registerSignCodeModify1.getStatus() == 0) {
                        ToatUtils.showShort1(this, registerSignCodeModify1.getInfo());
                    } else {
                        if (registerSignCodeModify1.getStatus() == 1 && registerSignCodeModify1.getState().equals("success")) {
//                            SharedPreferencesUtils.put(this, "username", registerSignCodeModify1.getUsername());
//                            SharedPreferencesUtils.put(this, "uid", registerSignCodeModify1.getUid());
//                            finish();
                            map.put("username", registerSignCodeModify1.getUsername());
                            map.put("uid", registerSignCodeModify1.getUid());
                            getUserLoginAuthorStatus(phone);//判断是否允许登录

                        } else {
                            ToatUtils.showShort1(this, registerSignCodeModify1.getInfo());
                        }
                    }
                }


                break;
            case "getUserLoginAuthorStatus"://登录状态    ----是否可以登录

              JSONObject  object = new JSONObject(result);
                String code = object.getString("code")+"";
                if (!TextUtils.isEmpty(code) && code.equals("0000")){

                    JSONObject object1 = object.getJSONObject("data");
                    String status = object1.getString("status")+"";
                    if (!TextUtils.isEmpty(status) && status.equals("1")){
                        //允许登录
                        SharedPreferencesUtils.put(this, "username", map.get("username"));
                        SharedPreferencesUtils.put(this, "uid", map.get("uid"));
                        if (logintype ==1){
                            SharedPreferencesUtils.put(this, "password", map.get("password"));
                        }
                        finish();
                    }else {
                        //不允许登录 清空Share
                        SharedPreferencesUtils.logoutSuccess(this);
                        map.clear();
                        ToatUtils.showShort1(this,object.getString("msg")+"");
                        finish();
                    }

                }


                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void getUserLoginAuthorStatus(String phontNo){//是否允许登录
        Map<String,Object> map = new HashMap<>();
        String phoneDeviceId =  CTelephoneInfo.getPhoneUDID(LogoActivity.this);
        phoneDeviceId =StringUtil.getMD5String(phoneDeviceId);
        String locaIP = NetWorkUtils.getHostIP()+"";
        locaIP = StringUtil.getMD5String(locaIP);
        map.put("deviceNo",phoneDeviceId);
        map.put("ip",locaIP);
        map.put("mobile",phontNo+"");
        OkHttpManager.postAsync(HttpURL.getInstance().USER_LOGIN_STATUS,"getUserLoginAuthorStatus",map,this);

    }



}
