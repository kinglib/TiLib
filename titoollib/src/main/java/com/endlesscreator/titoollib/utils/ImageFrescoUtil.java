package com.endlesscreator.titoollib.utils;

import android.net.Uri;
import android.text.TextUtils;

import com.endlesscreator.tibaselib.frame.TApp;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

public class ImageFrescoUtil {

    private static final String TAG = ImageFrescoUtil.class.getName();

    public static void init() {
        Fresco.initialize(TApp.getInstance());
    }

    public static void load(SimpleDraweeView aSimpleDraweeView, String aPathOrUrl) {
        Uri aUri = TextUtils.isEmpty(aPathOrUrl) ? null : Uri.parse(aPathOrUrl);
        load(aSimpleDraweeView, aUri);
    }

    /**
     * 普通加载图片，静态图
     */
    public static void load(SimpleDraweeView aSimpleDraweeView, Uri aResUri) {
        if (aSimpleDraweeView == null) return;
        aSimpleDraweeView.setImageURI(aResUri);
    }

    public static void loadAnim(SimpleDraweeView aSimpleDraweeView, String aHighPathOrUrl, String aLowPathOrUrl) {
        Uri aHighUri = TextUtils.isEmpty(aHighPathOrUrl) ? null : Uri.parse(aHighPathOrUrl);
        Uri aLowUri = TextUtils.isEmpty(aLowPathOrUrl) ? null : Uri.parse(aLowPathOrUrl);
        loadAnim(aSimpleDraweeView, aHighUri, aLowUri);
    }

    /**
     * 加载动态图
     *
     * @param aHighResUri 最终要加载的图，支持webp动图，gif动图
     * @param aLowResUri  预加载图，一般传小图，静态图
     */
    public static void loadAnim(SimpleDraweeView aSimpleDraweeView, Uri aHighResUri, Uri aLowResUri) {
        if (aSimpleDraweeView == null) return;
        PipelineDraweeControllerBuilder lBuilder = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setOldController(aSimpleDraweeView.getController())
                .setLowResImageRequest(ImageRequest.fromUri(aLowResUri))
                .setImageRequest(ImageRequest.fromUri(aHighResUri));
        DraweeController controller = lBuilder.build();
        aSimpleDraweeView.setController(controller);
    }

}
