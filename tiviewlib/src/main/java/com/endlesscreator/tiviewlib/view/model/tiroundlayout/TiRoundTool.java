package com.endlesscreator.tiviewlib.view.model.tiroundlayout;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.view.View;


public class TiRoundTool {

    private ITiRoundView mTiRoundView;

    private float[] mRadii = new float[8];   // top-left, top-right, bottom-right, bottom-left
    //    private int mRadius;              // 圆角大小
    private int mStrokeColor;               // 描边颜色
    private int mStrokeWidth;               // 描边半径
    private boolean mClipBackground;        // 是否剪裁背景

    private RectF mLayer;                   // 画布图层大小
    private Region mAreaRegion;             // 内容区域
    private Paint mPaint;                   // 画笔
    private Path mClipPath;                 // 剪裁区域路径


    public TiRoundTool(ITiRoundView aTiRoundView) {
        mTiRoundView = aTiRoundView;
    }

    public void initAttrs(View aView, int aStrokeColor, int aStrokeWidth, boolean aClipBackground, float lLeftTopRadius, float lRightTopRadius, float lRightBottomRadius, float lLeftBottomRadius) {

        mStrokeColor = aStrokeColor;
        mStrokeWidth = aStrokeWidth;
        mClipBackground = aClipBackground;

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

        aView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    public void onSizeChanged(View aView, int w, int h, int oldw, int oldh) {
        mTiRoundView.onSizeChangedSuper(w, h, oldw, oldh);
        mLayer.set(0, 0, w, h);
        refreshRegion(aView);
    }

    public void dispatchDraw(Canvas canvas) {
        canvas.saveLayer(mLayer, null, Canvas.ALL_SAVE_FLAG);
        mTiRoundView.dispatchDrawSuper(canvas);
        onClipDraw(canvas);
        canvas.restore();
    }

    public void draw(View aView, Canvas canvas) {
        refreshRegion(aView);
        if (mClipBackground) {
            canvas.save();
            canvas.clipPath(mClipPath);
            mTiRoundView.drawSuper(canvas);
            canvas.restore();
        } else {
            mTiRoundView.drawSuper(canvas);
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
