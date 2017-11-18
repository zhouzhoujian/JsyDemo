package cn.jishiyu11.xjsjd.view;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * BaseViewHolder 顶级父类
 * vvguoliang 2017-6-23
 */
public class BaseViewHolder<T> extends RecyclerView.ViewHolder{

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public BaseViewHolder(Context context,ViewGroup parent, int layoutId) {
        super(LayoutInflater.from(context).inflate(layoutId, parent, false));
        onInitializeView();
    }

    public void onInitializeView() {

    }

    public <T extends View> T findViewById(@IdRes int resId) {
        return (T) itemView.findViewById(resId);
    }

    public void setData(final T data) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemViewClick(data);
            }
        });
    }

    public void onItemViewClick(T data) {

    }

}
