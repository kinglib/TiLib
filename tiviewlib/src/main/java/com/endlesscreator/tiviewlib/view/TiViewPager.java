package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.endlesscreator.tiviewlib.R;

public class TiViewPager extends ViewPager {

    private boolean mScrollEnable = true;

    public TiViewPager(Context context) {
        super(context);
    }

    public TiViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
    }

    public void initAttrs(Context context, AttributeSet attrs) {
        TypedArray lTypedArray = context.obtainStyledAttributes(attrs, R.styleable.TiViewPager);
        mScrollEnable = lTypedArray.getBoolean(R.styleable.TiViewPager_scroll_enable, mScrollEnable);
        lTypedArray.recycle();
    }

    public void setScrollEnable(boolean aScrollEnable) {
        mScrollEnable = aScrollEnable;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return mScrollEnable && super.onTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return mScrollEnable && super.onInterceptTouchEvent(ev);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
