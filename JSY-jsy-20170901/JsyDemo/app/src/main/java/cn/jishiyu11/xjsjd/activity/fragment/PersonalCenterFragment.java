package cn.jishiyu11.xjsjd.activity.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.LogoActivity;
import cn.jishiyu11.xjsjd.activity.SetUp.SetUPActivity;
import cn.jishiyu11.xjsjd.activity.SetUp.SetUpAboutActivity;
import cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage.FeedbackActivity;
import cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage.HelpCenterActivity;
import cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage.IntegralShop;
import cn.jishiyu11.xjsjd.base.BaseFragment;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.IdcardValidator;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.view.BottomDialog;

import com.umeng.analytics.MobclickAgent;

import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/6/23.
 * <p>
 * 个人中心
 */
@SuppressWarnings({"deprecation", "ConstantConditions", "ResultOfMethodCallIgnored"})
@SuppressLint({"ValidFragment", "InflateParams"})
public class PersonalCenterFragment extends BaseFragment implements View.OnClickListener {

    private Activity mActivity;

private TextView title;

    private LinearLayout p1,p2,p3,p4;
    private LinearLayout personal3_head_layout;

    public PersonalCenterFragment() {
        super();
    }

    public PersonalCenterFragment(Activity activity) {
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
        return R.layout.fra_personalcenterfragment;
    }

    private ImageView personal_camera;
    private TextView personal_logo;

    private UserCenterRealize userCenterRealize = new UserCenterRealize();

    @Override
    protected void initView() {
        TextView title_view = (TextView) findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_personal_center));
        title = (TextView) findViewById(R.id.personal_center_title);
        title.setText(this.getString(R.string.name_personal_center));
//        personal_camera = (ImageView) findViewById(R.id.personal_camera);
//        personal_camera.setOnClickListener(this);
//
        personal_logo = (TextView) findViewById(R.id.personal_logo);
        personal_logo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        personal_logo.getPaint().setAntiAlias(true);//抗锯齿
        personal_logo.setOnClickListener(this);
        findViewById(R.id.personal3_head_layout).setOnClickListener(this);

        //        findViewById(R.id.personal_loan_my).setOnClickListener(this);
//        findViewById(R.id.personal_loan_my_package).setOnClickListener(this);
//        personal_numder = (TextView) findViewById(R.id.personal_numder);
        p1 = getmRootView().findViewById(R.id.personal_bt1);
        p2 = getmRootView().findViewById(R.id.personal_bt2);
        p3 = getmRootView().findViewById(R.id.personal_bt3);
        p4 = getmRootView().findViewById(R.id.personal_bt4);
        p1.setOnClickListener(this);
        p2.setOnClickListener(this);
        p3.setOnClickListener(this);
        p4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.personal_bt1://积分商城
                    mActivity.startActivity(new Intent(mActivity,IntegralShop.class));
//                    mActivity.startActivity(new Intent(mActivity,PersonalDataUploadActivity.class));
//                    mActivity.startActivity(new Intent(mActivity,PersonalDataCertificatesActivity.class));

                break;
            case R.id.personal_bt2://帮助中心
                mActivity.startActivity(new Intent(mActivity, HelpCenterActivity.class));
                break;
                  case R.id.personal_bt3://关于我们
                      mActivity.startActivity(new Intent(mActivity, SetUpAboutActivity.class));
                break;
            case R.id.personal_bt4://设置
                mActivity.startActivity(new Intent(mActivity, SetUPActivity.class));
                break;
            case R.id.personal3_head_layout:
            case R.id.personal_logo://登录
                if (StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(mActivity, "uid", "").toString())) {
                    mActivity.startActivity(new Intent(mActivity, LogoActivity.class));
                }
                break;
        }
    }

    // 提示对话框方法
    public void showDialog(String btn_take, String btn_pick) {
        final BottomDialog sxsDialog = new BottomDialog(mActivity, R.layout.buttom_dialog);
        sxsDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        sxsDialog.setWidthHeight(AppUtil.getInstance().Dispay(mActivity)[0], 0);
        sxsDialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button1 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo1);
        button1.setText(btn_take);
        Button button = (Button) sxsDialog.findViewById(R.id.btn_pick_photo2);
        button.setText(btn_pick);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userCenterRealize.getFileByPhotograph(mActivity);//拍照外部调用
                sxsDialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//有
            @Override
            public void onClick(View v) {
                userCenterRealize.getFileByPhotoAlbum(mActivity);//相册外部调用
                sxsDialog.dismiss();
            }
        });
        sxsDialog.setOnClick(R.id.btn_cancel, new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                sxsDialog.dismiss();
            }
        });
        if (!mActivity.isFinishing()) {
            sxsDialog.show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(mActivity, "uid", "").toString())) {
            if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(mActivity, "realname", "").toString())) {
                String realname = SharedPreferencesUtils.get(mActivity, "realname", "").toString();
                realname = realname.substring(0, 1) + IdcardValidator.getInstance().getIDager(SharedPreferencesUtils.get(mActivity, "idcard", "").toString());
                personal_logo.setText(realname);
            } else {
                String username = SharedPreferencesUtils.get(mActivity, "username", "").toString();
                personal_logo.setText("****" + username.substring(username.length() - 4, username.length()));
            }
        } else {
            personal_logo.setText(mActivity.getString(R.string.name_loan_personal_logn));
        }
        MobclickAgent.onPageStart("PersonalCenterFragment"); //统计页面，"MainScreen"为页面名称，可自定义
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
        MobclickAgent.onPageEnd("PersonalCenterFragment");
    }
}
