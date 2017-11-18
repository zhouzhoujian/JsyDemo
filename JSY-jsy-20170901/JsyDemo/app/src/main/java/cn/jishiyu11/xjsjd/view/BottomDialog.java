package cn.jishiyu11.xjsjd.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import cn.jishiyu11.xjsjd.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 自定义dialog
 * 
 *vvguolaing 2017-06-29
 */
@SuppressWarnings("unchecked")
@SuppressLint("UseSparseArrays")
public class BottomDialog extends Dialog implements View.OnClickListener {
	protected View view;
	protected Map<Integer, View.OnClickListener> map = new HashMap<>();
	protected int width;
	protected int height;

	public BottomDialog(Context context, int layout) {
		super(context, R.style.customDialog);
		view = LayoutInflater.from(context).inflate(layout, null);
	}

	@Override
	public View findViewById(int id) {
		return view.findViewById(id);
	}

	public void setWidthHeight(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setOnClick(int viewId, View.OnClickListener onClickListener) {
		map.put(viewId, onClickListener);
		View view = findViewById(viewId);
		view.setOnClickListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(view);
		if (width <= 0) {
		} else {
			view.getLayoutParams().width = width;
		}
		if (height <= 0) {
		} else {
			view.getLayoutParams().height = height;
		}
		view.setLayoutParams(view.getLayoutParams());
	}

	@Override
	public void onClick(View v) {
		dismiss();
		if (map.containsKey(v.getId()) && map.get(v.getId()) != null) {
			map.get(v.getId()).onClick(v);
		}
	}
}
