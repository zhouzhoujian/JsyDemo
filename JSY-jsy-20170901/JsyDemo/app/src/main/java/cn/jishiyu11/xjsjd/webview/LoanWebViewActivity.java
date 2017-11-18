package cn.jishiyu11.xjsjd.webview;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.jishiyu11.xjsjd.R;
import cn.jishiyu11.xjsjd.base.BaseActivity;
import cn.jishiyu11.xjsjd.utils.CameraUtils.UserCenterRealize;
import cn.jishiyu11.xjsjd.utils.ImmersiveUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * Created by vvguoliang on 2017/7/3.
 * <p>
 * 内部浏览器显示页面
 */

public class LoanWebViewActivity extends BaseActivity implements View.OnClickListener {

    private WebView webview;

    private String url = "";

    private ProgressBar banner_progressBar;

    private TextView title_view;

    private LinearLayout banner_linear;

    private RelativeLayout fail_linear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.act_webview );
        findViewById();
        //沉浸式状态设置
        if (ImmersiveUtils.BuildVERSION()) {
            ImmersiveUtils.getInstance().getW_add_B( this );
        }
        initView();
    }

    @Override
    protected void findViewById() {
        url = getIntent().getExtras().getString( "url" );
        findViewById( R.id.title_image ).setVisibility( View.VISIBLE );
        findViewById( R.id.title_image ).setOnClickListener( this );
        title_view = findViewById( R.id.title_view );
        webview = findViewById( R.id.banner_webview );

        banner_progressBar = findViewById( R.id.banner_progressBar );

        banner_linear = findViewById( R.id.banner_linear );
        fail_linear = findViewById( R.id.fail_linear );
        fail_linear.setVisibility(View.GONE);
        findViewById(R.id.fail_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webview.reload();
            }
        });

        getSettings();

    }

    @Override
    protected void initView() {
        webview.setWebViewClient( webViewClient );
        webview.setWebChromeClient( webChromeClient );
        if (!TextUtils.isEmpty( url ) && !"null".equals( url )) {
            banner_linear.setVisibility( View.VISIBLE );
            fail_linear.setVisibility( View.GONE );
            webview.loadUrl( url );
        } else {
            title_view.setText( "" );
            banner_linear.setVisibility( View.GONE );
            fail_linear.setVisibility( View.VISIBLE );
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_image:
                finish();
                break;
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    private void getSettings() {
//        webview.setLayerType( View.LAYER_TYPE_SOFTWARE, null ); //渲染加速器
//        webview.getSettings().setRenderPriority( WebSettings.RenderPriority.HIGH ); //提高渲染的优先级
//        webview.removeJavascriptInterface( "searchBoxJavaBridge_" ); //防止360
        WebSettings settings = webview.getSettings();

//        settings.setBlockNetworkImage( true );
        settings.setUseWideViewPort( true );
        settings.setLoadWithOverviewMode( true );
        settings.setJavaScriptEnabled( true );
        settings.setJavaScriptCanOpenWindowsAutomatically( true );
        settings.setSaveFormData( false );
        settings.setDomStorageEnabled( true );
        settings.setAllowContentAccess( true );
        settings.setAllowFileAccess( true );
        settings.setDefaultTextEncodingName( "utf-8" );
        settings.setRenderPriority( WebSettings.RenderPriority.HIGH );
        settings.setCacheMode( WebSettings.LOAD_NO_CACHE ); //LOAD_NO_CACHE设置,缓存模式LOAD_DEFAULT

        if (Build.VERSION.SDK_INT >= 19) {
            settings.setLoadsImagesAutomatically( true );
        } else {
            settings.setLoadsImagesAutomatically( false );
        }
        settings.setDatabaseEnabled( true );
        settings.setLayoutAlgorithm( WebSettings.LayoutAlgorithm.SINGLE_COLUMN );
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }
        webview.getSettings().setBlockNetworkImage( false );

        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                Uri uri = Uri.parse(url);

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                startActivity(intent);
            }
        });

    }

    private WebViewClient webViewClient = new WebViewClient() {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, final String url) {
//            view.setVisibility(View.GONE);
//            view.loadUrl( url );

            if (url.startsWith("http") || url.startsWith("https")) { //http和https协议开头的执行正常的流程
                return false;
            } else {  //其他的URL则会开启一个Acitity然后去调用原生APP
                Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (in.resolveActivity(getPackageManager()) == null) {
                    //说明系统中不存在这个activity
                } else {
                    in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(in);
                    //如果想要加载成功跳转可以 这样
                }
                return true;
            }
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished( view, url );
            String title = view.getTitle();
            if (TextUtils.isEmpty( title )
                    || title.contains( "404" )
                    || title.contains( "找不到" )
                    || title.contains( "无法打开" )
                    ) {
                title_view.setText( title );
                banner_linear.setVisibility( View.GONE );
                fail_linear.setVisibility( View.VISIBLE );
            } else {
                banner_linear.setVisibility( View.VISIBLE );
                fail_linear.setVisibility( View.GONE );
                title_view.setText( view.getTitle() );
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            fail_linear.setVisibility( View.VISIBLE );
        }
    };

    private WebChromeClient webChromeClient = new WebChromeClient() {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (TextUtils.isEmpty( title )
                    || title.contains( "404" )
                    || title.contains( "找不到" )
                    || title.contains( "无法打开" )
                    ) {
                title_view.setText( title );
                banner_linear.setVisibility( View.GONE );
                fail_linear.setVisibility( View.VISIBLE );

            } else {
//                view.setVisibility(View.VISIBLE);
                banner_linear.setVisibility( View.VISIBLE );
                fail_linear.setVisibility( View.GONE );
                title_view.setText( view.getTitle() );
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged( view, newProgress );
            banner_progressBar.setProgress( newProgress );
            if (newProgress == 100) {
                banner_progressBar.setVisibility( View.GONE );
            } else {
                banner_progressBar.setVisibility( View.VISIBLE );
                view.setVisibility(View.VISIBLE);
            }
        }
        //For Android 3.0+
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            selectImage();
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            LoanWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), FCR);
        }

        // For Android 3.0+, above method not supported in some android 3+ versions, in such case we use this
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            selectImage();
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            LoanWebViewActivity.this.startActivityForResult(
                    Intent.createChooser(i, "File Browser"),
                    FCR);
        }

        //For Android 4.1+
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            selectImage();
            mUM = uploadMsg;
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("*/*");
            LoanWebViewActivity.this.startActivityForResult(Intent.createChooser(i, "File Chooser"), LoanWebViewActivity.FCR);
        }

        //For Android 5.0+
        public boolean onShowFileChooser(
                WebView webView, ValueCallback<Uri[]> filePathCallback,
                WebChromeClient.FileChooserParams fileChooserParams) {
            selectImage();
            if (mUMA != null) {
                mUMA.onReceiveValue(null);
            }
            mUMA = filePathCallback;
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(LoanWebViewActivity.this.getPackageManager()) != null) {
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                    takePictureIntent.putExtra("PhotoPath", mCM);
                } catch (IOException ex) {
                    Log.e("TAG", "Image file creation failed", ex);
                }
                if (photoFile != null) {
                    mCM = "file:" + photoFile.getAbsolutePath();
                    filePath = photoFile.getAbsolutePath();
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                } else {
                    takePictureIntent = null;
                }
            }
            Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
            contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
            contentSelectionIntent.setType("*/*");
            Intent[] intentArray;
            if (takePictureIntent != null) {
                intentArray = new Intent[]{takePictureIntent};
            } else {
                intentArray = new Intent[0];
            }

            Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
            chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
            chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
            startActivityForResult(chooserIntent, FCR);
            return true;
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview.canGoBack()) {
            webview.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
    /**
     * 打开图库,同时处理图片
     */
    private void selectImage() {
        compressPath = Environment.getExternalStorageDirectory().getPath() + "/QWB/temp";
        File file = new File(compressPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        compressPath = compressPath + File.separator + "compress.png";
        File image = new File(compressPath);
        if (image.exists()) {
            image.delete();
        }
    }

    // Create an image file
    private File createImageFile() throws IOException {
        @SuppressLint("SimpleDateFormat") String timeStamp = DateUtils.formatElapsedTime(System.currentTimeMillis());
        String imageFileName = "img_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private String mCM;
    private String filePath = "";
    private ValueCallback<Uri> mUM;
    private ValueCallback<Uri[]> mUMA;
    private final static int FCR = 1;
    String compressPath = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= 21) {
            Uri[] results = null;
            //Check if response is positive
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == FCR) {
                    if (null == mUMA) {
                        return;
                    }
                    if (intent == null) {
                        //Capture Photo if no image available
                        if (mCM != null) {
                            // results = new Uri[]{Uri.parse(mCM)};
                            results = new Uri[]{afterChosePic(filePath, compressPath)};
                        }
                    } else {
                        String dataString = intent.getDataString();
                        if (dataString != null) {
                            results = new Uri[]{Uri.parse(dataString)};
                            Log.d("tag", intent.toString());
//                            String realFilePath = getRealFilePath(Uri.parse(dataString));
//                            results = new Uri[]{afterChosePic(realFilePath, compressPath)};
                        }
                    }
                }
            }
            mUMA.onReceiveValue(results);
            mUMA = null;
        } else {
            if (requestCode == FCR) {
                if (null == mUM) return;
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUM.onReceiveValue(result);
                mUM = null;
            }
        }
    }


    /**
     * 选择照片后结束
     */
    private Uri afterChosePic(String oldPath, String newPath) {
        File newFile;
        try {
            newFile = FileUtils.compressFile(oldPath, newPath);
        } catch (Exception e) {
            e.printStackTrace();
            newFile = null;
        }
        return Uri.fromFile(newFile);
    }


}
