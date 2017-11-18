package cn.jishiyu11.xjsjd.http.http.i.httpbase;

import android.os.Handler;
import android.os.Looper;

import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.utils.ToatUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by 若兰 on 2016/1/23.
 * 一个懂得了编程乐趣的小白，希望自己
 * 能够在这个道路上走的很远，也希望自己学习到的
 * 知识可以帮助更多的人,分享就是学习的一种乐趣
 * QQ:1069584784
 * csdn:http://blog.csdn.net/wuyinlei
 */

public class OkHttpManager {

    /**
     * 静态实例
     */
    private static OkHttpManager sOkHttpManager;

    /**
     * okhttpclient实例
     */
    private OkHttpClient mClient;

    /**
     * 因为我们请求数据一般都是子线程中请求，在这里我们使用了handler
     */
    private Handler mHandler;

    /**
     * 构造方法
     */
    private OkHttpManager() {

        mClient = new OkHttpClient();

        /*
          在这里直接设置连接超时.读取超时，写入超时
         */
        mClient.newBuilder().connectTimeout(10 * 1000, TimeUnit.SECONDS);
        mClient.newBuilder().readTimeout(10 * 1000, TimeUnit.SECONDS);
        mClient.newBuilder().writeTimeout(10 * 1000, TimeUnit.SECONDS);

        /*
         * 如果是用的3.0之前的版本  使用以下直接设置连接超时.读取超时，写入超时
         */

        //client.setConnectTimeout(10, TimeUnit.SECONDS);
        //client.setWriteTimeout(10, TimeUnit.SECONDS);
        //client.setReadTimeout(30, TimeUnit.SECONDS);


        /*
         * 初始化handler
         */
        mHandler = new Handler(Looper.getMainLooper());
    }


    /*
     * 单例模式  获取OkHttpManager实例
     *
     * @return
     */
    public static OkHttpManager getInstance() {

        if (sOkHttpManager == null) {
            sOkHttpManager = new OkHttpManager();
        }
        return sOkHttpManager;
    }

    //-------------------------同步的方式请求数据--------------------------

    /*
     * 对外提供的get方法,同步的方式
     *
     * @param url 传入的地址
     * @return
     */
    public static Response getSync(String url) {

        //通过获取到的实例来调用内部方法
        return sOkHttpManager.inner_getSync(url);
    }

