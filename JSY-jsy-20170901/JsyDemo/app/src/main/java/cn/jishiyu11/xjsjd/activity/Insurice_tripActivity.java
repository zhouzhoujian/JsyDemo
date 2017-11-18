package cn.jishiyu11.xjsjd.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;

/**
 * Created by jsy_zj on 2017/11/9.
 */

public class Insurice_tripActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tripactivity);
        findViewById();
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B( this );
        }
        //沉浸式状态设置
    }

    @Override
    protected void findViewById() {
        findViewById( R.id.title_image ).setVisibility( View.VISIBLE );
        findViewById( R.id.title_image ).setOnClickListener( this );
        TextView title_view = findViewById( R.id.title_view );
        title_view.setText( this.getString( R.string.get_100w_trip2 ) );
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_image:
                finish();
                break;
        }
    }
}
