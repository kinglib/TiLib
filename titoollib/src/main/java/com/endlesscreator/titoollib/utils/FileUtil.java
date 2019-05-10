package com.endlesscreator.titoollib.utils;

import android.os.Environment;


import com.endlesscreator.tibaselib.frame.TApp;

import java.io.File;

public class FileUtil {
    private static final String TAG = FileUtil.class.getName();

    /**
     * 给本地路径添加标识符 file://
     */
    public static String formatFilePath(String aPath) {
        return (aPath != null && aPath.startsWith("/") ? "file://" : "") + aPath;
    }

    // 递归方式 计算文件的大小
    public static long getDirFilesSize(File aFile) {
        if (aFile == null || !aFile.exists()) return 0;
        if (aFile.isFile())
            return aFile.length();
        File[] lChildFiles = aFile.listFiles();
        long lTotalSize = 0;
        if (lChildFiles != null)
            for (File lChildFile : lChildFiles)
                lTotalSize += getDirFilesSize(lChildFile);
        return lTotalSize;
    }

    /**
     * 删除目录下的所有子文件及子文件夹
     */
    public static void deleteChildFiles(File aDir) {
        if (aDir != null && aDir.exists() && aDir.isDirectory()) {
            for (File lFile : aDir.listFiles()) {
                if (lFile.isDirectory()) {
                    deleteChildFiles(lFile);
                }
                lFile.delete();
            }
        }
    }

    public static File getDownloadFileDir() {
        try {
            //根据手机状态获取存储根路径
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
                return new File(TApp.getInstance().getApplicationContext().getExternalCacheDir(), "DownloadCache");
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return new File(TApp.getInstance().getApplicationContext().getCacheDir(), "DownloadCache");
    }

    public static String getFileNameFromUrl(String aUrl) {
        if (aUrl == null || aUrl.length() == 0)
            return null;
        int lStartIndex = aUrl.lastIndexOf("/") + 1;
        if (lStartIndex < 1)
            return null;
        int lEndIndex = aUrl.indexOf("?");
        if (lEndIndex <= lStartIndex)
            return aUrl.substring(lStartIndex);
        return aUrl.substring(lStartIndex, lEndIndex);
    }

}
