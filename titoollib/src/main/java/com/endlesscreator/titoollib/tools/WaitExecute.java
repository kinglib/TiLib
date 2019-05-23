package com.endlesscreator.titoollib.tools;

public class WaitExecute {

    private long mPreviousExecuteTime = 0;
    private long mInterval = 500;

    public WaitExecute() {
    }

    public WaitExecute(long aInterval) {
        setInterval(mInterval);
    }

    public void setInterval(long aInterval) {
        this.mInterval = aInterval;
        reset();
    }

    public void reset() {
        mPreviousExecuteTime = 0;
    }

    public void execute() {
        mPreviousExecuteTime = System.currentTimeMillis();
    }

    public boolean can() {
        return can(true);
    }

    public boolean can(boolean aAutoRefresh) {
        long lCurrentTime = System.currentTimeMillis();
        boolean lResult = lCurrentTime - mPreviousExecuteTime > mInterval;
        if (aAutoRefresh && lResult) mPreviousExecuteTime = lCurrentTime;
        return lResult;
    }

}
