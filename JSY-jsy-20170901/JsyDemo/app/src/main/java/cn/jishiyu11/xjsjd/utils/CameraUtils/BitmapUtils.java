package cn.jishiyu11.xjsjd.utils.CameraUtils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import cn.jishiyu11.xjsjd.utils.AppUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by vvguoliang on 2017/7/1.
 * 图片类
 */

public class BitmapUtils {

    /**
     * 判断是否有SD卡
     *
     * @return
     */
    public static boolean existSDCard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 从文件中读取图片
     *
     * @param file
     * @return
     */
    public static Bitmap getFileBitmap(File file) {
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try {
            //把图片转化为字节流
            fis = new FileInputStream(file);
            //把流转化图片
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();//关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError ignored) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 删除文档
     *
     * @param file
     */
    public static boolean deleteFile(File file) {
        try {
            if (file != null) {
                if (FileUtils.deleteFile(file.getAbsolutePath())) {
                    AppUtil.getInstance().mImageFile = null;
                    Log.d("删除文件", "true");
                    return true;
                } else {
                    return false;
                }
            } else {
                Log.d("删除文件路径为空", "false");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("删除文件", "false");
            return false;
        }
    }


    /**
     * 保存图片
     *
     * @param bitmap
     * @param file
     */
    public static boolean saveBitmapFile(Bitmap bitmap, File file) {
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static class FileUtils {

        /**
         * 删除单个文件
         *
         * @param filePath 被删除文件的文件名
         * @return 文件删除成功返回true，否则返回false
         */
        public static boolean deleteFile(String filePath) {
            File file = new File(filePath);
            return file.isFile() && file.exists() && file.delete();
        }


        /**
         * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
         *
         * @param context
         * @param imageUri
         * @author yaoxing
         * @date 2014-10-12
         */
        @TargetApi(19)
        public static String getImageAbsolutePath(Activity context, Uri imageUri) {
            if (context == null || imageUri == null)
                return null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
                if (isExternalStorageDocument(imageUri)) {
                    String docId = DocumentsContract.getDocumentId(imageUri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }
                } else if (isDownloadsDocument(imageUri)) {
                    String id = DocumentsContract.getDocumentId(imageUri);
                    Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                    return getDataColumn(context, contentUri, null, null);
                } else if (isMediaDocument(imageUri)) {
                    String docId = DocumentsContract.getDocumentId(imageUri);
                    String[] split = docId.split(":");
                    String type = split[0];
                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }
                    String selection = MediaStore.Images.Media._ID + "=?";
                    String[] selectionArgs = new String[]{split[1]};
                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            } // MediaStore (and general)
            else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
                // Return the remote address
                if (isGooglePhotosUri(imageUri))
                    return imageUri.getLastPathSegment();
                return getDataColumn(context, imageUri, null, null);
            }
            // File
            else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
                return imageUri.getPath();
            }
            return null;
        }

        private static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
            Cursor cursor = null;
            String column = MediaStore.Images.Media.DATA;
            String[] projection = {column};
            try {
                cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(column);
                    return cursor.getString(index);
                }
            } finally {
                if (cursor != null)
                    cursor.close();
            }
            return null;
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is ExternalStorageProvider.
         */
        private static boolean isExternalStorageDocument(Uri uri) {
            return "com.android.externalstorage.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is DownloadsProvider.
         */
        private static boolean isDownloadsDocument(Uri uri) {
            return "com.android.providers.downloads.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is MediaProvider.
         */
        private static boolean isMediaDocument(Uri uri) {
            return "com.android.providers.media.documents".equals(uri.getAuthority());
        }

        /**
         * @param uri The Uri to check.
         * @return Whether the Uri authority is Google Photos.
         */
        public static boolean isGooglePhotosUri(Uri uri) {
            return "com.google.android.apps.photos.content".equals(uri.getAuthority());
        }
    }

}
