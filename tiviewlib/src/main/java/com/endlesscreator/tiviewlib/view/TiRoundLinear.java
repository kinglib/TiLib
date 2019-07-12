package com.endlesscreator.tiviewlib.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.endlesscreator.tiviewlib.R;
import com.endlesscreator.tiviewlib.view.model.tiround.ITiRoundView;
import com.endlesscreator.tiviewlib.view.model.tiround.TiRoundTool;


/**
 * 圆角布局
 * <p>
 * {@link TiRoundImg} 的升级版
 * <p>
 * 若单独加载一张图片圆角时，可使用 {@link TiRoundImg} 就不用再包装一层
 * <p>
 * 圆角规则注意事项：
 * 各边圆角大于各边边距时，按照所有边的宽高比例等比缩小圆角，和 {@link TiRoundImg} 略有不同，按需使用
 * <p>
 * 注意：尽量避免使用裁剪背景，因为裁剪可能会出现边缘锯齿
 */
public class TiRoundLinear extends LinearLayout implements ITiRoundView {

    private TiRoundTool mTiRoundTool;

    public TiRoundLinear(Context context) {
        this(context, null);
    }

    public TiRoundLinear(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public TiRoundLinear(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TiRoundLinear(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttrs(context, attrs);
    }


    public void initAttrs(Context context, AttributeSet attrs) {
        mTiRoundTool = new TiRoundTool(this);

        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiRoundLinear);
        int lStrokeColor = lTypedArray.getColor(R.styleable.TiRoundLinear_stroke_color, Color.TRANSPARENT);
        int lStrokeWidth = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_stroke_width, 0);
        boolean lClipBackground = lTypedArray.getBoolean(R.styleable.TiRoundLinear_clip_bg, false);
        int lRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_left_top_radius, lRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_right_top_radius, lRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_right_bottom_radius, lRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLinear_left_bottom_radius, lRadius);
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