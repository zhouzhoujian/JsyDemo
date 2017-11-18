package cn.jishiyu11.xjsjd.utils.PublicClass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;

/**
 * Created by vvguoliang on 2017/7/1.
 * <p>
 * 短信倒计时
 */

public class CommissioningTimerUtils extends CountDownTimer {

    private Button mButton;
    private Context context;
    private Handler mHandler;

    /**
     * @param textView          The TextView
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receiver
     *                          {@link #onTick(long)} callbacks.
     */
    public CommissioningTimerUtils(Context context, Handler mHandler, Button textView, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
        this.mButton = textView;
        this.context = context;
        this.mHandler = mHandler;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setText(millisUntilFinished / 1000 + "秒 | 跳转");  //设置倒计时时间
//        mButton.setBackgroundResource(R.drawable.bg_identify_code_press); //设置按钮为灰色，这时是不能点击的

        /*
          超链接 URLSpan
          文字背景颜色 BackgroundColorSpan
          文字颜色 ForegroundColorSpan
          字体大小 AbsoluteSizeSpan
          粗体、斜体 StyleSpan
          删除线 StrikethroughSpan
          下划线 UnderlineSpan
          图片 ImageSpan
         */
        SpannableString spannableString = new SpannableString(mButton.getText().toString());  //获取按钮上的文字
        ForegroundColorSpan span = new ForegroundColorSpan(Color.WHITE);
        /*
         * public void setSpan(Object what, int start, int end, int flags) {
         * 主要是start跟end，start是起始位置,无论中英文，都算一个。
         * 从0开始计算起。end是结束位置，所以处理的文字，包含开始位置，但不包含结束位置。
         */
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//将倒计时的时间设置为红色
        mButton.setText(spannableString);
    }

    private int name = 0;

    @Override
    public void onFinish() {
        if (!context.getClass().getName().equals("cn.jishiyu11.xjsjd.activity.MainActivity") && name == 0) {
            name++;
            Message message = new Message();
            message.what = 101;
            mHandler.sendMessage(message);
        }
    }
}
