package cn.jishiyu11.xjsjd.activity.IdentityCheck;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.FaceIDCard.LoadingActivity;
import cn.jishiyu11.xjsjd.activity.FaceIDCard.ResultActivity;
import cn.jishiyu11.xjsjd.activity.Insurice_tripActivity;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CameraUtils.BitmapUtils;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.CustomDialogUtils;
import cn.jishiyu11.xjsjd.utils.FaceUtils;
import cn.jishiyu11.xjsjd.utils.IdcardValidator;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.BottomDialog;

import com.megvii.idcardlib.IDCardScanActivity;
import com.megvii.idcardquality.IDCardQualityLicenseManager;
import com.megvii.idcardquality.bean.IDCardAttr;
import com.megvii.licensemanager.Manager;
import com.megvii.livenesslib.LivenessActivity;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.jishiyu11.xjsjd.view.CustomDialog;
import okhttp3.Request;

import static android.os.Build.VERSION_CODES.M;
import static cn.jishiyu11.xjsjd.activity.FaceIDCard.LoadingActivity.EXTERNAL_STORAGE_REQ_CAMERA_CODE;

/**
 * Created by vvguoliang on 2017/6/27.
 * <p>
 * 小额钱袋  身份验证第一步
 * 证件上传
 */

@SuppressWarnings("ConstantConditions")
public class PersonalDataCertificatesActivity extends BaseActivity implements View.OnClickListener, DataCallBack {

    private TextView title_complete,checkBox;

    private RelativeLayout personal_data_certificates_positive;

    private RelativeLayout personal_data_certificates_other_sid;

    private RelativeLayout personal_data_certificates_face_recognition;

    private TextView face_recognition_correct_text;

    private ImageView face_recognition_camera;

    private ImageView positive;

    private ImageView other_sid;

    private ImageView face_recognition;
    private int PAGE_INTO_LIVENESS = 101;//活体识别

    public static Activity PERSONALDATAS1 = null;

    private String getpath = "";

    private File file1 = null;

    private File file2 = null;

    private byte[] bitmap3 = null;

    private int bitmapint = 0;

    private Bitmap bitmap4 = null;

    private UserCenterRealize userCenterRealize = new UserCenterRealize();

    private Intent intent = null;

    private EditText person_name, person_idcard;
    private TextView person_phonenum;
    private Button person_next;

    private String userUID = "";
    private String userPhoneNum="";
    private CustomDialog customloadingDialog;

    private  int[] checkBoxImg = {R.mipmap.check_false,R.mipmap.check_true};
    private boolean isCheckChecked = true;

