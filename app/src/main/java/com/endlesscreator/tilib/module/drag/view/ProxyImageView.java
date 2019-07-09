package com.endlesscreator.tilib.module.drag.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;


/**
 * 要放到{@link RelativeLayout}最顶层，并填充全屏，以便于初始化适配状态栏高度
 */
public class ProxyImageView extends android.support.v7.widget.AppCompatImageView {

    // 状态记录
    private static final int NONE = 0; // 初始状态
    private static final int DRAG = 2; // 单指拖动
    private static final int ZOOM_OR_ROTATE = 4; // 多指缩放旋转
    private int mCurModel = NONE;

    // 初始记录，用于同步被代理者参数
    private ContentImageView mContentImageView;
    private int[] mLocationOnScreen = new int[2];
    private Integer mStatusBarHeight;
    private float mContentLeftMargin, mContentTopMargin;

    // 操作记录
    private Bitmap mContentBitmap;
    private float mInitScale, mMaxScale, mMinScale, mBaseScale; // XY等比
    private double mPointerDownSize = 1;
    private float mPointerCenterX, mPointerCenterY, mPointerDownRotate;

    private Paint mPaint;
    private Matrix mMatrix;

    private float mDownRawX, mDownRawY;

    /*
       矩阵信息
         MSCALEL_X  MSKEW_X     MTRANS_X
         MSKEW_Y    MSCALEL_Y   MTRANS_Y
         MPERSP_0   MPERSP_1    MPERSP_2
         ---------------辅助说明--------------------
         设：
            Rotate = 旋转角度：如60
            ScaleX = x缩放值：如0.46
         则：
            MSCALEL_X = ScaleX * cos(Rotate) ：如0.46 * 0.5 = 0.23
         注：
            cos(Rotate) = Math.cos(Rotate * Math.PI / 180)

         设：
            Rotate = 旋转角度：如30
            ScaleX = x缩放值：如0.46
         则：
            MSKEW_X = ScaleX * -sin(Rotate) ：如0.46 * 0.5 = 0.23
         注：
            MSKEW_Y、MSCALEL_Y 同理

         矩阵说明：
         |   x   | |   cos θ   -sin θ  0    | |   x0  |
         |   y   |=|   sin θ   cos θ   0    | |   y0  |
         |   1   | |   0        0        1    | |   1   |

     */
    private float[] mBaseMatrixInfo = {
            1, 0, 0,
            0, 1, 0,
            0, 0, 1
    };

    public ProxyImageView(Context context) {
        super(context);
        init();
    }

