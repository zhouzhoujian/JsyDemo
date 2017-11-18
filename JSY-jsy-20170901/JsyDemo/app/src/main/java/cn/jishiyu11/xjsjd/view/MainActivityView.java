package cn.jishiyu11.xjsjd.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.jishiyu11.xjsjd.control.MyTextView;
import cn.jishiyu11.xjsjd.utils.DisplayUtils;

import java.util.ArrayList;

/**
 * Created by vvguoliang on 2017/6/23.
 *
 * 控制Fragment
 */

public class MainActivityView extends LinearLayout {

    private int image_width;
    private int image_height;
    private float text_size;
    private Context context;
    /**
     * 选中图片数组
     */
    private int[] selectedImage;
    /**
     * 未选中图片数组
     */
    private int[] unSelectedImage;


    private ArrayList<TextView> textViews = new ArrayList<>();


    private ArrayList<ImageView> imageViews = new ArrayList<>();


    public OnItemClickListener onItemClickListener;


    public MainActivityView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public MainActivityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MainActivityView);
//        image_height = typedArray.getInteger(R.styleable.MainActivityView_image_height, 60);
//        image_width = typedArray.getInteger(R.styleable.MainActivityView_image_width, 60);
//        text_size = typedArray.getDimension(R.styleable.MainActivityView_text_size, 2);
//        typedArray.recycle();
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    /**
     * 动态添加布局
     *
     * @param titles          导航标题
     * @param selectedImage   选中时的图片
     * @param unSelectedImage 未选中时的图片
     * @param screenWidth     屏幕的宽度
     * @param mHeight         控件自身高度
     * @param context
     */
    @SuppressWarnings("deprecation")
    public void setLayout(int[] titles, int[] selectedImage, int[] unSelectedImage, int screenWidth, int mHeight,
                          Context context) {
        this.context = context;
        this.selectedImage = selectedImage;
        this.unSelectedImage = unSelectedImage;
        setOrientation(LinearLayout.HORIZONTAL);
        if (screenWidth > 720) {
            text_size = DisplayUtils.px2dip(context, 20) * 16 / 9;
        } else if (screenWidth <= 720 && screenWidth > 480) {
            text_size = DisplayUtils.px2dip(context, 20) * 16 / 9 - 5;
        } else if (screenWidth <= 480) {
            text_size = DisplayUtils.px2dip(context, 20) * 16 / 9 - 23;
        }
        if (titles != null && titles.length != 0) {
            int widthScale = screenWidth / titles.length;
            for (int i = 0; i < titles.length; i++) {
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CENTER);

                LayoutParams layoutLp = new LayoutParams(widthScale,
                        LayoutParams.MATCH_PARENT);
                layoutLp.gravity = Gravity.CENTER;
                layout.setLayoutParams(layoutLp);

                ImageView image = new ImageView(context);
                LayoutParams imageLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                image.setImageDrawable(context.getResources().getDrawable(unSelectedImage[i]));
                image.setLayoutParams(imageLp);

                MyTextView tv_title = new MyTextView(context);
                LayoutParams textLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                tv_title.setTextSize(text_size);
                tv_title.setText(titles[i]);
                tv_title.setLayoutParams(textLp);

                layout.addView(image);
                layout.addView(tv_title);

                layout.setOnClickListener(new OnClickListener() {


                    @Override
                    public void onClick(View v) {
                        int position = (Integer) v.getTag();
                        setColorLing(position);
                        if (onItemClickListener != null) {
                            onItemClickListener.onItemClick(position);
                        }
                    }
                });
                layout.setTag(i);
                addView(layout, widthScale, mHeight);
                imageViews.add(image);
                textViews.add(tv_title);
            }
        }
    }


    /**
     * 底部导航点击接口回调
     */
    public interface OnItemClickListener {
        void onItemClick(int position);
    }


    /**
     * 设置文本和图片为亮色
     *
     * @param position
     */
    public void setColorLing(int position) {
        setColorDark();
        for (int i = 0; i < textViews.size(); i++) {
            if (position == i) {
//                textViews.get(i).setTextColor(Color.parseColor("#FF524F"));
                textViews.get(i).setTextColor(Color.parseColor("#2898f8"));
                imageViews.get(i).setImageDrawable(context.getResources().getDrawable(selectedImage[i]));
            }
        }
    }


    /**
     * 设置文本和图片为暗色
     */
    public void setColorDark() {
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(Color.parseColor("#666666"));
            imageViews.get(i).setImageDrawable(context.getResources().getDrawable(unSelectedImage[i]));
        }
    }
}
