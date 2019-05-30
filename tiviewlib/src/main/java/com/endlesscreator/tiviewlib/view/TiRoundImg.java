package com.endlesscreator.tiviewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.endlesscreator.tiviewlib.R;
import com.endlesscreator.tiviewlib.view.model.tiroundlayout.ITiRoundView;
import com.endlesscreator.tiviewlib.view.model.tiroundlayout.TiRoundTool;

/**
 * 圆角图片
 * <p>
 * {@link TiRoundLayout} 的简版，单用于显示图片时不用再包装一层
 */
public class TiRoundImg extends AppCompatImageView implements ITiRoundView {

    private TiRoundTool mTiRoundTool;

    public TiRoundImg(Context context) {
        this(context, null);
    }

    public TiRoundImg(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public TiRoundImg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
        mTiRoundTool = new TiRoundTool(this);

        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiRoundImg);
        int lStrokeColor = lTypedArray.getColor(R.styleable.TiRoundImg_stroke_color, Color.TRANSPARENT);
        int lStrokeWidth = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_stroke_width, 0);
        int lRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_left_top_radius, lRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_right_top_radius, lRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_right_bottom_radius, lRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_left_bottom_radius, lRadius);
        lTypedArray.recycle();

        mTiRoundTool.initAttrs(this, lStrokeColor, lStrokeWidth, true, lLeftTopRadius, lRightTopRadius, lRightBottomRadius, lLeftBottomRadius);
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