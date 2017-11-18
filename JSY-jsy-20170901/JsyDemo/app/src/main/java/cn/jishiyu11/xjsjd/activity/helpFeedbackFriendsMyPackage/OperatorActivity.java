package cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
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

import com.megvii.livenesslib.util.IFile;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData4;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CameraUtils.BitmapUtils;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.CustomDialogUtils;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.PublicClass.CountDownTimerUtils;
import cn.jishiyu11.xjsjd.utils.PublicClass.ShowDialog;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringListUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import cn.jishiyu11.xjsjd.view.PublicPhoneDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.SocketHandler;

import cn.jishiyu11.xjsjd.webview.ImageUtils;
import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/7/20.
 * 运营商 页面
 */

public class OperatorActivity extends BaseActivity implements View.OnClickListener, DataCallBack {

    private EditText operator_phone;

    private EditText operator_password;

    private TextView operator_no_password;

    private CheckBox operator_checkbox;

    private TextView operator_clausr_text;

    private Button operator_submit_button;

    private LinearLayout operator_linear;//填写数据

    private LinearLayout operator_linear1;//正在处理

    private LinearLayout operator_linear2;//结果

    private ImageView operator_logo;

    private boolean checkbox = true;

    private int booNoPassword = 0;

    private Intent intent = null;

    private UserCenterRealize userCenterRealize = new UserCenterRealize();

    private String type = "";//手机卡服务运营商   移动联通电信

    private boolean booType = false;

    private Handler handler = null;

    private int auth = 0;

    private String phoneArea;//手机卡归属地
    private CustomDialog customDialog1, customDialog2;

    private String checkCode1 = "";
    private String checkCode2 = "";

    private String userNo = "";
    private String IDCardNo = "";
    private String RealName = "";
    private int LOGIN_TYPE = 0;  //0:up  服务密码  1:dp  sms  2:udp   服务加短信