    /*
     * GET方式请求的内部逻辑处理方式，同步的方式
     *
     * @param url
     * @return
     */
    private Response inner_getSync(String url) {
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            //同步请求返回的是response对象
            response = mClient.newCall(request).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    /*
     * 对外提供的同步获取String的方法
     *
     * @param url
     * @return
     */
    public static String getSyncString(String url) {
        return sOkHttpManager.inner_getSyncString(url);
    }


    /*
     * 同步方法
     */
    private String inner_getSyncString(String url) {
        String result = null;
        try {
            /*
             * 把取得到的结果转为字符串，这里最好用string()
             */
            result = inner_getSync(url).body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    //-------------------------异步的方式请求数据--------------------------
    public static void getAsync(String url, String name, DataCallBack callBack) {
        getInstance().inner_getAsync(url, name, callBack);
    }

    /*
     * 内部逻辑请求的方法
     *
     * @param url
     * @param callBack
     * @return
     */
    private void inner_getAsync(String url, final String name, final DataCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    deliverDataFailure(request, name, e, callBack);
                }
                deliverDataSuccess(result, name, callBack);
            }
        });
    }


    /*
     * 分发失败的时候调用
     *
     * @param request
     * @param e
     * @param callBack
     */
    private void deliverDataFailure(final Request request, final String name, final IOException e, final DataCallBack callBack) {
        /*
         * 在这里使用异步处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.requestFailure(request, name, e);
                }
            }
        });
    }

    /*
     * 分发成功的时候调用
     *
     * @param result
     * @param callBack
     */
    private void deliverDataSuccess(final String result, final String name, final DataCallBack callBack) {
        /*
         * 在这里使用异步线程处理
         */
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        if (getjson(result)) {
                            callBack.requestSuccess(result, name);
                        } else {
                            callBack.requestFailure(null, name, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private boolean getjson(String result) {
        try {
            JSONObject object = new JSONObject(result);
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }


    //-------------------------提交表单--------------------------

    public static void postAsync(String url, String name, Map<String, Object> params, DataCallBack callBack) {
        getInstance().inner_postAsync(url, name, params, callBack);
    }

    private void inner_postAsync(String url, final String name, Map<String, Object> params, final DataCallBack callBack) {

        RequestBody requestBody;
        if (params == null) {
            params = new HashMap<>();
        }
        /*
         * 如果是3.0之前版本的，构建表单数据是下面的一句
         */
        //FormEncodingBuilder builder = new FormEncodingBuilder();

        /*
         * 3.0之后版本
         */
        FormBody.Builder builder = new FormBody.Builder();
        /*
         * 在这对添加的参数进行遍历，map遍历有四种方式，如果想要了解的可以网上查找
         */
        for (Map.Entry<String, Object> map : params.entrySet()) {
            String key = map.getKey();
            String value;
            /*
             * 判断值是否是空的
             */
            if (map.getValue() == null) {
                value = "";
            } else {
                value = map.getValue().toString();
            }
            /*
             * 把key和value添加到formbody中
             */
            builder.add(key, value);
        }
        requestBody = builder.build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(requestBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }


    //-------------------------文件下载--------------------------
    public static void downloadAsync(String url, String name, String desDir, DataCallBack callBack) {
        getInstance().inner_downloadAsync(url, name, desDir, callBack);
    }

    /*
     * 下载文件的内部逻辑处理类
     *
     * @param url      下载地址
     * @param desDir   目标地址
     * @param callBack
     */
    private void inner_downloadAsync(final String url, final String name, final String desDir, final DataCallBack callBack) {
        final Request request = new Request.Builder().url(url).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                /*
                 * 在这里进行文件的下载处理
                 */
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;
                try {
                    //文件名和目标地址
                    File file = new File(desDir, getFileName(url));
                    //把请求回来的response对象装换为字节流
                    inputStream = response.body().byteStream();
                    fileOutputStream = new FileOutputStream(file);
                    int len;
                    byte[] bytes = new byte[2048];
                    //循环读取数据
                    while ((len = inputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, len);
                    }
                    //关闭文件输出流
                    fileOutputStream.flush();
                    //调用分发数据成功的方法
                    deliverDataSuccess(file.getAbsolutePath(), name, callBack);
                } catch (IOException e) {
                    //如果失败，调用此方法
                    deliverDataFailure(request, name, e, callBack);
                    e.printStackTrace();
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }

                }
            }

        });
    }

    /*
     * 根据文件url获取文件的路径名字
     *
     * @param url
     * @return
     */
    private String getFileName(String url) {
        int separatorIndex = url.lastIndexOf("/");
        String path = (separatorIndex < 0) ? url : url.substring(separatorIndex + 1, url.length());
        return path;
    }

    public static void uploadAsync(String url, final String name, String uid, String is_face, File name1, File name2, final DataCallBack callBack) {
        getInstance().inner_postAsync(url, name, uid, is_face, name1, name2, callBack);
    }

    private void inner_postAsync(String url, final String name, String uid, String is_face, File name1, File name2, final DataCallBack callBack) {
        /* 第一个要上传的file */
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);

        /* 第一个要上传的file */
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);

        String file1Name = "jsy.png";
        MultipartBody mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "p" */
                .addFormDataPart("uid", uid)
            /* 底下是上传了两个文件 */
                .addFormDataPart("photo1", name1.toString(), fileBody1)
                .addFormDataPart("photo2", name2.toString(), fileBody2)
                .addFormDataPart("is_face", is_face)
                .build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(mBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }

    public static void uploadAsyncFaceResult(String url, final String name, String uid,
                                             String real_name, String card_no, String ocr_nation,
                                             String ocr_address, String compare_score, String ocr_gender,String is_check,
                                             File name1, File name2, File name3, File name4,
                                             final DataCallBack callBack) {
        getInstance().inner_postAsyncFaceResult(url, name, uid,
                real_name, card_no, ocr_nation,
                ocr_address, compare_score, ocr_gender,is_check,
                name1, name2, name3, name4, callBack);
    }

    private void inner_postAsyncFaceResult(String url, final String name, String uid,
                                           String real_name, String card_no, String ocr_nation,
                                           String ocr_address, String compare_score, String ocr_gender,String is_check,
                                           File name1, File name2, File name3, File name4,
                                           final DataCallBack callBack) {
        /* 第一个要上传的file */
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);

        /* 第二个要上传的file */
        RequestBody fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);

 /* 第三个要上传的file */
        RequestBody fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);

 /* 第四个要上传的file */
        RequestBody fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);

        String file1Name = "jsy.png";
        MultipartBody mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "p" */
                .addFormDataPart("uid", uid)
                .addFormDataPart("real_name", real_name)
                .addFormDataPart("is_check", is_check)
                .addFormDataPart("card_no", card_no)
                .addFormDataPart("ocr_nation", ocr_nation)
                .addFormDataPart("ocr_gender", ocr_gender)
                .addFormDataPart("ocr_address", ocr_address)
                .addFormDataPart("compare_score", compare_score)
            /* 底下是上传了两个文件 */
                .addFormDataPart("front_photo", name1.toString(), fileBody1)
                .addFormDataPart("back_photo", name2.toString(), fileBody2)
                .addFormDataPart("head_portrait", name2.toString(), fileBody3)
                .addFormDataPart("live_photo", name2.toString(), fileBody4)
                .build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(mBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }


    public static void facepostimage1(String url, final String name, String api_key, String api_secret, String legality, File image, final DataCallBack callBack) {
        getInstance().inner_postAsyncFace(url, name, api_key, api_secret, legality, image, callBack);
    }


    private void inner_postAsyncFace(String url, final String name, String api_key, String api_secret, String legality, File image, final DataCallBack callBack) {
        /* 第一个要上传的file */
        RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), image);

        String file1Name = "jsy.png";
        MultipartBody mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "p" */
                .addFormDataPart("api_key", api_key)
            /* 底下是上传了两个文件 */
                .addFormDataPart("image", image.toString(), fileBody1)
                .addFormDataPart("api_secret", api_secret)
                .addFormDataPart("legality", legality)
                .build();
        //结果返回
        final Request request = new Request.Builder().url(url).post(mBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }

    public static void feedbackAsync(String url, final String name, String uid, String problem, String mobile, String email, File name1,
                                     final DataCallBack callBack) {
        getInstance().inner_postAsync(url, name, uid, problem, mobile, email, name1, callBack);
    }

    private void inner_postAsync(String url, final String name, String uid, String problem, String mobile, String email, File name1,
                                 final DataCallBack callBack) {
        MultipartBody mBody;
        if (name1 != null) {
 /* 第一个要上传的file */
            RequestBody fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            String file1Name = "jsy.png";
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "p" */
                    .addFormDataPart("user_id", uid)
            /* 底下是上传了两个文件 */
                    .addFormDataPart("photo", name1.toString(), fileBody1)
                    .addFormDataPart("problem", problem)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("email", email)
                    .build();
        } else {
            String file1Name = "jsy.png";
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
            /* 上传一个普通的String参数 , key 叫 "p" */
                    .addFormDataPart("user_id", uid)
            /* 底下是上传了两个文件 */
                    .addFormDataPart("problem", problem)
                    .addFormDataPart("mobile", mobile)
                    .addFormDataPart("email", email)
                    .build();
        }
        //结果返回
        final Request request = new Request.Builder().url(url).post(mBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }


    public static void uploadAsync(String url, final String name, String uid, File name1, File name2, File name3, File name4,
                                   final DataCallBack callBack) {
        getInstance().inner_postAsync(url, name, uid, name1, name2, name3, name4, callBack);
    }

    private void inner_postAsync(String url, final String name, String uid, File name1, File name2, File name3, File name4,
                                 final DataCallBack callBack) {
        String file1Name = "jsy.png";
        MultipartBody mBody = null;
        RequestBody fileBody1;
        RequestBody fileBody2;
        RequestBody fileBody3;
        RequestBody fileBody4;
        if (name1 != null && name2 != null && name3 != null && name4 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();

        } else if (name1 != null && name2 != null && name3 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .build();
        } else if (name2 != null && name3 != null && name4 != null) {
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name1 != null && name3 != null && name4 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name1 != null && name2 != null && name4 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name1 != null && name2 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .build();
        } else if (name1 != null && name3 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .build();
        } else if (name1 != null && name4 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name2 != null && name3 != null) {
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .build();
        } else if (name2 != null && name4 != null) {
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name3 != null && name4 != null) {
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        } else if (name1 != null) {
            fileBody1 = RequestBody.create(MediaType.parse("application/octet-stream"), name1);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo1", name1.toString(), fileBody1)
                    .build();
        } else if (name2 != null) {
            fileBody2 = RequestBody.create(MediaType.parse("application/octet-stream"), name2);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo2", name2.toString(), fileBody2)
                    .build();
        } else if (name3 != null) {
            fileBody3 = RequestBody.create(MediaType.parse("application/octet-stream"), name3);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo3", name3.toString(), fileBody3)
                    .build();
        } else if (name4 != null) {
            fileBody4 = RequestBody.create(MediaType.parse("application/octet-stream"), name4);
            mBody = new MultipartBody.Builder(file1Name).setType(MultipartBody.FORM)
                    .addFormDataPart("uid", uid)
                    .addFormDataPart("photo4", name4.toString(), fileBody4)
                    .build();
        }
        //结果返回
        final Request request = new Request.Builder().url(url).post(mBody).build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                deliverDataFailure(request, name, e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                deliverDataSuccess(result, name, callBack);
            }
        });
    }

}
