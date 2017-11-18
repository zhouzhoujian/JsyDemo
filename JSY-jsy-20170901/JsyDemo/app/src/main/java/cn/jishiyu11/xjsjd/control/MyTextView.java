package cn.jishiyu11.xjsjd.control;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class MyTextView extends TextView {

    public MyTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initTextView(context);
    }

    public MyTextView(Context context) {
        super(context);
        initTextView(context);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initTextView(context);
    }

    private void initTextView(Context mContext) {
//        this.setTypeface(SXSApplication.typeface);

    }

}
