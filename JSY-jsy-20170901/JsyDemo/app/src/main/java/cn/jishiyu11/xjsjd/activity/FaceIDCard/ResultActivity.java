package cn.jishiyu11.xjsjd.activity.FaceIDCard;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.megvii.idcardquality.bean.IDCardAttr;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.SDCardHelper;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.CustomDialog;
import okhttp3.Request;

public class ResultActivity extends BaseActivity implements DataCallBack {
    private ImageView mIDCardImageView;
    private ImageView mPortraitImageView;
    private TextView mIDCardSize;
    private TextView mPortraitSize;
    IDCardAttr.IDCardSide mIDCardSide;
    private Bitmap idcardBmp;
    private Bitmap img;
    private boolean isIdCardBmp = true;
    private byte[] idcardBMImg;
    private byte[] idcardBM;
    private boolean isIma = true;

    private CustomDialog customDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resutl);
        setdialog();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B( this );
        }
        mIDCardSide = getIntent().getIntExtra("side", 0) == 0 ? IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT
                : IDCardAttr.IDCardSide.IDCARD_SIDE_BACK;
        init();
    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    void init() {
        mIDCardImageView = (ImageView) findViewById(R.id.result_idcard_image);
        mPortraitImageView = (ImageView) findViewById(R.id.result_portrait_image);

        mIDCardSize = (TextView) findViewById(R.id.result_idcard_size);
        mPortraitSize = (TextView) findViewById(R.id.result_portrait_size);
        {
            byte[] idcardImgData = getIntent().getByteArrayExtra("idcardImg");
            idcardBmp = BitmapFactory.decodeByteArray(idcardImgData, 0,
                    idcardImgData.length);
            if (idcardImgData.length < 100) {
                isIdCardBmp = false;//判断是否有照片
            }
            idcardBM = idcardImgData;
            mIDCardImageView.setImageBitmap(idcardBmp);
            mIDCardSize.setText(idcardBmp.getWidth() + "_"
                    + idcardBmp.getHeight());
        }
        if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
            byte[] portraitImgData = getIntent().getByteArrayExtra(
                    "portraitImg");
            idcardBMImg = portraitImgData;
            img = BitmapFactory.decodeByteArray(portraitImgData, 0,
                    portraitImgData.length);
            mPortraitImageView.setImageBitmap(img);
            mPortraitSize.setText(img.getWidth() + "_" + img.getHeight());
        }
    }

    public void isImageSure(View view) {
        switch (view.getId()) {
            case R.id.cancle:
                finish();
                break;
            case R.id.sure:
                postFaceIDCard();
                break;
        }

    }

    private void postFaceIDCard() {

        if (isIdCardBmp) {
            String date = FaceUtils.getInstance().getCurrentDataTime();
//            String IdCardNo = FaceUtils.getInstance().UserChainIDCardId;
            String date1 = FaceUtils.getInstance().getCurrentDataTime() + 1;
            if (SDCardHelper.saveFileToSDCardCacheDir(idcardBM, date + ".png", ResultActivity.this)) {
                String imagePath = ResultActivity.this.getExternalCacheDir().getPath() + "/" + date + ".png";
                File file = new File(imagePath);
                if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
                    FaceUtils.getInstance().IDCardFrontBM = imagePath;//正面照
                    if (SDCardHelper.saveFileToSDCardCacheDir(idcardBMImg,date1+".png",ResultActivity.this)) {
                        FaceUtils.getInstance().IDCardFrontImg = ResultActivity.this.getExternalCacheDir().getPath() + "/" + date1 + ".png";
                    } else {
                    }
                } else {
                    FaceUtils.getInstance().IDCardReverseBM = imagePath;//反面照
                }
                String legality = "0";  //返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。
                customDialog.show();
                if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT){
                    OkHttpManager.facepostimage1(HttpURL.getInstance().FACE_IDCARD_URL, "faceIDCard", FaceUtils.getInstance().apikey, FaceUtils.getInstance().apisecret, legality, file, this);
                }else {
                    OkHttpManager.facepostimage1(HttpURL.getInstance().FACE_IDCARD_URL, "backIDCard", FaceUtils.getInstance().apikey, FaceUtils.getInstance().apisecret, legality, file, this);
                }
            }
        }
    }

    //加载 progressdialog
    private void setdialog() {
        customDialog = new CustomDialog(ResultActivity.this, R.style.new_circle_progress);
        View contentView = LayoutInflater.from(ResultActivity.this).inflate(R.layout.customdialog_laoding, null);
        customDialog.setContentView(contentView);
        customDialog.setCanceledOnTouchOutside(false);
        customDialog.setCancelable(false);

        Window dialogWindow = customDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setAttributes(lp);
       TextView display =  contentView.findViewById(R.id.loading_text);
       display.setText("正在解析，请等待。。。");

    }


    @Override
    public void requestFailure(Request request, String name, IOException e) {
        System.out.println(name + "=====" + request);
        customDialog.dismiss();
        ToatUtils.showShort1(ResultActivity.this, "获取数据失败，请重新上传");
        finish();
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {

        if (name.equals("faceIDCard")) {
            System.out.println(name + "=====" + result);
            JSONObject jsonObject = new JSONObject(result);
//			IDCardFront idCardFront = new IDCardFront();
//			idCardFront.setName(jsonObject.getString("name"));
//			idCardFront.setId_card_number(jsonObject.getString("id_card_number"));
            Map<String, Object> map = new HashMap<>();
            map.put("real_name", jsonObject.getString("name"));
            map.put("card_no", jsonObject.getString("id_card_number"));
            map.put("ocr_gender", jsonObject.getString("gender"));
            map.put("ocr_nation",jsonObject.getString("race"));//民族
            map.put("ocr_address",jsonObject.getString("address"));//家庭住址

            FaceUtils.getInstance().UserChainName = jsonObject.getString("name")+"";
            FaceUtils.getInstance().UserChainIDCardId = jsonObject.getString("id_card_number")+"";
            FaceUtils.getInstance().setChinaNametoShare(ResultActivity.this,jsonObject.getString("name")+"");
            FaceUtils.getInstance().setChinaIDCardIdtoShare(ResultActivity.this,jsonObject.getString("id_card_number")+"");

            FaceUtils.getInstance().IDCardFront = map;
            System.out.println(name + "=====" + map.toString());

        }else {
        }
        if (mIDCardSide == IDCardAttr.IDCardSide.IDCARD_SIDE_FRONT) {
            FaceUtils.getInstance().IsHaveIDCardFrontBM = true;
        } else {
            FaceUtils.getInstance().IsHaveIDCardReverseBM = true;
        }
        customDialog.dismiss();
        finish();

    }
}