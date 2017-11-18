package cn.jishiyu11.xjsjd.utils;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * Created by jsy_zj on 2017/11/2.
 */

public class FaceUtils {

    public String apikey = "JQWMHnSn4TJwkIM39LVquQNTGAttYB8w";//Face++
    public String apisecret = "l1E4qh4WRtw2GLpIIUZNCEc5xSZHwKbY";//Face++

    public String IDCardFrontBM = "";//身份证正面照
    public String IDCardReverseBM = "";//身份证反面照
    public String IDCardFrontImg = "";//身份证正面照

    public String LivaCheckImg = "";//活体检测头像

    public boolean IsNetAuthor = false;//获取网络授权

    public String UUIDString = "";

    public boolean IsHaveIDCardFrontBM = false;//是否有正面照

    public boolean IsHaveIDCardReverseBM = false;//是否有背面照

    public String UserChainName = "";//身份证名称

    public String UserChainIDCardId = "";//身份证id

    public boolean IsHavaLiveUpSuccess = false;//上传结果成功否

    public boolean IsHaveCheckBox = false;

    public void setChinaNametoShare(Context context,String chinaName){
        SharedPreferencesUtils.put(context,"UserChinaName",chinaName);
    }
  public void setChinaIDCardIdtoShare(Context context,String chinaIDCardId){
        SharedPreferencesUtils.put(context,"ChinaIDCardId",chinaIDCardId);
    }

    public Map<String,Object> IDCardFront;//身份证信息


//    public String getLivaCheck_bestImgPath(Context context){//活体检测头像
//        return LivaCheckImg;
//    }
//     public String getLivaCheck_evnImgPath(Context context){//活体检测头像
//        return LivaCheckImg;
//    }

    /**
     * 返回当前时间   例：20171102145712
     * */
    public String getCurrentDataTime(){

        SimpleDateFormat sDateFormat    =   new SimpleDateFormat("yyyyMMddhhmmss");
        String    date    =    sDateFormat.format(new  java.util.Date());

        return date;
    }


    /**
     * 单例对象实例
     */
    private static class FaceUtilHolder {
        static final FaceUtils INSTANCE = new FaceUtils();
    }

    public static FaceUtils getInstance() {
        return FaceUtilHolder.INSTANCE;
    }

}
