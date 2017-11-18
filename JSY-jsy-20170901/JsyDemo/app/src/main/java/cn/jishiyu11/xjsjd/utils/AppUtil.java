package cn.jishiyu11.xjsjd.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by vvguoliang on 2017/6/24.
 * <p>
 * APP 操作
 */
@SuppressWarnings("JavaDoc")
@SuppressLint("HardwareIds")
public class AppUtil {


    /**
     * 单例对象实例
     */
    private static class AppUtilHolder {
        static final AppUtil INSTANCE = new AppUtil();
    }

    public static AppUtil getInstance() {
        return AppUtil.AppUtilHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private AppUtil() {
    }

    /**
     * 屏幕分辨率
     *
     * @param context
     * @return
     */
    public int[] Dispay(Activity context) {
        WindowManager windowManager = context.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return new int[]{screenWidth, screenHeight};
    }

    public final int MY_PERMISSIONS_REQUEST_CAMERA = 100;//拍照权限
    public final int MY_PERMISSIONS_REQUEST_READ_SD = 101;//读SD卡权限
    public final int MY_PERMISSIONS_REQUEST_WRITE_SK = 102;//写SD卡权限
    public final int MY_PERMISSIONS_REQUEST_READ_SD_PHOTOALBUM = 103;//写SD卡权限
    public final int MY_PERMISSIONS_REQUEST_WRITE_SK_PHOTOALBUM = 104;//写SD卡权限
    public final int CAPTURE_IMAGE_REQUEST = 104;//拍照后的返回值
    public final int LOAD_IMAGE_REQUEST = 105;//相册的返回值
    public final int CLIP_IMAGE_REQUEST = 106;//剪裁图片的返回值
    public final String IMAGE_TYPE = "image/*";
    public File mOutFile;//图片uri路径
    public File mImageFile = null;//图片file路径
    public final int MY_PERMISSIONS_REQUEST_CONTACTS = 107;
    public final int MY_PERMISSIONS_PHONE_DIAL = 108;
    public final int MY_PERMISSIONS_PHONE_IMEI = 109;
    public final int MY_PERMISSIONS_PHONE_READWRITE = 110;

    public final int TIME = 2000;

    public Integer mBuildVersion = android.os.Build.VERSION.SDK_INT;//当前SDK版本


    public String PHONE_MOVE = "10086";

    public String PHONE_UNICOM = "10010";

    public String PHONE_TELECOM = "10000";


    /**
     * [获取应用程序版本名称信息]
     *
     * @param context
     * @return 当前应用的版本名称 版本号
     */
    public String getVersionName(int code, Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0 );
            if (code == 1) {
                return packageInfo.versionName;
            } else {
                return packageInfo.versionCode + "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void getManager(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE );
        imm.showSoftInput( view, InputMethodManager.SHOW_FORCED );
        imm.hideSoftInputFromWindow( view.getWindowToken(), 0 );
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static int[] getScreenDispaly(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService( Context.WINDOW_SERVICE );
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        return result;
    }

    /**
     * 获取城市数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getJson(Context context, String fileName) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open( fileName );
            BufferedInputStream bufferedInputStream = new BufferedInputStream( inputStream );
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bufferedInputStream.read( buffer )) != -1) {
                baos.write( buffer, 0, len );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toString();
    }


    private static final String CHANNEL_KEY = "UMENG_CHANNEL";
    private static String mChannel;

    /**
     * 返回市场。 如果获取失败返回defaultChannel
     *
     * @param context
     * @return
     */
    public String getChannel(Context context, int id_channle) {
        // 从apk中获取
        mChannel = getChannelFromApk1( context, CHANNEL_KEY );
        return mChannle( id_channle, mChannel );
    }

    private String getChannelFromApk1(Context context, String channelKey) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo( context.getPackageName(),
                    PackageManager.GET_META_DATA );
            return appInfo.metaData.getString( channelKey );
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * 转二进制
     *
     * @param bm
     * @return
     */
    public byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress( Bitmap.CompressFormat.PNG, 100, baos );
        return baos.toByteArray();
    }

    public String getMacAddress() {
        String macAddress;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface;
        try {
            networkInterface = NetworkInterface.getByName( "eth1" );
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName( "wlan0" );
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();
            for (byte b : addr) {
                buf.append( String.format( "%02X:", b ) );
            }
            if (buf.length() > 0) {
                buf.deleteCharAt( buf.length() - 1 );
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        return macAddress;
    }


    /**
     * 渠道id
     */
    private String mChannle(int id_channle, String channle) {
        if ("QD0111".equals( channle )) {
            if (id_channle == 1) {
                return "安卓应用市场" + "QD0111";
            } else {
                return "QD0111";
            }
        } else if ("QD0026".equals( channle )) {
            if (id_channle == 1) {
                return "360手机助手" + "QD0026";
            } else {
                return "QD0026";
            }
//        } else if ("QD0029".equals( channle )) {
//            if (id_channle == 1) {
//                return "豌豆荚开发者中心" + "QD0029";
//            } else {
//                return "QD0029";
//            }
        } else if ("QD0107".equals( channle )) {
            if (id_channle == 1) {
                return "木蚂蚁开发者中心" + "QD0107";
            } else {
                return "QD0107";
            }
        } else if ("QD0035".equals( channle )) {
            if (id_channle == 1) {
                return "小米应用商店" + "QD0035";
            } else {
                return "QD0035";
            }
        } else if ("QD0085".equals( channle )) {
            if (id_channle == 1) {
                return "华为应用商店" + "QD0085";
            } else {
                return "QD0085";
            }
//        } else if ("QD0028".equals( channle )) {
//            if (id_channle == 1) {
//                return "PP助手开发者中心" + "QD0028";
//            } else {
//                return "QD0028";
//            }
        } else if ("QD0030".equals( channle )) {
            if (id_channle == 1) {
                return "安智开发者联盟" + "QD0030";
            } else {
                return "QD0030";
            }
        } else if ("QD0021".equals( channle )) {
            if (id_channle == 1) {
                return "OPPO商店" + "QD0021";
            } else {
                return "QD0021";
            }
        } else if ("QD0025".equals( channle )) {
            if (id_channle == 1) {
                return "魅族商店" + "QD0025";
            } else {
                return "QD0025";
            }
        } else if ("QD0022".equals( channle )) {
            if (id_channle == 1) {
                return "VIVO商店" + "QD0022";
            } else {
                return "QD0022";
            }
        } else if ("QD0018".equals( channle )) {
            if (id_channle == 1) {
                return "联通沃商店" + "QD0018";
            } else {
                return "QD0018";
            }
        } else if ("QD0024".equals( channle )) {
            if (id_channle == 1) {
                return "搜狗手机助手" + "QD0024";
            } else {
                return "QD0024";
            }
        } else if ("QD0108".equals( channle )) {
            if (id_channle == 1) {
                return "应用汇" + "QD0108";
            } else {
                return "QD0108";
            }
        } else if ("QD0109".equals( channle )) {
            if (id_channle == 1) {
                return "酷派" + "QD0109";
            } else {
                return "QD0109";
            }
        } else if ("QD0034".equals( channle )) {
            if (id_channle == 1) {
                return "应用宝" + "QD0034";
            } else {
                return "QD0034";
            }
        } else if ("QD0031".equals( channle )) {
            if (id_channle == 1) {
                return "历趣市场" + "QD0031";
            } else {
                return "QD0031";
            }
        } else if ("QD0112".equals( channle )) {
            if (id_channle == 1) {
                return "机锋开发者平台" + "QD0112";
            } else {
                return "QD0112";
            }
        } else if ("QD0113".equals( channle )) {
            if (id_channle == 1) {
                return "自然" + "QD0113";
            } else {
                return "QD0113";
            }
        } else if ("QD0106".equals( channle )) {
            if (id_channle == 1) {
                return "三星" + "QD0106";
            } else {
                return "QD0106";
            }
//        } else if ("QD0110".equals( channle )) {
//            if (id_channle == 1) {
//                return "神马" + "QD0110";
//            } else {
//                return "QD0110";
//            }
        } else if ("QD0023".equals( channle )) {
            if (id_channle == 1) {
                return "百度手机助手" + "QD0023";
            } else {
                return "QD0023";
            }
        } else if ("QD0020".equals( channle )) {
            if (id_channle == 1) {
                return "sogou开发者" + "QD0020";
            } else {
                return "QD0020";
            }
        } else if ("QD0019".equals( channle )) {
            if (id_channle == 1) {
                return "优亿市场" + "QD0019";
            } else {
                return "QD0019";
            }
        } else if ("QD0016".equals( channle )) {
            if (id_channle == 1) {
                return "91手机商城发布中心" + "QD0016";
            } else {
                return "QD0016";
            }
        } else if ("QD0033".equals( channle )) {
            if (id_channle == 1) {
                return "联想乐商店" + "QD0033";
            } else {
                return "QD0033";
            }
        } else if ("QD0032".equals( channle )) {
            if (id_channle == 1) {
                return "锤子科技开发者" + "QD0032";
            } else {
                return "QD0032";
            }
        } else if ("QD0012".equals( channle )) {
            if (id_channle == 1) {
                return "乐视" + "QD0012";
            } else {
                return "QD0012";
            }
        } else if ("QD0115".equals( channle )) {
            if (id_channle == 1) {
                return "酷安" + "QD0115";
            } else {
                return "QD0115";
            }
        } else if ("QD0116".equals( channle )) {
            if (id_channle == 1) {
                return "阿里平台" + "QD0116";
            } else {
                return "QD0115";
            }
        } else if ("QD0007".equals( channle )) {
            if (id_channle == 1) {
                return "今日头条" + "QD0007";
            } else {
                return "QD0007";
            }
        } else if ("QD0141".equals( channle )) {
            if (id_channle == 1) {
                return "易云市场" + "QD0141";
            } else {
                return "QD0141";
            }
        }  else if ("QD0081".equals( channle )) {
            if (id_channle == 1) {
                return "广点通-分包-3" + "QD0081";
            } else {
                return "QD0081";
            }
        } else if ("QD0080".equals( channle )) {
            if (id_channle == 1) {
                return "广点通-分包-2" + "QD0080";
            } else {
                return "QD0080";
            }
        } else if ("QD0079".equals( channle )) {
            if (id_channle == 1) {
                return "广点通-分包-3" + "QD0079";
            } else {
                return "QD0079";
            }
        } else if ("QD0073".equals( channle )) {
            if (id_channle == 1) {
                return "新浪A" + "QD0073";
            } else {
                return "QD0073";
            }
        } else if ("QD0074".equals( channle )) {
            if (id_channle == 1) {
                return "新浪B" + "QD0074";
            } else {
                return "QD0074";
            }
        } else if ("QD0075".equals( channle )) {
            if (id_channle == 1) {
                return "新浪C" + "QD0075";
            } else {
                return "QD0075";
            }
        } else if ("QD0054".equals( channle )) {
            if (id_channle == 1) {
                return "应用宝-推广" + "QD0054";
            } else {
                return "QD0054";
            }
        } else if ("QD0009".equals( channle )) {
            if (id_channle == 1) {
                return "今日头条" + "QD0009";
            } else {
                return "QD0009";
            }
        } else if ("QD0099".equals( channle )) {
            if (id_channle == 1) {
                return "乐推-E" + "QD0099";
            } else {
                return "QD0099";
            }
        } else if ("QD0098".equals( channle )) {
            if (id_channle == 1) {
                return "乐推-D" + "QD0098";
            } else {
                return "QD0098";
            }
        } else if ("QD0097".equals( channle )) {
            if (id_channle == 1) {
                return "乐推-C" + "QD0097";
            } else {
                return "QD0097";
            }
        } else if ("QD0096".equals( channle )) {
            if (id_channle == 1) {
                return "乐推-B" + "QD0096";
            } else {
                return "QD0096";
            }
        } else if ("QD0095".equals( channle )) {
            if (id_channle == 1) {
                return "乐推-A" + "QD0095";
            } else {
                return "QD0095";
            }
        } else if ("QD0057".equals( channle )) {
            if (id_channle == 1) {
                return "应用宝推广" + "QD0057";
            } else {
                return "QD0057";
            }
        } else if ("QD0056".equals( channle )) {
            if (id_channle == 1) {
                return "应用宝推广" + "QD0056";
            } else {
                return "QD0056";
            }
        } else if ("QD0008".equals( channle )) {
            if (id_channle == 1) {
                return "QQ浏览器" + "QD0008";
            } else {
                return "QD0008";
            }
        } else {
            return "";
        }
    }

}
