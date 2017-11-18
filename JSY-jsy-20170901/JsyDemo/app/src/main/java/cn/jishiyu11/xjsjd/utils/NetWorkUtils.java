package cn.jishiyu11.xjsjd.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.List;

/**
 * 
 * @类名称：NetWorkUtils
 * @类描述：
 * @创建时间：2015-6-26 上午11:43:44
 * @修改人：
 * @修改时间：2015-6-26 上午11:43:44
 * @修改备注：
 * @version
 * 
 */
public class NetWorkUtils {
	/**
	 * @param context
	 * @return 返回有网络状态
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 网络连接提示
	 * 
	 * @param context
	 */
	public static void netWorkStateTips(Context context) {
		if (!isNetworkConnected(context))
			Toast.makeText(context, "无法连接到网络！", Toast.LENGTH_LONG).show();
	}
	/**
	 * 判断是否是wifi连接
	 * @param context
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static boolean isWifiConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mWiFiNetworkInfo = mConnectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mWiFiNetworkInfo != null) {
				return mWiFiNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	/**
	 * GPS 是否打开
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isGpsEnabled(Context context) {
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		List<String> accessibleProviders = locationManager.getProviders(true);
		return accessibleProviders != null && accessibleProviders.size() > 0;

	}
	public static void openSetting(Activity activity){
		Intent intent=new Intent("/");
		ComponentName cm=new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
		intent.setComponent(cm);
		intent.setAction("android.intent.action.VIEW");
		activity.startActivityForResult(intent, 0);
	}

	/**
	 * 获取ip地址  内网
	 * @return
	 */
	public static String getHostIP() {

		String hostIp = null;
		try {
			Enumeration nis = NetworkInterface.getNetworkInterfaces();
			InetAddress ia = null;
			while (nis.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) nis.nextElement();
				Enumeration<InetAddress> ias = ni.getInetAddresses();
				while (ias.hasMoreElements()) {
					ia = ias.nextElement();
					if (ia instanceof Inet6Address) {
						continue;// skip ipv6
					}
					String ip = ia.getHostAddress();
					if (!"127.0.0.1".equals(ip)) {
						hostIp = ia.getHostAddress();
						break;
					}
				}
			}
		} catch (SocketException e) {
			Log.i("yao", "SocketException");
			e.printStackTrace();
		}
		return hostIp;

	}

	/**
	 * 获取IP地址    外网
	 * @return
	 */
	public static String GetNetIp() {
		URL infoUrl = null;
		InputStream inStream = null;
		String line = "";
		try {
			infoUrl = new URL("http://pv.sohu.com/cityjson?ie=utf-8");
			URLConnection connection = infoUrl.openConnection();
			HttpURLConnection httpConnection = (HttpURLConnection) connection;
			int responseCode = httpConnection.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				inStream = httpConnection.getInputStream();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inStream, "utf-8"));
				StringBuilder strber = new StringBuilder();
				while ((line = reader.readLine()) != null)
					strber.append(line + "\n");
				inStream.close();
				// 从反馈的结果中提取出IP地址
				int start = strber.indexOf("{");
				int end = strber.indexOf("}");
				String json = strber.substring(start, end + 1);
				if (json != null) {
					try {
						JSONObject jsonObject = new JSONObject(json);
						line = jsonObject.optString("cip");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return line;
	}

}
