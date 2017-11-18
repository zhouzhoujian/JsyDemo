package cn.jishiyu11.xjsjd.http.http.i;

import java.io.IOException;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/7/1.
 * 网络接口定义
 */

public interface DataCallBack {

    void requestFailure(Request request, String name, IOException e);

    void requestSuccess(String result, String name) throws Exception;
}
