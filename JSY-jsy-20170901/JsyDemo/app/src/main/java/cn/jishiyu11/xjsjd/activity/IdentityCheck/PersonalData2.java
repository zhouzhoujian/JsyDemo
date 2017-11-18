package cn.jishiyu11.xjsjd.activity.IdentityCheck;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jishiyu11.xjsjd.EntityClass.Personal2LifeAreJsonBean;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.control.MyTextView;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.DisplayUtils;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.JsonFileReader;
import cn.jishiyu11.xjsjd.utils.PublicClass.ShowDialog;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.TimeUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import okhttp3.Request;

/**
 * Created by jsy_zj on 2017/11/7.
 * <p>
 * 小额钱袋第二步
 * 个人资料
 */

public class PersonalData2 extends BaseActivity implements View.OnClickListener, DataCallBack {

    private MyTextView personal2_dwell_style, personal2_profession, personal2_education_text, personal2_merriage_text, personal2_life_arename, personal2_relation;
    private String[] dwell_styles = {"有住房，无房贷", "有住房，有房贷", "与父母/配偶同住"
            , "租房同住", "单位宿舍/用房", "学生公寓"};
    private String[] education_state = {"硕士及以上", "本科", "大专", "中专/高中及以下"};
    private String[] merriage_state = {"已婚", "未婚", "离异"};
    private String[] profession_state = {"企业主", "个体工商户", "上班人群", "学生", "无固定职业"};
    private String[] relation_state = {"父母","配偶","兄弟","姐妹"};


    private EditText personal2_mail, personal2_life_adress, personal2_company, personal2_company_adress, personal2_company_phone, personal2_kinsfolk,
            personal2_kinsfolk_phonenum, personal2_emergency_contact_name, personal2_emergency_contact_phonenum;

    private ArrayList<Personal2LifeAreJsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    public static Activity PERSONALDATAS2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_data);
        getHpptuserInfo();
        findViewById();
        PERSONALDATAS2 = this;
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
        initJsonData();
    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_loan_personal_data));


        findViewById(R.id.personal2_dwell_style_layout).setOnClickListener(this);//居住方式layout
        findViewById(R.id.personal2_education).setOnClickListener(this);//学历
        findViewById(R.id.personal2_merriage_state).setOnClickListener(this);//婚姻状况
        findViewById(R.id.personal2_profession_state).setOnClickListener(this);//职业
        findViewById(R.id.personal2_life_arename_state).setOnClickListener(this);//省市区
        findViewById(R.id.personal2_relation_state).setOnClickListener(this);//亲属关系

        personal2_dwell_style = findViewById(R.id.personal2_dwell_style);//居住方式text

        personal2_mail = findViewById(R.id.personal2_mail);//个人邮箱
        personal2_profession = findViewById(R.id.personal2_profession);//职业
        personal2_life_adress = findViewById(R.id.personal2_life_adress);//居住地址
        personal2_education_text = findViewById(R.id.personal2_education_text);//学历
        personal2_merriage_text = findViewById(R.id.personal2_merriage_text);//婚姻

        personal2_life_arename = findViewById(R.id.personal2_life_arename);//省市区
        personal2_company = findViewById(R.id.personal2_company);//公司名称
        personal2_company_adress = findViewById(R.id.personal2_company_adress);//公司地址
        personal2_company_phone = findViewById(R.id.personal2_company_phone);//公司电话
        personal2_kinsfolk = findViewById(R.id.personal2_kinsfolk);//亲属
        personal2_relation = findViewById(R.id.personal2_relation);//关系
        personal2_kinsfolk_phonenum = findViewById(R.id.personal2_kinsfolk_phonenum);//亲属电话
        personal2_emergency_contact_name = findViewById(R.id.personal2_emergency_contact_name);//紧急联系人
        personal2_emergency_contact_phonenum = findViewById(R.id.personal2_emergency_contact_phonenum);//紧急联系人手机号码

        findViewById(R.id.personal2_data_next).setOnClickListener(this);//

    }

    @Override
    protected void initView() {
    }

    private void getHpptuserInfo() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", Long.parseLong(SharedPreferencesUtils.get(this, "uid", "").toString()));
        OkHttpManager.postAsync(HttpURL.getInstance().USERINFO, "username_uid", map, this);
    }

    private void getHttp() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", Long.parseLong(SharedPreferencesUtils.get(this, "uid", "").toString()));
        OkHttpManager.postAsync(HttpURL.getInstance().USERINFOADD, "username_add", map, this);
    }

    /**
     * 个人资料拉去
     */
    private void getHttpstater() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", Long.parseLong(SharedPreferencesUtils.get(this, "uid", "").toString()));
        OkHttpManager.postAsync(HttpURL.getInstance().STATUS, "username_list", map, this);
    }

    private void postPersonalData2ToService() {

        Map<String, Object> map = new HashMap<>();
        map.put("uid", SharedPreferencesUtils.get(this, "uid", ""));
        map.put("edu", personal2_education_text.getText() + "");
        map.put("career", personal2_profession.getText() + "");
        map.put("company_name", personal2_company.getText() + "");
        map.put("company_address", personal2_company_adress.getText() + "");
        map.put("company_phone", personal2_company_phone.getText() + "");
        map.put("marital_status", personal2_merriage_text.getText() + "");
        map.put("emergency_contact", personal2_emergency_contact_name.getText() + "");
        map.put("contact_mobile", personal2_emergency_contact_phonenum.getText() + "");
        map.put("kinsfolk", personal2_kinsfolk.getText() + "");
        map.put("kinsfolk_mobile", personal2_kinsfolk_phonenum.getText() + "");
        map.put("relation", personal2_relation.getText() + "");
        map.put("email", personal2_mail.getText() + "");
        map.put("live_address", personal2_life_arename.getText() + "");
        map.put("detail_address", personal2_life_adress.getText() + "");
        map.put("live_type", personal2_dwell_style.getText() + "");

        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL2_DATAS, "postPersonalData2ToService", map, this);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image://返回
                finish();
                break;
            case R.id.title_complete://完成
                getHttp();
                break;
            case R.id.personal2_dwell_style_layout://居住方式选择

                if (TimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //弹出Toast或者Dialog
                    ShowDialog.getInstance().getDialog(this, getcar_estate(), "personal2_dwell_style", mHandler, 1003);
                }

                break;
            case R.id.personal2_education:
                //弹出Toast或者Dialog
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //弹出Toast或者Dialog
                    ShowDialog.getInstance().getDialog(this, geteducation_state(), "personal2_education", mHandler, 1000);
                }
                break;
            case R.id.personal2_merriage_state:
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //弹出Toast或者Dialog
                    ShowDialog.getInstance().getDialog(this, getmerriage_state(), "personal2_merriage_state", mHandler, 1001);
                }
                break;
            case R.id.personal2_profession_state:
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //弹出Toast或者Dialog
                    ShowDialog.getInstance().getDialog(this, getprofession_state(), "personal2_profession_state", mHandler, 1002);
                }
                break;
            case R.id.personal2_relation_state:
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                } else {
                    //弹出Toast或者Dialog
                    ShowDialog.getInstance().getDialog(this, getrelation_state(), "personal2_relation_state", mHandler, 1004);
                }
                break;

            case R.id.personal2_life_arename_state:

                showPickerView();

                break;
            case R.id.personal2_data_next://下一步
                //进行上传和界面跳转

                goNextPager();
