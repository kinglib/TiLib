package com.endlesscreator.tiviewlib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.endlesscreator.titoollib.utils.ScreenManager;

/**
 * 状态栏占位View
 */
public class TiStatusBarView extends View {

    public TiStatusBarView(Context context) {
        super(context);
    }

    public TiStatusBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TiStatusBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TiStatusBarView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置宽高
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                ScreenManager.getInstance().getStatusBarHeight(getResources()));
    }
}
