package cn.jishiyu11.xjsjd.EntityClass;

/**
 * Created by jsy_zj on 2017/11/10.
 */

public class Fragment2ListData {
    private String id;
    private String uid;
    private String platform;
    private String amount;
    private String loanDate;
    private String isEditAmount;
    private String isEditLoanDate;
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Fragment2ListData(String id, String uid, String platform, String amount, String loanDate, String isEditAmount, String isEditLoanDate, String icon) {
        this.id = id;
        this.uid = uid;
        this.platform = platform;
        this.amount = amount;
        this.loanDate = loanDate;
        this.isEditAmount = isEditAmount;
        this.isEditLoanDate = isEditLoanDate;
        this.icon = icon;
    }

    public Fragment2ListData(String id, String uid, String platform, String amount, String loanDate, String isEditAmount, String isEditLoanDate) {
        this.id = id;
        this.uid = uid;
        this.platform = platform;
        this.amount = amount;
        this.loanDate = loanDate;
        this.isEditAmount = isEditAmount;
        this.isEditLoanDate = isEditLoanDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Fragment2ListData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getIsEditAmount() {
        return isEditAmount;
    }

    public void setIsEditAmount(String isEditAmount) {
        this.isEditAmount = isEditAmount;
    }

    public String getIsEditLoanDate() {
        return isEditLoanDate;
    }

    public void setIsEditLoanDate(String isEditLoanDate) {
        this.isEditLoanDate = isEditLoanDate;
    }

    @Override
    public String toString() {
        return "Fragment2ListData{" +
                "id='" + id + '\'' +
                ", uid='" + uid + '\'' +
                ", platform='" + platform + '\'' +
                ", amount='" + amount + '\'' +
                ", loanDate='" + loanDate + '\'' +
                ", isEditAmount='" + isEditAmount + '\'' +
                ", isEditLoanDate='" + isEditLoanDate + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
