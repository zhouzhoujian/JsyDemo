package cn.jishiyu11.xjsjd.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.Share.BitmapUtils;


@SuppressWarnings({"ResourceType", "AccessStaticViaInstance"})
@SuppressLint({"PrivateApi", "InlinedApi"})
public class ImmersiveUtils {

    private final static int STATE_BAR_ID = 10000;

    /**
     * 单例对象实例
     */
    private static class ImmersiveUtilsHolder {
        static final ImmersiveUtils INSTANCE = new ImmersiveUtils();
    }

    public static ImmersiveUtils getInstance() {
        return ImmersiveUtils.ImmersiveUtilsHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private ImmersiveUtils() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }

    /**
     * 需要设置activity主题为<item name="android:windowContentOverlay">@null</item>
     *
     * @param activity
     * @param color
     */
    public static void setStateBar(Activity activity, int color) {
        if (BuildVERSION()) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(false);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            statusView.setId(STATE_BAR_ID);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (decorView.findViewById(STATE_BAR_ID) != null) {
                decorView.removeView(decorView.findViewById(STATE_BAR_ID));
            }
            decorView.addView(statusView);
        }
    }

    /**
     * 需要设置activity主题为<item name="android:windowContentOverlay">@null</item>
     *
     * @param activity color
     */
    public static void setStateBar(Activity activity, Bitmap bitmap) {
        if (BuildVERSION()) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(false);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, bitmap);
            statusView.setId(STATE_BAR_ID);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            if (decorView.findViewById(STATE_BAR_ID) != null) {
                decorView.removeView(decorView.findViewById(STATE_BAR_ID));
            }
            decorView.addView(statusView);
        }
    }

    /**
     * * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color
     * 状态栏颜色值 * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, int color) {
        // 获得状态栏高度
        int statusBarHeight = getStateHeight(activity);

        // 绘制一个和状态栏一样高的矩形
        View statusView = new View(activity);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setBackgroundColor(color);
        return statusView;
    }

    public static int getStateHeight(Activity activity) {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        int statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        return statusBarHeight;
    }

    /**
     * * 生成一个和状态栏大小相同的矩形条 * * @param activity 需要设置的activity * @param color
     * 状态栏颜色值 * @return 状态栏矩形条
     */
    private static View createStatusView(Activity activity, Bitmap bitmap) {
        // 获得状态栏高度
        int statusBarHeight = getStateHeight(activity);

        // 绘制一个和状态栏一样高的矩形
        ImageView statusView = new ImageView(activity);
        statusView.setScaleType(ScaleType.FIT_XY);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                statusBarHeight);
        statusView.setLayoutParams(params);
        statusView.setImageBitmap(bitmap);
        return statusView;
    }

    /**
     * 解决7。0以上版本中出现问题
     * 版本 5。0以上版本才能用
     */

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static void Colorgerle(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);//完全透明
    }

    /**
     * 封装状态栏中出现问题
     */
    public static void stateBarTint(Activity activity, String color, boolean flag, boolean isImmerStateBar) {
        if (BuildVERSION()) {
            if (isImmerStateBar)
                ImmersiveUtils.setStateBar(activity, Color.parseColor(color));
            FlymeSetStatusBarLightMode(activity, activity.getWindow(), flag);
            MIUISetStatusBarLightMode(activity, activity.getWindow(), flag);
            if (flag)
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if (Build.VERSION.SDK_INT >= 23)
                Colorgerle(activity);
        }
    }

    public static void FlymeSetStatusBarLightMode(Activity activity, Window window, boolean dark) {
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    //产品页和发现页状态栏字体颜色为黑色，如果标识为BLACK，则跳过
                    if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString())
                            && SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString().contains("BLACK")) {

                    } else {
                        value &= ~bit;//清除黑色字体
                    }
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
            } catch (Exception ignored) {

            }
        }
    }

    public static void MIUISetStatusBarLightMode(Activity activity, Window window, boolean dark) {
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    //产品页和发现页状态栏字体颜色为黑色，如果标识为BLACK，则跳过
                    if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString())
                            && SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString().contains("BLACK")) {

                    } else {
                        extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                    }
                }
            } catch (Exception ignored) {

            }
        }
    }

    /**
     * 小米 魅族 版本 的判断返回 true 或者 false
     */
    public static boolean BuildVERSION() {
        return Build.VERSION.SDK_INT >= 19 || Build.BRAND.contains("Meizu") || Build.BRAND.contains("Xiaomi");
    }

    /**
     * 已知系统类型时，设置状态栏黑色字体图标
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     */
    public static void StatusBarLightMode(Activity activity) {
        //产品页和发现页状态栏字体颜色为黑色，如果标识为BLACK，则跳过
        if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString())
                && SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString().contains("WHITE")) {
            return;
        }
        if (BuildVERSION()) {
            MIUISetStatusBarLightMode(activity, activity.getWindow(), true);
            FlymeSetStatusBarLightMode(activity, activity.getWindow(), true);
            if (Build.VERSION.SDK_INT >= 23) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }
    }

    /**
     * Fragment使用
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体，即白色字体
     */
    public static void StatusFragmentBarDarkMode(Activity activity) {
        //产品页和发现页状态栏字体颜色为黑色，如果标识为BLACK，则跳过
        if (!StringUtil.isNullOrEmpty(SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString())
                && SharedPreferencesUtils.get(activity, "STATUS_FONT_COLOR", "").toString().contains("BLACK")) {
            return;
        }
        //fragment首页和我的页状态栏字体颜色为白色
        if (BuildVERSION()) {
            MIUISetStatusBarLightMode(activity, activity.getWindow(), false);
            FlymeSetStatusBarLightMode(activity, activity.getWindow(), false);
            if (Build.VERSION.SDK_INT >= 23) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * Activity使用
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体，即白色字体
     */
    public static void StatusBarDarkMode(Activity activity) {
        //状态栏背景色为红色时字体颜色为白色
        if (BuildVERSION()) {
            MIUISetStatusBarLightMode(activity, activity.getWindow(), false);
            FlymeSetStatusBarLightMode(activity, activity.getWindow(), false);
            if (Build.VERSION.SDK_INT >= 23) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 设置状态栏颜色 * * @param activity 需要设置的activity * @param color 状态栏颜色值
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void setColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 生成一个状态栏大小的矩形
            View statusView = createStatusView(activity, color);
            // 添加 statusView 到布局中
            ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
            decorView.addView(statusView);
            // 设置根布局的参数
            ViewGroup rootView = (ViewGroup) ((ViewGroup) activity.findViewById(android.R.id.content)).getChildAt(0);
            rootView.setFitsSystemWindows(true);
            rootView.setClipToPadding(true);
        }
    }

    /**
     * 设置黑色
     *
     * @param activity
     */
    public void getBlack(Activity activity) {
        setTranslucentStatus(activity, true);
        stateBarTint(activity, "#00000000", true, false);
        StatusBarLightMode(activity);
    }

    /**
     * 设置白色
     *
     * @param activity
     */
    public void getWhite(Activity activity) {
        setTranslucentStatus(activity, true);
        stateBarTint(activity, "#00000000", true, false);
        //设置状态栏白色字体
        StatusFragmentBarDarkMode(activity);
    }

    public void getW_add_B(Activity activity) {
        setTranslucentStatus(activity, true);
//        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.personal_center_title_back);
        Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(),R.mipmap.work_task_back);
//        setStateBar(activity, Color.parseColor("#81cffc"));//设置状态栏为湖蓝色
//        setStateBar(activity, Color.parseColor("#ffffff"));
//        setStateBar(activity, R.mipmap.personal_center_title_back);
        setStateBar(activity,bitmap);
        stateBarTint(activity, "#305591", true, false);
        //设置状态栏白色字体
        StatusFragmentBarDarkMode(activity);
    }

    @TargetApi(19)
    protected void setTranslucentStatus(Activity activity, boolean no) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        winParams.flags |= bits;
        win.setAttributes(winParams);
    }

}
