package cn.jishiyu11.xjsjd.utils.CameraUtils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import cn.jishiyu11.xjsjd.EntityClass.ContactInfo;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.interfaces.UserCenterModel;
import cn.jishiyu11.xjsjd.service.UpdataService;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CTelephoneInfo;
import cn.jishiyu11.xjsjd.utils.PublicClass.ShowDialog;
import cn.jishiyu11.xjsjd.utils.TimeUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.jishiyu11.xjsjd.utils.CameraUtils.BitmapUtils.*;

/**
 * Created by vvguoliang on 2017/7/1.
 * <p>
 * 授权+拍照和剪切
 */

public class UserCenterRealize implements UserCenterModel {

    @Override
    public void getFileByPhotograph(Context context) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                //申请相机权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                        AppUtil.getInstance().MY_PERMISSIONS_REQUEST_CAMERA);
            } else {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    //申请读SD权限
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD);
                } else {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                            PackageManager.PERMISSION_GRANTED) {
                        //申请写SD权限
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK);
                    } else {
                        startPhotograph(context);
                    }
                }
            }
        } else {
            //拍照
            startPhotograph(context);
        }

    }

    @Override
    public File getImagefile() {
        File mediaStorageDir = null;
        File mediaFile;
        try {
            mediaStorageDir = new File(Environment.getExternalStorageDirectory().toString() + "/IMAGE/");
//            Log.d("创建文件夹成功", mediaStorageDir.toString());
        } catch (Exception e) {
            e.printStackTrace();
//            Log.d("创建文件夹失败", mediaStorageDir.toString());
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
//                Log.d("创建文件失败", "WRITE_EXTERNAL_STORAGE 权限");
                return null;
            }
        }
        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        Log.d("创建文件成功", mediaFile.toString());
        return mediaFile;

    }

    @Override
    public void getFileByPhotoAlbum(Context context) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED) {
                //申请读SD权限
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD_PHOTOALBUM);
            } else {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    //申请写SD权限
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK_PHOTOALBUM);
                } else {
                    startPhotoAlbum(context);
                }
            }
        } else {
            startPhotoAlbum(context);
        }
    }

    @Override
    public void startPhotoAlbum(Context context) {
        if (!existSDCard()) {
            Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT).show();
            return;
        }
        Activity a = (Activity) context;
        Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        getAlbum.setType(AppUtil.getInstance().IMAGE_TYPE);
        a.startActivityForResult(getAlbum, AppUtil.getInstance().LOAD_IMAGE_REQUEST);
    }

    @Override
    public void startPhotograph(Context context) {
        if (!existSDCard()) {
            Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_SHORT).show();
            return;
        }
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion < 24) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            AppUtil.getInstance().mImageFile = getImagefile();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(AppUtil.getInstance().mImageFile));
            activity.startActivityForResult(intent, AppUtil.getInstance().CAPTURE_IMAGE_REQUEST);
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues contentValues = new ContentValues(1);
            AppUtil.getInstance().mImageFile = getImagefile();
            contentValues.put(MediaStore.Images.Media.DATA, AppUtil.getInstance().mImageFile.getAbsolutePath());
            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            activity.startActivityForResult(intent, AppUtil.getInstance().CAPTURE_IMAGE_REQUEST);
        }
    }


    @Override
    public void startClip(Activity activity, File file) {
        if (null == file) {
            return;
        }
        AppUtil.getInstance().mOutFile = getImagefile();
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(activity, file), AppUtil.getInstance().IMAGE_TYPE);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 180);
        intent.putExtra("outputY", 180);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(AppUtil.getInstance().mOutFile));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, AppUtil.getInstance().CLIP_IMAGE_REQUEST);
    }

    @Override
    public Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Uri uri;
        @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            uri = Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                uri = context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                uri = null;
            }
        }
        return uri;
    }

    @Override
    public void startPhone(Context context, Handler mHandler, int booint) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                //申请联系人权限  允许程序读取用户联系人数据
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CONTACTS},
                        AppUtil.getInstance().MY_PERMISSIONS_REQUEST_CONTACTS);
            } else {
                getContactID(activity, mHandler, booint);
            }
        } else {
            getContactID(activity, mHandler, booint);
        }
    }

    @Override
    public void getContactID(Activity activity, Handler mHandler, int booint) {
        // 获取联系人数据
        ContentResolver cr = activity.getContentResolver();//获取所有电话信息（而不是联系人信息），这样方便展示
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,// 姓名
                ContactsContract.CommonDataKinds.Phone.NUMBER,// 电话号码
        };
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if (cursor == null) {
            return;
        }//最终要返回的数据
        result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String number = cursor.getString(1);
            //保存到对象里
            ContactInfo info = new ContactInfo();
            info.setName(name);
            info.setNumber(number);
            //保存到集合里
            result.add(info);
        }//用完记得关闭
        cursor.close();
        if (TimeUtils.isFastDoubleClick()) {
        } else {
            if (booint == 1) {
                //弹出Toast或者Dialog
                ShowDialog.getInstance().getDialog(activity, getCursor(result),
                        "cursor", mHandler, 100);
            } else {
                //弹出Toast或者Dialog
                ShowDialog.getInstance().getDialog(activity, getCursor(result),
                        "cursor", mHandler, 101);
            }
        }
    }

    @Override
    public void startPhoneDial(Context context, String phone) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                //申请联系人权限  允许程序读取用户联系人数据
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE},
                        AppUtil.getInstance().MY_PERMISSIONS_PHONE_DIAL);
            } else {
                getPhoneDial(activity, phone);
            }
        } else {
            getPhoneDial(activity, phone);
        }
    }

    @Override
    public void getPhoneDial(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void getIMEIPHONE(Context context, Handler mHandler, int imei) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                //申请联系人权限  允许程序读取用户联系人数据
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_SMS},
                        AppUtil.getInstance().MY_PERMISSIONS_PHONE_IMEI);
            } else {
                getPhone(context, mHandler, imei);
            }
        } else {
            getPhone(context, mHandler, imei);
        }
    }

    @Override
    public void getUpdata(Context context, String url) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //申请联系人权限  允许程序读取用户联系人数据
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK);
            } else {
                Intent intent = new Intent(context, UpdataService.class);
                intent.putExtra("url", url);
                context.startService(intent);
            }
        } else {
            Intent intent = new Intent(context, UpdataService.class);
            intent.putExtra("url", url);
            context.startService(intent);
        }
    }

    private int sxs_icon = R.mipmap.ic_launcher;
    private Bitmap thumb = null;

    @Override
    public void getReadWRite(Context context, Handler mHandler) {
        Activity activity = (Activity) context;
        if (AppUtil.getInstance().mBuildVersion >= 23) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //申请联系人权限  允许程序读取用户联系人数据
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        AppUtil.getInstance().MY_PERMISSIONS_PHONE_READWRITE);
            } else {
                thumb = BitmapFactory.decodeResource(context.getResources(), sxs_icon);
                cn.jishiyu11.xjsjd.Share.BitmapUtils.getBitmap(thumb);
                Message message = new Message();
                message.obj = thumb;
                mHandler.sendMessage(message);
            }
        } else {
            thumb = BitmapFactory.decodeResource(context.getResources(), sxs_icon);
            cn.jishiyu11.xjsjd.Share.BitmapUtils.getBitmap(thumb);
            Message message = new Message();
            message.obj = thumb;
            mHandler.sendMessage(message);
        }

    }

    /**
     * 获取手机号信息
     *
     * @param context
     * @param mHandle
     * @param imei
     */
    private void getPhone(Context context, Handler mHandle, int imei) {
        CTelephoneInfo telephonyInfo = CTelephoneInfo.getInstance(context);
        telephonyInfo.setCTelephoneInfo();
        String phone = telephonyInfo.getLine1Number();
        String imeiSIM1 = telephonyInfo.getImeiSIM1();
        String imeiSIM2 = telephonyInfo.getImeiSIM2();
        boolean isSIM1Ready = telephonyInfo.isSIM1Ready();
        boolean isSIM2Ready = telephonyInfo.isSIM2Ready();
        if (isSIM1Ready && isSIM2Ready) {
            Message message = new Message();
            message.what = imei;
            message.obj = phone + "#" + imeiSIM1 + "#" + imeiSIM2;
            mHandle.sendMessage(message);
        } else if (isSIM1Ready) {
            Message message = new Message();
            message.what = imei;
            message.obj = phone + "#" + imeiSIM1;
            mHandle.sendMessage(message);
        } else {
            Message message = new Message();
            message.what = imei;
            message.obj = phone + "#" + imeiSIM1;
            mHandle.sendMessage(message);
        }
    }

    private List<ContactInfo> result = null;

    private List<Map<String, Object>> getCursor(List<ContactInfo> contactInfoList) {
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; contactInfoList.size() > i; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("name", contactInfoList.get(i).getName());
            map.put("number", contactInfoList.get(i).getNumber());
            map.put("boolean", "1");
            mapList.add(map);
        }
        return mapList;
    }
}
