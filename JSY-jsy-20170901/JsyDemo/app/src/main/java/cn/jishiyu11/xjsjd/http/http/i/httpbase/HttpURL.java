package cn.jishiyu11.xjsjd.http.http.i.httpbase;

import android.content.Context;

import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;

import java.io.Serializable;

/**
 * Created by vvguoliang on 2017/7/1.
 * <p>
 * OKhttp请求
 */

public class HttpURL implements Serializable {

    /**
     * 单例对象实例
     */
    private static class HttpURLHolder {
        static final HttpURL INSTANCE = new HttpURL();
    }

    public static HttpURL getInstance() {
        return HttpURL.HttpURLHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private HttpURL() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }

    private String HTTP_URL_KEY = "httpUrlKey";
    private String HTTP_URL = "http://app.jishiyu11.cn/index.php?g=app";

    public String HTTP_URL_PATH = "http://app.jishiyu11.cn/data/upload";

    public String HTTP_OPERATOR = "http://api.tanzhishuju.com/api/gateway";

    public String HTTP_URL_JUAICHA = "http://www.kuaicha.info/mobile/credit/credit.html";

    public String HTTP_DETAILS = "https://www.jishiyu007.com/app/zhiqinggaozhi_bj.html";

    private String HTTP_PATH = "http://47.93.122.140:8001//index.php?g=app";

    public void initUrl(Context context) {
        if (SharedPreferencesUtils.contains( context, HTTP_URL_KEY )) {
            HTTP_URL = SharedPreferencesUtils.get( context, HTTP_URL_KEY, HTTP_URL ).toString();
        }
        HTTP_URL = HTTP_URL.replace( "\n", "" );
        HTTP_URL = HTTP_URL.replace( " ", "" );

    }

    /**
     * 更新URL地址
     *
     * @param context
     * @param url
     */
    public void updateUrl(Context context, String url) {
        SharedPreferencesUtils.put( context, HTTP_URL_KEY, url );
    }

    public String CODE = HTTP_URL + "&m=login&a=send_code";

    public String LOGO = HTTP_URL + "&m=login&a=dologin";

    public String logoinString = "http://47.93.122.140:8003/index.php?g=app"+"&m=login&a=dologin";//登录测试

    public String PASSWORD = HTTP_URL + "&m=register&a=reset_password";

    public String REGISTER_CODE = HTTP_URL + "&m=register&a=send_code";

    public String REGISTER = HTTP_URL + "&m=register&a=doregister";

    public String BANNER = HTTP_URL + "&m=banner&a=postList";

    public String HOMEPRODUCT = HTTP_URL + "&m=product&a=change_list";

    public String BANK = HTTP_URL + "&m=bank&a=postList";

    public String PRODUCT = HTTP_URL + "&m=product&a=postList";

    public String PRODUCT_DETAIL = HTTP_URL + "&m=product&a=postDetail";//产品详情

    public String PRODUCT_FILTER = HTTP_URL + "&m=product&a=filter";

    public String SESAMECREDIT = HTTP_URL + "&m=alipay&a=signEncrypt";

    public String PRODUCTTYPE = HTTP_URL + "&m=product&a=product_type";

    public String USERINFO = HTTP_URL + "&m=userinfo&a=postDetail";

    public String FEEDBACK = HTTP_PATH + "&m=feedback&a=postAdd";

    public String USERINFOADD = HTTP_URL + "&m=userinfo&a=postAdd";

    public String PERSONALDATACREDIT = HTTP_URL + "&m=userdetail&a=credit_list";

    public String PERSONALDATACREDITADD = HTTP_URL + "&m=userdetail&a=credit_add";

    public String COMPANYSTATUSADD = HTTP_URL + "&m=userdetail&a=company_status_add";

    public String COMPANYSTATUSLLIST = HTTP_URL + "&m=userdetail&a=company_status_list";

    public String FAMILYADD = HTTP_URL + "&m=userdetail&a=family_add";

    public String FAMILYLIST = HTTP_URL + "&m=userdetail&a=family_list";

    public String OTHERADD = HTTP_URL + "&m=userdetail&a=other_add";

