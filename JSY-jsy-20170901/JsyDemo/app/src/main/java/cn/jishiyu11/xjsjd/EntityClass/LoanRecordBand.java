package cn.jishiyu11.xjsjd.EntityClass;

/**
 * Created by vvguoliang on 2017/9/1.
 */

public class LoanRecordBand {

    private String pro_name = "";
    private String amount = "";
    private String img = "";
    private String created_at = "";
    private String deadline = "";
    private String unit = "";

    public LoanRecordBand() {
    }

    public LoanRecordBand(String pro_name, String s, String created_at) {
    }

    public LoanRecordBand(String pro_name, String amount, String img, String created_at , String deadline , String unit) {
        this.amount = amount;
        this.pro_name = pro_name;
        this.img = img;
        this.created_at = created_at;
        this.deadline = deadline;
        this.unit = unit;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getPro_name() {
        return pro_name;
    }

    public void setPro_name(String pro_name) {
        this.pro_name = pro_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
