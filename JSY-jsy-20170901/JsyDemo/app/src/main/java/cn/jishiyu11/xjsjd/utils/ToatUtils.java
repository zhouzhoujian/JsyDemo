package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ToatUtils {

    private ToatUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean isShow = true;

    public static Context context;
    private static Toast toast;

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort1(Context context, CharSequence message) {
        if (toast == null || ToatUtils.context != context) {
            ToatUtils.context = context;
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 120);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, CharSequence message) {

        if (context == null)
            return;
        if (isShow)
            ToatUtils.showShort1(context, message);
    }

    /**
     * 短时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showShort(Context context, int message) {
        if (toast == null || ToatUtils.context != context) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, CharSequence message) {
        if (toast == null || ToatUtils.context != context) {
            ToatUtils.context = context;
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 120);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 长时间显示Toast
     *
     * @param context
     * @param message
     */
    public static void showLong(Context context, int message) {
        if (toast == null || ToatUtils.context != context) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, CharSequence message, int duration) {
        if (toast == null || ToatUtils.context != context) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param message
     * @param duration
     */
    public static void show(Context context, int message, int duration) {
        if (toast == null || ToatUtils.context != context) {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.setText(message);
        toast.show();
    }

}
