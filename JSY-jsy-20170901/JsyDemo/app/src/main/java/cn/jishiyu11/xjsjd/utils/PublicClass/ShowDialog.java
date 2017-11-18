package cn.jishiyu11.xjsjd.utils.PublicClass;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.view.BottomDialog;
import cn.jishiyu11.xjsjd.view.PublicDialog;
import cn.jishiyu11.xjsjd.view.PublicEditTextDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/6/30.
 * 公共类 Diaog
 */

@SuppressWarnings({"ResultOfMethodCallIgnored", "ConstantConditions"})
public class ShowDialog implements Serializable {

    /**
     * 单例对象实例
     */
    private static class ShowDialogHolder {
        static final ShowDialog INSTANCE = new ShowDialog();
    }

    public static ShowDialog getInstance() {
        return ShowDialogHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private ShowDialog() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }

    // 提示对话框方法
    public void showDialog(final Activity context, final String name, final String btn_take, final String btn_pick,
                           final Handler mHanler, final int species) {
        final BottomDialog sxsDialog = new BottomDialog(context, R.layout.buttom_dialog);
        sxsDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        sxsDialog.setWidthHeight(AppUtil.getInstance().Dispay(context)[0], 0);
        sxsDialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button1 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo1);
        button1.setText(btn_take);
        Button button = (Button) sxsDialog.findViewById(R.id.btn_pick_photo2);
        button.setText(btn_pick);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.put(context, name, btn_take);
                Message message = new Message();
                message.what = species;
                message.obj = btn_take;
                mHanler.dispatchMessage(message);
                sxsDialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//有
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils.put(context, name, btn_pick);
                Message message = new Message();
                message.what = species;
                message.obj = btn_pick;
                mHanler.dispatchMessage(message);
                sxsDialog.dismiss();
            }
        });
        sxsDialog.setOnClick(R.id.btn_cancel, new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                sxsDialog.dismiss();
            }
        });
        if (!context.isFinishing()) {
            sxsDialog.show();
        }
    }

    // 提示对话框方法
    public void showDialog(final Activity context, final String name, final String[] strings, final Handler mHandler, final int type) {
        final BottomDialog sxsDialog = new BottomDialog(context, R.layout.buttom_dialog);
        sxsDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        sxsDialog.setWidthHeight(AppUtil.getInstance().Dispay(context)[0], 0);
        sxsDialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button1 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo1);
        Button button2 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo2);
        Button button3 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo3);
        Button button4 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo4);
        Button button5 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo5);
        Button button6 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo6);
        Button button7 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo7);
        Button button8 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo8);
        Button button9 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo9);
        Button button10 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo10);

        List<Button> buttonList = new ArrayList<>();
        buttonList.add(button1);
        buttonList.add(button2);
        buttonList.add(button3);
        buttonList.add(button4);
        buttonList.add(button5);
        buttonList.add(button6);
        buttonList.add(button7);
        buttonList.add(button8);
        buttonList.add(button9);
        buttonList.add(button10);

        for (int i = 0; strings.length > i; i++) {
            for (int j = 0; buttonList.size() > j; j++) {
                if (i == j) {
                    buttonList.get(j).setText(strings[i]);
                    buttonList.get(j).setVisibility(View.VISIBLE);
                    final int finalJ = j;
                    buttonList.get(j).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferencesUtils.put(context, name, strings[finalJ]);
                            Message message = new Message();
                            message.what = type;
                            message.obj = strings[finalJ];
                            mHandler.dispatchMessage(message);
                            sxsDialog.dismiss();
                        }
                    });
                }
            }
        }
        sxsDialog.setOnClick(R.id.btn_cancel, new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                sxsDialog.dismiss();
            }
        });
        if (!context.isFinishing()) {
            sxsDialog.show();
        }
    }

    public void getDialog(Activity mActivity, List<Map<String, Object>> list, String name, final Handler mHandler, final int what) {
        PublicDialog.Builder builder = new PublicDialog.Builder(mActivity);
        String stringa = SharedPreferencesUtils.get(mActivity, name, "").toString();
        if (!StringUtil.isNullOrEmpty(stringa)) {
            List<Map<String, Object>> listViewEntities = SharedPreferencesUtils.getInfo(mActivity, stringa);
            if (null != listViewEntities && listViewEntities.size() > 0) {
                list.clear();
                list = listViewEntities;
            }
        }
        final List<Map<String, Object>> finalList = list;
        builder.setItems(list, name, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; finalList.size() > i; i++) {
                    if (finalList.get(i).get("boolean").equals("2")) {
                        Message message = new Message();
                        message.what = what;
                        if (!finalList.get(i).toString().contains("number")) {
                            message.obj = finalList.get(i).get("name").toString();
                        } else {
                            message.obj = finalList.get(i).get("name").toString() + "," + finalList.get(i).get("number").toString();
                        }
                        mHandler.dispatchMessage(message);
                    }
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    /**
     *   传递参数
     * @param context
     * @param title
     * @param msg
     * @param type
     * @param id
     * @param mHandler
     */
    public void getEdiText(final Context context, String title, String msg, final int type, final String id, final Handler mHandler) {
        PublicEditTextDialog.Builder builder = new PublicEditTextDialog.Builder(context, type, id);
        builder.setTitle(title);
        builder.setEditTitle(msg);
        builder.setContentViewCancel("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = new Message();
                message.what = type;
                message.obj = "1";
                mHandler.sendMessage(message);
                dialog.dismiss();
            }
        });
        builder.setContentViewDetermine("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message message = new Message();
                message.what = type;
                message.obj = SharedPreferencesUtils.get(context, id, "").toString();
                mHandler.sendMessage(message);
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

}
