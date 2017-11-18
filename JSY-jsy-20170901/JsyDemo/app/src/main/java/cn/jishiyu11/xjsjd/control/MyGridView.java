package cn.jishiyu11.xjsjd.control;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridView;

/**
 * Created by vvguoliang on 2017/7/25.
 * 自定义 Gridview
 */

public class MyGridView extends GridView{

    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 设置上下不滚动
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //true:禁止滚动
        return ev.getAction() == MotionEvent.ACTION_MOVE || super.dispatchTouchEvent(ev);

    }
}
