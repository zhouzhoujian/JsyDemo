package cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.PublicClass.ShowDialog;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import cn.jishiyu11.xjsjd.view.PublicPhoneDialog;
import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/7/20.
 * 运营商 页面
 */

public class OperatorActivityBase extends BaseActivity implements View.OnClickListener, DataCallBack {

    private EditText operator_phone;

    private EditText operator_password;

    private TextView operator_no_password;

    private CheckBox operator_checkbox;

    private TextView operator_clausr_text;

    private Button operator_submit_button;

    private Button check_again;

    private LinearLayout operator_linear;//填写数据

    private LinearLayout operator_linear1;//正在处理

    private LinearLayout operator_linear2;//结果

    private ImageView operator_logo;

    private String sgin;

    private boolean checkbox = true;

    private int booNoPassword = 0;

    private String otherInfo = "";

    private Intent intent = null;

    private UserCenterRealize userCenterRealize = new UserCenterRealize();

    private String type = "";//手机卡服务运营商   移动联通电信

    private String isID = "isID";

    private boolean booType = false;

    private Handler handler = null;

    private int auth = 0;

    private String token;

    private String phoneArea;//手机卡归属地
    private CustomDialog customDialog1, customDialog2;

    private String checkCode = "";

    private int CheckCodeTimes = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_operator);
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.check_again:
                    customDialog2.show();
                break;
            case R.id.operator_submit_button://运营商提交
                if (checkbox) {
                    if (TextUtils.isEmpty(operator_password.getText().toString())) {
                        booNoPassword = 2;
                    } else {
                        booNoPassword = 1;
                        operator_linear.setVisibility(View.GONE);
                        operator_linear1.setVisibility(View.VISIBLE);
                        operator_linear2.setVisibility(View.GONE);
                    }
                    getSIGNPhone();
                } else {
                    ToatUtils.showShort1(OperatorActivityBase.this, "请" +
                            this.getString(R.string.operator_agree) + this.getString(R.string.operator_clause));
                }
                break;
            case R.id.title_image:
                intent = new Intent();
                intent.putExtra("operator", "2");
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.operator_no_password:
                booNoPassword = 2;
//                getSIGNPhone();
                break;
            case R.id.operator_complete_button:
