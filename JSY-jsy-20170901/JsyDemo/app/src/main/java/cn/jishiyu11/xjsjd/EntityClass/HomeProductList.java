package cn.jishiyu11.xjsjd.EntityClass;

import java.util.List;

/**
 * Created by vvguoliang on 2017/7/3.
 * 首页中产品list
 */

public class HomeProductList {

    private List<HomeProduct> homeProductList;
    private String code = "";
    private String msg = "";
    private String page_count = "";
    private String review = "";

    public List<HomeProduct> getHomeProductList() {
        return homeProductList;
    }

    public void setHomeProductList(List<HomeProduct> homeProductList) {
        this.homeProductList = homeProductList;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getPage_count() {
        return page_count;
    }

    public void setPage_count(String page_count) {
        this.page_count = page_count;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
