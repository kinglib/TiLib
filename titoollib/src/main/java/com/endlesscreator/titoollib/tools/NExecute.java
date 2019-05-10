package com.endlesscreator.titoollib.tools;


import com.endlesscreator.titoollib.utils.AlgorithmUtil;

/**
 * 一个事件在固定时间内执行N次的封装类
 * 也可用作多次点击事件（一般点击间隔在 142ms - 300ms 之间， 不急不躁正常的连续点击平均在 220ms 左右）
 */

public class NExecute {

    private long[] mExecuteHits;

    private int mCount;
    private long mTotalTime;

    public NExecute(int aCount, long aTotalTime) {
        // 参数异常的情况下，默认500毫秒执行2次
        mCount = aCount > 0 ? aCount : 2;
        mTotalTime = aTotalTime > 0 ? aTotalTime : 500;
    }

    /**
     * @return 是否在指定时间内执行了指定次数，当返回true时，自动复位
     */
    public boolean execute() {
        if (mExecuteHits == null) mExecuteHits = new long[mCount];
        boolean lResult = AlgorithmUtil.nExecute(mExecuteHits, mTotalTime);
        if (lResult) mExecuteHits = null;
        return lResult;
    }

}
