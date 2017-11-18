package cn.jishiyu11.xjsjd.service;

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.FileProvider;
import android.webkit.MimeTypeMap;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.utils.ToatUtils;

import java.io.File;

/**
 * Created by vvguoliang on 2017/7/26.\
 * <p>
 * 版本更新
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
public class UpdataService extends Service {
    /**
     * 安卓系统下载类
     **/
    private DownloadManager manager;
    /**
     * 接收下载完的广播
     **/
    private DownloadCompleteReceiver receiver;
    private String url;
    private String DOWNLOADPATH = "/apk/";//下载路径，如果不定义自己的路径，6.0的手机不自动安装

    /**
     * 初始化下载器
     **/
    private void initDownManager() {
        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        receiver = new DownloadCompleteReceiver();
        //设置下载地址
        DownloadManager.Request down = new DownloadManager.Request(Uri.parse(url));
        // 设置允许使用的网络类型，这里是移动网络和wifi都可以
        down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);
        down.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        down.setMimeType(mimeString);
        // 下载时，通知栏显示途中
        down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        // 显示下载界面
        down.setVisibleInDownloadsUi(true);
        // 设置下载后文件存放的位置
        down.setDestinationInExternalPublicDir(DOWNLOADPATH, "jsy.apk");
        down.setTitle(this.getString(R.string.app_name));
        // 将下载请求放入队列
        manager.enqueue(down);
        //注册下载广播
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        url = intent.getStringExtra("url");
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "jsy.apk";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            // 调用下载
            initDownManager();
        } catch (Exception e) {
            e.printStackTrace();
            ToatUtils.showShort1(getApplicationContext(), "下载失败");
        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        if (receiver != null)
            // 注销下载广播
            unregisterReceiver(receiver);
        super.onDestroy();
    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
                //获取下载的文件id
                long downId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                if (manager.getUriForDownloadedFile(downId) != null) {
                    //自动安装apk
                    File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + DOWNLOADPATH + "jsy.apk");
                    openFile(file, context);
                    //installAPK(context);
                } else {
                    ToatUtils.showShort1(context, "下载失败");
                }
                //停止服务并关闭广播
                UpdataService.this.stopSelf();
            }
        }
    }

    public void openFile(File file, Context context) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
            data = FileProvider.getUriForFile(context, "cn.jishiyu11.xjsjd.fileProvider", file);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public String getMIMEType(File var0) {
        String var1;
        String var2 = var0.getName();
        String var3 = var2.substring(var2.lastIndexOf(".") + 1, var2.length()).toLowerCase();
        var1 = MimeTypeMap.getSingleton().getMimeTypeFromExtension(var3);
        return var1;
    }
}
