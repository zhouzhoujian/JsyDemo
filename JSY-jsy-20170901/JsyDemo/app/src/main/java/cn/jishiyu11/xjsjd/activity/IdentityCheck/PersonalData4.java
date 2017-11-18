package cn.jishiyu11.xjsjd.activity.IdentityCheck;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.CommonAdapter;
import cn.jishiyu11.xjsjd.utils.CustomDialogUtils;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.utils.ViewHolder;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import okhttp3.Request;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/6/27.
 * <p>
 * 绑定银行卡
 */

public class PersonalData4 extends BaseActivity implements View.OnClickListener, DataCallBack {

    private Intent intent = null;

//    private LinearLayout personal4_bank_name;

    private EditText personal4_bank_cardnum, personal4_bank_user_phonenum;

    private TextView personal4_bank_name_tv;

    private CustomDialog customDialog, customDialog2, customLoadingDialog;

    private List<String> listBankData = new ArrayList<>();

    private String[] banks = {"中国工商银行", "招商银行", "中国农业银行", "中国建设银行", "中国银行"
            , "中国民生银行", "中国光大银行", "中信银行", "交通银行", "兴业银行", "上海浦东发展银行"
            , "中国人民银行", "华夏银行", "深圳发展银行", "广东发展银行", "国家开发银行"
            , "中国邮政储蓄银行", "中国进出口银行", "中国农业发展银行", "中国银行香港分行"
            , "北京银行", "北京农村商业银行", "天津银行", "上海银行", "上海农村商业银行", "南京银行"
            , "宁波银行", "杭州市商业银行", "深圳平安银行", "深圳农村商业银行", "温州银行", "厦门国际银行"
            , "济南市商业银行", "重庆银行", "哈尔滨银行", "成都市商业银行", "包头市商业银行", "南昌市商业银行"
            , "贵阳商业银行", "兰州市商业银行", "常熟农村商业银行", "青岛市商业银行", "徽商银行"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_data_bank_card);
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
            statusFragmentBarDarkMode();
        }
        setBankArrtoList();
        setdialog();
        setdialog2();
        customLoadingDialog = CustomDialogUtils.getInstance().LoadingDialog(PersonalData4.this, "正在绑定，请等待。。。");
    }

    private void setBankArrtoList() {
        for (String s : banks) {
            listBankData.add(s);
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.personal_bank_savings_linear:
            case R.id.personal_bank_savings_card:
                ToatUtils.showShort1(this, "此功能暂时未开放");
                break;
            case R.id.title_image:
                intent = new Intent();
                intent.putExtra("complete", "2");
                setResult(108, intent);
                finish();
                break;
            case R.id.title_complete:
                intent = new Intent();
                intent.putExtra("complete", "2");
                setResult(108, intent);
                finish();
                break;
            case R.id.personal4_bank_name://银行卡名称
                customDialog.show();
                break;
            case R.id.personal4_data_next:

                IsCouldBindBankCard();

                break;
        }

    }

    /**
     * 绑定银行卡判断
     */
    private void IsCouldBindBankCard() {
        if (!TextUtils.isEmpty(personal4_bank_name_tv.getText())
                && !TextUtils.isEmpty(personal4_bank_cardnum.getText())
                && !TextUtils.isEmpty(personal4_bank_user_phonenum.getText())
                ) {
            if (!StringUtil.isMobileNO(personal4_bank_user_phonenum.getText() + "")) {
                ToatUtils.showShort1(this, "手机号填写错误1");
            } else {
                customLoadingDialog.show();
                postDataForBank();
            }
        } else {
            ToatUtils.showShort1(this, "不能为空");
        }
    }

    @Override
    protected void findViewById() {
        TextView personal_bank_savings_card = findViewById(R.id.personal_bank_savings_card);
        personal_bank_savings_card.setOnClickListener(this);

        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);

        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_loan_personal_data_bank_card));

        findViewById(R.id.personal_bank_savings_linear).setOnClickListener(this);//银行分类
        findViewById(R.id.personal4_bank_name).setOnClickListener(this);
        personal4_bank_cardnum = findViewById(R.id.personal4_bank_cardnum);
        personal4_bank_user_phonenum = findViewById(R.id.personal4_bank_user_phonenum);
        personal4_bank_name_tv = findViewById(R.id.personal4_bank_name_tv);
        findViewById(R.id.personal4_data_next).setOnClickListener(this);//绑定银行卡
    }

    @Override
    protected void initView() {

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent();
            intent.putExtra("complete", "2");
            setResult(108, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //银行名称选择界面
    private void setdialog() {
        customDialog = new CustomDialog(PersonalData4.this, R.style.mydialog);
        View contentView = LayoutInflater.from(PersonalData4.this).inflate(R.layout.personaldata4_custondialog_layout, null);
        customDialog.setContentView(contentView);
        customDialog.setCanceledOnTouchOutside(false);

        Window dialogWindow = customDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        final ListView listView = contentView.findViewById(R.id.personaldata4_dialog_listview);
        Button others = contentView.findViewById(R.id.personaldata4_dialog_button);

        listView.setAdapter(new CommonAdapter<String>(PersonalData4.this, listBankData, R.layout.personaldata4_dialog_listview_item) {
            @Override
            protected void convertlistener(ViewHolder holder, String s) {

            }

            @Override
            public void convert(ViewHolder holder, String s) {
                holder.getTextview(R.id.personaldata4_dialog_listview_i).setText(s);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                personal4_bank_name_tv.setText(listBankData.get(position));
                customDialog.dismiss();
            }
        });

        others.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog2.show();
                customDialog.dismiss();
            }
        });

    }

    //用户填写界面
    private void setdialog2() {
        customDialog2 = new CustomDialog(PersonalData4.this, R.style.mydialog);
        View contentView = LayoutInflater.from(PersonalData4.this).inflate(R.layout.dialog_input, null);
        customDialog2.setContentView(contentView);
        customDialog2.setCanceledOnTouchOutside(false);

        Window dialogWindow = customDialog2.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        TextView tv_input_title = contentView.findViewById(R.id.tv_input_title);
        final EditText et_input_value = contentView.findViewById(R.id.et_input_value);
        TextView tv_input_confirm = contentView.findViewById(R.id.tv_input_confirm);

        tv_input_title.setText("请输入银行名称");

//        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//// 接受软键盘输入的编辑文本或其它视图
//        inputMethodManager.showSoftInput(et_input_value, InputMethodManager.SHOW_FORCED);

        tv_input_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(et_input_value.getText())) {
                    personal4_bank_name_tv.setText(et_input_value.getText() + "");
                    customDialog2.dismiss();
                } else {
                    ToatUtils.showShort1(PersonalData4.this, "请输入银行名称");
                }
            }
        });


    }

    private void postDataForBank() {//上传银行卡信息
        Map<String, Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(this, "uid", ""));
