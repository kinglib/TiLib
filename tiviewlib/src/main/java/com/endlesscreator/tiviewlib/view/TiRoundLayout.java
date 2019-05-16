package com.endlesscreator.tiviewlib.view;

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
import android.widget.RelativeLayout;

import com.endlesscreator.tiviewlib.R;


/**
 * 圆角布局
 */
public class TiRoundLayout extends RelativeLayout {

    public float[] mRadii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    private int mRadius;              // 圆角大小
    public int mStrokeColor;               // 描边颜色
    public int mStrokeWidth;               // 描边半径
    public boolean mClipBackground;        // 是否剪裁背景


    public RectF mLayer;                   // 画布图层大小
    public Region mAreaRegion;             // 内容区域
    public Paint mPaint;                   // 画笔
    public Path mClipPath;                 // 剪裁区域路径

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
        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiRoundLayout);
        mStrokeColor = lTypedArray.getColor(R.styleable.TiRoundLayout_stroke_color, Color.TRANSPARENT);
        mStrokeWidth = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_stroke_width, 0);
        mClipBackground = lTypedArray.getBoolean(R.styleable.TiRoundLayout_clip_bg, true);
        if (mRadius == 0) mRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_top_radius, mRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_top_radius, mRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_bottom_radius, mRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_bottom_radius, mRadius);
        lTypedArray.recycle();


        mRadii[0] = lLeftTopRadius;
        mRadii[1] = lLeftTopRadius;

        mRadii[2] = lRightTopRadius;
        mRadii[3] = lRightTopRadius;

        mRadii[4] = lRightBottomRadius;
        mRadii[5] = lRightBottomRadius;

        mRadii[6] = lLeftBottomRadius;
        mRadii[7] = lLeftBottomRadius;

        mLayer = new RectF();
        mAreaRegion = new Region();
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mClipPath = new Path();
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mLayer.set(0, 0, w, h);
        refreshRegion(this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        super.dispatchDraw(canvas);
        onClipDraw(canvas);
        canvas.restore();
    }


    @Override
    public void draw(Canvas canvas) {
        refreshRegion(this);
        if (mClipBackground) {
            canvas.save();
            canvas.clipPath(mClipPath);
            super.draw(canvas);
            canvas.restore();
        } else {
            super.draw(canvas);
        }
    }

    public void onClipDraw(Canvas aCanvas) {
        if (mStrokeWidth > 0) {
            // 将与描边区域重叠的内容裁剪掉
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            mPaint.setColor(Color.WHITE);
            mPaint.setStrokeWidth(mStrokeWidth * 2);
            mPaint.setStyle(Paint.Style.STROKE);
            aCanvas.drawPath(mClipPath, mPaint);
            // 绘制描边
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            mPaint.setColor(mStrokeColor);
            mPaint.setStyle(Paint.Style.STROKE);
            aCanvas.drawPath(mClipPath, mPaint);
        }
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        aCanvas.drawPath(mClipPath, mPaint);
    }


    public void refreshRegion(View aView) {
        int lW = (int) mLayer.width();
        int lH = (int) mLayer.height();
        RectF lAreas = new RectF();
        lAreas.left = aView.getPaddingLeft();
        lAreas.top = aView.getPaddingTop();
        lAreas.right = lW - aView.getPaddingRight();
        lAreas.bottom = lH - aView.getPaddingBottom();
        mClipPath.reset();
        mClipPath.addRoundRect(lAreas, mRadii, Path.Direction.CW);
        Region clip = new Region((int) lAreas.left, (int) lAreas.top, (int) lAreas.right, (int) lAreas.bottom);
        mAreaRegion.setPath(mClipPath, clip);
    }
}