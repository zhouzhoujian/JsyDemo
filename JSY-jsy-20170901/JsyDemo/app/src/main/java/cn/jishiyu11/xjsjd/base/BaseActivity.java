package cn.jishiyu11.xjsjd.base;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.CommissioningActivity;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.NetWorkUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;

import java.util.List;

/**
 * @author: 秦国良
 * @data: 2016年5月16日 下午6:46:18
 * @ModifiedPerson:
 * @ModifiedPersonData：2016年5月16日下午6:46:18 @ModifyRemarks：
 * @version: V1.1.0
 * @Copyright 沙小僧
 */
@SuppressWarnings("StatementWithEmptyBody")
@SuppressLint("NewApi")
public abstract class BaseActivity extends FragmentActivity {//implements OnClickRefreshListener, OnPowerKeyListener {

    private long time = -1;

    private Receiver receiver;

    private boolean isImmerStateBar = true;

    private String color = "#ffffff";

    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        BaseActivityManager.getActivityManager().pushActivity(this);
    }

    /**
     * 禁止到老的沉浸方式
     * 在setContentView之前调用
     */
    public void setImmerStateBar(boolean isImmerStateBar) {
        this.isImmerStateBar = isImmerStateBar;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getWindow().getDecorView().setBackgroundResource(R.color.white);
        stateBarTint(color, flag);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        stateBarTint(color, flag);
    }

    @Override
    public void setContentView(View view, LayoutParams params) {
        super.setContentView(view, params);
        stateBarTint(color, flag);
    }

    /**
     * 广播
     */
    private class Receiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (receiver != null) {
            unregisterReceiver(receiver);
            receiver = null;
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode, Bundle options) {
        if (System.currentTimeMillis() - time < 1000) {
            return;
        }
        time = System.currentTimeMillis();
        IsClass(intent);
        super.startActivityForResult(intent, requestCode, options);
    }

    @Override
    protected void onSaveInstanceState(Bundle arg0) {
        super.onSaveInstanceState(arg0);
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        String _pkgName = activityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
    }

    /**
     * 绑定控件id
     */
    protected abstract void findViewById();

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
    /**
     * 通过Action启动Activity，并且含有Bundle数据
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public boolean NetWorkshow(Intent intent) {
        if (intent.getComponent() != null) {
        }
        return false;
    }

    public void IsClass(Intent intent) {
        if (NetWorkshow(intent)) {
        } else {
            if (!NetWorkUtils.isNetworkConnected(this) && intent.getComponent() != null
                    && !intent.getComponent().getClassName().equals(CommissioningActivity.class.getName())) {
                ToatUtils.showShort1(this, getString(R.string.no_network_timed));
            }
        }
    }

    public void stateBarTint(String color, boolean flag) {
        ImmersiveUtils.stateBarTint(this, color, flag, this.isImmerStateBar);
    }

    //清除状态栏黑色字体,即状态栏显示白色字体，Fragment使用
    public void statusFragmentBarDarkMode() {
        //沉浸式状态栏
        ImmersiveUtils.stateBarTint(this, "#00000000", true, false);
        //设置状态栏白色字体
        ImmersiveUtils.StatusFragmentBarDarkMode(this);
    }
}