    public String OTHERLIST = HTTP_URL + "&m=userdetail&a=other_list";

    public String HOUSEADD = HTTP_URL + "&m=userdetail&a=house_add";

    public String HOUSELIST = HTTP_URL + "&m=userdetail&a=house_list";

    public String CARADD = HTTP_URL + "&m=userdetail&a=car_add";

    public String CARLIST = HTTP_URL + "&m=userdetail&a=car_list";

    public String PARPERSADD = HTTP_PATH + "&m=userdetail&a=parpers_add";

    public String PARPERSLIST = HTTP_URL + "&m=userdetail&a=parpers_list";

    public String STATUS = HTTP_URL + "&m=userinfo&a=status";

    public String IDCARDADD = HTTP_PATH + "&m=userdetail&a=idcard_add";

    public String SHARE = HTTP_URL + "&m=userinfo&a=share";

    public String AUTHORIZE = HTTP_URL + "&m=alipay&a=anyAuthorize";

    public String SIGN = HTTP_URL + "&m=userdetail&a=mobileSign";

    public String BASEADD = HTTP_URL + "&m=userdetail&a=base_add";

    public String OTHER_INFO = HTTP_URL + "&m=userdetail&a=other_info_add";

    public String PRODUCTINDEX = HTTP_URL + "&m=product&a=index";

    public String USERDATAIL = HTTP_URL + "&m=userdetail&a=isAuth";

//    public String USERDATAILAUTH = HTTP_URL + "&m=operator&a=postAdd";
    public String USERDATAILAUTH = "http://47.93.122.140:8003/index.php?g=app" + "&m=operator&a=postAdd";

    public String ACTIVITY = HTTP_URL + "&m=toutiao&a=activate";

    public String BOOTAPP = HTTP_URL + "&m=app&a=boot";

    public String UPDATE = HTTP_URL + "&m=app&a=update";

    public String HITSPRODUCT = HTTP_URL + "&m=product&a=hits";

    public String REGISTERCODE = HTTP_URL + "&m=register&a=bycode";

    public String USERDETAILBASE = HTTP_URL + "&m=userdetail&a=base_list";

    public String USERDETAILOTHER = HTTP_URL + "&m=userdetail&a=other_info_list";

    public String USERDETAILIDCARD = HTTP_URL + "&m=userdetail&a=idcard_list";

    public String PRODUCTCATELIST = HTTP_URL + "&m=productcate&a=getList";

    //提交评论
    public String SUBMITEVALUATE = HTTP_URL + "&m=comment&a=postAdd";

    /* 评论 拉取*/
    public String COMMENTLIST = HTTP_URL + "&m=comment&a=getList";

    /* 拉取借款记录 */
    public String USERINFORECORD = HTTP_URL + "&m=loanrecord&a=getList";
    /*提交借款记录*/
    public String USERINFOPOSTHISTRY = HTTP_URL + "&m=loanrecord&a=postAdd";

    /*浏览记录*/
    public String USERINFORBROWS = HTTP_URL + "&m=userinfo&a=record";

    /*FaceId====IDCard*/
    public String FACE_IDCARD_URL ="https://api.faceid.com/faceid/v1/ocridcard";

    //小额钱包首页firstpager
//    public String FIRST_PAGER_DATA = HTTP_URL + "&m=app&a=index";
    public String FIRST_PAGER_DATA = "http://47.93.122.140:8003/index.php?g=app" + "&m=app&a=index";

    //获取个人资料填写进度
//    public String PERSONAL_DATA_STATION = HTTP_URL+"&m=datastatus&a=postStatus";
    public String PERSONAL_DATA_STATUS = "http://47.93.122.140:8003/index.php?g=app"+"&m=datastatus&a=postStatus";

    //上传银行卡认证信息
//    public String PERSONAL_BANK_DATA = "HTTP_URL+"&m=app&a=index";
    public String PERSONAL_BANK_DATA = "http://47.93.122.140:8003/index.php?g=app"+"&m=app&a=index";

