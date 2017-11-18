package cn.jishiyu11.xjsjd.utils;

import cn.jishiyu11.xjsjd.EntityClass.HomeProduct;
import cn.jishiyu11.xjsjd.EntityClass.HomeProductList;
import cn.jishiyu11.xjsjd.EntityClass.LoanRecordBand;
import cn.jishiyu11.xjsjd.EntityClass.LoanRecordBandList;
import cn.jishiyu11.xjsjd.EntityClass.ProductSu;
import cn.jishiyu11.xjsjd.EntityClass.ProductSuList;
import cn.jishiyu11.xjsjd.EntityClass.RegisterSignCodeModify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vvguoliang on 2017/7/1.
 * <p>
 * 公共Json数据解析
 */

public class JsonData {

    /**
     * 单例对象实例
     */
    private static class JsonDataHolder {
        static final JsonData INSTANCE = new JsonData();
    }

    public static JsonData getInstance() {
        return JsonData.JsonDataHolder.INSTANCE;
    }

    /**
     * private的构造函数用于避免外界直接使用new来实例化对象
     */
    private JsonData() {
    }

    /**
     * readResolve方法应对单例对象被序列化时候
     */
    private Object readResolve() {
        return getInstance();
    }

    /**
     * 登入 注册 修改密码
     *
     * @param string
     * @return
     */
    public RegisterSignCodeModify getJsonLogoCode(String string) {
        RegisterSignCodeModify registerSignCodeModify = new RegisterSignCodeModify();
        try {
            JSONObject jsonObject = new JSONObject( string );
            registerSignCodeModify.setInfo( jsonObject.optString( "info" ) );
            registerSignCodeModify.setReferer( jsonObject.optBoolean( "referer" ) );
            registerSignCodeModify.setState( jsonObject.optString( "state" ) );
            registerSignCodeModify.setStatus( jsonObject.optInt( "status" ) );
            registerSignCodeModify.setUrl( jsonObject.optString( "url" ) );
            registerSignCodeModify.setToken( jsonObject.optString( "token" ) );
            registerSignCodeModify.setUid( jsonObject.optString( "uid" ) );
            registerSignCodeModify.setUsername( jsonObject.optString( "username" ) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return registerSignCodeModify;
    }


    /**
     * 首页底部数据
     *
     * @param data
     * @return
     */
    public HomeProductList getJsonLoanProduct(String data) {
        JSONObject object;
        HomeProductList homeProductList = new HomeProductList();
        List<HomeProduct> productList = new ArrayList<>();
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "list" ) );
            for (int i = 0; array.length() > i; i++) {
                HomeProduct homeProduct = new HomeProduct();
                JSONObject jsonObject = array.getJSONObject( i );
                homeProduct.setApi_type( jsonObject.optString( "api_type" ) );
                homeProduct.setCreated_at( jsonObject.optString( "created_at" ) );
                homeProduct.setData_id( jsonObject.optString( "data_id" ) );
                homeProduct.setEdufanwei( jsonObject.optString( "edufanwei" ) );
                homeProduct.setFeilv( jsonObject.optString( "feilv" ) );
                homeProduct.setFv_unit( jsonObject.optString( "fv_unit" ) );
                homeProduct.setId( jsonObject.optString( "id" ) );
                homeProduct.setImg( jsonObject.optString( "img" ) );
                homeProduct.setOrder( jsonObject.optString( "order" ) );
                homeProduct.setOther_id( jsonObject.optString( "other_id" ) );
                homeProduct.setPro_describe( jsonObject.optString( "pro_describe" ) );
                homeProduct.setPro_hits( jsonObject.optString( "hits" ) );
                homeProduct.setPro_link( jsonObject.optString( "pro_link" ) );
                homeProduct.setPro_name( jsonObject.optString( "pro_name" ) );
                homeProduct.setQixianfanwei( jsonObject.optString( "qixianfanfanwei" ) );
                homeProduct.setQx_unit( jsonObject.optString( "qx_unit" ) );
                homeProduct.setStatus( jsonObject.optString( "status" ) );
                homeProduct.setTiaojian( jsonObject.optString( "tiaojin" ) );
                homeProduct.setType( jsonObject.optString( "type" ) );
                homeProduct.setUpdated_at( jsonObject.optString( "updated_at" ) );
                homeProduct.setZuikuaifangkuan( jsonObject.optString( "zuikuaifangkuan" ) );
                homeProduct.setHits( jsonObject.optString( "hits" ) );
                homeProduct.setCat_id( jsonObject.optString( "cat_id" ) );
                homeProduct.setIs_activity( jsonObject.optString( "is_new" ) );
                homeProduct.setIs_new( jsonObject.optString( "is_activity" ) );
                productList.add( homeProduct );
            }
            homeProductList.setHomeProductList( productList );
            homeProductList.setPage_count( object.optString( "page_count" ) );
            homeProductList.setReview( object.optString( "review" ) );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return homeProductList;
    }

