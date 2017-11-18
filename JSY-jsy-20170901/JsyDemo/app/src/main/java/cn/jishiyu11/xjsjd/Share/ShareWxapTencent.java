package cn.jishiyu11.xjsjd.Share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.BottomDialog;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.tencent.wxop.stat.StatConfig;

import java.util.ArrayList;

/**
 * Created by vvguoliang on 2017/2/13.
 * 邀请好友
 */

@SuppressWarnings("ConstantConditions")
public class ShareWxapTencent implements View.OnClickListener {

    public ShareWxapTencent(Activity mActivity, String url, String mQqTitle, String mQqSummary, String mWxwebtitle,
                            String mWxwebdescription, Bitmap bitmap) {
        super();
        this.mActivity = mActivity;
        if (!TextUtils.isEmpty(url) || !url.equals("null")) {
            this.url = url;
        } else {
            ToatUtils.showShort1(mActivity, "操作失败，请重试");
            return;
        }
        if (!TextUtils.isEmpty(mQqTitle) && !mQqTitle.equals("null")) {
            this.mQqTitle = mQqTitle;
        }
        if (!TextUtils.isEmpty(mQqSummary) && !mQqSummary.equals("null")) {
            this.mQqSummary = mQqSummary;
        }
        if (!TextUtils.isEmpty(mWxwebtitle) && !mWxwebtitle.equals("null")) {
            this.mWxwebtitle = mWxwebtitle;
        }
        if (!TextUtils.isEmpty(mWxwebdescription) && !mWxwebdescription.equals("null")) {
            this.mWxwebdescription = mWxwebdescription;
        }
        this.bitmap = bitmap;
        getShare();
    }

    private Activity mActivity;

    private String url = "";

    /**
     * 腾讯分享
     */
    public static Tencent mTencent;
    /**
     * 微信分享
     */
    private IWXAPI api;

    private String mQqTitle = "[沙小僧理财]送你600元启动资金,跟小沙一起取金吧!";

    private String mQqSummary = "预期15%年化收益，会理财更赚钱！沙小僧，靠谱儿！";

    private String mWxwebtitle = "【沙小僧理财】送你600元启动资金,跟小沙一起取金吧!";

    private String mWxwebdescription = "我在沙小僧理财，安全可靠福利又多，新手注册即送600元，最高15%年化收益，快来一起赚~";

    private BottomDialog mSXSDialog;

    private Bitmap bitmap;

    private void getShare() {
        api = WXAPIFactory.createWXAPI(mActivity, "wx43b913bd07fb3715", false);
        api.registerApp("wx43b913bd07fb3715");

        mTencent = Tencent.createInstance("1106281166", mActivity);
        StatConfig.setAppKey(mActivity, "Ghf9Q7U7IuQbOWDm");

        if (url == null || url.equals("")) {
            ToatUtils.showShort1(mActivity, "操作失败，请重试");
            return;
        }
        if (mSXSDialog == null) {
            mSXSDialog = new BottomDialog(mActivity, R.layout.my_friends_botton_popupwindow);
        }
        mSXSDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        mSXSDialog.getWindow().setGravity(Gravity.BOTTOM);
        mSXSDialog.setWidthHeight(AppUtil.getScreenDispaly(mActivity)[0], 0);
        mSXSDialog.setOnClick(R.id.tab_rb_1, this);
        mSXSDialog.setOnClick(R.id.tab_rb_2, this);
        mSXSDialog.setOnClick(R.id.tab_rb_3, this);
        mSXSDialog.setOnClick(R.id.tab_rb_4, this);
        mSXSDialog.setOnClick(R.id.my_pop_cancel_button, null);
        if (!mActivity.isFinishing()) {
            mSXSDialog.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_rb_1:// 微信
                shareToWXshare(0);
                break;
            case R.id.tab_rb_2:// 朋友圈
                shareToWXshare(1);
                break;
            case R.id.tab_rb_3:// QQ空间
                shareToQQzone();
                break;
            case R.id.tab_rb_4:// QQ
                onClickShare();
                break;
            default:
                break;
        }
    }

    private void onClickShare() {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, mQqTitle);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, mQqSummary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, BitmapUtils.FILE_PATH + "/jsy_ic_launcher.png");
//        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, "");SHARE_TO_QQ_IMAGE_LOCAL_URL
//        params.putString(QQShare.SHARE_TO_QQ_EXT_INT, "其他附加功能");
        if (mTencent != null)
            mTencent.shareToQQ(mActivity, params, new BaseUIListener(mActivity));
    }

    private void shareToQQzone() {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, mQqTitle);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, mQqSummary);
        params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, url);
        ArrayList<String> imageUrls = new ArrayList<>();
        imageUrls.add(BitmapUtils.FILE_PATH + "/jsy_ic_launcher.png");
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        params.putInt(QzoneShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        if (mTencent != null)
            mTencent.shareToQzone(mActivity, params, new BaseUIListener(mActivity));
    }

    private void shareToWXshare(final int WX_FRIENDS) {
        /*
         * 微信分享 （这里仅提供一个分享网页的示例，其它请参看官网示例代码）
         *
         * @param flag
         *            (0:分享到微信好友，1：分享到微信朋友圈)
         */
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = mWxwebtitle;
        msg.description = mWxwebdescription;
        // 这里替换一张自己工程里的图片资源
        msg.setThumbImage(bitmap);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = WX_FRIENDS == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        if (api != null)
            api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
