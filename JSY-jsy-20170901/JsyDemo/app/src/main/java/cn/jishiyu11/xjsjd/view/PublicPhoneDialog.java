package cn.jishiyu11.xjsjd.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;

/**
 * Created by vvguoliang on 2017/7/21.
 * <p>
 * 公共电话弹出框
 */

public class PublicPhoneDialog extends Dialog {

    public PublicPhoneDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private DialogInterface.OnClickListener onClickListenerPhoneCancel;
        private DialogInterface.OnClickListener onClickListenerPhoneDetermine;
        private String msg = "";
        private String namecancel = "";
        private String namedetermine = "";

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public PublicPhoneDialog.Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public PublicPhoneDialog.Builder setTiltleMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public PublicPhoneDialog.Builder setContentViewCancel(String name, DialogInterface.OnClickListener onClickListenerPhoneCancel) {
            this.namecancel = name;
            this.onClickListenerPhoneCancel = onClickListenerPhoneCancel;
            return this;
        }

        public PublicPhoneDialog.Builder setContentViewDetermine(String name, DialogInterface.OnClickListener onClickListenerPhoneDetermine) {
            this.namedetermine = name;
            this.onClickListenerPhoneDetermine = onClickListenerPhoneDetermine;
            return this;
        }

        public PublicPhoneDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PublicPhoneDialog dialog = new PublicPhoneDialog(context, R.style.Dialog);
            @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_phone, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));

            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            TextView dialog_phone_title = layout.findViewById(R.id.dialog_phone_title);
            TextView dialog_phone_context = layout.findViewById(R.id.dialog_phone_context);
            Button dialog_phone_button_cancel = layout.findViewById(R.id.dialog_phone_button_cancel);
            Button dialog_phone_button_determine = layout.findViewById(R.id.dialog_phone_button_determine);

            if (!TextUtils.isEmpty(title)) {
                dialog_phone_title.setText(title);
            }

            if (!TextUtils.isEmpty(msg)) {
                dialog_phone_context.setText(msg);
            }

            if (onClickListenerPhoneCancel != null) {
                dialog_phone_button_cancel.setText(namecancel);
                dialog_phone_button_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListenerPhoneCancel.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            } else {
                dialog_phone_button_cancel.setVisibility(View.GONE);
            }

            if (onClickListenerPhoneDetermine != null) {
                dialog_phone_button_determine.setText(namedetermine);
                dialog_phone_button_determine.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickListenerPhoneDetermine.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            } else {
                dialog_phone_button_determine.setVisibility(View.GONE);
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
