package cn.jishiyu11.xjsjd.EntityClass;

/**
 * Created by vvguoliang on 2017/7/3.
 * 首页列表数据
 */

public class HomeProduct {

    private String id = "";
    private String pro_name = "";
    private String pro_describe = "";
    private String pro_link = "";
    private String pro_hits = "";
    private String img = "";
    private String order = "";
    private String edufanwei = "";
    private String feilv = "";
    private String fv_unit = "";
    private String zuikuaifangkuan = "";
    private String qixianfanwei = "";
    private String qx_unit = "";
    private String type = "";
    private String data_id = "";
    private String other_id = "";
    private String status = "";
    private String created_at = "";
    private String updated_at = "";
    private String tiaojian = "";
    private String api_type = "";
    /**
     * pro_hits : null
     * hits : 1165
     * cat_id : 1
     * is_new : /20170904/tuijian.png
     * is_activity : /20170904/huodong.png
     */

    private String hits;
    private String cat_id;
    private String is_new;
    private String is_activity;

    public HomeProduct(String id, String pro_name, String pro_describe, String pro_link, String pro_hits, String img, String order, String edufanwei, String feilv, String fv_unit, String zuikuaifangkuan, String qixianfanwei, String qx_unit, String type, String data_id, String other_id, String status, String created_at, String updated_at, String tiaojian, String api_type, String hits, String cat_id, String is_new, String is_activity) {
        this.id = id;
        this.pro_name = pro_name;
        this.pro_describe = pro_describe;
        this.pro_link = pro_link;
        this.pro_hits = pro_hits;
        this.img = img;
        this.order = order;
        this.edufanwei = edufanwei;
        this.feilv = feilv;
        this.fv_unit = fv_unit;
        this.zuikuaifangkuan = zuikuaifangkuan;
        this.qixianfanwei = qixianfanwei;
        this.qx_unit = qx_unit;
        this.type = type;
        this.data_id = data_id;
        this.other_id = other_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tiaojian = tiaojian;
        this.api_type = api_type;
        this.hits = hits;
        this.cat_id = cat_id;
        this.is_new = is_new;
        this.is_activity = is_activity;
    }

    public HomeProduct() {
    }

    public HomeProduct(String id, String pro_name, String pro_describe, String pro_link, String pro_hits, String img, String order,
                       String edufanwei, String feilv, String fv_unit, String zuikuaifangkuan, String qixianfanwei, String qx_unit,
                       String type, String data_id, String other_id, String status, String created_at, String updated_at, String tiaojian,
                       String api_type) {
        this.id = id;
        this.pro_name = pro_name;
        this.pro_describe = pro_describe;
        this.pro_link = pro_link;
        this.pro_hits = pro_hits;
        this.img = img;
        this.order = order;
        this.edufanwei = edufanwei;
        this.feilv = feilv;
        this.fv_unit = fv_unit;
        this.zuikuaifangkuan = zuikuaifangkuan;
        this.qixianfanwei = qixianfanwei;
        this.qx_unit = qx_unit;
        this.type = type;
        this.data_id = data_id;
        this.other_id = other_id;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.tiaojian = tiaojian;
        this.api_type = api_type;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getPro_describe() {
        return pro_describe;
    }

    public void setPro_describe(String pro_describe) {
        this.pro_describe = pro_describe;
    }

    public String getPro_link() {
        return pro_link;
    }

    public void setPro_link(String pro_link) {
        this.pro_link = pro_link;
    }

    public String getPro_hits() {
        return pro_hits;
    }

    public void setPro_hits(String pro_hits) {
        this.pro_hits = pro_hits;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getEdufanwei() {
        return edufanwei;
    }

    public void setEdufanwei(String edufanwei) {
        this.edufanwei = edufanwei;
    }

    public String getFeilv() {
        return feilv;
    }

    public void setFeilv(String feilv) {
        this.feilv = feilv;
    }

    public String getFv_unit() {
        return fv_unit;
    }

    public void setFv_unit(String fv_unit) {
        this.fv_unit = fv_unit;
    }

    public String getZuikuaifangkuan() {
        return zuikuaifangkuan;
    }

    public void setZuikuaifangkuan(String zuikuaifangkuan) {
        this.zuikuaifangkuan = zuikuaifangkuan;
    }

    public String getQixianfanwei() {
        return qixianfanwei;
    }

    public void setQixianfanwei(String qixianfanwei) {
        this.qixianfanwei = qixianfanwei;
    }

    public String getQx_unit() {
        return qx_unit;
    }

    public void setQx_unit(String qx_unit) {
        this.qx_unit = qx_unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData_id() {
        return data_id;
    }

    public void setData_id(String data_id) {
        this.data_id = data_id;
    }

    public String getOther_id() {
        return other_id;
    }

    public void setOther_id(String other_id) {
        this.other_id = other_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getTiaojian() {
        return tiaojian;
    }

    public void setTiaojian(String tiaojian) {
        this.tiaojian = tiaojian;
    }

    public String getApi_type() {
        return api_type;
    }

    public void setApi_type(String api_type) {
        this.api_type = api_type;
    }

    public String getHits() {
        return hits;
    }

    public void setHits(String hits) {
        this.hits = hits;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getIs_new() {
        return is_new;
    }

    public void setIs_new(String is_new) {
        this.is_new = is_new;
    }

    public String getIs_activity() {
        return is_activity;
    }

    public void setIs_activity(String is_activity) {
        this.is_activity = is_activity;
    }
}
