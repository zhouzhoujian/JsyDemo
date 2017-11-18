package cn.jishiyu11.xjsjd.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 判断ＳＤ卡是否挂载
 * 获取ＳＤ卡的根目录
 * 获取ＳＤ卡总容量
 * 获取ＳＤ卡剩余容量
 * 向ＳＤ卡９大共有目录保存数据
 * 向ＳＤ卡私有File目录写入数据
 * 向ＳＤ卡私有Cache目录写入数据
 * 从ＳＤ卡中读取数据
 * vvguoliang
 */
@SuppressWarnings({"ConstantConditions", "ResultOfMethodCallIgnored"})
public class SDCardUtils {

    /**
     * 判断ＳＤ卡是否挂载
     */
    public static boolean isSDCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 返回ＳＤ卡根目录
     *
     * @return
     */
    public static String getSDCardRootDir() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    /**
     * 获取ＳＤ卡总容量
     */
    public static long getSDCardTotalSize() {
        if (isSDCardMounted()) {
            //StatFs Statistic File System  簇
            StatFs sf = new StatFs(getSDCardRootDir());
            int blockSize = sf.getBlockSize();
            int blockCount = sf.getBlockCount();

            return blockSize * blockCount / 1024 / 1024;  //返回ＭＢ
        }
        return 0;
    }

    /**
     * 获取ＳＤ卡可用容量
     *
     * @return
     */
    public static long getSDCardAvailableSize() {
        if (isSDCardMounted()) {
            StatFs sf = new StatFs(getSDCardRootDir());
            int availableBlockCount = sf.getAvailableBlocks();
            int blockSize = sf.getBlockSize();

            return availableBlockCount * blockSize / 1024 / 1024;
        }
        return 0;
    }

    /**
     * 向ＳＤ卡９大共有目录保存数据
     *
     * @param data     需要保存的数据
     * @param type     　区分９大共有目录的type
     * @param fileName 另存为的文件名称
     * @return
     */
    public static boolean saveFileToPublicDirectory(byte[] data,
                                                    String type, String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            File fileDir =
                    Environment.getExternalStoragePublicDirectory(type);
            File file = new File(fileDir, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 向ＳＤ卡私有File目录写入数据
     *
     * @param context  　上下文，用来找到storage/sdcard0/Android/data/packageName/files
     * @param data     需要保存的数据
     * @param type     　文件加类型
     * @param fileName 　另存为的文件名名称
     * @return
     */
    public static boolean saveFileToExternalFileDir(Context context, byte[] data,
                                                    String type, String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            File fileDir = context.getExternalFilesDir(null);
            File file = new File(fileDir, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 向ＳＤ卡私有Cache目录写入数据
     *
     * @param context  　上下文，用来找到storage/sdcard0/Android/data/packageName/cache
     * @param data     需要保存的数据
     * @param fileName 　另存为的文件名名称
     * @return
     */
    public static boolean saveFileToExternalCacheDir(Context context, byte[] data,
                                                     String fileName) {
        if (isSDCardMounted()) {
            BufferedOutputStream bos = null;
            File fileDir = context.getExternalCacheDir();
            File file = new File(fileDir, fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                bos = new BufferedOutputStream(new FileOutputStream(file));
                bos.write(data);
                bos.flush();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bos != null) {
                    try {
                        bos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return false;
    }

    /**
     * 从ＳＤ卡中读取数据
     *
     * @param fileAbsolutePath 　　读取文件的绝对路径
     * @return
     */
    public static byte[] loadDataFromSDCard(String fileAbsolutePath) {
        if (isSDCardMounted()) {
            BufferedInputStream bis = null;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                bis = new BufferedInputStream(
                        new FileInputStream(fileAbsolutePath));
                byte[] buffer = new byte[1024 * 8];
                int len;
                while ((len = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                    baos.flush();
                }
                return baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (bis != null) {
                        bis.close();
                    }
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static String getSDCardCacheDir(Context context) {
        return context.getExternalCacheDir().getPath();
    }

    /**
     * 进行文件夹删除
     */
    private static String[] filename = new String[]{"sxs", "cache"};

    public static void setSDdelete(Context context) {
        if (isSDCardMounted()) {//存在执行
            deleteAllFiles(context, getSDCardRootDir(), filename);
        }
    }

    /**
     * 判断文件夹是否存在，进行删除操作
     *
     * @param filePath
     * @param fileName
     */
    private static void deleteAllFiles(Context context, String filePath, String[] fileName) {
        File f = new File(filePath);
        if (f.exists()) {
            for (String aFileName : fileName) {
                File file = new File(filePath + "/" + aFileName);
                clear(file);
            }
        } else {
            ToatUtils.showShort1(context, "文件不存在");
        }
    }

    /**
     * 若将整个sxs文件夹删除，则只需调用这个方法
     */
    private static void clear(File file) {
        if (file.exists()) { //指定文件是否存在
            if (file.isFile()) { //该路径名表示的文件是否是一个标准文件
                file.delete(); //删除该文件
            } else if (file.isDirectory()) { //该路径名表示的文件是否是一个目录（文件夹）
                File[] files = file.listFiles(); //列出当前文件夹下的所有文件
                for (File f : files) {
                    clear(f); //递归删除
                    //Log.d("fileName", f.getName()); //打印文件名
                }
            }
            file.delete(); //删除文件夹（song,art,lyric）
        }
    }

}

