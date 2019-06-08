package com.endlesscreator.tiviewlib.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.endlesscreator.tiviewlib.R;

/**
 * 圆角图片
 * <p>
 * {@link TiRoundLayout} 的简版，单用于显示图片时不用再包装一层
 * <p>
 * 圆角规则注意事项：
 * 各边圆角大于各边边距时，按照单独每个边的宽高比例等比缩小圆角，和 {@link TiRoundLayout} 略有不同，按需使用
 */
public class TiRoundImg extends AppCompatImageView {

    private float[] mRadii = new float[8];   // left_top, right_top, right_bottom, left_bottom

    private Paint mBgPaint, mFgPaint;
    private Path mPath;

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

        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiRoundImg);
        int lRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_radius, 0);

        int lLeftTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_left_top_radius, lRadius);
        int lRightTopRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_right_top_radius, lRadius);
        int lRightBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_right_bottom_radius, lRadius);
        int lLeftBottomRadius = lTypedArray.getDimensionPixelSize(R.styleable.TiRoundImg_left_bottom_radius, lRadius);
        lTypedArray.recycle();

        mRadii[0] = lLeftTopRadius;
        mRadii[1] = lLeftTopRadius;

        mRadii[2] = lRightTopRadius;
        mRadii[3] = lRightTopRadius;

        mRadii[4] = lRightBottomRadius;
        mRadii[5] = lRightBottomRadius;

        mRadii[6] = lLeftBottomRadius;
        mRadii[7] = lLeftBottomRadius;

        mBgPaint = new Paint();
        mBgPaint.setColor(Color.WHITE);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT)); //取下层绘制非交集部分

        mFgPaint = new Paint();
        mFgPaint.setXfermode(null);
    }

    @Override
    public void draw(Canvas canvas) {

        int aWidth = getWidth();
        int aHeight = getHeight();

        Bitmap lBitmap = Bitmap.createBitmap(aWidth, aHeight, Bitmap.Config.ARGB_8888);
        Canvas lNewCanvas = new Canvas(lBitmap);
        super.draw(lNewCanvas);

        // 若半径值设置过大，则按比例缩小
        float[] lLeftTop = {mRadii[0], mRadii[1]};
        float[] lRightTop = {mRadii[2], mRadii[3]};
        float[] lRightBottom = {mRadii[4], mRadii[5]};
        float[] lLeftBottom = {mRadii[6], mRadii[7]};
        if (lLeftTop[0] + lLeftBottom[0] > aHeight) {
            if (lLeftTop[0] == 0) {
                lLeftBottom[0] = aHeight;
            } else {
                float lRatio = aHeight / (lLeftTop[0] + lLeftBottom[0]);
                lLeftTop[0] = lLeftTop[0] * lRatio;
                lLeftBottom[0] = lLeftBottom[0] * lRatio;
            }
        }
        if (lRightTop[0] + lRightBottom[0] > aHeight) {
            if (lRightTop[0] == 0) {
                lRightBottom[0] = aHeight;
            } else {
                float lRatio = aHeight / (lRightTop[0] + lRightBottom[0]);
                lRightTop[0] = lRightTop[0] * lRatio;
                lRightBottom[0] = lRightBottom[0] * lRatio;
            }
        }
        if (lLeftTop[1] + lRightTop[1] > aWidth) {
            if (lLeftTop[1] == 0) {
                lRightTop[1] = aWidth;
            } else {
                float lRatio = aWidth / (lLeftTop[1] + lRightTop[1]);
                lLeftTop[1] = lLeftTop[1] * lRatio;
                lRightTop[1] = lRightTop[1] * lRatio;
            }
        }
        if (lRightBottom[1] + lLeftBottom[1] > aWidth) {
            if (lRightBottom[1] <= 0) {
                lLeftBottom[1] = aWidth;
            } else {
                float lRatio = aWidth / (lRightBottom[1] + lLeftBottom[1]);
                lRightBottom[1] = lRightBottom[1] * lRatio;
                lLeftBottom[1] = lLeftBottom[1] * lRatio;
            }
        }

        if (lLeftTop[0] > 0 || lLeftTop[1] > 0)
            draw(lNewCanvas, 0, lLeftTop[0], 0, 0, lLeftTop[1], 0,
                    0, 0, lLeftTop[1] * 2, lLeftTop[0] * 2, -90, -90);
        if (lLeftBottom[0] > 0 || lLeftBottom[1] > 0)
            draw(lNewCanvas, 0, aHeight - lLeftBottom[0], 0, aHeight, lLeftBottom[1], aHeight,
                    0, aHeight - lLeftBottom[0] * 2, lLeftBottom[1] * 2, aHeight, 90, 90);
        if (lRightTop[0] > 0 || lRightTop[1] > 0)
            draw(lNewCanvas, aWidth, lRightTop[0], aWidth, 0, aWidth - lRightTop[1], 0,
                    aWidth - lRightTop[1] * 2, 0, aWidth, lRightTop[0] * 2, -90, 90);
        if (lRightBottom[0] > 0 || lRightBottom[1] > 0)
            draw(lNewCanvas, aWidth - lRightBottom[1], aHeight, aWidth, aHeight, aWidth, aHeight - lRightBottom[0],
                    aWidth - lRightBottom[1] * 2, aHeight - lRightBottom[0] * 2, aWidth, aHeight, 0, 90);

        canvas.drawBitmap(lBitmap, 0, 0, mFgPaint);
        lBitmap.recycle();
    }

    private void draw(Canvas canvas, float aStartPointX, float aStartPointY, float aCenterPointX, float aCenterPointY, float aEndPointX, float aEndPointY, float aLeft, float aTop, float aRight, float aBottom, float aStartAngle, float aSweepAngle) {
        if (mPath == null) mPath = new Path();
        else mPath.reset();

        mPath.moveTo(aStartPointX, aStartPointY);
        mPath.lineTo(aCenterPointX, aCenterPointY);
        mPath.lineTo(aEndPointX, aEndPointY);

        mPath.arcTo(new RectF(aLeft, aTop, aRight, aBottom), aStartAngle, aSweepAngle);
        mPath.close();
        canvas.drawPath(mPath, mBgPaint);
    }

}