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
import android.widget.RelativeLayout;

import com.endlesscreator.tiviewlib.R;


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
public class TiRoundLayout extends RelativeLayout {

    private float[] mRadii = new float[8];   // left_top, right_top, right_bottom, left_bottom
    //    private int mRadius;              // 圆角大小
    private int mStrokeColor;               // 描边颜色
    private int mStrokeWidth;               // 描边半径
    private boolean mClipBackground;        // 是否剪裁背景

    private RectF mLayer;                   // 画布图层大小
    private Region mAreaRegion;             // 内容区域
    private Paint mPaint;                   // 画笔
    private Path mClipPath;                 // 剪裁区域路径


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
        int lStrokeColor = lTypedArray.getColor(R.styleable.TiRoundLayout_stroke_color, Color.TRANSPARENT);
        int lStrokeWidth = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_stroke_width, 0);
        boolean lClipBackground = lTypedArray.getBoolean(R.styleable.TiRoundLayout_clip_bg, false);
        int lRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_top_radius, lRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_top_radius, lRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_right_bottom_radius, lRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundLayout_left_bottom_radius, lRadius);
        lTypedArray.recycle();

        mStrokeColor = lStrokeColor;
        mStrokeWidth = lStrokeWidth;
        mClipBackground = lClipBackground;

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


    private void onClipDraw(Canvas aCanvas) {
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


    private void refreshRegion(View aView) {
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