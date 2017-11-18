package cn.jishiyu11.xjsjd.Share;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class BitmapUtils {

    /**
     * 文件保存的路径
     */
    public static final String FILE_PATH = Environment.getExternalStorageDirectory().getPath();

    /**
     * 从本地SD卡获取网络图片，key是url的MD5值
     *
     * @return
     */
    public static void getBitmap(Bitmap bitmap) {
        try {
            String fileName = "jsy_ic_launcher.png";
            File file = new File(FILE_PATH + "/" + fileName);
            if (!file.exists()) {
                file.mkdir();
            }
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
