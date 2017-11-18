package cn.jishiyu11.xjsjd.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.UIListenerManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.UUID;

/**
 * Created by vvguoliang on 2017/7/26.
 * 获取双卡中的IMEI
 */

public class CTelephoneInfo {

    private String imeiSIM1;// IMEI
    private String imeiSIM2;//IMEI
    private String iNumeric1;//sim1 code number
    private String iNumeric2;//sim2 code number
    private boolean isSIM1Ready;//sim1
    private boolean isSIM2Ready;//sim2
    private String iDataConnected1 = "0";//sim1 0 no, 1 connecting, 2 connected, 3 suspended.
    private String iDataConnected2 = "0";//sim2
    @SuppressLint("StaticFieldLeak")
    private static CTelephoneInfo CTelephoneInfo;
    private static Context mContext;

    private String line1Number;
    private String line2Number;

    private CTelephoneInfo() {
    }

    public synchronized static CTelephoneInfo getInstance(Context context){
        if(CTelephoneInfo == null) {
            CTelephoneInfo = new CTelephoneInfo();
        }
        mContext = context;
        return CTelephoneInfo;
    }

    public String getImeiSIM1() {
        return imeiSIM1;
    }

    public String getImeiSIM2() {
        return imeiSIM2;
    }

    public boolean isSIM1Ready() {
        return isSIM1Ready;
    }

    public boolean isSIM2Ready() {
        return isSIM2Ready;
    }

    public boolean isDualSim(){
        return imeiSIM2 != null;
    }

    public String getLine1Number() {
        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public String getLine2Number() {
        return line2Number;
    }

    public void setLine2Number(String line2Number) {
        this.line2Number = line2Number;
    }

    public boolean isDataConnected1(){
        return TextUtils.equals(iDataConnected1, "2") || TextUtils.equals(iDataConnected1, "1");
    }

    public boolean isDataConnected2(){
        return TextUtils.equals(iDataConnected2, "2") || TextUtils.equals(iDataConnected2, "1");
    }

    public String getINumeric1(){
        return iNumeric1;
    }

    public String getINumeric2(){
        return iNumeric2;
    }

    public String getINumeric(){
        if(imeiSIM2 != null){
            if(iNumeric1 != null && iNumeric1.length() > 1)
                return iNumeric1;

            if(iNumeric2 != null && iNumeric2.length() > 1)
                return iNumeric2;
        }
        return iNumeric1;
    }

    @SuppressLint({"HardwareIds", "MissingPermission"})
    public void setCTelephoneInfo(){
        TelephonyManager telephonyManager = ((TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE));
        CTelephoneInfo.imeiSIM1 = telephonyManager.getDeviceId();
        CTelephoneInfo.imeiSIM2 = null;
        this.setLine1Number(telephonyManager.getLine1Number());
        try {
            CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceIdGemini", 0);
            CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceIdGemini", 1);
            CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperatorGemini", 0);
            CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperatorGemini", 1);
            CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataStateGemini", 0);
            CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            try {
                CTelephoneInfo.imeiSIM1 = getOperatorBySlot(mContext, "getDeviceId", 0);
                CTelephoneInfo.imeiSIM2 = getOperatorBySlot(mContext, "getDeviceId", 1);
                CTelephoneInfo.iNumeric1 = getOperatorBySlot(mContext, "getSimOperator", 0);
                CTelephoneInfo.iNumeric2 = getOperatorBySlot(mContext, "getSimOperator", 1);
                CTelephoneInfo.iDataConnected1 = getOperatorBySlot(mContext, "getDataState", 0);
                CTelephoneInfo.iDataConnected2 = getOperatorBySlot(mContext, "getDataState", 1);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
            }
        }
        CTelephoneInfo.isSIM1Ready = telephonyManager.getSimState() == TelephonyManager.SIM_STATE_READY;
        CTelephoneInfo.isSIM2Ready = false;

        try {
            CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 0);
            CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimStateGemini", 1);
        } catch (GeminiMethodNotFoundException e) {
            e.printStackTrace();
            try {
                CTelephoneInfo.isSIM1Ready = getSIMStateBySlot(mContext, "getSimState", 0);
                CTelephoneInfo.isSIM2Ready = getSIMStateBySlot(mContext, "getSimState", 1);
            } catch (GeminiMethodNotFoundException e1) {
                //Call here for next manufacturer's predicted method name if you wish
                e1.printStackTrace();
            }
        }
    }

    private static  String getOperatorBySlot(Context context, String predictedMethodName, int slotID)
            throws GeminiMethodNotFoundException {
        String inumeric = null;
        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        try{
            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());
            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimID = telephonyClass.getMethod(predictedMethodName, parameter);
            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimID.invoke(telephony, obParameter);
            if(ob_phone != null){
                inumeric = ob_phone.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }
        return inumeric;
    }

    private static  boolean getSIMStateBySlot(Context context, String predictedMethodName, int slotID) throws GeminiMethodNotFoundException {

        boolean isReady = false;

        TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        try{

            Class<?> telephonyClass = Class.forName(telephony.getClass().getName());

            Class<?>[] parameter = new Class[1];
            parameter[0] = int.class;
            Method getSimStateGemini = telephonyClass.getMethod(predictedMethodName, parameter);

            Object[] obParameter = new Object[1];
            obParameter[0] = slotID;
            Object ob_phone = getSimStateGemini.invoke(telephony, obParameter);

            if(ob_phone != null){
                int simState = Integer.parseInt(ob_phone.toString());
                if(simState == TelephonyManager.SIM_STATE_READY){
                    isReady = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new GeminiMethodNotFoundException(predictedMethodName);
        }

        return isReady;
    }

    private static class GeminiMethodNotFoundException extends Exception {
        /**
         *
         */
        private static final long serialVersionUID = -3241033488141442594L;

        public GeminiMethodNotFoundException(String info) {
            super(info);
        }
    }
    /**
     * 获取设备的唯一设备吗
     * */
    public static String getPhoneDeviceID(Context context){
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String DEVICE_ID = tm.getDeviceId()+"";
        return DEVICE_ID;
    }


    @SuppressLint("MissingPermission")
    public static String getPhoneUDID(Context context){
        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String tmDevice= "";
        String tmSerial = "";
        String androidId = "";
        if (tm!=null){
            tmDevice = "" + tm.getDeviceId();
            tmSerial = "" + tm.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(
                    context.getContentResolver(),
                    android.provider.Settings.Secure.ANDROID_ID);
        }

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
        String deviceId = deviceUuid.toString();
        return "XEQB-AND"+deviceId;
    }

}
