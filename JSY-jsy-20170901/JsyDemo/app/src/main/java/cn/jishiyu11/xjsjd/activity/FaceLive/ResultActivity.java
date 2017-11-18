package cn.jishiyu11.xjsjd.activity.FaceLive;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.livenesslib.util.ImmersiveUtils;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.FaceLive.view.RotaterView;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData2;
import cn.jishiyu11.xjsjd.activity.IdentityCheck.PersonalData3;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.CustomDialogUtils;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.SDCardHelper;
import cn.jishiyu11.xjsjd.utils.SDCardUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import okhttp3.Request;


public class ResultActivity extends Activity implements View.OnClickListener,DataCallBack{
    private TextView textView;
    private ImageView mImageView;
    private LinearLayout ll_result_image;
    private ImageView bestImage, envImage;
    private byte[] livaFacebytes;
    private boolean IsHaveImg;
    private String delta;
    private CustomDialog customloadingdialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result1);
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
        init();
        customloadingdialog = CustomDialogUtils.getInstance().LoadingDialog(this,"正在验证中。。。");
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.result_status);
        textView = (TextView) findViewById(R.id.result_text_result);
        ll_result_image = (LinearLayout) findViewById(R.id.ll_result_image);
        bestImage = (ImageView) findViewById(R.id.iv_best);
        envImage = (ImageView) findViewById(R.id.iv_env);
        findViewById(R.id.result_next).setOnClickListener(this);
        findViewById(R.id.result_sure).setOnClickListener(this);
        Bundle bundle = getIntent().getExtras();
        String resultOBJ = bundle.getString("result");

        try {
            JSONObject result = new JSONObject(resultOBJ);
            textView.setText(result.getString("result"));

            int resID = result.getInt("resultcode");
            if (resID == R.string.verify_success) {
                doPlay(R.raw.meglive_success);
            } else if (resID == R.string.liveness_detection_failed_not_video) {
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed_timeout) {
                doPlay(R.raw.meglive_failed);
            } else if (resID == R.string.liveness_detection_failed) {
                doPlay(R.raw.meglive_failed);
            } else {
                doPlay(R.raw.meglive_failed);
            }

            boolean isSuccess = result.getString("result").equals(
                    getResources().getString(R.string.verify_success));
            mImageView.setImageResource(isSuccess ? R.drawable.result_success
                    : R.drawable.result_failded);
            if (isSuccess) {
                delta = bundle.getString("delta");
                Map<String, byte[]> images = (Map<String, byte[]>) bundle.getSerializable("images");
                if (images.containsKey("image_best")) {
                    byte[] bestImg = images.get("image_best");
                    if (bestImg != null && bestImg.length > 0) {
                        Bitmap bestBitMap = BitmapFactory.decodeByteArray(bestImg, 0, bestImg.length);
                        bestImage.setImageBitmap(bestBitMap);
                        livaFacebytes = bestImg;
                        IsHaveImg=true;
                    }
                }
                if (images.containsKey("image_env")) {
                    byte[] envImg = images.get("image_env");
                    if (envImg != null && envImg.length > 0) {
                        Bitmap envBitMap = BitmapFactory.decodeByteArray(envImg, 0, envImg.length);
                        envImage.setImageBitmap(envBitMap);
                    }
                }
                ll_result_image.setVisibility(View.VISIBLE);
//                imageVerify(images,delta);
            } else {
                ll_result_image.setVisibility(View.GONE);
            }
            doRotate(isSuccess);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 如何调用Verify2.0方法
     * <p>
     */
//    public void imageVerify(Map<String, byte[]> images, String delta) {
    public void imageVerify(Map<String,Object> map, String delta) {
        RequestParams requestParams = new RequestParams();
//        requestParams.put("name", "身份证姓名");
//        requestParams.put("idcard", "身份证号码");
        requestParams.put("idcard_name", map.get("real_name"));
        requestParams.put("idcard_number", map.get("card_no"));


        try {
//            requestParams.put("image_ref1", new FileInputStream(new File("image_idcard")));// 传入身份证头像照片路径

            String ref1IamgePath = FaceUtils.getInstance().IDCardFrontImg;
            requestParams.put("image_ref1", new FileInputStream(new File(ref1IamgePath)));// 传入身份证头像照片路径
//            String bestIamgePath = FaceUtils.getInstance().getLivaCheck_bestImgPath(ResultActivity.this);
            String bestIamgePath = FaceUtils.getInstance().LivaCheckImg;
            requestParams.put("image_best", new FileInputStream(new File(bestIamgePath)));// 传入活体头像照片路径
        } catch (Exception e) {
        }
        requestParams.put("delta", delta);
//        requestParams.put("api_key", "API_KEY");
        requestParams.put("api_key", FaceUtils.getInstance().apikey);
//        requestParams.put("api_secret", "API_SECRET");
        requestParams.put("api_secret", FaceUtils.getInstance().apisecret);

        requestParams.put("comparison_type", 1 + "");
        requestParams.put("face_image_type", "meglive");

//        for (Map.Entry<String, byte[]> entry : images.entrySet()) {
//            requestParams.put(entry.getKey(),
//                    new ByteArrayInputStream(entry.getValue()));
//        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = "https://api.megvii.com/faceid/v2/verify";
        asyncHttpClient.post(url, requestParams,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int i, Header[] headers, byte[] bytes) {

                        String successStr = new String(bytes);
                        Log.i("result","verify成功："+successStr);
                        System.out.println("========="+successStr);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(successStr);
                            if (!jsonObject.has("error")) {
                                // 活体最好的一张照片和公安部系统上身份证上的照片比较
                                double confidence = jsonObject.getJSONObject(
                                        "result_faceid")
                                        .getDouble("confidence");
                                FaceUtils.getInstance().IDCardFront.put("compare_score",confidence);
                                JSONObject jsonObject2 = jsonObject
                                        .getJSONObject("result_faceid")
                                        .getJSONObject("thresholds");
                                double threshold = jsonObject2
                                        .getDouble("1e-3");
                                double tenThreshold = jsonObject2
                                        .getDouble("1e-4");
                                double hundredThreshold = jsonObject2
                                        .getDouble("1e-5");

                                try {
                                    // 活体最好的一张照片和拍摄身份证上的照片的比较
                                    JSONObject jObject = jsonObject
                                            .getJSONObject("result_ref1");
                                    double idcard_confidence = jObject
                                            .getDouble("confidence");
                                    double idcard_threshold = jObject
                                            .getJSONObject("thresholds")
                                            .getDouble("1e-3");
                                    double idcard_tenThreshold = jObject
                                            .getJSONObject("thresholds")
                                            .getDouble("1e-4");
                                    double idcard_hundredThreshold = jObject
                                            .getJSONObject("thresholds")
                                            .getDouble("1e-5");
                                } catch (Exception e) {

                                }
                                // 解析faceGen
                                JSONObject jObject = jsonObject
                                        .getJSONObject("face_genuineness");

                                float mask_confidence = (float) jObject
                                        .getDouble("mask_confidence");
                                float mask_threshold = (float) jObject
                                        .getDouble("mask_threshold");//此字段只有在调用时传入了image_env参数才返回
//                                float screen_replay_confidence = (float) jObject
//                                        .getDouble("screen_replay_confidence");//此字段只有在调用时传入了image_env参数才返回
//                                float screen_replay_threshold = (float) jObject
//                                        .getDouble("screen_replay_threshold");//此字段只有在调用时传入了image_env参数才返回
                                float synthetic_face_confidence = (float) jObject
                                        .getDouble("synthetic_face_confidence");
                                float synthetic_face_threshold = (float) jObject
                                        .getDouble("synthetic_face_threshold");
                                float synthetic_face_replace = (float) jObject
                                        .getDouble("face_replaced");

                                if((mask_confidence < mask_threshold) && (synthetic_face_confidence<synthetic_face_threshold) && (synthetic_face_replace==0)){

                                    Log.i("face_true","真脸+++++");
//                                    finish();
                                    postPersonalDataToService();//上传到后台服务器

                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int i, Header[] headers,
                                          byte[] bytes, Throwable throwable) {
                        String fail = new String(bytes);
                        // 请求失败
                        Log.i("result","verify失败"+fail);
                        ToatUtils.showShort1(ResultActivity.this,"verify失败");
                        customloadingdialog.dismiss();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, LoadingActivity.class);
        startActivity(intent);
    }

    public static void startActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, ResultActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View view) {
//        int id = view.getId();
//        if (id == R.id.result_next) {
//            finish();
//        }
        switch (view.getId()){
            case R.id.result_sure://网络请求进行对比
                customloadingdialog.show();
                doFacecheck();//保存活体照片
//                imageVerify(FaceUtils.getInstance().IDCardFront,delta);
                break;
            case R.id.result_next://返回键

                finish();
                break;
        }

    }

    public void doFacecheck(){//活体照片保存

        if (IsHaveImg) {
//            String    date    = FaceUtils.getInstance().getCurrentDataTime();
            String IdCardN0 = FaceUtils.getInstance().UserChainIDCardId+"";
            if (SDCardHelper.saveFileToSDCardCacheDir(livaFacebytes,IdCardN0+"_3.png", ResultActivity.this)) {
                String imagePath = ResultActivity.this.getExternalCacheDir().getPath()+"/"+IdCardN0+"_3.png";
                    FaceUtils.getInstance().LivaCheckImg = imagePath;//活体检测照
                imageVerify(FaceUtils.getInstance().IDCardFront,delta);
            }
        }
    }

    private void postPersonalDataToService(){

        Map<String,Object> map = FaceUtils.getInstance().IDCardFront;


            File fileIDCardFront = new File(FaceUtils.getInstance().IDCardFrontBM+"");
            File fileIDCardFrontImg = new File(FaceUtils.getInstance().IDCardFrontImg+"");
            File fileIDCardReverse = new File(FaceUtils.getInstance().IDCardReverseBM+"");
            String filePathFront = ResultActivity.this.getExternalCacheDir().getPath() + "/" + FaceUtils.getInstance().UserChainIDCardId + "_0.png";
            String filePathFrontImg = ResultActivity.this.getExternalCacheDir().getPath() + "/" + FaceUtils.getInstance().UserChainIDCardId + "_2.png";
            String filePathReverse = ResultActivity.this.getExternalCacheDir().getPath() + "/" + FaceUtils.getInstance().UserChainIDCardId + "_1.png";

            File file1 = new File(filePathFront);
            if (fileIDCardFront.renameTo(file1)) {
                FaceUtils.getInstance().IDCardFrontBM = filePathFront;
            }
            File file2 = new File(filePathFrontImg);
            if (fileIDCardFrontImg.renameTo(file2)){
                FaceUtils.getInstance().IDCardFrontImg = filePathFrontImg;
            }
              File file3 = new File(filePathReverse);
            if (fileIDCardReverse.renameTo(file3)){
                FaceUtils.getInstance().IDCardReverseBM = filePathReverse;
            }

         file1 = new File(FaceUtils.getInstance().IDCardFrontBM);
        System.out.println(file1.getTotalSpace()+"");
         file2 = new File(FaceUtils.getInstance().IDCardReverseBM);
        System.out.println(file2.getTotalSpace()+"");
         file3 = new File(FaceUtils.getInstance().IDCardFrontImg);
        System.out.println(file3.getTotalSpace()+"");
        File file4 = new File(FaceUtils.getInstance().LivaCheckImg);
        System.out.println(file4.getTotalSpace()+"");

        OkHttpManager.uploadAsyncFaceResult(HttpURL.getInstance().PERSONAL_UPLOAD_FACE_RESULT,
                "postPersonalDataToService", SharedPreferencesUtils.get(this,"uid","")+""
                ,map.get("real_name")+"",map.get("card_no")+"",map.get("ocr_nation")+""
                ,map.get("ocr_address")+"",map.get("compare_score")+"",
                map.get("ocr_gender")+"",(FaceUtils.getInstance().IsHaveCheckBox?1:0)+"",
                file1,file2,file3,file4,this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        switch (name){

            case "postPersonalDataToService":
            customloadingdialog.dismiss();
                    ToatUtils.showShort1(this,"验证失败！");
                break;
        }
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name){
            case "postPersonalDataToService":
                customloadingdialog.dismiss();
                JSONObject jsonObject = new JSONObject(result);
                String uploadresult = jsonObject.getString("msg");
                if (uploadresult.equals("成功")) {
                    FaceUtils.getInstance().IsHavaLiveUpSuccess = true;
                    ToatUtils.showShort1(this,"验证完成！");
                }
                finish();
                break;
        }
    }

    private void doRotate(boolean success) {
        RotaterView rotaterView = (RotaterView) findViewById(R.id.result_rotater);
        rotaterView.setColour(success ? 0xff4ae8ab : 0xfffe8c92);
        final ImageView statusView = (ImageView) findViewById(R.id.result_status);
        statusView.setVisibility(View.INVISIBLE);
        statusView.setImageResource(success ? R.drawable.result_success
                : R.drawable.result_failded);

        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(rotaterView,
                "progress", 0, 100);
        objectAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.setDuration(600);
        objectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Animation scaleanimation = AnimationUtils.loadAnimation(
                        ResultActivity.this, R.anim.scaleoutin);
                statusView.startAnimation(scaleanimation);
                statusView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        objectAnimator.start();
    }

    private MediaPlayer mMediaPlayer = null;

    private void doPlay(int rawId) {
        if (mMediaPlayer == null)
            mMediaPlayer = new MediaPlayer();

        mMediaPlayer.reset();
        try {
            AssetFileDescriptor localAssetFileDescriptor = getResources()
                    .openRawResourceFd(rawId);
            mMediaPlayer.setDataSource(
                    localAssetFileDescriptor.getFileDescriptor(),
                    localAssetFileDescriptor.getStartOffset(),
                    localAssetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception localIOException) {
            localIOException.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}