    public ProductSuList getJsonProduct(String data) {
        JSONObject object;
        ProductSuList productSuList = new ProductSuList();
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            if (object.has( "recommend" )) {
                productSuList.setProductSus( getJsonProductlist( object.optString( "recommend" ) ) );
            }
            if (object.has( "quick" )) {
                productSuList.setProductSuList( getJsonProductlist( object.optString( "quick" ) ) );
            }
            List<String> list1 = new ArrayList<>();
            list1.add( "好评推荐" );
            list1.add( "极速贷款" );
            productSuList.setProduct( list1 );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productSuList;
    }

    private List<ProductSu> getJsonProductlist(String recommend) {
        List<ProductSu> productSuslist = new ArrayList<>();
        try {
            JSONArray array = new JSONArray( recommend );
            for (int i = 0; array.length() > i; i++) {
                ProductSu productSu = new ProductSu();
                JSONObject jsonObject = array.optJSONObject( i );
                productSu.setApi_type( jsonObject.optString( "api_type" ) );
                productSu.setCreated_at( jsonObject.optString( "created_at" ) );
                productSu.setData_id( jsonObject.optString( "data_id" ) );
                productSu.setData_name( jsonObject.optString( "data_name" ) );
                productSu.setEdufanwei( jsonObject.optString( "edufanwei" ) );
                productSu.setFeilv( jsonObject.optString( "feilv" ) );
                productSu.setFv_unit( jsonObject.optString( "fv_unit" ) );
                productSu.setId( jsonObject.optString( "id" ) );
                productSu.setOrder( jsonObject.optString( "order" ) );
                productSu.setOther_id( jsonObject.optString( "other_id" ) );
                productSu.setPro_describe( jsonObject.optString( "pro_describe" ) );
                productSu.setPro_hits( jsonObject.optString( "pro_hits" ) );
                productSu.setPro_link( jsonObject.optString( "pro_link" ) );
                productSu.setPro_name( jsonObject.optString( "pro_name" ) );
                productSu.setQixianfanwei( jsonObject.optString( "qixianfanwei" ) );
                productSu.setQx_unit( jsonObject.optString( "qx_unit" ) );
                productSu.setStatus( jsonObject.optString( "status" ) );
                productSu.setTiaojian( jsonObject.optString( "tiaojian" ) );
                productSu.setType( jsonObject.optString( "type" ) );
                productSu.setUpdated_at( jsonObject.optString( "updated_at" ) );
                productSu.setZuikuaifangkuan( jsonObject.optString( "zuikuaifangkuan" ) );
                productSu.setImg( jsonObject.optString( "img" ) );
                productSuslist.add( productSu );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productSuslist;
    }

    public List<Map<String, String>> getJsonPersonalDataCredit(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "edu", jsonObject.optString( "edu" ) );
                map.put( "creditcard", jsonObject.optString( "creditcard" ) );
                map.put( "credit_record", jsonObject.optString( "credit_record" ) );
                map.put( "liabilities_status", jsonObject.optString( "liabilities_status" ) );
                map.put( "loan_record", jsonObject.optString( "loan_record" ) );
                map.put( "taobao_id", jsonObject.optString( "taobao_id" ) );
                map.put( "loan_use", jsonObject.optString( "loan_use" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> getJsonPersonalDataenterprise(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "company_identity", jsonObject.optString( "company_identity" ) );
                map.put( "company_share", jsonObject.optString( "company_share" ) );
                map.put( "address", jsonObject.optString( "address" ) );
                map.put( "type", jsonObject.optString( "type" ) );
                map.put( "industry", jsonObject.optString( "industry" ) );
                map.put( "charter_date", jsonObject.optString( "charter_date" ) );
                map.put( "operation_year", jsonObject.optString( "operation_year" ) );
                map.put( "private", jsonObject.optString( "private" ) );
                map.put( "public", jsonObject.optString( "public" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> getJsonPersonalDataFamily(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "marriage_status", jsonObject.optString( "marriage_status" ) );
                map.put( "city", jsonObject.optString( "city" ) );
                map.put( "address", jsonObject.optString( "address" ) );
                map.put( "hj_address", jsonObject.optString( "hj_address" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> getJsonPersonalDataOther(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "kinsfolk_name", jsonObject.optString( "kinsfolk_name" ) );
                map.put( "kinsfolk_mobile", jsonObject.optString( "kinsfolk_mobile" ) );
                map.put( "urgency_name", jsonObject.optString( "urgency_name" ) );
                map.put( "urgency_mobile", jsonObject.optString( "urgency_mobile" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> getJsonPersonalDataHose(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "house", jsonObject.optString( "house" ) );
                map.put( "house_address", jsonObject.optString( "house_address" ) );
                map.put( "house_type", jsonObject.optString( "house_type" ) );
                map.put( "house_price", jsonObject.optString( "house_price" ) );
                map.put( "installment", jsonObject.optString( "installment" ) );
                map.put( "mortgage", jsonObject.optString( "mortgage" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Map<String, String>> getJsonPersonalDataCar(String data) {
        List<Map<String, String>> list = new ArrayList<>();
        JSONObject object;
        try {
            object = new JSONObject( data );
            object = new JSONObject( object.optString( "data" ) );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                Map<String, String> map = new HashMap<>();
                map.put( "id", jsonObject.optString( "id" ) );
                map.put( "uid", jsonObject.optString( "uid" ) );
                map.put( "car", jsonObject.optString( "car" ) );
                map.put( "car_price", jsonObject.optString( "car_price" ) );
                map.put( "use_time", jsonObject.optString( "use_time" ) );
                map.put( "installment", jsonObject.optString( "installment" ) );
                map.put( "mortgage", jsonObject.optString( "mortgage" ) );
                map.put( "created_at", jsonObject.optString( "created_at" ) );
                map.put( "updated_at", jsonObject.optString( "updated_at" ) );
                list.add( map );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public LoanRecordBandList getJsonLoanRexord(String result) {
        List<LoanRecordBand> loanRecordBands = new ArrayList<>();
        LoanRecordBandList loanRecordBandList = new LoanRecordBandList();
        try {
            JSONObject object = new JSONObject( result );
            JSONArray array = new JSONArray( object.optString( "data" ) );
            for (int i = 0; array.length() > i; i++) {
                JSONObject jsonObject = array.optJSONObject( i );
                LoanRecordBand loanRecordBand = new LoanRecordBand();
                loanRecordBand.setCreated_at( jsonObject.getString( "created_at" ) );
                loanRecordBand.setImg( jsonObject.optString( "img" ) );
//                loanRecordBand.setPro_describe( jsonObject.optString( "pro_describe" ) );
                loanRecordBand.setPro_name( jsonObject.optString( "pro_name" ) );
                loanRecordBand.setAmount(jsonObject.optString("amount"));
                loanRecordBand.setDeadline(jsonObject.optString("deadline"));
                loanRecordBand.setUnit(jsonObject.optString("unit"));
                loanRecordBands.add( loanRecordBand );
            }
            loanRecordBandList.setLoanRecordBands( loanRecordBands );
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loanRecordBandList;
    }


}
