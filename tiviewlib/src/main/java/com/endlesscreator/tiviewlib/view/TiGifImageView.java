package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;

import pl.droidsonroids.gif.GifImageView;

public class TiGifImageView extends GifImageView {

    public TiGifImageView(Context context) {
        super(context);
    }

    public TiGifImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TiGifImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TiGifImageView(Context context, AttributeSet attrs, int defStyle, int defStyleRes) {
        super(context, attrs, defStyle, defStyleRes);
    }
}
