package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * 自己编写的万能ViewHolder
 * by jsy_zj
 * */
public class ViewHolder{
	private SparseArray<View>mViwes;
	private int mPosition;
	private View mConvertView;
	public ViewHolder(Context context,ViewGroup parent,int position,int layoutId){
		this.mViwes =new SparseArray<View>();
		this.mPosition=position;
		mConvertView=LayoutInflater.from(context).inflate(layoutId, parent, false);
		mConvertView.setTag(this);
	}
	public static ViewHolder get(Context context, ViewGroup parent, int position, int layoutId, View convertView){
		if (convertView ==null) {
			
			return new ViewHolder(context, parent, position, layoutId);
		}
		else {
			ViewHolder holder=(ViewHolder) convertView.getTag();
			holder.mPosition=position;
			return holder;
		}
	}
	public View getConvertView(){
		return mConvertView;
	}
	/**
	 * 获得position
	 * */
	public int getPosition(){
		return mPosition;
		
	}
	/**
	 * 通过viewId获取控件,输入 R.id...
	 * @param viewId
	 * @return  View
	 * */
	public <T extends View>T getView(int viewId){
		View view=mViwes.get(viewId);
		if (view==null) {
			view =mConvertView.findViewById(viewId);
			mViwes.put(viewId, view);
		}
		return (T) view;
	} 
	/**
	 * 设置TextView内容,输入 R.id..., 和  data.gettext.
	 * @param int viewId,String text
	 * @return ViewHolder 
	 * */
	public ViewHolder setText(int viewId, String text){
		TextView tv=getView(viewId);
		tv.setText(text);
		return this;
	}
	/**
	 * 设置ImageView内容,输入 R.id..., 和  res文件夹.
	 * @param int viewId ,res文件
	 * @return ViewHolder 
	 * */
	public ViewHolder setImageResource(int viewId, int resId){
		ImageView iv=getView(viewId);
		iv.setImageResource(resId);
		return this;
	}
	/**
	 * 设置TextView内容,输入 R.id..., 和  data.gettext.
	 * @param int viewId,String text
	 * @return ViewHolder
	 * */
	public TextView getTextview(int viewId){
		TextView tv=getView(viewId);

		return tv;
	}



	/**
	 * 设置ImageView内容,输入 R.id..., 和  Bitmap .
	 * @param int viewId 
	 * @return ViewHolder 
	 * */
	public ViewHolder setImageBitamap(int viewId, Bitmap bitmap){
		ImageView iv=getView(viewId);
		iv.setImageBitmap(bitmap);
		return this;
	}
	public ViewHolder setImageURI(int viewId, String url){
		ImageView iv=getView(viewId);
		Bitmap bm=getImageContent(url);
		iv.setImageBitmap(bm);
		return this;
	}
	public static Bitmap getImageContent(String params){
		//获取网络图片
		URL url;
		Bitmap bm = null;
		try {
			url = new URL(params);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			InputStream is = connection.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			//能够把输入字节流转化为bitmap对象。
			//bitmap-->位图，可以直接显示在imageview当中
			 bm = BitmapFactory.decodeStream(bis);
			 //关闭流，最好写在finally当中
			 is.close();
			 bis.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bm;
	}
}
