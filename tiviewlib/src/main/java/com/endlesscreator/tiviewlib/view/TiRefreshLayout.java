package com.endlesscreator.tiviewlib.view;

import android.content.Context;
import android.util.AttributeSet;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;

/**
 * 解决在调用 {@link SmartRefreshLayout#autoRefresh} 之后，立刻调用 {@link SmartRefreshLayout#finishRefresh} 不生效的问题
 * 注意，这期间 {@link SmartRefreshLayout#isRefreshing()} 方法取的值也是有延迟的
 */
public class TiRefreshLayout extends SmartRefreshLayout {

    private long mAutoRefreshTimeMillis, mAutoLoadMoreTimeMillis;

    public TiRefreshLayout(Context context) {
        super(context);
    }

    public TiRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TiRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean autoRefresh(int delayed, int duration, float dragRate) {
        mAutoRefreshTimeMillis = System.currentTimeMillis() + delayed;
        return super.autoRefresh(delayed, duration, dragRate);
    }

    @Override
    public SmartRefreshLayout finishRefresh(int delayed, boolean success) {
        long lInterval = System.currentTimeMillis() - mAutoRefreshTimeMillis;
        if (lInterval + delayed < 1000) { // 自动刷新并且在1秒内立即停止
            delayed = (int) (1000 - lInterval);
        }
        return super.finishRefresh(delayed, success);
    }

    @Override
    public boolean autoLoadMore(int delayed, int duration, float dragRate) {
        mAutoLoadMoreTimeMillis = System.currentTimeMillis() + delayed;
        return super.autoLoadMore(delayed, duration, dragRate);
    }

    @Override
    public SmartRefreshLayout finishLoadMore(int delayed, boolean success, boolean noMoreData) {
        long lInterval = System.currentTimeMillis() - mAutoLoadMoreTimeMillis;
        if (lInterval + delayed < 1000) { // 自动刷新并且在1秒内立即停止
            delayed = (int) (1000 - lInterval);
        }
        return super.finishLoadMore(delayed, success, noMoreData);
    }

    @Override
    public boolean isRefreshing() {
        return super.isRefreshing() || isWaitAutoRefreshing();
    }

    @Override
    public boolean isLoading() {
        return super.isLoading() || getWaitAutoLoading();
    }

    public boolean isWaitAutoRefreshing() {
        return System.currentTimeMillis() - mAutoRefreshTimeMillis <= 1000;
    }

    public boolean getWaitAutoLoading() {
        return System.currentTimeMillis() - mAutoLoadMoreTimeMillis <= 1000;
    }
}
