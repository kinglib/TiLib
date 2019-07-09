package com.endlesscreator.tilib.module.drag.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;

/**
 * 需要先设置 {@link #setProxyImageView(ProxyImageView)}
 */
public class ContentImageView extends android.support.v7.widget.AppCompatImageView {

    private ProxyImageView mProxyImageView;

    private static final int NONE = 0;// 初始状态
    private static final int EDIT = 1;// 进入操作状态（双指触发）
    private int mCurModel = NONE;

    private GestureDetector mDetector;
    private OnOperateListener mOperateListener;

    public ContentImageView(Context context) {
        super(context);
        init();
    }

    public ContentImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ContentImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mDetector = new GestureDetector(getContext(), mGestureListener);
    }

    public void setProxyImageView(ProxyImageView aProxyImageView) {
        mProxyImageView = aProxyImageView;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (mProxyImageView != null) {
            int lPointerCount = event.getPointerCount();
            ViewParent lParent = getParent();
            if (lPointerCount >= 2) {
                mCurModel = EDIT;
                if (lParent != null) lParent.requestDisallowInterceptTouchEvent(true);
            }
            if (mCurModel == EDIT) {
                mProxyImageView.dispatchTouchEventProxy(event, this);
            }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (mCurModel == EDIT) {
                        mCurModel = NONE;
                        if (lParent != null) lParent.requestDisallowInterceptTouchEvent(false);
                    }
                    break;
            }
            if (mOperateListener != null) mDetector.onTouchEvent(event);
            return true; // 默认接收事件不往下传递，两指操作变为缩放看大图
        }
        if (mOperateListener != null) mDetector.onTouchEvent(event);
        return super.dispatchTouchEvent(event);
    }


    private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        private final int DOUBLE_TAP_TIMEOUT = ViewConfiguration.getDoubleTapTimeout();

        @Override
        public boolean onDown(MotionEvent e) {
            removeCallbacks(mClickRunnable);
            return false;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            postDelayed(mClickRunnable, DOUBLE_TAP_TIMEOUT);
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            if (mOperateListener != null) mOperateListener.onDoubleClick(ContentImageView.this);
            return false;
        }

    };

    private Runnable mClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mOperateListener != null) mOperateListener.onClick(ContentImageView.this);
        }
    };


    public void setOperateListener(OnOperateListener aOperateListener) {
        mOperateListener = aOperateListener;
    }

    public interface OnOperateListener {
        void onClick(ContentImageView aView);

        void onDoubleClick(ContentImageView aView);
    }

}
