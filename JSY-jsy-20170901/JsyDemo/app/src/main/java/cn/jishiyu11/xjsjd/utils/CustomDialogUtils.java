package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import cn.jishiyu11.xjsjd.EntityClass.Fragment2LoanDetails;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.view.CustomDialog;

/**
 * Created by jsy_zj on 2017/11/8.
 */

public class CustomDialogUtils {


    /**
     * 单例对象实例
     */
    private static class CustomDialogUtilsHolder {
        static final CustomDialogUtils INSTANCE = new CustomDialogUtils();
    }

    public static CustomDialogUtils getInstance() {
        return CustomDialogUtils.CustomDialogUtilsHolder.INSTANCE;
    }


    public CustomDialog LoadingDialog(Context context, String displayContent) {
        CustomDialog customDialog = new CustomDialog(context, R.style.new_circle_progress);
        View contentView = LayoutInflater.from(context).inflate(R.layout.customdialog_laoding, null);
        customDialog.setContentView(contentView);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);

        Window dialogWindow = customDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
        return customDialog;
    }

    public CustomDialog setFragment2Dialog(Context context, Fragment2LoanDetails details) {//
        final CustomDialog dialog = new CustomDialog(context, R.style.add_dialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.fragmnent2_item_dialog, null);
        dialog.setContentView(contentView);
//        dialog.setCanceledOnTouchOutside(false);//点击返回是否取消
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);

        return dialog;
    }

}