    //上传Face对比结果
//    public String PERSONAL_UPLOAD_FACE_RESULT = "HTTP_URL+"&m=faceocr&a=postAdd";
    public String PERSONAL_UPLOAD_FACE_RESULT = "http://47.93.122.140:8003/index.php?g=app"+"&m=faceocr&a=postAdd";

    //获取授权状态
    public String PERSONAL3_AUTHSTATUS = HTTP_URL+"&m=authstatus&a=postStatus";
//    public String PERSONAL3_AUTHSTATUS = "http://47.93.122.140:8003/index.php?g=app"+"&m=authstatus&a=postStatus";

 //上传个人资料   验证personal2
//    public String PERSONAL2_DATAS = HTTP_URL+"&m=personaldata&a=postAdd";
    public String PERSONAL2_DATAS = "http://47.93.122.140:8003/index.php?g=app"+"&m=personaldata&a=postAdd";

   //获取fragment2  数据
//    public String FRAGMENT2_DATAS = HTTP_URL+"&m=personaldata&a=postAdd";
    public String FRAGMENT2_DATAS = "http://47.93.122.140:8003/index.php?g=app"+"&m=pushorder&a=postList";

 //获取fragment2  dialog
//    public String FRAGMENT2_DATAS = HTTP_URL+"&m=personaldata&a=postAdd";
    public String FRAGMENT2_DATAS_DIALOG = "http://47.93.122.140:8003/index.php?g=app"+"&m=pushorder&a=postDetail";

 //获取fragment2  dialog  确认借款
//    public String FRAGMENT2_DATAS_DIALOG_SURE = HTTP_URL+"&m=pushorder&a=postExtract";
    public String FRAGMENT2_DATAS_DIALOG_SURE = "http://47.93.122.140:8003/index.php?g=app"+"&m=pushorder&a=postExtract";

//获取fragment2  dialog  确认借款
//    public String PERSONAL3_POST_AUTHSTATUS = HTTP_URL+"&m=authstatus&a=postStatus";
    public String PERSONAL3_POST_AUTHSTATUS = "http://47.93.122.140:8003/index.php?g=app"+"&m=authstatus&a=postStatus";


    //获取是否可以登录getUserLoginAuthorStatus
    public String USER_LOGIN_STATUS = HTTP_URL +"&m=device&a=record";
//    public String USER_LOGIN_STATUS = "http://47.93.122.140:8003/index.php?g=app"+"&m=device&a=record";

 //获取是否可以登录
//    public String USER_LOGOUT_STATUS = HTTP_URL +"&m=device&a=loginout";
    public String USER_LOGOUT_STATUS = "http://47.93.122.140:8003/index.php?g=app"+"&m=device&a=loginout";

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:10
     * 方法说明：算话运营商认证接口
     */
//    public final String HTTPURL_SUANHUA="http://39.106.125.157:8083/jsyYys";
    public final String HTTPURL_SUANHUA="http://39.106.125.157:8083/jsyYys";
    public String SH_INTI = HTTPURL_SUANHUA+"/keepWord/kctc";

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:10
     * 方法说明：算话运营商登录接口
     */
    public String SH_LOGIN_USER = HTTPURL_SUANHUA + "/keepWord/ktlf";

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:09
     * 方法说明：算话运营商验证接口
     */
    public String SH_LOGIN_CHECK_USER = HTTPURL_SUANHUA + "/keepWord/ksfv";

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:08
     * 方法说明：算话运营商获取短信验证码接口
     */
    public String SH_USER_DATA_CHECKCODE_SMS = HTTPURL_SUANHUA + "/keepWord/smsvc";

    /**
     * @author jsy_zj
     * created at 2017/11/17 14:09
     * 方法说明：算话运营商获取图片验证码接口
     */
    public String SH_USER_DATA_CHECKCODE_IMG = HTTPURL_SUANHUA + "/keepWord/kic";


    /**
     * @author jsy_zj
     * created at 2017/11/17 14:09
     * 方法说明：拉取活体检测信息
     */
//    public String DATA_FOR_LIVE =  HTTP_URL + "&m=faceocr&a=postDetail";
    public String DATA_FOR_LIVE =  "http://47.93.122.140:8003/index.php?g=app" + "&m=faceocr&a=postDetail";




}
