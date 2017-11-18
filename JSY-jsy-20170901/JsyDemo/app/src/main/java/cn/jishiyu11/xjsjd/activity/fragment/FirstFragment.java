package cn.jishiyu11.xjsjd.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.jishiyu11.xjsjd.EntityClass.FragmengData.FirstPagerDisplayData;
import cn.jishiyu11.xjsjd.EntityClass.model.JsonBean;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData2;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData3;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData4;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalDataCertificatesActivity;
import cn.jishiyu11.xjsjd.activity.LogoActivity;
import cn.jishiyu11.xjsjd.activity.MainActivity;
import cn.jishiyu11.xjsjd.base.BaseFragment;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.DisplayUtils;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import cn.jishiyu11.xjsjd.view.Marquee;
import cn.jishiyu11.xjsjd.view.MarqueeView;
import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/23.
 * <p>
 * 首页
 */
@SuppressWarnings({"deprecation", "ConstantConditions", "ResultOfMethodCallIgnored"})
@SuppressLint({"ValidFragment", "InflateParams"})
public class FirstFragment extends BaseFragment implements View.OnClickListener, DataCallBack {

    private Activity mActivity;
    private MarqueeView marqueeView;
    private List<Marquee> marquees;
    private String[] welcomeArrays;//成员变量
    private SeekBar seekbar_ed_bar, seekbar_date_bar;
    private TextView seekbar_ed;
    private int seekbar_ed_wid;
    private String[] loanDates;

    private ImageView first_icon;

    private TextView first_ed, first_ed_chosed, first_ed_fw, seekbar_date, first_date_chosed, first_Date_fw, first_success_loan;

    private FirstPagerDisplayData displayData;

    private Button first_now_apply;

    private LinearLayout ed_layout,data_layout;
    private int PERSONALDATASTATUS;

    private int minEd;
    private int maxEd;
    private int minData;
    private int maxData;

    public FirstFragment() {
        super();
    }

    public FirstFragment(Activity activity) {
        super(activity);
        this.mActivity = activity;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mActivity = (Activity) context;
    }

    @Override
    protected int getLayout() {
        return R.layout.fra_firstfragment;
    }

