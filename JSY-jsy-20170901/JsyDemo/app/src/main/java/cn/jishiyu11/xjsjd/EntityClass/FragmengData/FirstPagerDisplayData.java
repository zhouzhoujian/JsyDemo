package cn.jishiyu11.xjsjd.EntityClass.FragmengData;

/**
 * Created by jsy_zj on 2017/11/8.
 */

public class FirstPagerDisplayData {

    /**
     * id : 1
     * amount_range : [500,10000]
     * time_limit : [7,365]
     * created_at : 2017-11-07 14:16:44
     * updated_at : 2017-11-07 14:16:47
     * unit : 天
     */

    private String id;
    private String amount_range;
    private String time_limit;
    private String created_at;
    private String updated_at;
    private String unit;

    public FirstPagerDisplayData() {
    }

    public FirstPagerDisplayData(String id, String amount_range, String time_limit, String created_at, String updated_at, String unit) {
        this.id = id;
        this.amount_range = amount_range;
        this.time_limit = time_limit;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmount_range() {
        return amount_range;
    }

    public void setAmount_range(String amount_range) {
        this.amount_range = amount_range;
    }

    public String getTime_limit() {
        return time_limit;
    }

    public void setTime_limit(String time_limit) {
        this.time_limit = time_limit;
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
