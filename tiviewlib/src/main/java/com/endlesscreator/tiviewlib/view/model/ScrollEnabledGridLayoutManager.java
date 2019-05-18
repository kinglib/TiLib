package com.endlesscreator.tiviewlib.view.model;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;



/**
 * 可支持禁止水平或竖直滚动，用于多RecyclerView的纵向或横向组合
 * <p>
 * 与 {@link android.support.v7.widget.RecyclerView} 或其子类配合使用，常用的子类如 {@link com.endlesscreator.tiviewlib.view.TiRecyclerView}
 * <p>
 */

public class ScrollEnabledGridLayoutManager extends GridLayoutManager {
    private boolean mInitScrollEnabled = true;
    private boolean mScrollEnabled = mInitScrollEnabled;

    public ScrollEnabledGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public ScrollEnabledGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public ScrollEnabledGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * 初始化Enabled状态
     */
    public void initScrollEnabled(boolean aInitScrollEnabled) {
        mInitScrollEnabled = aInitScrollEnabled;
        setScrollEnabled(mInitScrollEnabled);
    }

    /**
     * 临时修改Enabled状态
     */
    public void setScrollEnabled(boolean aScrollEnabled) {
        mScrollEnabled = aScrollEnabled;
    }

    /**
     * 还原初始状态
     */
    public void revertScrollEnabled() {
        setScrollEnabled(mInitScrollEnabled);
    }

    @Override
    public boolean canScrollVertically() {
        return mScrollEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return mScrollEnabled && super.canScrollHorizontally();
    }
}
