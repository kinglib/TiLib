package com.endlesscreator.titoollib.utils;

import android.content.Context;
import android.widget.ImageView;

import com.endlesscreator.tibaselib.frame.TApp;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

public class ImageUtil {
    private static final String TAG = ImageUtil.class.getName();

    /**
     * picasso图片缓存文件名称
     * {@link com.squareup.picasso.Utils#PICASSO_CACHE}
     */
    private static final String PICASSO_CACHE = "picasso-cache";

    public static void load(ImageView aImageView, String aPathOrUrl) {
        load(null, aImageView, aPathOrUrl);
    }

    public static void load(Context aContext, ImageView aImageView, String aPathOrUrl) {
        loading(aContext, aImageView, aPathOrUrl, -1, -1);
    }

    public static void loading(ImageView aImageView, String aPathOrUrl, int aLoadingRes, int aLoadErrorRes) {
        loading(null, aImageView, aPathOrUrl, aLoadingRes, aLoadErrorRes);
    }

    public static void loading(Context aContext, ImageView aImageView, String aPathOrUrl, int aLoadingRes, int aLoadErrorRes) {
        if (checkLoadNull(aImageView, aPathOrUrl)) return;
        RequestCreator load = Picasso.with(aContext == null ? TApp.getInstance() : aContext).load(FileUtil.formatFilePath(aPathOrUrl));
        if (aLoadingRes > 0) load = load.placeholder(aLoadingRes);
        if (aLoadErrorRes > 0) load = load.error(aLoadErrorRes);
        load.noFade().into(aImageView);
    }

    public static void load(ImageView aImageView, String aPathOrUrl, int aTargetWidth, int aTargetHeight) {
        load(null, aImageView, aPathOrUrl, aTargetWidth, aTargetHeight);
    }

    public static void load(Context aContext, ImageView aImageView, String aPathOrUrl, int aTargetWidth, int aTargetHeight) {
        if (checkLoadNull(aImageView, aPathOrUrl)) return;
        Picasso.with(aContext == null ? TApp.getInstance() : aContext).load(FileUtil.formatFilePath(aPathOrUrl)).resize(aTargetWidth, aTargetHeight).noFade().into(aImageView);
    }

    public static void loadFit(ImageView aImageView, String aPathOrUrl) {
        loadFit(null, aImageView, aPathOrUrl);
    }

    public static void loadFit(Context aContext, ImageView aImageView, String aPathOrUrl) {
        if (checkLoadNull(aImageView, aPathOrUrl)) return;
        Picasso.with(aContext).load(FileUtil.formatFilePath(aPathOrUrl)).fit().into(aImageView);
    }

    private static boolean checkLoadNull(ImageView aImageView, String aPathOrUrl) {
        if (aImageView == null) return true;
        if (aPathOrUrl == null) {
            aImageView.setImageBitmap(null);
            return true;
        }
        return "".equals(aPathOrUrl.trim());
    }

    /**
     * 获取picasso图片缓存路径
     * {@link com.squareup.picasso.Utils#createDefaultCacheDir(Context)}
     */
    private static File getCacheFile() {
        return new File(TApp.getInstance().getApplicationContext().getCacheDir(), PICASSO_CACHE);
    }

    /**
     * 获取图片缓存大小
     * 包括 网络缓存图片 和 设置头像生成的临时文件
     */
    public static long getCacheSize() {
        return FileUtil.getDirFilesSize(getCacheFile());
    }

    /**
     * 清空图片缓存
     * 包括 网络缓存图片 和 设置头像生成的临时文件
     */
    public static void clearCache() {
        FileUtil.deleteChildFiles(getCacheFile());
    }
}