//        map.put("name",SharedPreferencesUtils.get(this,"UserChinaName",""));
        map.put("bankCard", personal4_bank_cardnum.getText() + "");
//        map.put("identityCard",SharedPreferencesUtils.get(this,"ChinaIDCardId",""));
        map.put("phone", personal4_bank_user_phonenum.getText() + "");
        map.put("bankName", personal4_bank_name_tv.getText() + "");
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL_BANK_DATA, "postDataForBank", map, this);

    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        if (name.equals("postDataForBank")) {
            customLoadingDialog.dismiss();
            ToatUtils.showShort1(this, "绑定失败！");
        }
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        if (name.equals("postDataForBank")) {
            customLoadingDialog.dismiss();

            JSONObject jsonObject = new JSONObject(result);
            JSONObject object = jsonObject.getJSONObject("data");
            String resultCode = object.getString("resultCode") + "";
            if (resultCode.equals("1200")) {
                ToatUtils.showShort1(this, "绑定成功！");
                finish();
            } else {
                String desc = object.getString("desc") + "";
                ToatUtils.showShort1(this, desc);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (PersonalDataCertificatesActivity.PERSONALDATAS1 != null)
            PersonalDataCertificatesActivity.PERSONALDATAS1.finish();
        if (PersonalData2.PERSONALDATAS2 != null)
            PersonalData2.PERSONALDATAS2.finish();
        if (PersonalData3.PERSONALDATAS3 != null)
            PersonalData3.PERSONALDATAS3.finish();


    }


}
