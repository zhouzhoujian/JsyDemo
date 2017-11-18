package cn.jishiyu11.xjsjd.interfaces;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;

import java.io.File;

/**
 * Created by vvguoliang on 2017/7/1.
 *
 * 权限接口调用
 */

public interface UserCenterModel {
    /**
     * 拍照
     *
     * @param context
     */
    void getFileByPhotograph(Context context);

    /**
     * 相册
     *
     * @param context
     */
    void getFileByPhotoAlbum(Context context);

    /**
     * 创建img文件
     *
     * @return
     */
    File getImagefile();

    /**
     * 开始拍照
     *
     * @param context
     */
    void startPhotograph(Context context);

    /**
     * 调用相册
     *
     * @param context
     */
    void startPhotoAlbum(Context context);


    /**
     * 剪裁图片
     *
     * @param activity
     */
    void startClip(Activity activity, File file);

    /**
     * API24 以上调用
     *
     * @param context
     * @param imageFile
     * @return
     */
    Uri getImageContentUri(Context context, File imageFile);

    /**
     * 读取联系人权限
     *
     * @param context
     */
    void startPhone(Context context, Handler mHandler, int booint);

    /**
     * 获取联系人信息
     *
     * @param activity
     */
    void getContactID(Activity activity, Handler mHandler, int booint);

    /**
     * 拨打电话权限
     *
     * @param context
     */
    void startPhoneDial(Context context, String phone);

    /**
     * 进行拨打电话
     *
     * @param context
     * @param phone
     */
    void getPhoneDial(Context context, String phone);

    /**
     * 获取IMEI权限
     */

    void getIMEIPHONE(Context context, Handler mHandler, int imei);

    /**
     * 版本更新
     *
     * @param context
     */
    void getUpdata(Context context, String url);

    /**
     * 写入和读取
     * @param context
     */
    void getReadWRite(Context context , Handler mHandler);

}