package cn.jishiyu11.xjsjd.activity.SetUp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.CommissioningActivity;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CTelephoneInfo;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.NetWorkUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.PublicPhoneDialog;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/28.
 * <p>
 * 设置
 */

public class SetUPActivity extends BaseActivity implements View.OnClickListener, DataCallBack {
    private Intent intent = null;

    private String update_url = "";

    private UserCenterRealize userCenterRealize = new UserCenterRealize();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_set_up);
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
            case R.id.set_up_about:
                startActivity(new Intent(SetUPActivity.this, SetUpAboutActivity.class));
                break;
            case R.id.set_up_password:
//                if (!TextUtils.isEmpty(SharedPreferencesUtils.get(this,"uid","")+"")){
                intent = new Intent(SetUPActivity.this, SetUpPasswordActivity.class);
                intent.putExtra("name", "2");
                startActivity(intent);
//                }else {
//                    ToatUtils.showShort1(this,"请先登录!");
//                }
                break;
            case R.id.set_up_sign_out:
                if (!TextUtils.isEmpty(SharedPreferencesUtils.get(this,"uid","")+"")){
                getPhone("提示", "您是否确认安全退出?");
                }else {
                    ToatUtils.showShort1(this,"未登录  ");
                }
                break;
            case R.id.set_up_sign_upde:
                getUPDATE();
                break;
        }

    }

    private void getUPDATE() {
        Map<String, Object> map = new HashMap<>();
        map.put("version", AppUtil.getInstance().getVersionName(1, this));
        OkHttpManager.postAsync(HttpURL.getInstance().UPDATE, "update", map, this);
    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_loan_personal_setup));

        findViewById(R.id.set_up_about).setOnClickListener(this);
        findViewById(R.id.set_up_password).setOnClickListener(this);
        findViewById(R.id.set_up_sign_upde).setOnClickListener(this);
        findViewById(R.id.set_up_sign_out).setOnClickListener(this);

    }

    @Override
    protected void initView() {

    }

    private void getPhone(String title, String msg) {
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
                getUserLogoutAuthorStatus();
                SharedPreferencesUtils.logoutSuccess(SetUPActivity.this);
                dialog.dismiss();
                finish();
            }
        });
        builder.create().show();
    }

    private void getPhone3(String title, String msg) {
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
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {

    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        JSONObject object = new JSONObject(result);
        if (name.equals("getUserLogoutAuthorStatus")){
            if (object.optString("code").equals("0000")) {
                ToatUtils.showShort1(this,"安全退出成功！");
            }

        }else {
            if (object.optString("code").equals("0001")) {
                getPhone3("提示", object.optString("msg"));
            } else if (object.optString("code").equals("0000")) {
                object = new JSONObject(object.optString("data"));
                update_url = object.optString("update_url");
                String type = object.optString("type");
                String content = object.optString("content");
                getPhone1(content, type);
            }
        }
    }

    private void getPhone1(String msg, final String phone) {
        PublicPhoneDialog.Builder builder = new PublicPhoneDialog.Builder(this);
        builder.setTiltleMsg(msg);
        if (phone.equals("2")) {
            builder.setTitle("建议更新");
            builder.setContentViewCancel("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            builder.setTitle("强制更新");
        }
        builder.setContentViewDetermine("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                userCenterRealize.getUpdata(SetUPActivity.this, update_url);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void getUserLogoutAuthorStatus(){
        Map<String,Object> map = new HashMap<>();
        String phoneDeviceId =  CTelephoneInfo.getPhoneUDID(SetUPActivity.this);
        phoneDeviceId =StringUtil.getMD5String(phoneDeviceId);
        String locaIP = NetWorkUtils.getHostIP()+"";
        locaIP = StringUtil.getMD5String(locaIP);
        map.put("deviceNo",phoneDeviceId);
        map.put("ip",locaIP);
        map.put("mobile",SharedPreferencesUtils.get(this,"username","")+"");
        OkHttpManager.postAsync(HttpURL.getInstance().USER_LOGOUT_STATUS,"getUserLogoutAuthorStatus",map,this);

    }

}
