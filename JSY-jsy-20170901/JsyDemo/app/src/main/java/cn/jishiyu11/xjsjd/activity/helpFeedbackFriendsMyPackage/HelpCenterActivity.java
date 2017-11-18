package cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by vvguoliang on 2017/6/28.
 * 帮助中心
 */

public class HelpCenterActivity extends BaseActivity implements View.OnClickListener {

    private ImageView help_application_image;
    private LinearLayout help_application_linear;
    private ImageView help_application_loan_image;
    private LinearLayout help_application_loan_linear;
    private ImageView help_application_loan_success_image;
    private LinearLayout help_application_loan_success_linear;
    private ImageView help_application_loan_repayment_image;
    private LinearLayout help_application_loan_repayment_linear;
    private ImageView help_application_loan_order_image;
    private LinearLayout help_application_loan_order_linear;
    private ImageView help_application_loan_fail_image;
    private LinearLayout help_application_loan_fail_linear;

    private boolean application_image = false;
    private boolean application_loan_image = false;
    private boolean application_loan_success_image = false;
    private boolean application_loan_repayment_image = false;
    private boolean application_loan_order_image = false;
    private boolean application_loan_fail_image = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_help_center);
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
            case R.id.title_image:
                finish();
                break;
            case R.id.help_application_lin:
            case R.id.help_application_image:
                if (application_image) {
                    application_image = false;
                    help_application_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_linear.setVisibility(View.GONE);
                } else {
                    application_image = true;
                    help_application_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_linear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.help_application_loan_lin:
            case R.id.help_application_loan_image:
                if (application_loan_image) {
                    application_loan_image = false;
                    help_application_loan_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_loan_linear.setVisibility(View.GONE);
                } else {
                    application_loan_image = true;
                    help_application_loan_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_loan_linear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.help_application_loan_success_lin:
            case R.id.help_application_loan_success_image:
                if (application_loan_success_image) {
                    application_loan_success_image = false;
                    help_application_loan_success_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_loan_success_linear.setVisibility(View.GONE);
                } else {
                    application_loan_success_image = true;
                    help_application_loan_success_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_loan_success_linear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.help_application_loan_repayment_lin:
            case R.id.help_application_loan_repayment_image:
                if (application_loan_repayment_image) {
                    application_loan_repayment_image = false;
                    help_application_loan_repayment_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_loan_repayment_linear.setVisibility(View.GONE);
                } else {
                    application_loan_repayment_image = true;
                    help_application_loan_repayment_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_loan_repayment_linear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.help_application_loan_order_lin:
            case R.id.help_application_loan_order_image:
                if (application_loan_order_image) {
                    application_loan_order_image = false;
                    help_application_loan_order_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_loan_order_linear.setVisibility(View.GONE);
                } else {
                    application_loan_order_image = true;
                    help_application_loan_order_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_loan_order_linear.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.help_application_loan_fail_lin:
            case R.id.help_application_loan_fail_image:
                if (application_loan_fail_image) {
                    application_loan_fail_image = false;
                    help_application_loan_fail_image.setImageResource(R.mipmap.ic_loan_help_up);
                    help_application_loan_fail_linear.setVisibility(View.GONE);
                } else {
                    application_loan_fail_image = true;
                    help_application_loan_fail_image.setImageResource(R.mipmap.ic_loan_help_down);
                    help_application_loan_fail_linear.setVisibility(View.VISIBLE);
                }
                break;
        }

    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.name_loan_personal_help_center));

        help_application_image = findViewById(R.id.help_application_image);
        help_application_linear = findViewById(R.id.help_application_linear);

        help_application_loan_image = findViewById(R.id.help_application_loan_image);
        help_application_loan_linear = findViewById(R.id.help_application_loan_linear);

        help_application_loan_success_image = findViewById(R.id.help_application_loan_success_image);
        help_application_loan_success_linear = findViewById(R.id.help_application_loan_success_linear);

        help_application_loan_repayment_image = findViewById(R.id.help_application_loan_repayment_image);
        help_application_loan_repayment_linear = findViewById(R.id.help_application_loan_repayment_linear);

        help_application_loan_order_image = findViewById(R.id.help_application_loan_order_image);
        help_application_loan_order_linear = findViewById(R.id.help_application_loan_order_linear);

        help_application_loan_fail_image = findViewById(R.id.help_application_loan_fail_image);
        help_application_loan_fail_linear = findViewById(R.id.help_application_loan_fail_linear);
    }

    @Override
    protected void initView() {
        help_application_image.setOnClickListener(this);
        help_application_image.setImageResource(R.mipmap.ic_loan_help_up);

        help_application_loan_image.setOnClickListener(this);
        help_application_loan_image.setImageResource(R.mipmap.ic_loan_help_up);

        help_application_loan_success_image.setOnClickListener(this);
        help_application_loan_success_image.setImageResource(R.mipmap.ic_loan_help_up);

        help_application_loan_repayment_image.setOnClickListener(this);
        help_application_loan_repayment_image.setImageResource(R.mipmap.ic_loan_help_up);

        help_application_loan_order_image.setOnClickListener(this);
        help_application_loan_order_image.setImageResource(R.mipmap.ic_loan_help_up);

        help_application_loan_fail_image.setOnClickListener(this);
        help_application_loan_fail_image.setImageResource(R.mipmap.ic_loan_help_up);

        findViewById(R.id.help_application_lin).setOnClickListener(this);
        findViewById(R.id.help_application_loan_lin).setOnClickListener(this);
        findViewById(R.id.help_application_loan_success_lin).setOnClickListener(this);
        findViewById(R.id.help_application_loan_repayment_lin).setOnClickListener(this);
        findViewById(R.id.help_application_loan_order_lin).setOnClickListener(this);
        findViewById(R.id.help_application_loan_fail_lin).setOnClickListener(this);
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
}
