package cn.jishiyu11.xjsjd.EntityClass;

/**
 * Created by jsy_zj on 2017/11/10.
 */

public class Fragment2LoanDetails {


    /**
     * id : 1
     * uid : 100
     * orderCode :
     * platformId :  msj
     * platform : 马上金
     * amount : 1000
     * poundage : 100
     * rate : 0.05%
     * overdueFine : 100
     * poundageType : 1
     * isEditAmount : 2
     * isEditLoanDate : 1
     * loanDate : 14
     * repaymentDate : 2017-11-21 16:45:25
     * status : 2
     * created_at : 2017-11-09 16:45:35
     * updated_at : 2017-11-09 16:45:38
     */

    private String id                   ;
    private String uid                  ;
    private String orderCode        ;
    private String platformId     ;
    private String platform         ;
    private String amount           ;
    private String poundage             ;
    private String rate             ;
    private String overdueFine          ;
    private String poundageType             ;
    private String isEditAmount             ;
    private String isEditLoanDate               ;
    private String loanDate             ;
    private String repaymentDate                ;
    private String status               ;
    private String created_at               ;
    private String updated_at               ;
    private String account;
    private String real_name;
    private String  bank_card;

    public Fragment2LoanDetails(String id, String uid, String orderCode, String platformId, String platform, String amount, String poundage, String rate, String overdueFine, String poundageType, String isEditAmount, String isEditLoanDate, String loanDate, String repaymentDate, String status, String created_at, String updated_at, String account, String real_name, String bank_card) {
        this.id = id;
        this.uid = uid;
        this.orderCode = orderCode;
        this.platformId = platformId;
        this.platform = platform;
        this.amount = amount;
        this.poundage = poundage;
        this.rate = rate;
        this.overdueFine = overdueFine;
        this.poundageType = poundageType;
        this.isEditAmount = isEditAmount;
        this.isEditLoanDate = isEditLoanDate;
        this.loanDate = loanDate;
        this.repaymentDate = repaymentDate;
        this.status = status;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.account = account;
        this.real_name = real_name;
        this.bank_card = bank_card;
    }

    public Fragment2LoanDetails() {
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
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

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPoundage() {
        return poundage;
    }

    public void setPoundage(String poundage) {
        this.poundage = poundage;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getOverdueFine() {
        return overdueFine;
    }

    public void setOverdueFine(String overdueFine) {
        this.overdueFine = overdueFine;
    }

    public String getPoundageType() {
        return poundageType;
    }

    public void setPoundageType(String poundageType) {
        this.poundageType = poundageType;
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

    public String getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(String loanDate) {
        this.loanDate = loanDate;
    }

    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
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

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }
}