    public ProxyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ProxyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true); // 消除锯齿
        mPaint.setStyle(Paint.Style.STROKE); // 绘制空心圆或 空心矩形
        mMatrix = new Matrix();
    }


    public void dispatchTouchEventProxy(MotionEvent event, ContentImageView aContentImageView) {

        if (mCurModel == NONE && aContentImageView == null) {
            revertLocation();
            return;
        }

        float lRawX = event.getRawX();
        float lRawY = event.getRawY();

        int lPointerCount = event.getPointerCount();

        // 初始化显示位置
        if (mCurModel == NONE) {
            initLocation(aContentImageView);
        }

        // 初始化操作模式
        if (lPointerCount == 1 && mCurModel != DRAG) {
            mCurModel = DRAG;
            mDownRawX = lRawX;
            mDownRawY = lRawY;
            saveSnapshot();
        } else if (lPointerCount > 1 && mCurModel != ZOOM_OR_ROTATE) {
            mCurModel = ZOOM_OR_ROTATE;
            initZoomRotateDown(event);
            saveSnapshot();
        }

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_MOVE:

                if (mCurModel == DRAG) {
                    move(lRawX - mDownRawX, lRawY - mDownRawY);

                } else if (mCurModel == ZOOM_OR_ROTATE) {
                    double lScale = Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2)) / mPointerDownSize;
                    float lRotate = computeDegree(new PointF(event.getX(0), event.getY(0)), new PointF(event.getX(1), event.getY(1)));
                    float lMoveX = getCenterX(event) - mPointerCenterX;
                    float lMoveY = getCenterY(event) - mPointerCenterY;
                    scaleRotateMove((float) lScale, lRotate - mPointerDownRotate, mPointerCenterX, mPointerCenterY, lMoveX, lMoveY);

                }

                break;

            case MotionEvent.ACTION_POINTER_DOWN:
                // TODO 暂不处理
                break;

            case MotionEvent.ACTION_POINTER_UP:
                // TODO 暂不处理

                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                revertLocation();
                mCurModel = NONE;

                break;
        }
    }

    private void initZoomRotateDown(MotionEvent event) {
        mPointerDownSize = Math.sqrt(Math.pow(event.getX(1) - event.getX(0), 2) + Math.pow(event.getY(1) - event.getY(0), 2));
        if (mPointerDownSize < 1) mPointerDownSize = 1;// 两个手指之间的距离不到1像素，基数就定为1像素
        mPointerCenterX = getCenterX(event);
        mPointerCenterY = getCenterY(event);
        mPointerDownRotate = computeDegree(new PointF(event.getX(0), event.getY(0)), new PointF(event.getX(1), event.getY(1)));
    }

    private float getCenterX(MotionEvent event) {
        return (event.getX(0) + event.getX(1)) / 2 + mContentLeftMargin;
    }

    private float getCenterY(MotionEvent event) {
        return (event.getY(0) + event.getY(1)) / 2 + mContentTopMargin;
    }

    /**
     * 计算两点与垂直方向夹角
     */
    private float computeDegree(PointF p1, PointF p2) {
        float tran_x = p1.x - p2.x;
        float tran_y = p1.y - p2.y;
        float degree = 0.0f;
        float angle = (float) (Math.asin(tran_x / Math.sqrt(tran_x * tran_x + tran_y * tran_y)) * 180 / Math.PI);
        if (!Float.isNaN(angle)) {
            if (tran_x >= 0 && tran_y <= 0) {//第一象限
                degree = angle;
            } else if (tran_x <= 0 && tran_y <= 0) {//第二象限
                degree = angle;
            } else if (tran_x <= 0 && tran_y >= 0) {//第三象限
                degree = -180 - angle;
            } else if (tran_x >= 0 && tran_y >= 0) {//第四象限
                degree = 180 - angle;
            }
        }
        return degree;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mContentBitmap != null) {
            canvas.drawBitmap(mContentBitmap, mMatrix, mPaint);
            return;
        }
        super.onDraw(canvas);
    }

    public void move(float aDx, float aDy) {
        if (mMatrix == null) return;
        mMatrix.setValues(mBaseMatrixInfo);
        mMatrix.postTranslate(aDx, aDy);
        invalidate();
    }

    public void scaleRotateMove(float aScale, float aRotate, float aPointerCenterX, float aPointerCenterY, float aDx, float aDy) {
        if (mMatrix == null) {
            return;
        }
        // 对最大最小缩放值加控制，大于最大值取最大值，小于最小值取最小值
        if (aScale * mBaseScale > mMaxScale) {
            aScale = mMaxScale / mBaseScale;
        } else if (aScale * mBaseScale < mMinScale) {
            aScale = mMinScale / mBaseScale;
        }
        mMatrix.setValues(mBaseMatrixInfo);
        mMatrix.postScale(aScale, aScale, aPointerCenterX, aPointerCenterY);
//        mMatrix.postRotate(aRotate, aPointerCenterX, aPointerCenterY); // TODO 暂时忽略旋转
        mMatrix.postTranslate(aDx, aDy);
        invalidate();
    }

    /**
     * 保存快照信息（记录上一次的位置）
     */
    public void saveSnapshot() {
        if (mMatrix == null) return;
        float[] lMatrixInfo = new float[9];
        mMatrix.getValues(lMatrixInfo);
        mBaseMatrixInfo = lMatrixInfo;
        /*
            推导过程：
            已知：
            ScaleX * -sin(Rotate) = mBaseMatrixInfo[1]
            ScaleX * cos(Rotate) = mBaseMatrixInfo[0]
            则：
             --> -sin(Rotate)/cos(Rotate) = -tan(Rotate) = -Math.tan(Rotate / 180 * Math.PI) = -Math.tan(T) = mBaseMatrixInfo[1] / mBaseMatrixInfo[0]
             --> T = -Math.atan(- mBaseMatrixInfo[1] / mBaseMatrixInfo[0])
             --> ScaleX = mBaseMatrixInfo[0] / Math.cos(T)
             注： 需要考虑 mBaseMatrixInfo[1] 为 0 的情况，此时mBaseMatrixInfo[0]就是真正的缩放值。 还有 ScaleX 不能为负数
         */
        mBaseScale = mBaseMatrixInfo[1] == 0 ? mBaseMatrixInfo[0] : (float) Math.abs(mBaseMatrixInfo[0] / Math.cos(-Math.atan(-mBaseMatrixInfo[1] / mBaseMatrixInfo[0])));
    }

    private void initLocation(ContentImageView aContentImageView) {
        mContentImageView = aContentImageView;
        mContentImageView.getLocationOnScreen(mLocationOnScreen);

        if (mStatusBarHeight == null) {
            int[] lLocationOnScreen = new int[2];
            getLocationOnScreen(lLocationOnScreen);
            mStatusBarHeight = lLocationOnScreen[1];
        }

        mContentLeftMargin = mLocationOnScreen[0];
        mContentTopMargin = mLocationOnScreen[1] - mStatusBarHeight;

        mContentImageView.setDrawingCacheEnabled(true);
        mContentBitmap = Bitmap.createBitmap(aContentImageView.getDrawingCache());
        mContentImageView.setDrawingCacheEnabled(false);
        setImageBitmap(mContentBitmap);
        mContentImageView.setImageBitmap(null);

        mInitScale = 1;
        mMaxScale = mInitScale * 5; // 能放大的倍数
        mMinScale = mInitScale / 1; // 能缩小的倍数
        saveSnapshot();

        mMatrix.setScale(mInitScale, mInitScale, 0, 0);
        mMatrix.postTranslate(mContentLeftMargin, mContentTopMargin);
        postInvalidate(); // 初始化显示

        setClickable(true); // 之后的事件将全部被拦截，包括ContentView新增触点的事件
    }

    private void revertLocation() {
        // TODO 回到原处，暂时不用动画
        if (mContentImageView != null) {
            Drawable lContentDrawable = mContentImageView.getDrawable();
            if (lContentDrawable == null || lContentDrawable.getIntrinsicWidth() <= 0) {
                mContentImageView.setImageBitmap(mContentBitmap);
            }
        }
        mContentBitmap = null;
        mContentImageView = null;
        setImageBitmap(null);

        setClickable(false);
    }

}
