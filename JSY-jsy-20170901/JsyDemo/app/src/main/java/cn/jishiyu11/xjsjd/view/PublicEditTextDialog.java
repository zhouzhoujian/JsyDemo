package cn.jishiyu11.xjsjd.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.utils.IdcardValidator;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;

/**
 * Created by vvguoliang on 2017/7/21.
 * <p>
 * 带编辑框的弹出框
 */

public class PublicEditTextDialog extends Dialog {

    public PublicEditTextDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private DialogInterface.OnClickListener onClickListenerCancel;
        private String namecancel = "";
        private DialogInterface.OnClickListener onClickListenerDetermine;
        private String namedetermine = "";

        private int type = 0;
        private String id = "";

        private String msg = "";


        public Builder(Context context, int type, String id) {
            this.context = context;
            this.type = type;
            this.id = id;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public PublicEditTextDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PublicEditTextDialog.Builder setEditTitle(String msg) {
            this.msg = msg;
            return this;
        }

        public PublicEditTextDialog.Builder setContentViewCancel(String name, DialogInterface.OnClickListener clickListener) {
            this.namecancel = name;
            this.onClickListenerCancel = clickListener;
            return this;
        }

        public PublicEditTextDialog.Builder setContentViewDetermine(String name, DialogInterface.OnClickListener clickListener) {
            this.namedetermine = name;
            this.onClickListenerDetermine = clickListener;
            return this;
        }

        public PublicEditTextDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final PublicEditTextDialog dialog = new PublicEditTextDialog(context, R.style.Dialog);
            @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_edittext, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            TextView dialog_editText_title = layout.findViewById(R.id.dialog_editText_title);
            final EditText dialog_editText_context = layout.findViewById(R.id.dialog_editText_context);
            Button dialog_editText_button_cancel = layout.findViewById(R.id.dialog_editText_button_cancel);
            Button dialog_editText_button_determine = layout.findViewById(R.id.dialog_editText_button_determine);
            TextView dialog_editText_text = layout.findViewById(R.id.dialog_editText_text);

            if (!TextUtils.isEmpty(msg)) {
                dialog_editText_text.setText(msg);
            } else {
                dialog_editText_text.setVisibility(View.GONE);
            }

            if (id.equals("idcard")) {
                dialog_editText_context.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
            }

            if (!TextUtils.isEmpty(title)) {
                dialog_editText_title.setText(title);
            }

            if (onClickListenerCancel != null) {
                dialog_editText_button_cancel.setText(namecancel);
                dialog_editText_button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListenerCancel.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            if (onClickListenerDetermine != null) {
                dialog_editText_button_determine.setText(namedetermine);
                dialog_editText_button_determine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (id) {
                            case "idcard"://身份证
                                if (dialog_editText_context.getText().length() == 0) {
                                    ToatUtils.showShort1(context, "请输入身份证号");
                                } else if (!IdcardValidator.getInstance().isValidatedAllIdcard(dialog_editText_context.getText().toString())) {
                                    ToatUtils.showShort1(context, "请输入正确身份证号");
                                } else {
                                    SharedPreferencesUtils.put(context, id, dialog_editText_context.getText().toString());
                                    onClickListenerDetermine.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                                break;
                            case "realname"://姓名
                                if (dialog_editText_context.getText().length() == 0) {
                                    ToatUtils.showShort1(context, "请输入姓名");
                                } else if (dialog_editText_context.getText().length() < 2) {
                                    ToatUtils.showShort1(context, "请输入正确姓名");
                                } else {
                                    SharedPreferencesUtils.put(context, id, dialog_editText_context.getText().toString());
                                    onClickListenerDetermine.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                }
                                break;
                            case "code":
                                SharedPreferencesUtils.put(context, id, dialog_editText_context.getText().toString());
                                onClickListenerDetermine.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                                break;
                            default:
                                SharedPreferencesUtils.put(context, id, dialog_editText_context.getText().toString());
                                onClickListenerDetermine.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
