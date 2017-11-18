package cn.jishiyu11.xjsjd.base;

import android.app.Application;
import android.widget.Toast;

import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by vvguoliang on 2017/6/23.
 *
 * Application 开始打开启动页面
 */

public class JSYApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // 设置该CrashHandler为程序的默认处理器
        UnCeHandler catchExcep = new UnCeHandler( this );
        Thread.setDefaultUncaughtExceptionHandler( catchExcep );

        //初始化链接地址
        HttpURL.getInstance().initUrl( this );
        //使用集成测试服务
        MobclickAgent.setDebugMode( true );
    }


}
