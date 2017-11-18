package cn.jishiyu11.xjsjd.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jishiyu11.xjsjd.EntityClass.Fragment2LoanDetails;
import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.activity.SetUp.SetUPActivity;
import cn.jishiyu11.xjsjd.activity.fragment.FirstFragment;
import cn.jishiyu11.xjsjd.activity.fragment.PersonalCenterFragment;
import cn.jishiyu11.xjsjd.activity.fragment.TwoFragment;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.base.BaseActivityManager;
import cn.jishiyu11.xjsjd.http.http.i.DataCallBack;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.HttpURL;
import cn.jishiyu11.xjsjd.http.http.i.httpbase.OkHttpManager;
import cn.jishiyu11.xjsjd.utils.AppUtil;
import cn.jishiyu11.xjsjd.utils.CTelephoneInfo;
import cn.jishiyu11.xjsjd.utils.CameraUtils.BitmapUtils;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;
import cn.jishiyu11.xjsjd.utils.NetWorkUtils;
import cn.jishiyu11.xjsjd.utils.SharedPreferencesUtils;
import cn.jishiyu11.xjsjd.utils.StringUtil;
import cn.jishiyu11.xjsjd.utils.ToatUtils;
import cn.jishiyu11.xjsjd.view.MainActivityView;
import cn.jishiyu11.xjsjd.webview.LoanWebViewActivity;
import okhttp3.Request;

import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 2017-6-23
 * 基础主类
 */
public class MainActivity extends BaseActivity implements MainActivityView.OnItemClickListener {

    private Activity activity;

    public static Activity mainActivity;

    private MainActivityView mainActivityView;

    private int[] titles = {R.string.name_first_fm, R.string.name_loan, R.string.name_personal_center};
    private int[]  unSelectedImage= {R.mipmap.ic_loan_dark, R.mipmap.ic_loansupermarket_dark,
            R.mipmap.ic_personalcenter_dark};
    private int[] selectedImage = {R.mipmap.ic_loan_brightness, R.mipmap.ic_quickcard_brightness,
            R.mipmap.ic_personalcenter_brightness};

    private int mHeight;
    private boolean isGetHeight = true;

    private int mSelectPosition = 0;

    private List<Fragment> listfragment = new ArrayList<>();

    private List<Fragment> listnewftagment = new ArrayList<>();

    private UserCenterRealize userCenterRealize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        setImmerStateBar(false);
        this.activity = this;
        userCenterRealize = new UserCenterRealize();
        setContentView(R.layout.activity_main);
        mainActivity = this;
        findViewById();

        String url = getIntent().getStringExtra("url")+"";
        if (url!="" &&!url.isEmpty() && !url.equals("null")){
            Intent intent = new Intent( MainActivity.this, LoanWebViewActivity.class );
            intent.putExtra( "url", url );
            startActivity( intent );
        }

    }

    @Override
    protected void findViewById() {
        int position = getIntent().getIntExtra("selectPosition", -1);
        if (position == -1) {
            mSelectPosition = 0;
        } else {
            mSelectPosition = position;
        }
        initView();

        if (position == -1) {
        } else {
            showFragment(position);
        }
        SharedPreferencesUtils.put(activity, "STATUS_FONT_COLOR", "WHITE");
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B(this);
//            ImmersiveUtils.getInstance().getWhite(this);
        }

    }

    @Override
    protected void initView() {
        for (int i = 0; i <= 3; i++) {
            listfragment.add(null);
        }
        listnewftagment.add(new FirstFragment(MainActivity.this));
        listnewftagment.add(new TwoFragment(MainActivity.this));
        listnewftagment.add(new PersonalCenterFragment(MainActivity.this));

        // 获取屏幕宽度
        Display dm = getWindowManager().getDefaultDisplay();
        final int screenWith = dm.getWidth();
        mainActivityView = findViewById(R.id.act_main_tab);
        // 初始化获取底部导航自身高度
        final ViewTreeObserver vt = mainActivityView.getViewTreeObserver();
        vt.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                if (isGetHeight) {
                    mHeight = mainActivityView.getHeight();
                    mainActivityView.setLayout(titles, selectedImage, unSelectedImage, screenWith, mHeight, MainActivity.this);
                    mainActivityView.setColorLing(mSelectPosition);
                    mainActivityView.setOnItemClickListener(MainActivity.this);
                    isGetHeight = false;
                }
                return true;
            }
        });
        showFragment(mSelectPosition);
    }

    @Override
    public void onItemClick(int position) {
        showFragment(position);
    }

    /**
     * 动态添加和显示fragment
     * @param position
     */
    private void showFragment(int position) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
