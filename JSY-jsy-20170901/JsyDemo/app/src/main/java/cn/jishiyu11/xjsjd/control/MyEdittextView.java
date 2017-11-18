package cn.jishiyu11.xjsjd.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by vvguoliang on 2017/6/28.
 * 编辑框
 */

@SuppressLint("AppCompatCustomView")
public class MyEdittextView extends EditText{

    public MyEdittextView(Context context) {
        super(context);
        initEdittextView(context);
    }

    public MyEdittextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initEdittextView(context);
    }

    public MyEdittextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initEdittextView(context);
    }

    private void initEdittextView(Context mContext) {
//        this.setTypeface(SXSApplication.typeface);

    }

}
