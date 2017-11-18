package cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;

/**
 * Created by jsy_zj on 2017/11/6.
 */

public class IntegralShop extends BaseActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.integralshop_layout);
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText(this.getString(R.string.integral_shop_title_name));


    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image:
                finish();
                break;
        }
    }
}