    private int mSide = 0;//0 正面  1反面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_personal_data_certifcates);
        findViewById();
        PERSONALDATAS1 = this;
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
        }
        userUID = SharedPreferencesUtils.get(this, "uid", "").toString();
        userPhoneNum = SharedPreferencesUtils.get(this, "username", "").toString();
        network();
    }

    @Override
    protected void findViewById() {
        findViewById(R.id.title_image).setVisibility(View.VISIBLE);
        findViewById(R.id.title_image).setOnClickListener(this);
        title_complete = findViewById(R.id.title_complete);
//        title_complete.setVisibility( View.VISIBLE );
//        title_complete.setOnClickListener( this );
//        title_complete.setText( this.getString( R.string.name_loan_personal_data_preservation ) );
        TextView title_view = findViewById(R.id.title_view);
        title_view.setText("基本信息");

        face_recognition_correct_text = findViewById(R.id.face_recognition_correct_text);

        face_recognition_camera = findViewById(R.id.face_recognition_camera);

        positive = findViewById(R.id.positive);
        other_sid = findViewById(R.id.other_sid);
        face_recognition = findViewById(R.id.face_recognition);

        findViewById(R.id.personal_data_certificates_positive).setOnClickListener(this);
        findViewById(R.id.personal_data_certificates_other_sid).setOnClickListener(this);
        findViewById(R.id.personal_data_certificates_face_recognition).setOnClickListener(this);
//        getIDcard();

        checkBox = findViewById(R.id.personal2_checkbox);
        checkBox.setOnClickListener(this);
        findViewById(R.id.personal2_trip_insurice   ).setOnClickListener(this);//

        person_name = findViewById(R.id.personal_data_name);
        person_idcard = findViewById(R.id.personal_data_idcard);
        person_phonenum = findViewById(R.id.personal_data_phonenum);
        person_next = findViewById(R.id.personal_data_next);


        person_next.setOnClickListener(this);//下一步
        person_phonenum.setText(userUID + "");
        customloadingDialog = CustomDialogUtils.getInstance().LoadingDialog(PersonalDataCertificatesActivity.this,"正在解析，请等待。。。");
    }

    @Override
    protected void initView() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image:
                intent = new Intent();
                intent.putExtra("operator", "2");
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case R.id.title_complete:
                getHttp();
                break;
            case R.id.personal_data_certificates_positive:
                getpath = "1";
                mSide = 0;
                showDialog(PersonalDataCertificatesActivity.this.getString(R.string.name_loan_personal_camera),
                        PersonalDataCertificatesActivity.this.getString(R.string.name_loan_personal_album));
                break;
            case R.id.personal_data_certificates_other_sid:
                mSide = 1;
                getpath = "2";
                showDialog(PersonalDataCertificatesActivity.this.getString(R.string.name_loan_personal_camera),
                        PersonalDataCertificatesActivity.this.getString(R.string.name_loan_personal_album));
                break;
            case R.id.personal_data_certificates_face_recognition:
                getpath = "3";
                userCenterRealize.getFileByPhotograph(this);
                break;

            case R.id.personal_data_next://下一步
                if (FaceUtils.getInstance().IsHavaLiveUpSuccess){
                    Intent intent = new Intent(PersonalDataCertificatesActivity.this, PersonalData2.class);
                    startActivity(intent);
//            finish();
                }else {
                    goNextpager();
                };

                break;

            case R.id.personal2_checkbox://默认选中
                if (isCheckChecked) {
                    isCheckChecked = false;
                 checkBox.setBackgroundResource(checkBoxImg[0]);
                }else {
                    isCheckChecked = true;
                    checkBox.setBackgroundResource(checkBoxImg[1]);
                }
                FaceUtils.getInstance().IsHaveCheckBox = isCheckChecked;
                break;
            case R.id.personal2_trip_insurice://点击查看
                    Intent intent = new Intent(this, Insurice_tripActivity.class);
            startActivity(intent);
                break;

        }
    }

    private void goNextpager() {//下一步
        if (!TextUtils.isEmpty(person_name.getText())
                && !TextUtils.isEmpty(person_idcard.getText())
                && !TextUtils.isEmpty(person_phonenum.getText())) {
            ToatUtils.showShort1(this, "可以哦");

            if (person_name.getText().length() > 1) {
                if (IdcardValidator.getInstance().isValidatedAllIdcard(person_idcard.getText() + "")) {
                    if (StringUtil.isMobileNO(person_phonenum.getText() + "")) {
                        //姓名 身份证号 电话号码 填写完整
                        startActivityForResult(new Intent(this, LivenessActivity.class), PAGE_INTO_LIVENESS);
//                        finish();
                    } else {
                        ToatUtils.showShort1(this, "手机号填写错误");
                    }
                } else {
                    ToatUtils.showShort1(this, "身份证填写错误");
                }

            } else {
                ToatUtils.showShort1(this, "请填写完整姓名");
            }

        } else {
            ToatUtils.showShort1(this, "要写完整哦亲~");
        }
    }

    private void getHttp() {
        if (file1 != null && file2 != null
//                && TextUtils.isEmpty( Arrays.toString( bitmap3 ))
                ) {
            bitmapint = 0;
            ToatUtils.showShort1(this, "您还没有上传图片，不能点击完成");
            return;
        } else {
            bitmapint = 1;
        }
//        OkHttpManager.uploadAsync( HttpURL.getInstance().IDCARDADD, "username_add",
//                SharedPreferencesUtils.get( this, "uid", "" ).toString(), bitmapint + "", file1, file2, this );


    }

    private void getIDcard() {
        Map<String, Object> map = new HashMap<>();
        map.put("uid", Long.parseLong(SharedPreferencesUtils.get(this, "uid", "").toString()));
        OkHttpManager.postAsync(HttpURL.getInstance().USERDETAILIDCARD, "username_idcatd", map, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        setIDCardDisplay();
    }

    private void setIDCardDisplay() {
//        if (FaceUtils.getInstance().IsHavaLiveUpSuccess){
//            Intent intent = new Intent(PersonalDataCertificatesActivity.this, PersonalData2.class);
//            startActivity(intent);
////            finish();
//        }

        if (FaceUtils.getInstance().IsHaveIDCardFrontBM) {
             file1 = new File(FaceUtils.getInstance().IDCardFrontBM);
            positive.setImageBitmap(BitmapUtils.getFileBitmap(file1));
            findViewById(R.id.positive_camera).setVisibility(View.GONE);
            person_name.setText(FaceUtils.getInstance().UserChainName+"");
            person_idcard.setText(FaceUtils.getInstance().UserChainIDCardId+"");
        }
        if (FaceUtils.getInstance().IsHaveIDCardReverseBM) {
            file2 = new File(FaceUtils.getInstance().IDCardReverseBM);
            other_sid.setImageBitmap(BitmapUtils.getFileBitmap(file2));
            findViewById(R.id.other_sid_camera).setVisibility(View.GONE);

        }
        person_phonenum.setText(userPhoneNum+"");
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        ToatUtils.showShort1(this, this.getString(R.string.network_timed));
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        switch (name) {
            case "username_add":
                JSONObject object = new JSONObject(result);
                intent = new Intent();
                if (object.optString("code").equals("0000")) {
                    intent.putExtra("operator", "1");
                } else {
                    intent.putExtra("operator", "2");
                }
                setResult(RESULT_CANCELED, intent);
                finish();
                break;
            case "username_idcatd":
                object = new JSONObject(result);
                object = new JSONObject(object.optString("data"));
                JSONArray array = new JSONArray(object.optString("data"));
                String front_img;
                if (array.length() > 0) {
                    JSONObject jsonObject = array.optJSONObject(0);
                    if (!TextUtils.isEmpty(jsonObject.optString("front_img")) && !"null".equals(jsonObject.optString("front_img")))
                        if (jsonObject.optString("front_img").contains("data/upload")) {
                            front_img = jsonObject.optString("front_img").replace("data/upload", "");
                            getPath(positive, front_img.trim());
                            findViewById(R.id.positive_camera).setVisibility(View.GONE);
                        } else {
                            getPath(positive, jsonObject.optString("front_img").trim());
                            findViewById(R.id.positive_camera).setVisibility(View.GONE);
                        }
                    if (!TextUtils.isEmpty(jsonObject.optString("back_img")) && !"null".equals(jsonObject.optString("back_img")))
                        if (jsonObject.optString("back_img").contains("data/upload")) {
                            front_img = jsonObject.optString("back_img").replace("data/upload", "");
                            getPath(other_sid, front_img.trim());
                            findViewById(R.id.other_sid_camera).setVisibility(View.GONE);
                        } else {
                            getPath(other_sid, jsonObject.optString("back_img").trim());
                            findViewById(R.id.other_sid_camera).setVisibility(View.GONE);
                        }
                }
                break;
            case "faceIDCard":
                System.out.println(name + "=====" + result);
                JSONObject jsonObject = new JSONObject(result);
//			IDCardFront idCardFront = new IDCardFront();
//			idCardFront.setName(jsonObject.getString("name"));
//			idCardFront.setId_card_number(jsonObject.getString("id_card_number"));
                Map<String, Object> map = new HashMap<>();
                map.put("name", jsonObject.getString("name"));
                FaceUtils.getInstance().UserChainName = jsonObject.getString("name")+"";
                map.put("id_card_number", jsonObject.getString("id_card_number"));
                FaceUtils.getInstance().UserChainIDCardId = jsonObject.getString("id_card_number")+"";
                FaceUtils.getInstance().IDCardFront = map;
                System.out.println(name + "=====" + map.toString());
                FaceUtils.getInstance().IsHaveIDCardFrontBM = true;
                person_name.setText(FaceUtils.getInstance().UserChainName+"");
                person_idcard.setText(FaceUtils.getInstance().UserChainIDCardId+"");
                customloadingDialog.dismiss();


                break;
            case "backIDCard":
                FaceUtils.getInstance().IsHaveIDCardReverseBM = true;
                customloadingDialog.dismiss();
                break;

        }
    }

    private void getPath(final ImageView imageView, String url) {
        Glide.with(this)
                .load(HttpURL.getInstance().HTTP_URL_PATH + url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        imageView.setImageResource(R.mipmap.ic_path_in_load);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        return false;
                    }
                })
                .into(imageView);

    }

    // 提示对话框方法
    public void showDialog(String btn_take, String btn_pick) {
        final BottomDialog sxsDialog = new BottomDialog(this, R.layout.buttom_dialog);
        sxsDialog.getWindow().setWindowAnimations(R.style.AnimBottom);
        sxsDialog.setWidthHeight(AppUtil.getInstance().Dispay(this)[0], 0);
        sxsDialog.getWindow().setGravity(Gravity.BOTTOM);
        Button button1 = (Button) sxsDialog.findViewById(R.id.btn_pick_photo1);
        button1.setText(btn_take);
        Button button = (Button) sxsDialog.findViewById(R.id.btn_pick_photo2);
        button.setVisibility(View.GONE);
        button.setText(btn_pick);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                userCenterRealize.getFileByPhotograph( PersonalDataCertificatesActivity.this );//拍照外部调用
                requestCameraPerm(mSide);
                sxsDialog.dismiss();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {//有
            @Override
            public void onClick(View v) {
                userCenterRealize.getFileByPhotoAlbum(PersonalDataCertificatesActivity.this);//相册外部调用
                sxsDialog.dismiss();
            }
        });
        sxsDialog.setOnClick(R.id.btn_cancel, new View.OnClickListener() {//取消
            @Override
            public void onClick(View v) {
                sxsDialog.dismiss();
            }
        });
        if (!isFinishing()) {
            sxsDialog.show();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_CAMERA) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予相机权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予读SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予写SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD_PHOTOALBUM) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.startPhotoAlbum(this);
            } else {
                Toast.makeText(this, "请授予读SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK_PHOTOALBUM) {
            if (!TextUtils.isEmpty(grantResults[0] + "") && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.startPhotoAlbum(this);
            } else {
                Toast.makeText(this, "请授予写SD卡权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照
        if (AppUtil.getInstance().CAPTURE_IMAGE_REQUEST == requestCode) {
            if (RESULT_OK == resultCode) {
                Log.d("拍照得到图片", AppUtil.getInstance().mImageFile.toString());
                int mDegree = BitmapUtils.getBitmapDegree(AppUtil.getInstance().mImageFile.getAbsolutePath());
                Log.d("拍照得到图片的角度：", String.valueOf(mDegree));
                if (mDegree == 90 || mDegree == 180 || mDegree == 270) {
                    try {
                        Bitmap mBitmap = BitmapUtils.getFileBitmap(AppUtil.getInstance().mImageFile);
                        Bitmap bitmap = BitmapUtils.rotateBitmapByDegree(mBitmap, mDegree);
                        if (BitmapUtils.saveBitmapFile(bitmap, AppUtil.getInstance().mImageFile)) {
                            userCenterRealize.startClip(this, AppUtil.getInstance().mImageFile);
                        } else {
                            Toast.makeText(this, "保存图片失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(this, "读取图片失败", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    userCenterRealize.startClip(this, AppUtil.getInstance().mImageFile);
                }
            }
            //相册
        } else if (AppUtil.getInstance().LOAD_IMAGE_REQUEST == requestCode) {
            if (data != null) {
                Uri uri = data.getData();
                String filepath = BitmapUtils.FileUtils.getImageAbsolutePath(this, uri);
                Log.d("相册获取到的文件路径", filepath);

                customloadingDialog.show();
                String legality = "0";  //返回身份证照片合法性检查结果，值只取“0”或“1”。“1”：返回； “0”：不返回。默认“0”。

                File file;
                file = new File(filepath);
                if (mSide==0) {
                    FaceUtils.getInstance().IDCardFrontBM  = filepath;
                    FaceUtils.getInstance().IsHaveIDCardFrontBM = true;
                    OkHttpManager.facepostimage1(HttpURL.getInstance().FACE_IDCARD_URL, "faceIDCard", FaceUtils.getInstance().apikey, FaceUtils.getInstance().apisecret, legality, file, this);
                }else {
                    FaceUtils.getInstance().IDCardReverseBM  = filepath;
                    FaceUtils.getInstance().IsHaveIDCardReverseBM = true;
                    OkHttpManager.facepostimage1(HttpURL.getInstance().FACE_IDCARD_URL, "backIDCard", FaceUtils.getInstance().apikey, FaceUtils.getInstance().apisecret, legality, file, this);
                }


//                File file = new File(filepath);
//                userCenterRealize.startClip(this, file);
            }
            //剪裁
        } else if (AppUtil.getInstance().CLIP_IMAGE_REQUEST == requestCode) {
            bitmap4 = BitmapUtils.getFileBitmap(AppUtil.getInstance().mOutFile);
            switch (getpath) {
                case "1":
                    file1 = AppUtil.getInstance().mOutFile;
                    positive.setImageBitmap(bitmap4);
                    findViewById(R.id.positive_camera).setVisibility(View.GONE);
                    break;
                case "2":
                    file2 = AppUtil.getInstance().mOutFile;
                    other_sid.setImageBitmap(bitmap4);
                    findViewById(R.id.other_sid_camera).setVisibility(View.GONE);
                    break;
                case "3":
                    bitmap3 = AppUtil.getInstance().bitmap2Bytes(bitmap4);
                    face_recognition.setImageBitmap(bitmap4);
                    face_recognition_correct_text.setVisibility(View.VISIBLE);
                    face_recognition_camera.setVisibility(View.VISIBLE);
                    break;
            }
            BitmapUtils.deleteFile(AppUtil.getInstance().mImageFile);
        }

        if (requestCode == INTO_IDCARDSCAN_PAGE && resultCode == RESULT_OK) {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("side", data.getIntExtra("side", 0));
            intent.putExtra("idcardImg", data.getByteArrayExtra("idcardImg"));
            if (data.getIntExtra("side", 0) == 0) {
                intent.putExtra("portraitImg", data.getByteArrayExtra("portraitImg"));
            }
            startActivity(intent);
        }

        if (requestCode == PAGE_INTO_LIVENESS && resultCode == RESULT_OK) {
//            String result = data.getStringExtra("result");
//            String delta = data.getStringExtra("delta");
//            Serializable images=data.getSerializableExtra("images");
            Bundle bundle=data.getExtras();
            cn.jishiyu11.xjsjd.activity.FaceLive.ResultActivity.startActivity(this, bundle);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            intent = new Intent();
            intent.putExtra("operator", "2");
            setResult(RESULT_CANCELED, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 获取联网授权
     */
    private void network() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Manager manager = new Manager(PersonalDataCertificatesActivity.this);
                IDCardQualityLicenseManager idCardLicenseManager = new IDCardQualityLicenseManager(
                        PersonalDataCertificatesActivity.this);
                manager.registerLicenseManager(idCardLicenseManager);
                UUID uuid1 = UUID.randomUUID();
                FaceUtils.getInstance().UUIDString = uuid1+"";
                String uuid = uuid1+"";
                manager.takeLicenseFromNetwork(uuid);
                String contextStr = manager.getContext(uuid);
                Log.w("ceshi", "contextStr====" + contextStr);

                Log.w("ceshi",
                        "idCardLicenseManager.checkCachedLicense()===" + idCardLicenseManager.checkCachedLicense());
                if (idCardLicenseManager.checkCachedLicense() > 0) {
                    FaceUtils.getInstance().IsNetAuthor = true;
                    ToatUtils.showShort1(PersonalDataCertificatesActivity.this, "chenggong");
                } else {
                    FaceUtils.getInstance().IsNetAuthor = false;
                    ToatUtils.showShort1(PersonalDataCertificatesActivity.this, "shibai");
                }
            }
        }).start();
    }

    private void requestCameraPerm(int side) {
        mSide = side;
        if (android.os.Build.VERSION.SDK_INT >= M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                //进行权限请求
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        EXTERNAL_STORAGE_REQ_CAMERA_CODE);
            } else {
                enterNextPage(side);
            }
        } else {
            enterNextPage(side);
        }
    }

    private static final int INTO_IDCARDSCAN_PAGE = 100;

    private void enterNextPage(int side) {
        FaceUtils.getInstance().IsHavaLiveUpSuccess = false;
        Intent intent = new Intent(this, IDCardScanActivity.class);
        intent.putExtra("side", side);
        intent.putExtra("isvertical", false);
        System.out.println("==========================华丽的分割线====================");
        startActivityForResult(intent, INTO_IDCARDSCAN_PAGE);
    }

}
