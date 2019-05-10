package com.endlesscreator.titoollib.tools;

import android.os.SystemClock;

import com.endlesscreator.titoollib.utils.DateUtil;


/**
 * 解决修改系统时间导致的获取时间戳变化差异的情况。
 * 多用于倒计时等情况。
 * 若仅用于真实（网络）时间戳获取，可用静态存储。
 */
public class TimeRecord {

    private Long mCalibrationTimeMillis, mCalibrationUptimeMillis;

    /**
     * 校准时间戳
     *
     * @param aCurrentTimeMillis 当前时间戳，一般为服务器返回，也可自定义数值（用于计时）。
     */
    public void calibrationTimeMillis(long aCurrentTimeMillis) {
        mCalibrationTimeMillis = DateUtil.getTimeMillisThirteenBits(aCurrentTimeMillis);
        mCalibrationUptimeMillis = elapsedRealtime();
    }

    /**
     * 获取与校准时间的间隔时间
     * 需要先调用 {@link #calibrationTimeMillis(long)} 校准时间戳，否则返回0
     */
    public long intervalTimeMillis() {
        if (mCalibrationUptimeMillis == null) return 0;
        return elapsedRealtime() - mCalibrationUptimeMillis;
    }

    /**
     * 获取校准后的当前时间戳
     * 需要先调用 {@link #calibrationTimeMillis(long)} 校准时间戳，否则返回系统时间戳
     */
    public long currentTimeMillis() {
        if (mCalibrationTimeMillis == null) return System.currentTimeMillis();
        return intervalTimeMillis() + mCalibrationTimeMillis;
    }

    public Long getCalibrationTimeMillis() {
        return mCalibrationTimeMillis;
    }

    public Long getCalibrationUptimeMillis() {
        return mCalibrationUptimeMillis;
    }

    /**
     * 自开机后，经过的时间，不包括深度睡眠的时间
     */
    public static long uptimeMillis() {
        return SystemClock.uptimeMillis();
    }

    /**
     * 自开机后，经过的时间，包括深度睡眠的时间
     */
    public static long elapsedRealtime() {
        return SystemClock.elapsedRealtime();
    }
}
