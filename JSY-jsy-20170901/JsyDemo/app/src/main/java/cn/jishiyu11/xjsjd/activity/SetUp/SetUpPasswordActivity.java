package cn.jishiyu11.xjsjd.activity.SetUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.EntityClass.RegisterSignCodeModify;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.LogoActivity;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.JsonData;
import cn.jishiyu11.xjsjd.utils.PublicClass.CountDownTimerUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/28.
 * <p>
 * 修改密码
 */

@SuppressWarnings("EqualsBetweenInconvertibleTypes")
public class SetUpPasswordActivity extends BaseActivity implements View.OnClickListener, DataCallBack {

    private EditText password_phone;

    private EditText password_edittext_code;

    private Button password_button_code;

    private EditText password_edittext_pass;

    private EditText password_edittext_pass_confirm;

    private CountDownTimerUtils mCountDownTimerUtils;

    private Button password_button_confirm;

    private String name = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_up_password);
        name = getIntent().getExtras().getString("name");
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image:
                finish();
                break;
            case R.id.password_button_code:
                if (StringUtil.isNullOrEmpty(password_phone.getText().toString())) {
                    ToatUtils.showShort1(this, "您输入的手机号码不能为空");
                } else if (!StringUtil.isMobileNO(password_phone.getText().toString())) {
                    ToatUtils.showShort1(this, "您输入的手机号码不正确,请重新输入");
                    password_phone.setText("");
                } else {
                    mCountDownTimerUtils = new CountDownTimerUtils(password_button_code, 60 * 1000, 1000);
                    mCountDownTimerUtils.start();
                    Map<String, Object> map = new HashMap<>();
                    map.put("mobile", Long.parseLong(password_phone.getText().toString().trim()));
                    OkHttpManager.postAsync(HttpURL.getInstance().REGISTER_CODE, "code", map, this);
                }
                break;
            case R.id.password_button_confirm:
                if (StringUtil.isNullOrEmpty(password_phone.getText().toString())) {
                    ToatUtils.showShort1(this, "您输入的手机号码不能为空");
                } else if (!StringUtil.isMobileNO(password_phone.getText().toString())) {
                    ToatUtils.showShort1(this, "您输入的手机号码不正确,请重新输入");
                    password_phone.setText("");
                } else if (StringUtil.isNullOrEmpty(password_button_code.getText().toString())) {
                    ToatUtils.showShort1(this, "请输入验证码");
                } else {
                    Map<String, Object> map = new HashMap<>();
                    map.put("mobile", Long.parseLong(password_phone.getText().toString()));
                    map.put("code", Long.parseLong(password_edittext_code.getText().toString()));
                    if (name.equals("1")) {
                        map.put("no", AppUtil.getInstance().getChannel(SetUpPasswordActivity.this, 2));
                        OkHttpManager.postAsync(HttpURL.getInstance().REGISTERCODE, "register_code", map, this);
                    } else {
                        if (StringUtil.isNullOrEmpty(password_edittext_pass.getText().toString())) {
                            ToatUtils.showShort1(this, "您输入的新密码为空");
                        } else if (StringUtil.isNullOrEmpty(password_edittext_pass_confirm.getText().toString())) {
                            ToatUtils.showShort1(this, "您输入的确认密码为空");
                        } else if (!password_edittext_pass.getText().toString().trim().equals(
                                password_edittext_pass_confirm.getText().toString().trim())) {
                            ToatUtils.showShort1(this, "您输入的新密码和确认密码不一致");
                            password_edittext_pass.setText("");
                            password_edittext_pass_confirm.setText("");
                        } else {
                            map.put("password", password_edittext_pass_confirm.getText());
                            OkHttpManager.postAsync(HttpURL.getInstance().PASSWORD, "password_code", map, this);
                        }
                    }
                }
                break;
        }

    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        password_phone = findViewById(R.id.password_phone);
        password_edittext_code = findViewById(R.id.password_edittext_code);
        password_edittext_pass = findViewById(R.id.password_edittext_pass);
        password_edittext_pass_confirm = findViewById(R.id.password_edittext_pass_confirm);

        password_button_code = findViewById(R.id.password_button_code);
        password_button_code.setOnClickListener(this);
        password_button_confirm = findViewById(R.id.password_button_confirm);
        password_button_confirm.setOnClickListener(this);
        if (name.equals("1")) {
            title_view.setText(this.getString(R.string.name_loan_logo_register));
            password_button_confirm.setText(this.getString(R.string.name_loan_logo_register));
            findViewById(R.id.agreement_password).setVisibility(View.VISIBLE);
            findViewById(R.id.password_edittext_pass_linear).setVisibility(View.GONE);
            findViewById(R.id.password_edittext_pass_confirm_linear).setVisibility(View.GONE);
        } else {
            if (!TextUtils.isEmpty(SharedPreferencesUtils.get(this, "username", "").toString())) {
                password_phone.setText(SharedPreferencesUtils.get(this, "username", "").toString());
            }
            title_view.setText(this.getString(R.string.name_loan_set_up_password));
            password_button_confirm.setText(this.getString(R.string.name_loan_personal_data_complete));
            findViewById(R.id.agreement_password).setVisibility(View.GONE);
            findViewById(R.id.password_edittext_pass_linear).setVisibility(View.VISIBLE);
            findViewById(R.id.password_edittext_pass_confirm_linear).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void initView() {

    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        switch (name) {
            case "code":
                mCountDownTimerUtils.onFinish();
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
            case "password_code":
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
            case "register_code":
                ToatUtils.showShort1(this, this.getString(R.string.network_timed));
                break;
        }
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        JSONObject object;
        switch (name) {
            case "code":
                RegisterSignCodeModify registerSignCodeModify = JsonData.getInstance().getJsonLogoCode(result);
                if (registerSignCodeModify.getStatus() == 0) {
                    ToatUtils.showShort1(this, registerSignCodeModify.getInfo());
                    mCountDownTimerUtils.onFinish();
                } else {
                    if (registerSignCodeModify.getStatus() == 1 && registerSignCodeModify.getState().equals("success")) {
                        ToatUtils.showShort1(this, registerSignCodeModify.getInfo());
                    }
                }
                break;
            case "password_code":
                object = new JSONObject(result);
                if (object.optString("state").equals("fail")) {
                    ToatUtils.showShort1(this, object.optString("info"));
                } else {
                    SharedPreferencesUtils.put(this, "username", password_phone.getText().toString());
                    SharedPreferencesUtils.put(this, "password", password_edittext_pass_confirm.getText());
                    finish();
                }
                break;
            case "register_code":
                object = new JSONObject(result);
                if (object.optString("state").equals("fail")) {
                    ToatUtils.showShort1(this, object.optString("info"));
                } else {
                    SharedPreferencesUtils.put(this, "username", password_phone.getText().toString());
                    SharedPreferencesUtils.put(this, "uid", object.optString("uid"));
                    finish();
                }
                break;
        }
    }
}
