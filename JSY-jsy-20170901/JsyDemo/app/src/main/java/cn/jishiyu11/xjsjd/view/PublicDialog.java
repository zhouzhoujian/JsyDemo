package cn.jishiyu11.xjsjd.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.adapter.DialogListView;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/6/28.
 * 公共弹出框 列表
 */

public class PublicDialog extends Dialog {

    public PublicDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder {
        private Context context;
        private String title;
        private View contentView;
        private DialogInterface.OnClickListener itemseButtonClickListener;
        private List<Map<String, Object>> itemses;
        private String name = "";

        public Builder(Context context) {
            this.context = context;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public void setItems(List<Map<String, Object>> items, String name, OnClickListener listener) {
            this.itemseButtonClickListener = listener;
            this.itemses = items;
            this.name = name;
        }

        public PublicDialog create() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final PublicDialog dialog = new PublicDialog(context, R.style.Dialog);
            @SuppressLint("InflateParams") View layout = inflater.inflate(R.layout.dialog_normal_layout, null);
            dialog.addContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            // set the dialog title
            ListView select_dialog_listview = layout.findViewById(R.id.select_dialog_listview);
            if (itemseButtonClickListener != null) {
                select_dialog_listview.setAdapter(new DialogListView(context, itemses));

                select_dialog_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (int i = 0; itemses.size() > i; i++) {
                            if (itemses.get(i).toString().contains("number")) {
                                if (i == position) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", itemses.get(i).get("name"));
                                    map.put("number", itemses.get(i).get("number"));
                                    map.put("boolean", "2");
                                    itemses.set(i, map);
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", itemses.get(i).get("name"));
                                    map.put("number", itemses.get(i).get("number"));
                                    map.put("boolean", "1");
                                    itemses.set(i, map);
                                }
                            } else {
                                if (i == position) {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", itemses.get(i).get("name"));
                                    map.put("boolean", "2");
                                    itemses.set(i, map);
                                } else {
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("name", itemses.get(i).get("name"));
                                    map.put("boolean", "1");
                                    itemses.set(i, map);
                                }
                            }
                        }
                        String strings = SharedPreferencesUtils.saveInfo(context, itemses);
                        SharedPreferencesUtils.put(context, name, strings);
                        itemseButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
