package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;


/**
 * 自己编写的万能ViewHolder
 * by jsy_zj
 * */
public abstract class CommonAdapter<T> extends BaseAdapter{
	protected List<T>mDatas;
	protected Context context;
	protected LayoutInflater layout;
	protected int item_id;
	
	public CommonAdapter(Context context, List<T>datas,int item_id) {
		this.context=context;
		this.mDatas=datas;
		layout=LayoutInflater.from(context);
		this.item_id=item_id;
	}


	@Override
	public int getCount() {
		return mDatas.size();
	}

	@Override
	public T getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		ViewHolder holder = ViewHolder.get(context, parent, position, item_id, convertView);
			convert(holder, getItem(position));
			convertlistener(holder,getItem(position));
			return holder.getConvertView();

	}

	protected abstract void convertlistener(ViewHolder holder, T t);

	/**
	 * convert
	 * @param t t集合的泛型
	 *
	 * */
	public abstract void convert(ViewHolder holder , T t);

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
};
	