    private String Phonepassword = "";//手机服务密码
    private String PhoneNo = "";
    //    private String CheckCode = "";//验证码
    private boolean CheckCodeType1 = false;
    private boolean CheckCodeType2 = false;
    private Bitmap bm;
    private ImageView dialog2_img;
    private ArrayList<String> logTypeArr;
    private CountDownTimerUtils countDownTimerUtils;
//    private String[] logKeys = {"servicePassword","name","idCard","smsCode","captcha"};


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
                        Phonepassword = operator_password.getText() + "";
                        if (Phonepassword.length() > 4) {
                            booNoPassword = 1;
                            operator_linear.setVisibility(View.GONE);
                            operator_linear1.setVisibility(View.VISIBLE);
                            operator_linear2.setVisibility(View.GONE);
                            PhoneNo = operator_phone.getText() + "";
                            CREATE_TASK_SH();
                        }
                    }
                } else {
                    ToatUtils.showShort1(OperatorActivity.this, "请" +
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

                break;
            case R.id.operator_complete_button:
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
            userNo = SharedPreferencesUtils.get(this, "uid", "") + "";
        }

//        setdialog1();
        customDialog1 = CustomDialogUtils.getInstance().LoadingDialog(this, "正在刷新...");
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
    protected void onStart() {
        super.onStart();
        getUSERDATAFORLIVE();//获取活体信息  真是姓名和身份证号
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:15
     * 方法说明：获取用户活体信息
     */
    private void getUSERDATAFORLIVE() {
        Map map = new HashMap();
        map.put("uid", userNo);
        OkHttpManager.postAsync(HttpURL.getInstance().DATA_FOR_LIVE, "getUSERDATAFORLIVE", map, this);
    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 11:47
     * 方法说明：获取算话初始化
     */
    private void CREATE_TASK_SH() {
        Map<String, Object> map = new HashMap<>();
        map.put("phone", operator_phone.getText() + "");
        map.put("name", RealName);
        map.put("idCard", IDCardNo);
        map.put("userNo", userNo);
        OkHttpManager.postAsync(HttpURL.getInstance().SH_INTI, "CREATE_TASK_SH", map, this);
    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:13
     * 方法说明：算话登录方法
     */
    private void SH_LOGIN() {
        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);
        OkHttpManager.postAsync(HttpURL.getInstance().SH_LOGIN_USER, "SH_LOGIN", map, this);
    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:18
     * 方法说明：提交表单验证
     */
    private void SH_LOGIN_CHECK() {
        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);
        map.put("phoneNo", PhoneNo);//手机号码

        for (String s : logTypeArr) {
            if (s.equals("servicePassword")) {
                map.put("servicePassword", Phonepassword);//服务密码
            } else if (s.equals("name")) {
                map.put("name", RealName);
            } else if (s.equals("idCard")) {
                map.put("idCard", IDCardNo);
            } else if (s.equals("smsCode")) {
                map.put("smsCode", checkCode1);//短信验证码
            } else if (s.equals("captcha")) {
                map.put("captcha", checkCode2);//图片验证码
            }
        }

        String json = StringListUtils.jsonEnclose(map).toString() + "";
        map.clear();
        map.put("data", json);
        OkHttpManager.postAsync(HttpURL.getInstance().SH_LOGIN_CHECK_USER, "SH_LOGIN_CHECK", map, this);
    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:21
     * 方法说明：获取短信验证码
     */
    private void SH_DATA_CHECKCODE_SMS() {
        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);
        OkHttpManager.postAsync(HttpURL.getInstance().SH_USER_DATA_CHECKCODE_SMS, "SH_DATA_CHECKCODE_SMS", map, this);
    }

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:24
     * 方法说明：刷新图片验证码
     */
    private void SH_DATA_CHECKCODE_IMG() {
        Map<String, Object> map = new HashMap<>();
        map.put("userNo", userNo);
        OkHttpManager.postAsync(HttpURL.getInstance().SH_USER_DATA_CHECKCODE_IMG, "SH_DATA_CHECKCODE_IMG", map, this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {

        customDialog2.dismiss();
        customDialog1.dismiss();
        switch (name) {
            case "CREATE_TASK_SH":
                break;
            case "SH_LOGIN":
                break;
            default:
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
        }
    }

    /**
     * 每1S Handler 发送一次 直到收到结果
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
        }
    };

    /**
     * 每2S Handler 发送一次 直到收到状态
     */
    private Runnable runnableStatus = new Runnable() {
        @Override
        public void run() {
        }
    };
    /**
     * 每2S Handler 发送一次 直到收到结果
     */
    private Runnable runnableResult = new Runnable() {
        @Override
        public void run() {
        }
    };

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        JSONObject object;
        String code;
        switch (name) {
            case "PostPersonalAuthorData":
                object = new JSONObject(result);
                String codes = object.getString("code") + "";
                if (!TextUtils.isEmpty(codes) && codes.equals("0000")) {
//                    setIsSuccessfulStatus(1);
                } else {
                    ToatUtils.showShort1(this, object.getString("msg") + "");
                }
                break;

            case "getUSERDATAFORLIVE":
                //获取活体检测信息
                object = new JSONObject(result);
                code = object.getString("code") + "";
                if (!TextUtils.isEmpty(code) && code.equals("0000")) {
                    JSONObject object2 = object.getJSONObject("data");
                    String card_no = object2.getString("card_no") + "";
                    String real_name = object2.getString("real_name") + "";
                    IDCardNo = card_no;
                    RealName = real_name;
                }

                break;
            case "CREATE_TASK_SH":

                object = new JSONObject(result);
                code = object.getString("retCode") + "";
                if (!TextUtils.isEmpty(code) && code.equals("001")) {//retCode == 001 为请求成功
                    //请求成功loginFields
                    SH_LOGIN();
                }else {
                    ToatUtils.showShort1(this,object.getString("retMsg"));
                }
                break;
            case "SH_LOGIN"://算话登录
                object = new JSONObject(result);
                code = object.getString("retCode") + "";
                if (!TextUtils.isEmpty(code) && code.equals("001")) {
//                    String logintype = object.getString("loginTypeCode") + "";
                    JSONArray logtypeArr = object.getJSONArray("loginFields");//登录必须条件
                    logTypeArr = new ArrayList<>();
                    String keyCode = "";
                    for (int i = 0; i < logtypeArr.length(); i++) {
                        logTypeArr.add(logtypeArr.getJSONObject(i).getString("name") + "");
                        keyCode = logtypeArr.getJSONObject(i).getString("name") + "";
                        if (keyCode.equals("smsCode")) {
                            CheckCodeType1 = true;//短信
                        }
                        if (keyCode.equals("captcha")) {
                            CheckCodeType2 = true;
                        }
                    }
//                    //获取登录验证方式
                    if (CheckCodeType1) {
                        setdialog2();
                        customDialog2.show();
                    } else if (CheckCodeType2) {
                        setdialog2();
                        customDialog2.show();
                        SH_DATA_CHECKCODE_IMG();
                    }
                    if (!CheckCodeType1 && !CheckCodeType2) {//没有任何验证码
                        SH_LOGIN_CHECK();
                    }
                }
                break;

            case "SH_LOGIN_CHECK"://算话验证 验证码
                object = new JSONObject(result);
                if (customDialog1.isShowing()) {
                    customDialog1.dismiss();
                }


                code = object.getString("retCode") + "";
                if (!TextUtils.isEmpty(code) && code.equals("001")) {
                    if (customDialog2.isShowing()) {
                        customDialog2.dismiss();
                    }
                    String step = object.getString("nextStep") + "";
                    if (!TextUtils.isEmpty(step)) {
                        if (step.equals("N/A")) {//运营商调用结束  可以退出了
                            setIsSuccessfulStatus(1);
                        } else {
//                            String logintype = object.getString("loginTypeCode") + "";
                            JSONArray logtypeArr = object.getJSONArray("loginFields");//登录必须条件
                            logTypeArr = new ArrayList<>();
                            String keyCode = "";
                            for (int i = 0; i < logtypeArr.length(); i++) {
                                logTypeArr.add(logtypeArr.getJSONObject(i).getString("name") + "");
                                keyCode = logtypeArr.getJSONObject(i).getString("name") + "";
                                if (keyCode.equals("smsCode")) {
                                    CheckCodeType1 = true;//短信
                                }
                                if (keyCode.equals("captcha")) {
                                    CheckCodeType2 = true;
                                }
                            }
//                    //获取登录验证方式
                            if (CheckCodeType1) {
                                setdialog2();
                                customDialog2.show();
                            } else if (CheckCodeType2) {
                                setdialog2();
                                customDialog2.show();
                                SH_DATA_CHECKCODE_IMG();
                            }
                            if (!CheckCodeType1 && !CheckCodeType2) {
                                SH_LOGIN_CHECK();
                            }
                        }
                    }
                } else {
                    ToatUtils.showShort1(this, object.getString("retMsg"));
                }

                break;
            case "SH_DATA_CHECKCODE_SMS"://获取短信验证码

                object = new JSONObject(result);
                code = object.getString("retCode");
                if (!TextUtils.isEmpty(code) && code.equals("001")) {
                    ToatUtils.showShort1(this,"送验证码成功!");
                }else {
                    ToatUtils.showShort1(this,object.getString("retMsg"));
                }
                break;
            case "SH_DATA_CHECKCODE_IMG"://获取图片验证码
                object = new JSONObject(result);
                code = object.getString("retCode");
                if (!TextUtils.isEmpty(code) && code.equals("001")) {
//                    String codeFormat = object.getString("codeFormat") + "";
                    String codeContent = object.getString("codeContent") + "";
                    bm = ImageUtils.Base642Bitmap(codeContent);

                    if (bm != null) {
                        dialog2_img.setImageBitmap(bm);
                    } else {
                        ToatUtils.showShort1(this, "图片验证码获取失败！");
                    }
                }else {
                    ToatUtils.showShort1(this,object.getString("retMsg"));
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

//    @SuppressLint("HandlerLeak")
//    private Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1000:
//                    if (msg.obj.toString().equals("1")) {
////                        ShowDialog.getInstance().getEdiText(OperatorActivity.this, "北京移动需要输入客服密码",
////                                "客服密码:", 1000, isID, mHandler);
//                    } else {
//                        if (TextUtils.isEmpty(msg.obj.toString())) {
//                            getPhone("移动客服电话", "请打客服" + AppUtil.getInstance().PHONE_MOVE + "电话,进行咨询客服密码",
//                                    AppUtil.getInstance().PHONE_MOVE);
//                        } else {
//                            booType = false;
////                            otherInfo = msg.obj.toString();
//                        }
//                    }
//                    break;
//                case 1001:
//                    if (msg.obj.toString().equals("1")) {
//                        ShowDialog.getInstance().getEdiText(OperatorActivity.this, "广西电信需要输入身份证号", "身份证号:",
//                                1001, "idcard", mHandler);
//                    } else {
////                        otherInfo = msg.obj.toString();
//                    }
//                    break;
//                case 1002:
//                    if (msg.obj.toString().equals("1")) {
//                        ShowDialog.getInstance().getEdiText(OperatorActivity.this, "山西电信需要输入身份证号", "身份证号:",
//                                1002, "idcard", mHandler);
//                    } else {
////                        otherInfo = msg.obj.toString();
//                    }
//                    break;
//                case 1003:
//                    if (msg.obj.toString().equals("1")) {
//                        ShowDialog.getInstance().getEdiText(OperatorActivity.this, "吉林电信需要编辑短信'CXXD'\n发送给10001获取验证码",
//                                "短信验证码:", 1003, "", mHandler);
//                    } else {
////                        otherInfo = msg.obj.toString();
//                    }
//                    break;
//                case 1004:
//                    break;
//                case 1005:
//                    if (msg.obj.toString().equals("1")) {//吉林需要自己发送请求验证码短信
//                        ShowDialog.getInstance().getEdiText(OperatorActivity.this, "吉林电信需要编辑短信'CXXD'\n发送给10001获取验证码",
//                                "短信验证码:", 1005, "", mHandler);
//                    } else {
////                        otherInfo = msg.obj.toString();
//                    }
//                    break;
//
//            }
//        }
//    };

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
                userCenterRealize.getPhoneDial(OperatorActivity.this, phone);
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
        customDialog2 = new CustomDialog(OperatorActivity.this, R.style.mydialog);
        View contentView = LayoutInflater.from(OperatorActivity.this).inflate(R.layout.dialog_input, null);
        customDialog2.setContentView(contentView);
        customDialog2.setCanceledOnTouchOutside(false);
        Window dialogWindow = customDialog2.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        TextView tv_input_title = contentView.findViewById(R.id.tv_input_title);
        final EditText et_input_value = contentView.findViewById(R.id.et_input_value);
        TextView tv_input_confirm = contentView.findViewById(R.id.tv_input_confirm);
        final Button et_input_value_button = contentView.findViewById(R.id.et_input_value_button);
        final LinearLayout dialog_layout1 = contentView.findViewById(R.id.dialog_layout1);
        final LinearLayout dialog_layout2 = contentView.findViewById(R.id.dialog_layout2);
        final EditText et_input_value2 = contentView.findViewById(R.id.et_input_value2);
        dialog2_img = contentView.findViewById(R.id.checkcode_img);
        dialog_layout2.setVisibility(View.GONE);
        et_input_value_button.setVisibility(View.VISIBLE);
        tv_input_title.setText("请输入验证码");

        if (CheckCodeType2 && CheckCodeType1) {
            dialog_layout1.setVisibility(View.VISIBLE);
            dialog_layout2.setVisibility(View.VISIBLE);
            et_input_value_button.setVisibility(View.VISIBLE);
            SH_DATA_CHECKCODE_IMG();
            et_input_value_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SH_DATA_CHECKCODE_SMS();
                    countDownTimerUtils = new CountDownTimerUtils(et_input_value_button, 60000, 1000);
                    countDownTimerUtils.start();
                }
            });
//
            dialog2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SH_DATA_CHECKCODE_IMG();
//                    customDialog2.dismiss();
                }
            });
        } else if (CheckCodeType2) {
            dialog_layout1.setVisibility(View.GONE);
            dialog_layout2.setVisibility(View.VISIBLE);
//            dialog2_img.setImageBitmap(bm);
            dialog2_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SH_DATA_CHECKCODE_IMG();
//                    customDialog2.dismiss();
                }
            });
        } else if (CheckCodeType1) {
            dialog_layout2.setVisibility(View.GONE);
            dialog_layout1.setVisibility(View.VISIBLE);
            et_input_value_button.setVisibility(View.VISIBLE);
            et_input_value_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {//发送短信验证码监听
                    SH_DATA_CHECKCODE_SMS();
                    countDownTimerUtils = new CountDownTimerUtils(et_input_value_button, 60000, 1000);
                    countDownTimerUtils.start();
                }
            });
        }


        tv_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog1.show();
                int step = 0;
                if (CheckCodeType2) {//图片验证码
                    if (!TextUtils.isEmpty(et_input_value2.getText())) {
                        if (TextUtils.isEmpty(et_input_value2.getText() + "") || et_input_value2.getText().length() < 4) {
                            et_input_value2.setText("");
                            ToatUtils.showShort1(OperatorActivity.this, "请输入正确的验证码");
                        } else {
                            checkCode2 = et_input_value2.getText() + "";//获取验证码
                            step += 1;
                        }
                    } else {
                        ToatUtils.showShort1(OperatorActivity.this, "请输入验证码");
                    }
                }
                if (CheckCodeType1) {
                    if (!TextUtils.isEmpty(et_input_value.getText())) {
                        if (TextUtils.isEmpty(et_input_value.getText() + "") || et_input_value.getText().length() < 4) {
                            et_input_value.setText("");
                            ToatUtils.showShort1(OperatorActivity.this, "请输入正确的验证码");
                        } else {
                            checkCode1 = et_input_value.getText() + "";//获取验证码
                            step += 1;
                        }
                    } else {
                        ToatUtils.showShort1(OperatorActivity.this, "请输入验证码");
                    }
                }

                if (CheckCodeType1 && CheckCodeType2 && (step == 2)) {
                    SH_LOGIN_CHECK();
                } else if (CheckCodeType1 && CheckCodeType2 == false && step == 1) {
                    SH_LOGIN_CHECK();
                } else if (CheckCodeType2 && CheckCodeType1 == false && step == 1) {
                    SH_LOGIN_CHECK();
                }
            }
        });
    }
}
