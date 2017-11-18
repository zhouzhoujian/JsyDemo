package cn.jishiyu11.xjsjd.EntityClass;

/**
 * Created by vvguoliang on 2017/7/1.
 * 注册 登入 验证码 修改
 */

public class RegisterSignCodeModify {

    private String info = "";
    private int status = 0;
    private String url = "";
    private Boolean referer = false;
    private String state = "";

    private String username = "";
    private String uid = "";
    private String token = "";

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getReferer() {
        return referer;
    }

    public void setReferer(Boolean referer) {
        this.referer = referer;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
