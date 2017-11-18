package cn.jishiyu11.xjsjd.EntityClass;

import java.util.List;

/**
 * Created by vvguoliang on 2017/9/1.
 */

public class LoanRecordBandList {
    private String code = "";
    private String msg = "";
    private List<LoanRecordBand> loanRecordBands;

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

    public List<LoanRecordBand> getLoanRecordBands() {
        return loanRecordBands;
    }

    public void setLoanRecordBands(List<LoanRecordBand> loanRecordBands) {
        this.loanRecordBands = loanRecordBands;
    }
}
