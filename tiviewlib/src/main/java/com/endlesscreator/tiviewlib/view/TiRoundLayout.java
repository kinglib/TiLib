package com.endlesscreator.tiviewlib.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.endlesscreator.tiviewlib.R;
import com.endlesscreator.tiviewlib.view.model.tiroundlayout.ITiRoundView;
import com.endlesscreator.tiviewlib.view.model.tiroundlayout.TiRoundTool;


/**
 * 圆角布局
 *
 * 原理为切割布局
 *
 */
public class TiRoundLayout extends RelativeLayout implements ITiRoundView {

    private TiRoundTool mTiRoundTool;

    public TiRoundLayout(Context context) {
        this(context, null);
    }

    public TiRoundLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public TiRoundLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TiRoundLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }


    public void initAttrs(Context context, AttributeSet attrs) {
        mTiRoundTool = new TiRoundTool(this);

        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiRoundLayout);
        int lStrokeColor = lTypedArray.getColor(R.styleable.TiRoundLayout_stroke_color, Color.TRANSPARENT);
        int lStrokeWidth = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_stroke_width, 0);
        boolean lClipBackground = lTypedArray.getBoolean(R.styleable.TiRoundLayout_clip_bg, false);
        int lRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_top_radius, lRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_top_radius, lRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_bottom_radius, lRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_bottom_radius, lRadius);
        lTypedArray.recycle();

        mTiRoundTool.initAttrs(this, lStrokeColor, lStrokeWidth, lClipBackground, lLeftTopRadius, lRightTopRadius, lRightBottomRadius, lLeftBottomRadius);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mTiRoundTool.onSizeChanged(this, w, h, oldw, oldh);
    }

    @Override
    public void onSizeChangedSuper(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        mTiRoundTool.dispatchDraw(canvas);
    }

    @Override
    public void dispatchDrawSuper(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void draw(Canvas canvas) {
        mTiRoundTool.draw(this, canvas);
    }

    @Override
    public void drawSuper(Canvas canvas) {
        super.draw(canvas);
    }
}