//                getUSERDATAILAUTH();
                ISOperatorSucce(1);//授权成功后返回获取授权界面
                break;
        }
    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        findViewById(R.id.check_again).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.operator_grant));

        operator_phone = findViewById(R.id.operator_phone);

        operator_password = findViewById(R.id.operator_password);

        operator_no_password = findViewById(R.id.operator_no_password);

        operator_checkbox = findViewById(R.id.operator_checkbox);

        operator_clausr_text = findViewById(R.id.operator_clausr_text);

        operator_submit_button = findViewById(R.id.operator_submit_button);

        operator_linear = findViewById(R.id.operator_linear);

        operator_linear1 = findViewById(R.id.operator_linear1);

        operator_linear2 = findViewById(R.id.operator_linear2);

        operator_logo = findViewById(R.id.operator_logo);
    }

    @Override
    protected void initView() {
        if (!TextUtils.isEmpty(SharedPreferencesUtils.get(this, "username", "").toString())) {
            operator_phone.setText(SharedPreferencesUtils.get(this, "username", "").toString());
        }
        setdialog2();
        setdialog1();
        operator_linear.setVisibility(View.VISIBLE);
        operator_linear1.setVisibility(View.GONE);
        operator_linear2.setVisibility(View.GONE);
        operator_logo.setImageResource(R.mipmap.ic_operator_logo);
        operator_checkbox.setChecked(true);
        operator_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//选中
                    checkbox = true;
                    operator_submit_button.setClickable(true);
                    operator_submit_button.setBackgroundResource(R.mipmap.ic_set_up_confirm_button);
                } else {//未选中
                    checkbox = false;
                    operator_submit_button.setClickable(false);
                    operator_submit_button.setBackgroundResource(R.mipmap.ic_set_up_no_confirm_button);
                }
            }
        });
        operator_submit_button.setOnClickListener(this);
        operator_no_password.setOnClickListener(this);
        findViewById(R.id.operator_complete_button).setOnClickListener(this);
        handler = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (booType) {
            ShowDialog.getInstance().getEdiText(this, "北京移动需要输入客服密码", "客服密码:", 1000, isID, mHandler);
        }
    }

    private void PostPersonalAuthorData(String json) {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", Long.parseLong(SharedPreferencesUtils.get(this, "uid", "").toString()));
        map.put("operator_data", json);
        OkHttpManager.postAsync(HttpURL.getInstance().USERDATAILAUTH, "PostPersonalAuthorData", map, this);
    }


    /**
     * 从探知获取已请求业务的轮训状态
     */
    private void getAuthorStatusFromTZ() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.common.getStatus");
        map.put("sign", sgin);
        map.put("token", token);
        OkHttpManager.postAsync(HttpURL.getInstance().HTTP_OPERATOR, "product_status", map, this);

    }

    /**
     * 从探知获取已请求业务的轮训状态
     */
    private void getAuthorStatusFromTZSign() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.common.getStatus");
        map.put("token", token);
        OkHttpManager.postAsync(HttpURL.getInstance().SIGN, "getAuthorStatusFromTZSign", map, this);

    }

    /**
     * 需要输入一次验证码
     */
    private void getAuthouStatusFromTZSetCheckCode1() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.mobile.sendSms");
        map.put("sign", sgin);
        map.put("token", token);
        map.put("smsCode", checkCode);
        OkHttpManager.postAsync(HttpURL.getInstance().HTTP_OPERATOR, "getAuthouStatusFromTZSetCheckCode1", map, this);
    }

    /**
     * 需要输入一次验获取第一次 的sign
     */
    private void getAuthouStatusFromTZSetCheckCode1Sign() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.mobile.sendSms");
        map.put("token", token);
        map.put("smsCode", checkCode);
        OkHttpManager.postAsync(HttpURL.getInstance().SIGN, "getAuthouStatusFromTZSetCheckCode1Sign", map, this);
    }


    /**
     * 请求服务器
     */
    private void getSIGNPhone() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.mobile.area");
        map.put("mobileNo", operator_phone.getText().toString().trim());
        OkHttpManager.postAsync(HttpURL.getInstance().SIGN, "product_phone", map, this);
    }

    /**
     * 请求服务器
     */
    private void getPERSONDATAFROMTZ() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.common.getResult");
        map.put("sign", sgin);
        map.put("token", token);
        map.put("bizType", "mobile");
        OkHttpManager.postAsync(HttpURL.getInstance().HTTP_OPERATOR, "getPERSONDATAFROMTZ", map, this);
    }

    /**
     * 请求服务器
     */
    private void getPERSONDATAFROMTZSign() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.common.getResult");
        map.put("token", token);
        map.put("bizType", "mobile");
        OkHttpManager.postAsync(HttpURL.getInstance().SIGN, "getPERSONDATAFROMTZSign", map, this);
    }

    /**
     * 请求探知数据 手机号
     */
    private void getProductHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
        map.put("version", "1.0.0");
        map.put("method", "api.mobile.area");
        map.put("sign", sgin);
        map.put("mobileNo", SharedPreferencesUtils.get(this, "username", "").toString());
        OkHttpManager.postAsync(HttpURL.getInstance().HTTP_OPERATOR, "product_http", map, this);
    }

    private void getSINGOPerator() {//向后台请求数据；  获取sign
        if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "idcard", "").toString())) {
            ShowDialog.getInstance().getEdiText(this, "需要输入身份证号", "身份证号:", 1004, "idcard", mHandler);
        } else if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "realname", "").toString())) {
            ShowDialog.getInstance().getEdiText(this, "需要输入姓名", "姓名:", 1004, "realname", mHandler);
        } else {
            Map<String, Object> map = new HashMap<>();
            map.put("apiKey", "0618854278903691");
//            map.put("contentType", "busi");
//            map.put("contentType", "alls");//全部信息
            map.put("contentType", "sms;busi;balance;recharge");//
            map.put("identityCardNo", SharedPreferencesUtils.get(this, "idcard", "").toString());
            map.put("identityName", SharedPreferencesUtils.get(this, "realname", "").toString());
            map.put("method", "api.mobile.get");
            if (!TextUtils.isEmpty(otherInfo)) {
                map.put("otherInfo", otherInfo);
            }
            map.put("password", Base64.encodeToString(operator_password.getText().toString().getBytes(),
                    Base64.DEFAULT).replace("\n", " ").trim());
            map.put("username", SharedPreferencesUtils.get(this, "username", "").toString());
            map.put("version", "1.0.0");
            OkHttpManager.postAsync(HttpURL.getInstance().SIGN, "product_phone_content", map, this);
        }
    }

    /**
     * 短信，归属地，余额，充值记录
     */
    private void getOperator() {
        Map<String, Object> map = new HashMap<>();
        map.put("apiKey", "0618854278903691");
//        map.put("contentType", "busi");//归属地
//        map.put("contentType", "alls");//全量数据
        map.put("contentType", "sms;busi;balance;recharge");//短信，归属地，余额，充值记录
        map.put("identityCardNo", SharedPreferencesUtils.get(this, "idcard", "").toString());
        map.put("identityName", SharedPreferencesUtils.get(this, "realname", "").toString());
        map.put("method", "api.mobile.get");
        if (!TextUtils.isEmpty(otherInfo)) {
            map.put("otherInfo", otherInfo);
        }
        map.put("password", Base64.encodeToString(operator_password.getText().toString().getBytes(),
                Base64.DEFAULT).replace("\n", " ").trim());
        map.put("username", SharedPreferencesUtils.get(this, "username", "").toString());
        map.put("version", "1.0.0");
        map.put("sign", sgin);
        OkHttpManager.postAsync(HttpURL.getInstance().HTTP_OPERATOR, "product_content_content", map, this);
    }


    //获取用户活体信息
    private void getUSERDATAFORLIVE(){
        Map map = new HashMap();
        map.put("uid",SharedPreferencesUtils.get(this,"uid","")+"");
        OkHttpManager.postAsync(HttpURL.getInstance().DATA_FOR_LIVE,"getUSERDATAFORLIVE",map,this);

    }


    @Override
    public void requestFailure(Request request, String name, IOException e) {
        ToatUtils.showShort1(this, this.getString(R.string.network_timed));
    }

    /**
     * 每1S Handler 发送一次 直到收到结果
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getProductHttp();
        }
    };

    /**
     * 每2S Handler 发送一次 直到收到状态
     */
    private Runnable runnableStatus = new Runnable() {
        @Override
        public void run() {
            getAuthorStatusFromTZ();
        }
    };
    /**
     * 每2S Handler 发送一次 直到收到结果
     */
    private Runnable runnableResult = new Runnable() {
        @Override
        public void run() {
            getPERSONDATAFROMTZ();
        }
    };

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        JSONObject object;
        switch (name) {
            case "product_phone"://获取归属地的轮训结果
                object = new JSONObject(result);
                object = new JSONObject(object.optString("data"));
                sgin = object.optString("sign");
                getProductHttp();
                break;
            case "product_http":
                if (TextUtils.isEmpty(result)) {
                    handler.postAtTime(runnable, 1000);
                } else {
                    handler.removeCallbacks(runnable);
                    object = new JSONObject(result);
                    type = object.optString("type");
                    String province = object.optString("province");
                    phoneArea = province;
                    if (object.optString("code").equals("0000")) {
                        if (booNoPassword == 1) {//提交
                            switch (province) {
                                case "北京":
                                    if (type.equals("移动")) {
                                        booType = true;
                                        ShowDialog.getInstance().getEdiText(this, "北京移动需要输入客服密码",
                                                "客服密码:", 1000, isID, mHandler);
                                    } else {
                                        getSINGOPerator();
                                    }
                                    break;
                                case "广西":
                                    if (type.equals("电信")) {
                                        if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "idcard", "").toString())) {
                                            ShowDialog.getInstance().getEdiText(this, "广西电信需要输入身份证号", "身份证号:",
                                                    1001, "idcard", mHandler);
                                        } else {
                                            otherInfo = SharedPreferencesUtils.get(this, "idcard", "").toString();
                                        }
                                    } else {
                                        getSINGOPerator();
                                    }
                                    break;
                                case "山西":
                                    if (type.equals("电信")) {
                                        if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "idcard", "").toString())) {
                                            ShowDialog.getInstance().getEdiText(this, "山西电信需要输入身份证号", "身份证号:",
                                                    1002, "idcard", mHandler);
                                        } else {
                                            otherInfo = SharedPreferencesUtils.get(this, "idcard", "").toString();
                                        }
                                    } else {
                                        getSINGOPerator();
                                    }
                                    break;
                                case "吉林":
                                    if (type.equals("电信")) {
                                        ShowDialog.getInstance().getEdiText(this, "吉林电信需要编辑短信'CXXD'\n发送给1001获取验证码",
                                                "短信验证码:", 1003, "code", mHandler);
                                    } else {
                                        getSINGOPerator();
                                    }
                                    break;
                                default:
                                    if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "idcard", "").toString())) {
                                        ShowDialog.getInstance().getEdiText(this, "需要输入身份证号", "身份证号:", 1004,
                                                "idcard", mHandler);
                                    } else if (TextUtils.isEmpty(SharedPreferencesUtils.get(this, "realname", "").
                                            toString())) {
                                        ShowDialog.getInstance().getEdiText(this, "需要输入姓名", "姓名:", 1004,
                                                "realname", mHandler);
                                    } else {
                                        getSINGOPerator();
                                    }
                            }
                        } else {//忘记密码
                            getType2();
                        }
                    }
                }
                break;
            case "product_content_content":
                object = new JSONObject(result);
                if (object.optString("code").equals("0010")) {
                    token = object.getString("token");
                    ToatUtils.showShort1(this, object.optString("msg"));
                    String additional = object.getString("additional");
                    if (!TextUtils.isEmpty(additional) && additional.equals("true")) {//需要验证码
                        customDialog2.show();
                    }else{//不需要验证码
                            getAuthorStatusFromTZSign();
                    }
                } else if (object.optString("code").equals("4001")) {//有一个任务正在执行
                    ToatUtils.showShort1(this, "请三分钟后重试！");
//                        finish();
                } else {
                    ToatUtils.showShort1(this, "授权失败！");
                }

                break;
            case "product_phone_content":
                object = new JSONObject(result);
                object = new JSONObject(object.optString("data"));
                sgin = object.optString("sign");
                getOperator();
                break;
            case "getAuthorStatusFromTZSign"://轮询状态前的sign

                object = new JSONObject(result);
                object = new JSONObject(object.optString("data"));
                sgin = object.optString("sign");
                getAuthorStatusFromTZ();
                break;
            case "product_status"://轮训查询数据状态
                if (TextUtils.isEmpty(result)) {
                    handler.postAtTime(runnableStatus, 2000);
                } else {
                    object = new JSONObject(result);
                    String code = object.getString("code");
                    if (!TextUtils.isEmpty(code)) {
                        if (code.equals("0000") || code.equals("0100") || code.equals("0010")) {
                            //成功  开始获取数据
                            token = object.getString("token");
                            getPERSONDATAFROMTZSign();
                        } else if (code.contains("0001")) {
                            if (type.contains("移动")) {
                                if (phoneArea.contains("云南") || phoneArea.contains("吉林") || phoneArea.contains("四川")) {
                                    customDialog2.show();
                                }else {
                                    getAuthouStatusFromTZSetCheckCode1Sign();//获取第一次签名
                                }
                            }else if (type.contains("电信")){
                                if (phoneArea.contains("山东")){
                                    customDialog2.show();
                                }else {
                                    getAuthouStatusFromTZSetCheckCode1Sign();//获取第一次签名
                                }
                            }
                        } else {
                            ToatUtils.showShort1(this, object.getString("msg"));
                        }

                    }
                    handler.removeCallbacks(runnableStatus);
                }

                break;
            case "getAuthouStatusFromTZSetCheckCode1Sign":
                object = new JSONObject(result);
                String code = object.getString("code");
                if (!TextUtils.isEmpty(code) && code.equals("0000")) {
                    object = new JSONObject(object.optString("data"));
                    sgin = object.optString("sign");
                    getAuthouStatusFromTZSetCheckCode1();//获取第一次的验证码结果
                }

                break;
            case "getAuthouStatusFromTZSetCheckCode1"://验证码第一次
                JSONObject object1 = new JSONObject(result);
                String code1 = object1.getString("code") + "";
                if (!TextUtils.isEmpty(code1) && (code1.equals("0009"))) {//写入成功
                    token = object1.getString("token") + "";
                    Thread.sleep(5000);
                    getAuthorStatusFromTZSign();

                } else {
                    ToatUtils.showShort1(this, object1.getString("msg") + "");
                }

                break;
            case "getPERSONDATAFROMTZSign":
                object = new JSONObject(result);
                object = new JSONObject(object.optString("data"));
                sgin = object.optString("sign");
                getPERSONDATAFROMTZ();
                break;
            case "getPERSONDATAFROMTZ"://获取运营商数据
                if (TextUtils.isEmpty(result)) {
                    handler.postAtTime(runnableResult, 2000);
                } else {
                    object = new JSONObject(result);
                    String codes = object.getString("code") + "";
                    if (!TextUtils.isEmpty(codes) && codes.equals("0000")) {//表明数据获取成功

                        String json = object.getString("data");
                            json = "{"+json+"}";
                        PostPersonalAuthorData(json);
                    } else {
                        ToatUtils.showShort1(this, object.getString("msg"));
                    }
                    handler.removeCallbacks(runnableResult);
                }
                break;
            case "PostPersonalAuthorData":
                object = new JSONObject(result);
                String codes = object.getString("code") + "";
                if (!TextUtils.isEmpty(codes) && codes.equals("0000")) {
                    setIsSuccessfulStatus(1);
                } else {
                    ToatUtils.showShort1(this, object.getString("msg") + "");
                }
                break;
        }
    }


    private void setIsSuccessfulStatus(int status) {//输入 授权状态 ，1  为授权成功  2为失败
        auth = status;

        operator_logo.setImageResource(R.mipmap.ic_operator_complete);
        operator_linear.setVisibility(View.GONE);
        operator_linear1.setVisibility(View.GONE);
        operator_linear2.setVisibility(View.VISIBLE);
    }


    private void ISOperatorSucce(int i) {//判断是否成功
        if (i == 1) {
            intent = new Intent();
            intent.putExtra("operator", "1");//返回Personal3 运营商结果
            setResult(RESULT_CANCELED, intent);
            finish();
        } else {

        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    if (msg.obj.toString().equals("1")) {
                        ShowDialog.getInstance().getEdiText(OperatorActivityBase.this, "北京移动需要输入客服密码",
                                "客服密码:", 1000, isID, mHandler);
                    } else {
                        if (TextUtils.isEmpty(msg.obj.toString())) {
                            getPhone("移动客服电话", "请打客服" + AppUtil.getInstance().PHONE_MOVE + "电话,进行咨询客服密码",
                                    AppUtil.getInstance().PHONE_MOVE);
                        } else {
                            booType = false;
                            otherInfo = msg.obj.toString();
                            getSINGOPerator();
                        }
                    }
                    break;
                case 1001:
                    if (msg.obj.toString().equals("1")) {
                        ShowDialog.getInstance().getEdiText(OperatorActivityBase.this, "广西电信需要输入身份证号", "身份证号:",
                                1001, "idcard", mHandler);
                    } else {
                        otherInfo = msg.obj.toString();
                        getSINGOPerator();
                    }
                    break;
                case 1002:
                    if (msg.obj.toString().equals("1")) {
                        ShowDialog.getInstance().getEdiText(OperatorActivityBase.this, "山西电信需要输入身份证号", "身份证号:",
                                1002, "idcard", mHandler);
                    } else {
                        otherInfo = msg.obj.toString();
                        getSINGOPerator();
                    }
                    break;
                case 1003:
                    if (msg.obj.toString().equals("1")) {
                        ShowDialog.getInstance().getEdiText(OperatorActivityBase.this, "吉林电信需要编辑短信'CXXD'\n发送给10001获取验证码",
                                "短信验证码:", 1003, "", mHandler);
                    } else {
                        otherInfo = msg.obj.toString();
                        getSINGOPerator();
                    }
                    break;
                case 1004:
                    getSINGOPerator();
                    break;
                case 1005:
                    if (msg.obj.toString().equals("1")) {//吉林需要自己发送请求验证码短信
                        ShowDialog.getInstance().getEdiText(OperatorActivityBase.this, "吉林电信需要编辑短信'CXXD'\n发送给10001获取验证码",
                                "短信验证码:", 1005, "", mHandler);
                    } else {
                        otherInfo = msg.obj.toString();
                        getAuthouStatusFromTZSetCheckCode1();
                    }
                    break;

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_PHONE_DIAL) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getType2();
            } else {
                Toast.makeText(this, "请授予拨打电话权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getType2() {
        switch (type) {
            case "移动":
                getPhone("移动客服电话", "请打客服" + AppUtil.getInstance().PHONE_MOVE + "电话,进行咨询服务密码",
                        AppUtil.getInstance().PHONE_MOVE);
                break;
            case "联通":
                getPhone("联通客服电话", "请打客服" + AppUtil.getInstance().PHONE_UNICOM + "电话,进行咨询服务密码",
                        AppUtil.getInstance().PHONE_UNICOM);
                break;
            case "电信":
                getPhone("电信客服电话", "请打客服" + AppUtil.getInstance().PHONE_TELECOM + "电话,进行咨询服务密码",
                        AppUtil.getInstance().PHONE_TELECOM);
                break;
        }
    }

    private void getPhone(String title, String msg, final String phone) {
        PublicPhoneDialog.Builder builder = new PublicPhoneDialog.Builder(this);
        builder.setTitle(title);
        builder.setTiltleMsg(msg);
        builder.setContentViewCancel("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setContentViewDetermine("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userCenterRealize.getPhoneDial(OperatorActivityBase.this, phone);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent();
            intent.putExtra("operator", "2");
            setResult(RESULT_CANCELED, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //用户填写界面
    private void setdialog2() {
        customDialog2 = new CustomDialog(OperatorActivityBase.this, R.style.mydialog);
        View contentView = LayoutInflater.from(OperatorActivityBase.this).inflate(R.layout.dialog_input, null);
        customDialog2.setContentView(contentView);
        customDialog2.setCanceledOnTouchOutside(false);
        Window dialogWindow = customDialog2.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        TextView tv_input_title = contentView.findViewById(R.id.tv_input_title);
        final EditText et_input_value = contentView.findViewById(R.id.et_input_value);
        TextView tv_input_confirm = contentView.findViewById(R.id.tv_input_confirm);
        tv_input_title.setText("请输入验证码");

        tv_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText())) {
                    if (TextUtils.isEmpty(et_input_value.getText() + "") || et_input_value.getText().length() < 4) {
                        et_input_value.setText("");
                        ToatUtils.showShort1(OperatorActivityBase.this, "请输入正确的验证码");
                    } else {
                        checkCode = et_input_value.getText() + "";//获取验证码
                        customDialog2.dismiss();
                        getAuthouStatusFromTZSetCheckCode1Sign();//获取第一次签名
                    }
                } else {
                    ToatUtils.showShort1(OperatorActivityBase.this, "请输入验证码");
                }
            }
        });
    }

    private void setdialog1() {
        customDialog1 = new CustomDialog(OperatorActivityBase.this, R.style.mydialog);
        View contentView = LayoutInflater.from(OperatorActivityBase.this).inflate(R.layout.dialog_input, null);
        customDialog1.setContentView(contentView);
        customDialog1.setCanceledOnTouchOutside(false);
        Window dialogWindow = customDialog1.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        TextView tv_input_title = contentView.findViewById(R.id.tv_input_title);
        final EditText et_input_value = contentView.findViewById(R.id.et_input_value);
        TextView tv_input_confirm = contentView.findViewById(R.id.tv_input_confirm);
        tv_input_title.setText("提醒");

        tv_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText())) {
                    if (TextUtils.isEmpty(checkCode) || checkCode.length() < 4) {
                        et_input_value.setText("");
                        ToatUtils.showShort1(OperatorActivityBase.this, "请输入正确的验证码");
                    } else {
                        checkCode = et_input_value.getText() + "";//获取验证码
                        customDialog1.dismiss();
//                        getAuthouStatusFromTZSetCheckCode1();//验证验证码
//                        getAuthouStatusFromTZSetCheckCode1Sign();//获取第一次签名

                    }
                } else {
                    ToatUtils.showShort1(OperatorActivityBase.this, "请输入验证码");
                }
            }
        });
    }


}
