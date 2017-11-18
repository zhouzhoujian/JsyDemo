package cn.jishiyu11.xjsjd.activity.helpFeedbackFriendsMyPackage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CameraUtils.BitmapUtils;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.IOException;

import okhttp3.Request;

/**
 * Created by vvguoliang on 2017/6/28.
 * 反馈建议
 */
@SuppressLint("SetTextI18n")
public class FeedbackActivity extends BaseActivity implements View.OnClickListener, DataCallBack {

    private EditText information_corporate_editText;
    private ImageView feedback_image;
    private TextView feedback_path;
    private EditText feedback_path_editText;
    private UserCenterRealize userCenterRealize;

    private TextView feedback_path_text;

    private int num = 270;

    private File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_feedback );
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B( this );
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image:
                finish();
                break;
            case R.id.feedback_path_button:
                getHttp();
                break;
            case R.id.feedback_image:
                userCenterRealize = new UserCenterRealize();
                userCenterRealize.getFileByPhotoAlbum( this );
                break;
        }
    }

    @Override
    protected void findViewById() {
        findViewById( R.id.title_image ).setVisibility( View.VISIBLE );
        findViewById( R.id.title_image ).setOnClickListener( this );
        TextView title_view = findViewById( R.id.title_view );
        title_view.setText( this.getString( R.string.name_loan_personal_feedback ) );

        information_corporate_editText = findViewById( R.id.information_corporate_editText );
        feedback_image = findViewById( R.id.feedback_image );
        feedback_path = findViewById( R.id.feedback_path );
        feedback_path_editText = findViewById( R.id.feedback_path_editText );
        findViewById( R.id.feedback_path_button ).setOnClickListener( this );
        feedback_image.setOnClickListener( this );
        information_corporate_editText.setFilters( new InputFilter[]{new InputFilter.LengthFilter( num )} );
        information_corporate_editText.addTextChangedListener( textWatcher );

        feedback_path_text = findViewById( R.id.feedback_path_text );
        feedback_path_text.setText( "0/" + num );
    }

    @Override
    protected void initView() {

    }

    private void getHttp() {
        OkHttpManager.feedbackAsync( HttpURL.getInstance().FEEDBACK, "feedback",
                SharedPreferencesUtils.get( this, "uid", "" ).toString(),
                information_corporate_editText.getText().toString(), feedback_path_editText.getText().toString(),
                feedback_path_editText.getText().toString(), file, this );
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume( this );
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause( this );
    }

    @Override
    public void requestFailure(Request request, String name, IOException e) {
        ToatUtils.showShort1( this, this.getString( R.string.network_timed ) );
    }

    @Override
    public void requestSuccess(String result, String name) throws Exception {
        if (name.equals( "feedback" )) {
            finish();
        }
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > num - 80) {
                ToatUtils.showShort1( FeedbackActivity.this, "您输入的文字剩下不多了,请简介概括说明" );
            }
            feedback_path_text.setText( s.length() + "/" + num );
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_CAMERA) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph( this );
            } else {
                Toast.makeText( this, "请授予相机权限", Toast.LENGTH_SHORT ).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph( this );
            } else {
                Toast.makeText( this, "请授予读SD卡权限", Toast.LENGTH_SHORT ).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph( this );
            } else {
                Toast.makeText( this, "请授予写SD卡权限", Toast.LENGTH_SHORT ).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD_PHOTOALBUM) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.startPhotoAlbum( this );
            } else {
                Toast.makeText( this, "请授予读SD卡权限", Toast.LENGTH_SHORT ).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK_PHOTOALBUM) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.startPhotoAlbum( this );
            } else {
                Toast.makeText( this, "请授予写SD卡权限", Toast.LENGTH_SHORT ).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        // 拍照
        if (AppUtil.getInstance().CAPTURE_IMAGE_REQUEST == requestCode) {
            if (RESULT_OK == resultCode) {
                Log.d( "拍照得到图片", AppUtil.getInstance().mImageFile.toString() );
                int mDegree = BitmapUtils.getBitmapDegree( AppUtil.getInstance().mImageFile.getAbsolutePath() );
                Log.d( "拍照得到图片的角度：", String.valueOf( mDegree ) );
                if (mDegree == 90 || mDegree == 180 || mDegree == 270) {
                    try {
                        Bitmap mBitmap = BitmapUtils.getFileBitmap( AppUtil.getInstance().mImageFile );
                        Bitmap bitmap = BitmapUtils.rotateBitmapByDegree( mBitmap, mDegree );
                        if (BitmapUtils.saveBitmapFile( bitmap, AppUtil.getInstance().mImageFile )) {
                            userCenterRealize.startClip( this, AppUtil.getInstance().mImageFile );
                        } else {
                            Toast.makeText( this, "保存图片失败", Toast.LENGTH_SHORT ).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText( this, "读取图片失败", Toast.LENGTH_SHORT ).show();
                    }
                } else {
                    userCenterRealize.startClip( this, AppUtil.getInstance().mImageFile );
                }
            }
            //相册
        } else if (AppUtil.getInstance().LOAD_IMAGE_REQUEST == requestCode) {
            if (data != null) {
                Uri uri = data.getData();
                String filepath = BitmapUtils.FileUtils.getImageAbsolutePath( this, uri );
                Log.d( "相册获取到的文件路径", filepath );
                File file = new File( filepath );
                userCenterRealize.startClip( this, file );
            }
            //剪裁
        } else if (AppUtil.getInstance().CLIP_IMAGE_REQUEST == requestCode) {
            Bitmap bitmap = BitmapUtils.getFileBitmap( AppUtil.getInstance().mOutFile );
            file = AppUtil.getInstance().mOutFile;
            feedback_image.setImageBitmap( bitmap );
            feedback_path.setVisibility( View.GONE );
            BitmapUtils.deleteFile( AppUtil.getInstance().mImageFile );
        }
    }
}
