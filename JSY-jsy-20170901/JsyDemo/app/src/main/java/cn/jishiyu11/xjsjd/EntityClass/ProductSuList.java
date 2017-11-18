package cn.jishiyu11.xjsjd.EntityClass;

import java.util.List;

/**
 * Created by vvguoliang on 2017/7/4.
 * 产品页数据集合
 */

public class ProductSuList {

    private String code = "";
    private String msg = "";

    private List<ProductSu> productSuList = null;

    private List<ProductSu> productSus = null;

    private List<String> product = null;

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

    public List<ProductSu> getProductSuList() {
        return productSuList;
    }

    public void setProductSuList(List<ProductSu> productSuList) {
        this.productSuList = productSuList;
    }

    public List<ProductSu> getProductSus() {
        return productSus;
    }

    public void setProductSus(List<ProductSu> productSus) {
        this.productSus = productSus;
    }

    public List<String> getProduct() {
        return product;
    }

    public void setProduct(List<String> product) {
        this.product = product;
    }
}
