package cn.jishiyu11.xjsjd.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.R;

import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/6/28.
 * <p>
 * 弹出框 list view 适配器
 */

public class DialogListView extends BaseAdapter {

    private List<Map<String, Object>> listString;
    private LayoutInflater mInflater;

    public DialogListView(Context context, List<Map<String, Object>> listString) {
        this.listString = listString;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return listString.size();
    }

    @Override
    public Object getItem(int position) {
        return listString.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.dialog_listview_item, null);
            viewHolder.dialog_listview_item_checkBox = convertView.findViewById(R.id.dialog_listview_item_checkBox);
            viewHolder.dialog_listview_item_textview = convertView.findViewById(R.id.dialog_listview_item_textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.dialog_listview_item_textview.setText(listString.get(position).get("name").toString());
        if (listString.get(position).get("boolean").equals("1")) {
            viewHolder.dialog_listview_item_checkBox.setImageResource(R.mipmap.ic_no_selected);
        } else {
            viewHolder.dialog_listview_item_checkBox.setImageResource(R.mipmap.ic_selected);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView dialog_listview_item_checkBox;
        TextView dialog_listview_item_textview;
    }
}