//        setImmerStateBar(false);
        for (int i = 0; i < listfragment.size(); i++) {
            if (position == i) {
                if (listfragment.get(i) == null) {
                    listfragment.remove(i);
                    listfragment.add(i, listnewftagment.get(i));
                    if (!listnewftagment.get(i).isAdded()) {
                        transaction.add(R.id.cat_main_fragment_content, listfragment.get(i));
                    } else {
                        transaction.show(listfragment.get(i));
                    }
                } else {
                    transaction.show(listfragment.get(i));
                }
                //fragment首页和我的页状态栏字体颜色为白色
                SharedPreferencesUtils.put(activity, "STATUS_FONT_COLOR", "WHITE");
                //沉浸式状态设置
                if (ImmersiveUtils.BuildVERSION()) {
                    ImmersiveUtils.getInstance().getW_add_B(this);
                }
            }
        }
        transaction.commitAllowingStateLoss();
    }

    public void showSwichFragment(int position){
        showFragment(position);
    }

    /**
     * 隐藏所有fragment
     * @param transaction
     */
    private void hideFragment(FragmentTransaction transaction) {
        for (int i = 0; i < listfragment.size(); i++) {
            if (listfragment.get(i) != null) {
                transaction.hide(listfragment.get(i));
            }
        }
    }

    /**
     * 连续按两次返回键就退出
     */
    private int keyBackClickCount = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (keyBackClickCount++) {

                case 0:

                    ToatUtils.showShort1(this, "再按一次退出");
                    Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            keyBackClickCount = 0;
                        }
                    }, 3000);
                    break;
                case 1:
                    BaseActivityManager.getActivityManager().finishAllActivity();
                    finish();
                    break;
                default:
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_CAMERA) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予相机权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予读SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.getFileByPhotograph(this);
            } else {
                Toast.makeText(this, "请授予写SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_READ_SD_PHOTOALBUM) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                userCenterRealize.startPhotoAlbum(this);
            } else {
                Toast.makeText(this, "请授予读SD卡权限", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == AppUtil.getInstance().MY_PERMISSIONS_REQUEST_WRITE_SK_PHOTOALBUM) {
            if (!TextUtils.isEmpty( grantResults[0] + "" ) && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                File file = new File(filepath);
                userCenterRealize.startClip(this, file);
            }
            //剪裁
        } else if (AppUtil.getInstance().CLIP_IMAGE_REQUEST == requestCode) {
            Log.d("剪裁得到图片", AppUtil.getInstance().mOutFile.toString());
            Bitmap bitmap = BitmapUtils.getFileBitmap(AppUtil.getInstance().mOutFile);
//            personal_camera.setImageBitmap(bitmap);
            BitmapUtils.deleteFile(AppUtil.getInstance().mImageFile);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getUserLogoutAuthorStatus();
        System.out.println("mainAcivity  ===========  destroy");

    }

    private void getUserLogoutAuthorStatus(){
        Map<String,Object> map = new HashMap<>();
        String phoneDeviceId =  CTelephoneInfo.getPhoneUDID(MainActivity.this);
        phoneDeviceId = StringUtil.getMD5String(phoneDeviceId);
        String locaIP = NetWorkUtils.getHostIP()+"";
        locaIP = StringUtil.getMD5String(locaIP);
        map.put("deviceNo",phoneDeviceId);
        map.put("ip",locaIP);
        map.put("mobile",SharedPreferencesUtils.get(this,"username","")+"");
        OkHttpManager.postAsync(HttpURL.getInstance().USER_LOGOUT_STATUS, "getUserLogoutAuthorStatus", map, new DataCallBack() {
            @Override
            public void requestFailure(Request request, String name, IOException e) {

            }

            @Override
            public void requestSuccess(String result, String name) throws Exception {
                JSONObject object = new JSONObject(result);
                if (name.equals("getUserLogoutAuthorStatus")){
                    if (object.optString("code").equals("0000")) {
                        ToatUtils.showShort1(MainActivity.this,"安全退出成功！");
                    }else {
                        ToatUtils.showShort1(MainActivity.this,object.getString("msg"));
                    }

                }

            }
        });

    }

}
