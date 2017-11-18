package cn.jishiyu11.xjsjd.control;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by vvguoliang on 2017/6/28.
 * 自定义 button
 */

@SuppressLint("AppCompatCustomView")
public class MyButton extends Button {

    public MyButton(Context context) {
        super(context);
        initButtonView(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initButtonView(context);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initButtonView(context);
    }

    private void initButtonView(Context mContext) {
//        this.setTypeface(SXSApplication.typeface);

    }
}