//                Intent intent = new Intent(this,PersonalData3.class);
//                this.startActivity(intent);
                break;
        }
    }

    private void goNextPager() {
        if (!TextUtils.isEmpty(personal2_mail.getText())
                && !TextUtils.isEmpty(personal2_education_text.getText())
                && !TextUtils.isEmpty(personal2_merriage_text.getText())
                && !TextUtils.isEmpty(personal2_profession.getText())
                && !TextUtils.isEmpty(personal2_life_adress.getText())
                && !TextUtils.isEmpty(personal2_dwell_style.getText())
                && !TextUtils.isEmpty(personal2_company.getText())
                && !TextUtils.isEmpty(personal2_company_adress.getText())
                && !TextUtils.isEmpty(personal2_company_phone.getText())
                && !TextUtils.isEmpty(personal2_kinsfolk.getText())
                && !TextUtils.isEmpty(personal2_relation.getText())
                && !TextUtils.isEmpty(personal2_kinsfolk_phonenum.getText())
                && !TextUtils.isEmpty(personal2_emergency_contact_name.getText())
                && !TextUtils.isEmpty(personal2_emergency_contact_phonenum.getText())
                && !TextUtils.isEmpty(personal2_life_arename.getText())
                ) {
            if (!StringUtil.isMobileNO(personal2_kinsfolk_phonenum.getText() + "")) {
                ToatUtils.showShort1(this, "手机号填写错误1");
            } else {
                if (!StringUtil.isMobileNO(personal2_emergency_contact_phonenum.getText() + "")) {
                    ToatUtils.showShort1(this, "手机号填写错误2");
                } else {
                    //进行上传和界面跳转
                    String username = SharedPreferencesUtils.get(this, "username", "") + "";
                    String phone1 = personal2_kinsfolk_phonenum.getText() + "";//亲属
                    String phone2 = personal2_emergency_contact_phonenum.getText() + "";//紧急联系人

                    String kinsfolk = personal2_kinsfolk.getText() + "";
                    String emergency = personal2_emergency_contact_name.getText() + "";

                    if (phone1.equals(phone2)) {
                        ToatUtils.showShort1(this, "家属和紧急联系人号码不能相同");
                    } else {
                        if (phone1.equals(username)
                                || phone2.equals(username)
                                ) {
                            ToatUtils.showShort1(this, "家属和紧急联系人号码不能和本人号码相同");
                        } else {
                            if (kinsfolk.equals(emergency)) {
                                ToatUtils.showShort1(this, "家属和紧急联系人姓名不能相同");
                            } else {
                                postPersonalData2ToService();
                            }
                        }

                    }
                }
            }
        } else {
            ToatUtils.showShort1(this, "请将信息填写完整");
        }
    }

    /**
     * 居住方式
     *
     * @return
     */
    private List<Map<String, Object>> getcar_estate() {
        List<Map<String, Object>> list_car_estate = new ArrayList<>();
        for (String aPurpose : dwell_styles) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", aPurpose);
            map.put("boolean", "1");
            list_car_estate.add(map);
        }
        return list_car_estate;
    }

    private List<Map<String, Object>> geteducation_state() {
        List<Map<String, Object>> list_car_estate = new ArrayList<>();
        for (String aPurpose : education_state) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", aPurpose);
            map.put("boolean", "1");
            list_car_estate.add(map);
        }
        return list_car_estate;
    }

    private List<Map<String, Object>> getmerriage_state() {
        List<Map<String, Object>> list_car_estate = new ArrayList<>();
        for (String aPurpose : merriage_state) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", aPurpose);
            map.put("boolean", "1");
            list_car_estate.add(map);
        }
        return list_car_estate;
    }

    private List<Map<String, Object>> getprofession_state() {
        List<Map<String, Object>> list_car_estate = new ArrayList<>();
        for (String aPurpose : profession_state) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", aPurpose);
            map.put("boolean", "1");
            list_car_estate.add(map);
        }
        return list_car_estate;
    }
  private List<Map<String, Object>> getrelation_state() {//亲属关系
        List<Map<String, Object>> list_car_estate = new ArrayList<>();
        for (String aPurpose : relation_state) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", aPurpose);
            map.put("boolean", "1");
            list_car_estate.add(map);
        }
        return list_car_estate;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1000:
                    personal2_education_text.setText(msg.obj.toString());
                    personal2_education_text.setTextColor(Color.BLACK);
                    break;
                case 1001:
                    personal2_merriage_text.setText(msg.obj.toString());
                    personal2_merriage_text.setTextColor(Color.BLACK);
                    break;
                case 1002:
                    personal2_profession.setText(msg.obj.toString());
                    personal2_profession.setTextColor(Color.BLACK);
                    break;
                case 1003:
                    personal2_dwell_style.setText(msg.obj.toString());
                    personal2_dwell_style.setTextColor(Color.BLACK);
                    break;
                case 1004:
                    personal2_relation.setText(msg.obj.toString());
                    personal2_relation.setTextColor(Color.BLACK);
                    break;
            }
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String complete = "";
        if (!TextUtils.isEmpty(data.getExtras().getString("complete"))) {
            complete = data.getExtras().getString("complete");
        }
        switch (requestCode) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getHttpstater();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        switch (name) {
            case "postPersonalData2ToService":
                ToatUtils.showShort1(this, "上传未成功，请检查网络");
                break;
        }

    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {

        switch (name) {
            case "postPersonalData2ToService":
                JSONObject object = new JSONObject(result);
                String msg = object.getString("msg") + "";
                if (!TextUtils.isEmpty(msg) && msg.equals("成功")) {
                    ToatUtils.showShort1(this, "完成");
                    Intent intent = new Intent(this, PersonalData3.class);
                    this.startActivity(intent);
//                    finish();
                }
                break;
        }
    }

    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String text = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                personal2_life_arename.setText(text);
            }
        }).setTitleText("省市区三级联动")
                .setDividerColor(Color.GRAY)
                .setTextColorCenter(Color.GRAY)
                .setContentTextSize(DisplayUtils.dip2px(this, 14))
                .setOutSideCancelable(false)
                .build();
          /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {   //解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        //  获取json数据
        String JsonData = JsonFileReader.getJson(this, "province_data.json");
        ArrayList<Personal2LifeAreJsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);

            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
    }

    public ArrayList<Personal2LifeAreJsonBean> parseData(String result) {//Gson 解析
        ArrayList<Personal2LifeAreJsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                Personal2LifeAreJsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), Personal2LifeAreJsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

}
