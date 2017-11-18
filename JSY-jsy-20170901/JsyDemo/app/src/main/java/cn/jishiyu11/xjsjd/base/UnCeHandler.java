package cn.jishiyu11.xjsjd.base;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Looper;
import android.util.Log;

import cn.jishiyu11.xjsjd.utils.TimeUtils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class UnCeHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler mDefaultHandler;
    private Application application;
    private long time = 0;

    public UnCeHandler(Application application) {
        // 获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        this.application = application;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (System.currentTimeMillis() - time < 1000) {//阻止app同一时间启动多次
                return;
            }
            time = System.currentTimeMillis();
            restart(application);
        }
    }

    /**
     * 重启应用程序
     *
     * @param context
     */
    @SuppressWarnings("WrongConstant")
    public static void restart(Context context) {
//		Intent intent = new Intent(context, GuidepageActivity.class);
//		PendingIntent restartIntent = PendingIntent.getActivity(context, -1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
//		 //退出程序
//		AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//		mgr.set(AlarmManager.RTC, System.currentTimeMillis(), restartIntent); // 1秒钟后重启应用
//		BaseActivityManager.getActivityManager().exitApp();
//		// 杀死该应用进程
//		android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }
        // 使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Looper.loop();
            }
        }.start();
        collectDeviceInfo(application.getApplicationContext());
        String str = formatCrashInfo(ex);
        Log.e("", "=========" + str);
        return true;
    }

    // 用来存储设备信息和异常信息
    private Map<String, String> deviceInfos = new HashMap<>();

    /**
     * 格式化日志信息
     *
     * @param context
     */
    public void collectDeviceInfo(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                deviceInfos.put("versionName", versionName);
                deviceInfos.put("versionCode", versionCode);
            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                deviceInfos.put(field.getName(), field.get(null).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取导致崩溃的错误信息
     * @param ex
     * @return 异常内容
     */
    private String formatCrashInfo(Throwable ex) {
        StringBuffer exception = new StringBuffer();
        for (Map.Entry<String, String> entry : deviceInfos.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (key.equals("FINGERPRINT") || key.equals("TIME") || key.equals("versionCode")
                    || key.equals("versionName") || key.equals("USER") || key.equals("MODEL") || key.equals("SERIAL")) {
                if (key.equals("TIME")) {
                    value = TimeUtils.getCurrentTimeInString();
                }
                exception.append(key).append("=").append(value).append("\n");
            }
        }
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        printWriter.close();
        String result = writer.toString();
        exception.append(result);
        return exception.toString();
    }
}