package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.endlesscreator.tiviewlib.R;


public class TiDotRadioButton extends android.support.v7.widget.AppCompatRadioButton {

    private Paint mPaint;

    private boolean mShowDot;
    private float mDotRadius, mMarginRight, mMarginTop;
    private int mDotColor;


    public TiDotRadioButton(Context context) {
        super(context);
        init(null);
    }

    public TiDotRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TiDotRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray lTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TiDotRadioButton);
            if (lTypedArray != null) {
                mShowDot = lTypedArray.getBoolean(R.styleable.TiDotRadioButton_show_dot, false);
                mDotRadius = lTypedArray.getDimension(R.styleable.TiDotRadioButton_dot_radius, 0);
                mMarginRight = lTypedArray.getDimension(R.styleable.TiDotRadioButton_dot_margin_right, 0);
                mMarginTop = lTypedArray.getDimension(R.styleable.TiDotRadioButton_dot_margin_top, 0);
                mDotColor = lTypedArray.getColor(R.styleable.TiDotRadioButton_dot_color, Color.RED);

                lTypedArray.recycle();
            }
        }
    }

    public void setShowDot(boolean aShow) {
        if (aShow == mShowDot) return;
        mShowDot = aShow;
        postInvalidate();
    }

    public boolean isShowDot() {
        return mShowDot;
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 画笔消除锯齿
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 绘制实心圆或 实心矩形;
        mPaint.setColor(mDotColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mShowDot || mDotRadius < 2) return;

        if (mPaint == null) initPaint();
        int lWidth = getWidth();
        float lX = lWidth - mMarginRight - mDotRadius;
        float lY = mMarginTop + mDotRadius;
        canvas.drawCircle(lX, lY, mDotRadius, mPaint);
    }
}