    @Override
    protected void initView() {
        first_icon = this.getmRootView().findViewById(R.id.first_icon);
        marqueeView = this.getmRootView().findViewById(R.id.marqueeView);//顶部滚动条控件
        seekbar_ed_bar = this.getmRootView().findViewById(R.id.seekbar_ed_bar);//金额拖动条
        seekbar_ed = this.getmRootView().findViewById(R.id.seekbar_ed);//金额textview
        first_ed = this.getmRootView().findViewById(R.id.first_ed);
        first_ed_chosed = this.getmRootView().findViewById(R.id.first_ed_chosed);
        first_ed_fw = this.getmRootView().findViewById(R.id.first_ed_fw);
        seekbar_date_bar = this.getmRootView().findViewById(R.id.seekbar_date_bar);
        first_date_chosed = this.getmRootView().findViewById(R.id.first_date_chosed);//借款拖动条
        seekbar_date = this.getmRootView().findViewById(R.id.seekbar_date);//借款选择
        first_Date_fw = this.getmRootView().findViewById(R.id.first_date_fw);//借款期限范围

        first_success_loan = this.getmRootView().findViewById(R.id.first_success_loan);//成功借款次数

//        first_now_apply = this.getmRootView().findViewById(R.id.first_now_apply);//立即申请
        this.getmRootView().findViewById(R.id.first_now_apply).setOnClickListener(this);

        ed_layout = this.getmRootView().findViewById(R.id.ed_layout);
        data_layout = this.getmRootView().findViewById(R.id.data_layout);

        setMarqueeViewData();
        seekbar_ed_wid = DisplayUtils.dip2px(mActivity, 300);
        seekbar_ed_bar.setOnSeekBarChangeListener(new Seekbarchangelisenter());
        seekbar_date_bar.setOnSeekBarChangeListener(new Seekbarchangelisenter2());

        getFirstPagerData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_now_apply:
                //进行申请判断操作
                if (StringUtil.isNullOrEmpty( SharedPreferencesUtils.get( mActivity, "uid", "" ).toString() )) {//判断是否登录    否
                    mActivity.startActivity( new Intent( mActivity, LogoActivity.class ) );
                }else {
                    Intent intent;
                    PERSONALDATASTATUS = 2;
                    if (PERSONALDATASTATUS ==0){
                        intent = new Intent(mActivity, PersonalDataCertificatesActivity.class);
                        startActivity(intent);
                    }else if (PERSONALDATASTATUS ==1){
                        intent = new Intent(mActivity, PersonalData2.class);
                        startActivity(intent);
                    }else if (PERSONALDATASTATUS ==2){
                        intent = new Intent(mActivity, PersonalData3.class);
                        startActivity(intent);
                    }else if (PERSONALDATASTATUS == 3){
                        intent = new Intent(mActivity, PersonalData4.class);
                        startActivity(intent);
                    }else if (PERSONALDATASTATUS == 4){
                        MainActivity activity = (MainActivity) getActivity();//显示借款界面
                        activity.showSwichFragment(1);
                    }

                }


                break;
        }
    }



    private void setMarqueeViewData() {//设置向上轮播图
        marquees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Marquee marquee = new Marquee();
            marquee.setContext(getWellcometips() + "**  " + phoneStarnum() + "****" + lastNum4Phone() + " 今日借款" + Random2() + "元" + " 已到账");
            marquees.add(marquee);
        }
        marqueeView.startWithList(marquees);
    }

    /**
     * 获取首页数据
     */
    private void getFirstPagerData() {

        Map<String, Object> map = new HashMap<>();

        OkHttpManager.postAsync(HttpURL.getInstance().FIRST_PAGER_DATA, "first_pager_data", map, this);
    }
    /**
     * 获取资料填写进度
     */
    private void getPersonalDataStatus(){
        Map<String,Object> map =new HashMap<>();
        map.put("uid",SharedPreferencesUtils.get( mActivity, "uid", "" ));
        OkHttpManager.postAsync(HttpURL.getInstance().PERSONAL_DATA_STATUS,"personalDataStatus"
        ,map,this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isNullOrEmpty( SharedPreferencesUtils.get( mActivity, "uid", "" ).toString() )) {//判断是否登录    否
            getPersonalDataStatus();
        }
//        getFirstPagerData();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FristFragment");
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {

    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {

        switch (name) {
            case "first_pager_data":
                if (result.contains("成功")) {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject data = jsonObject.getJSONObject("data");
                    displayData = new FirstPagerDisplayData();
                    displayData.setId(data.getString("id") + "");
                    displayData.setAmount_range(data.getString("amount_range") + "");
                    displayData.setTime_limit(data.getString("time_limit") + "");
                    displayData.setUnit(data.getString("unit") + "");
                    displayData.setCreated_at(data.getString("created_at") + "");
                    displayData.setUpdated_at(data.getString("updated_at") + "");
                    initViewData();
                }
                break;
            case "personalDataStatus":

                JSONObject jsonObject =new JSONObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                String status = data.get("status")+"";
                PERSONALDATASTATUS = Integer.parseInt(status);
                break;
        }

    }

    private void initViewData() {

        String amount_rang = displayData.getAmount_range().substring(1, displayData.getAmount_range().length() - 1);
        String[] ed_rang = amount_rang.split(",");
        first_ed.setText(ed_rang[1]);

        first_ed_chosed.setText(ed_rang[0]);
        first_ed_fw.setText(ed_rang[0] + "-" + ed_rang[1] + "元");
        seekbar_ed.setText(ed_rang[0] + "元");

        minEd = Integer.parseInt(ed_rang[0]);
        maxEd = Integer.parseInt(ed_rang[1]);

        int rangEd = maxEd - minEd;
        int seekbarEdLeng = rangEd / 100;

        seekbar_ed_bar.setMax(seekbarEdLeng);

        String time_limit = displayData.getTime_limit().substring(1, displayData.getTime_limit().length() - 1);
        String[] data_rang = time_limit.split(",");//区间值


        first_date_chosed.setText(data_rang[0] + "");
        first_Date_fw.setText(data_rang[0] + "-" + data_rang[1] + displayData.getUnit());
        seekbar_date.setText(data_rang[0]+displayData.getUnit());

//        minData = Integer.parseInt(data_rang[0]);
//        maxData = Integer.parseInt(data_rang[1]);
//        int rangData = maxData - minData;
//        seekbar_date_bar.setMax(rangData);

        loanDates = time_limit.split(",");//数组值
//        loanDates =new String[]{"3","5","7","9","15","27","30"};
        first_date_chosed.setText(data_rang[0] + "");
        first_Date_fw.setText(loanDates[0] + "-" + loanDates[loanDates.length-1] + displayData.getUnit());
        seekbar_date.setText(data_rang[0]+displayData.getUnit());
        seekbar_date_bar.setMax(loanDates.length-1);


    }

    public class Seekbarchangelisenter implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getMax() > 1) {

                double i = seekbar_ed_wid / (seekBar.getMax());
//                if (progress > (seekBar.getMax() / 2)) {
//                    ed_layout.setGravity(Gravity.RIGHT);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0, 0, (int) (seekbar_ed_wid - (i * progress)), 0);//4个参数按顺序分别是左上右下
//                    seekbar_ed.setLayoutParams(layoutParams);
//                } else {
                    ed_layout.setGravity(Gravity.LEFT);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins((int) (i * progress * 0.9), 0, 0, 0);//4个参数按顺序分别是左上右下
                    layoutParams.setMargins((int) (i * progress * 0.9), 0, 0, 0);//4个参数按顺序分别是左上右下
                    seekbar_ed.setLayoutParams(layoutParams);
//                }
                seekbar_ed.setText(minEd + (progress * 100) + "元");
                first_ed_chosed.setText(minEd + (progress * 100) + "");
            }


        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    public class Seekbarchangelisenter2 implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (seekBar.getMax() > 1) {
//                double i = seekbar_ed_wid / (seekBar.getMax() - 1);
                double i = seekbar_ed_wid / (seekBar.getMax());
//                if (progress > (seekBar.getMax() / 2)) {
//                    data_layout.setGravity(Gravity.RIGHT);
//                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(0, 0, (int) (seekbar_ed_wid - (i * progress)), 0);//4个参数按顺序分别是左上右下
//                    seekbar_date.setLayoutParams(layoutParams);
//                }else {
                    data_layout.setGravity(Gravity.LEFT);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins((int) (i*progress*0.75), 0,0, 0);//4个参数按顺序分别是左上右下
                    layoutParams.setMargins((int) (i*progress*0.9), 0,0, 0);//4个参数按顺序分别是左上右下
                    seekbar_date.setLayoutParams(layoutParams);

//                }
//                seekbar_date.setText(minData + progress * 1 + displayData.getUnit());
//                first_date_chosed.setText(minData + progress * 1 + "");

                  seekbar_date.setText(loanDates[progress] + displayData.getUnit());
                first_date_chosed.setText(loanDates[progress] + "");

            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    // 获得欢迎语资源
    public String getWellcometips() {
        welcomeArrays = this.getResources().getStringArray(R.array.tips);
        int id = (int) (Math.random() * (welcomeArrays.length - 1));//随机产生一个index索引
        return welcomeArrays[id];
    }

    private String phoneStarnum() {
        int i = 0;
        i = new Random().nextInt(23);

        return this.getResources().getStringArray(R.array.phonestarnum)[i] + "";
    }

    //生成随机数 手机后4为随机数
    private String testRandom1() {
        return (new Random()).nextInt(9) + "";
    }

    //生成随机数 手机后4为随机数
    private String testRandomphoneLast() {
        return (new Random()).nextInt(8999) + 1000 + "";
    }

    private String lastNum4Phone() {
        String lastnum = "";
        lastnum = testRandom1() + testRandom1() + testRandom1() + testRandom1();

        return lastnum;
    }

    private String Random2() {
        return (new Random()).nextInt(39) * 500 + 500 + "";
    }


}
