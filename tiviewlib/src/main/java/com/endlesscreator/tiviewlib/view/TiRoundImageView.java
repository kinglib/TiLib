package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;


import com.endlesscreator.titoollib.utils.AlgorithmUtil;
import com.endlesscreator.titoollib.utils.LogUtil;
import com.endlesscreator.tiviewlib.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 圆角ImageView控件
 * 注意使用第三方加载图片时，其原理必须是使用系统BitmapDrawable（如：使用 {@link ImageView#setImageBitmap(Bitmap)}} 方法），而非纯自定义的Drawable
 * ScaleType支持 ScaleType.CENTER_CROP 与 ScaleType.FIT_XY 两种模式，默认值为 ScaleType.CENTER_CROP （保证填充屏幕）
 *
 * 请使用 {@link TiRoundImg}
 */

@Deprecated
public class TiRoundImageView extends AppCompatImageView {
    private static final String TAG = TiRoundImageView.class.getName();

    private Paint mPaint;
    private Matrix mMatrix;
    private Xfermode mXFerModeSrcIn;
    private DrawFilter mDrawFilter;

    private Map<String, RectF> mLayerBoundsMap;
    private int mMaxClearLayerBoundsCount = 3;

    private float mLeftTopRadius;// 左上
    private float mRightTopRadius;// 右上
    private float mRightBottomRadius;// 右下
    private float mLeftBottomRadius;// 左下
    //在最终显示结果图片（不是控件）的基础上再增加的缩放倍数，方便解决图片边缘问题。为0时正常展示（基数为1），其值必须大于-1。当其小于0时为缩小结果，未填充图片区域为底色，不推荐其值小于0。
    private float mAddScale;

    private RectF mArcRectFTmp;
    private Path mDrawPath;

    private boolean mAutoClear = true;

    private Integer mDefaultImageRes;
    private float mDefaultImageWidth, mDefaultImageHeight;
    private int mDefaultImageBgColor;
    private Bitmap mDefaultBitmap;

    public TiRoundImageView(Context context) {
        super(context);
        init(null);
    }

    public TiRoundImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public TiRoundImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public TiRoundImageView(Context context, float aRadius) {
        super(context);
        init(null, aRadius);
    }

    public TiRoundImageView(Context context, float aLeftTopRadius, float aRightTopRadius, float aRightBottomRadius, float aLeftBottomRadius) {
        super(context);
        init(null, aLeftTopRadius, aRightTopRadius, aRightBottomRadius, aLeftBottomRadius);
    }

    private void init(AttributeSet attrs, float... aRadius) {
        if (aRadius == null || aRadius.length <= 0) {
            if (attrs != null) {
                TypedArray lTypedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TiRoundImageView);
                if (lTypedArray != null) {
                    setAddScale(lTypedArray.getFloat(R.styleable.TiRoundImageView_add_scale, 0));
                    setDefaultImageRes(lTypedArray.getResourceId(R.styleable.TiRoundImageView_loading_icon, 0), lTypedArray.getDimension(R.styleable.TiRoundImageView_loading_icon_width, 0),
                            lTypedArray.getDimension(R.styleable.TiRoundImageView_loading_icon_height, 0), lTypedArray.getColor(R.styleable.TiRoundImageView_loading_icon_background_fill_color, Color.TRANSPARENT));

                    float lRadius = lTypedArray.getDimension(R.styleable.TiRoundImageView_radius, 0);
                    if (lRadius > 0) {
                        setRadius(lRadius);
                    } else {
                        float lLTRadius = lTypedArray.getDimension(R.styleable.TiRoundImageView_left_top_radius, 0);
                        float lRTRadius = lTypedArray.getDimension(R.styleable.TiRoundImageView_right_top_radius, 0);
                        float lRBRadius = lTypedArray.getDimension(R.styleable.TiRoundImageView_right_bottom_radius, 0);
                        float lLBRadius = lTypedArray.getDimension(R.styleable.TiRoundImageView_left_bottom_radius, 0);

                        setRadius(lLTRadius, lRTRadius, lRBRadius, lLBRadius);
                    }
                    lTypedArray.recycle();
                }
            }
//            setRadius(getContext().getResources().getDimension(R.dimen.sw360_8dp));  // 默认圆角半径
        } else if (aRadius.length < 4) {
            setRadius(aRadius[0]);
        } else {
            setRadius(aRadius[0], aRadius[1], aRadius[2], aRadius[3]);
        }
    }

    public void setRadiusDefault() {
        setRadius(AlgorithmUtil.dp2px(getContext(), 8));// 默认圆角半径
    }

    public void setRadius(float mRadius) {
        setRadius(mRadius, mRadius, mRadius, mRadius);
    }

    public void setRadius(float aLeftTopRadius, float aRightTopRadius, float aRightBottomRadius, float aLeftBottomRadius) {
        mLeftTopRadius = aLeftTopRadius;
        mRightTopRadius = aRightTopRadius;
        mRightBottomRadius = aRightBottomRadius;
        mLeftBottomRadius = aLeftBottomRadius;
    }

    public void setAddScale(float aAddScale) {
        this.mAddScale = aAddScale;
    }

    public void setDefaultImageRes(Integer aDefaultImageRes) {
        setDefaultImageRes(aDefaultImageRes, 0, 0);
    }

    public void setDefaultImageRes(Integer aDefaultImageRes, float aDefaultImageWidth, float aDefaultImageHeight) {
        setDefaultImageRes(aDefaultImageRes, aDefaultImageWidth, aDefaultImageHeight, Color.TRANSPARENT);
    }

    public void setDefaultImageRes(Integer aDefaultImageRes, float aDefaultImageWidth, float aDefaultImageHeight, int aDefaultImageBgColor) {
        mDefaultImageRes = aDefaultImageRes == 0 ? null : aDefaultImageRes;
        mDefaultImageWidth = aDefaultImageWidth;
        mDefaultImageHeight = aDefaultImageHeight;
        mDefaultImageBgColor = aDefaultImageBgColor;
        mDefaultBitmap = null;
    }

    public void setMaxClearLayerBoundsCount(int aMaxClearLayerBoundsCount) {
        this.mMaxClearLayerBoundsCount = aMaxClearLayerBoundsCount;
    }

    public void setAutoClear(boolean aAutoClear) {
        mAutoClear = aAutoClear;
    }

    private void checkTools() {
        if (mPaint == null) {
            mPaint = new Paint();
            mPaint.setAntiAlias(true); // 画笔消除锯齿
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE); // 绘制实心圆或 实心矩形;
            mPaint.setColor(Color.WHITE);
        }
        if (mMatrix == null)
            mMatrix = new Matrix();
        if (mDrawPath == null)
            mDrawPath = new Path();
        if (mArcRectFTmp == null)
            mArcRectFTmp = new RectF();
        if (mXFerModeSrcIn == null)
            mXFerModeSrcIn = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        if (mDrawFilter == null)
            mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        if (mLayerBoundsMap == null)
            mLayerBoundsMap = new HashMap<>();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int lWidth = getWidth();
        int lHeight = getHeight();
        Bitmap lContentBitmap = getDrawable() instanceof BitmapDrawable ? ((BitmapDrawable) getDrawable()).getBitmap() : null;
        // 如果 没有加载图也没有默认图 或者 控件宽高值小于2个像素 或者 支持的几个扩展属性都用不到 就走父类的原逻辑
        if ((lContentBitmap == null && mDefaultImageRes == null) || lWidth < 2 || lHeight < 2 ||
                (mLeftTopRadius < 1 && mRightTopRadius < 1 && mRightBottomRadius < 1 && mLeftBottomRadius < 1 && (mAddScale <= -1 || mAddScale == 0))) {
            super.onDraw(canvas);
            return;
        }

        checkTools();

        // 画布消除锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (lContentBitmap == null) {// 没有设置src图片，则展示默认图。
            if (mDefaultBitmap == null) {
                try {// TODO, 需要考虑列表缓存，因为一般来说同一个列表只会用一张图默认图
                    mDefaultBitmap = BitmapFactory.decodeResource(getResources(), mDefaultImageRes);
                } catch (Throwable t) {
                    LogUtil.e(TAG, "decodeResource fail!", t);
                }
            }
            if (mDefaultBitmap != null) {
                if (mDefaultImageBgColor != Color.TRANSPARENT) {
                    mPaint.setColor(mDefaultImageBgColor);
                    drawRoundBg(lWidth, lHeight, canvas);
                    mPaint.setColor(Color.WHITE);// 背景绘制完成后在还原画笔色值（主要是透明度）
                }

                // 宽或高小于一个像素，就按 原始比例展示
                float lScaleX = mDefaultImageWidth < 1 ? 1 : mDefaultImageWidth / (float) mDefaultBitmap.getWidth();
                float lScaleY = mDefaultImageHeight < 1 ? 1 : mDefaultImageHeight / (float) mDefaultBitmap.getHeight();

                mMatrix.setScale(lScaleX, lScaleY, 0, 0);
                mMatrix.postTranslate((lWidth - mDefaultBitmap.getWidth() * lScaleX) / 2, (lHeight - mDefaultBitmap.getHeight() * lScaleY) / 2);

                canvas.drawBitmap(mDefaultBitmap, mMatrix, mPaint);
            }
            return;
        }

        String lLayerBoundsTag = lWidth + "-" + lHeight;
        RectF lLayerBounds = mLayerBoundsMap.get(lLayerBoundsTag);
        if (lLayerBounds == null) {
            lLayerBounds = new RectF(0f, 0f, lWidth, lHeight);
            if (mLayerBoundsMap.size() > mMaxClearLayerBoundsCount) {
                mLayerBoundsMap.clear();
            }
            mLayerBoundsMap.put(lLayerBoundsTag, lLayerBounds);
        }

        int lSaveCount;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            lSaveCount = canvas.saveLayer(lLayerBounds, mPaint);
        } else {
            lSaveCount = canvas.saveLayer(lLayerBounds, mPaint, Canvas.ALL_SAVE_FLAG);
        }

        drawRoundBg(lWidth, lHeight, canvas);

        // 开始混合模式绘制
        mPaint.setXfermode(mXFerModeSrcIn);

        // 绘制内容图
        float lScaleX = lWidth / (float) lContentBitmap.getWidth();
        float lScaleY = lHeight / (float) lContentBitmap.getHeight();
        if (getScaleType() != ScaleType.FIT_XY) {// 如果不是FIT_XY，就按CENTER_CROP处理
            float lScale = Math.max(lScaleX, lScaleY);
            lScaleX = lScale;
            lScaleY = lScale;
        }
        if (mAddScale > -1 && mAddScale != 0) {// 需要做额外的改变Scale处理
            lScaleX *= (1 + mAddScale);
            lScaleY *= (1 + mAddScale);
        }
        mMatrix.setScale(lScaleX, lScaleY, 0, 0);
        mMatrix.postTranslate((lWidth - lContentBitmap.getWidth() * lScaleX) / 2, (lHeight - lContentBitmap.getHeight() * lScaleY) / 2);
        canvas.drawBitmap(lContentBitmap, mMatrix, mPaint);

        mPaint.setXfermode(null);//清除混合模式

        canvas.restoreToCount(lSaveCount);//还原画布
    }

    private void drawRoundBg(int aWidth, int aHeight, Canvas aCanvas) {
        // 绘制显示区
        mDrawPath.reset(); // 记录扇形圆角外的封闭区域，与圆角的公共区域即为显示区域
        mPaint.setStrokeWidth(0);//扇形边界不填充才能绘制出圆形控件
        // 左上
        if (mLeftTopRadius >= 1) {
            float lUseLeftTopRadius = mLeftTopRadius + mRightTopRadius > aWidth && mLeftTopRadius > aWidth / 2 ? aWidth / 2 : mLeftTopRadius;// 圆角半径大于宽高的适配，邻边圆角半径之和大于宽或高时，大边取宽或高一半
            lUseLeftTopRadius = lUseLeftTopRadius + mLeftBottomRadius > aHeight && lUseLeftTopRadius > aHeight / 2 ? aHeight / 2 : lUseLeftTopRadius;
            mArcRectFTmp.left = 0;
            mArcRectFTmp.top = 0;
            mArcRectFTmp.right = lUseLeftTopRadius * 2;
            mArcRectFTmp.bottom = lUseLeftTopRadius * 2;
            aCanvas.drawArc(mArcRectFTmp, 180, 90, true, mPaint);// 左上弧形圆角
            mDrawPath.moveTo(0, lUseLeftTopRadius);
            mDrawPath.lineTo(lUseLeftTopRadius, lUseLeftTopRadius);
            mDrawPath.lineTo(lUseLeftTopRadius, 0);
        } else {
            mDrawPath.moveTo(0, 0);// moveTo: 此点为多边形的起点
        }
        // 右上
        if (mRightTopRadius >= 1) {
            float lUseRightTopRadius = mRightTopRadius + mLeftTopRadius > aWidth && mRightTopRadius > aWidth / 2 ? aWidth / 2 : mRightTopRadius;
            lUseRightTopRadius = lUseRightTopRadius + mRightBottomRadius > aHeight && lUseRightTopRadius > aHeight / 2 ? aHeight / 2 : lUseRightTopRadius;
            mArcRectFTmp.left = aWidth - lUseRightTopRadius * 2;
            mArcRectFTmp.top = 0;
            mArcRectFTmp.right = aWidth;
            mArcRectFTmp.bottom = lUseRightTopRadius * 2;
            aCanvas.drawArc(mArcRectFTmp, 270, 90, true, mPaint);// 右上弧形圆角
            mDrawPath.lineTo(aWidth - lUseRightTopRadius, 0);
            mDrawPath.lineTo(aWidth - lUseRightTopRadius, lUseRightTopRadius);
            mDrawPath.lineTo(aWidth, lUseRightTopRadius);
        } else {
            mDrawPath.lineTo(aWidth, 0);
        }
        // 右下
        if (mRightBottomRadius >= 1) {
            float lUseRightBottomRadius = mRightBottomRadius + mRightTopRadius > aHeight && mRightBottomRadius > aHeight / 2 ? aHeight / 2 : mRightBottomRadius;
            lUseRightBottomRadius = lUseRightBottomRadius + mLeftBottomRadius > aWidth && lUseRightBottomRadius > aWidth / 2 ? aWidth / 2 : lUseRightBottomRadius;
            mArcRectFTmp.left = aWidth - lUseRightBottomRadius * 2;
            mArcRectFTmp.top = aHeight - lUseRightBottomRadius * 2;
            mArcRectFTmp.right = aWidth;
            mArcRectFTmp.bottom = aHeight;
            aCanvas.drawArc(mArcRectFTmp, 0, 90, true, mPaint);// 右下弧形圆角
            mDrawPath.lineTo(aWidth, aHeight - lUseRightBottomRadius);
            mDrawPath.lineTo(aWidth - lUseRightBottomRadius, aHeight - lUseRightBottomRadius);
            mDrawPath.lineTo(aWidth - lUseRightBottomRadius, aHeight);
        } else {
            mDrawPath.lineTo(aWidth, aHeight);
        }
        // 左下
        if (mLeftBottomRadius >= 1) {
            float lUseLeftBottomRadius = mLeftBottomRadius + mLeftTopRadius > aHeight && mLeftBottomRadius > aHeight / 2 ? aHeight / 2 : mLeftBottomRadius;
            lUseLeftBottomRadius = lUseLeftBottomRadius + mRightBottomRadius > aWidth && lUseLeftBottomRadius > aWidth / 2 ? aWidth / 2 : lUseLeftBottomRadius;
            mArcRectFTmp.left = 0;
            mArcRectFTmp.top = aHeight - lUseLeftBottomRadius * 2;
            mArcRectFTmp.right = lUseLeftBottomRadius * 2;
            mArcRectFTmp.bottom = aHeight;
            aCanvas.drawArc(mArcRectFTmp, 90, 90, true, mPaint);// 左下弧形圆角
            mDrawPath.lineTo(lUseLeftBottomRadius, aHeight);
            mDrawPath.lineTo(lUseLeftBottomRadius, aHeight - lUseLeftBottomRadius);
            mDrawPath.lineTo(0, aHeight - lUseLeftBottomRadius);
        } else {
            mDrawPath.lineTo(0, aHeight);
        }
        // 使这些点构成封闭的多边形
        mDrawPath.close();
        mPaint.setStrokeWidth(1);//扇形区域用0画，所以多边形实心区要包含线边，否则会有空隙
        aCanvas.drawPath(mDrawPath, mPaint);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAutoClear) {
            clear();
        }
    }

    public void clear() {
        mPaint = null;
        mMatrix = null;
        mDrawPath = null;
        mXFerModeSrcIn = null;
        mDrawFilter = null;
        mDefaultBitmap = null;
        if (mLayerBoundsMap != null) {
            mLayerBoundsMap.clear();
            mLayerBoundsMap = null;
        }
    }
}
