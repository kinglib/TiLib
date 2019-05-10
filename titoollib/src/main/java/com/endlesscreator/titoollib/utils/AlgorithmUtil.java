package com.endlesscreator.titoollib.utils;

import android.content.Context;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.TypedValue;


import com.endlesscreator.tibaselib.frame.TApp;

import java.math.BigDecimal;

public class AlgorithmUtil {

    private static final String TAG = AlgorithmUtil.class.getName();

    /**
     * dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context aContext, int aValue) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, aValue, aContext.getApplicationContext().getResources().getDisplayMetrics());
    }

    /**
     * 需要项目使用的Application继承 {@link TApp} 才能使用此方法
     */
    public static int dp2px(int aValue) {
        return dp2px(TApp.getInstance(), aValue);
    }

    /**
     * @param aSource       要进行处理的值
     * @param aAccuracy     要保留几位小数
     * @param aRoundingMode BigDecimal.ROUND_DOWN 为向下(接近零)取，BigDecimal.ROUND_UP 为向上(远离零)取，BigDecimal.ROUND_HALF_UP 为四舍五入
     * @return
     */
    public static float retentionAccuracy(double aSource, int aAccuracy, int aRoundingMode) {
        if (aSource == 0) {
            return 0;
        }
        try {
            BigDecimal lBigDecimal = new BigDecimal(aSource);
            return lBigDecimal.setScale(aAccuracy, aRoundingMode).floatValue();
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        try {
            float lAddFloat;
            switch (aRoundingMode) {
                case BigDecimal.ROUND_DOWN:
                    lAddFloat = 0;
                    break;
                case BigDecimal.ROUND_UP:
                    lAddFloat = 1;
                    break;
                default:
                    lAddFloat = 0.5f;
                    break;
            }
            double lBaseNum = Math.pow(10, aAccuracy);
            return ((long) (aSource * lBaseNum + lAddFloat)) / (float) lBaseNum;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return (float) aSource;
    }

    /**
     * 结果保留一位小数
     */
    public static String formatFileSize(long aTotalSize) {
        return aTotalSize >= 1024 && aTotalSize < (1024 * 1024) ?
                retentionAccuracy(((aTotalSize * 10) >> 10) / 10f, 2, BigDecimal.ROUND_HALF_UP) + "KB" :
                aTotalSize >= (1024 * 1024) && aTotalSize < (1024 * 1024 * 1024) ?
                        retentionAccuracy(((aTotalSize * 10) >> 20) / 10f, 2, BigDecimal.ROUND_HALF_UP) + "MB" :
                        aTotalSize >= (1024 * 1024 * 1024) ?
                                retentionAccuracy(((aTotalSize * 10) >> 30) / 10f, 2, BigDecimal.ROUND_HALF_UP) + "GB" : aTotalSize + "B";
    }

    /**
     * @param aCacheHits     存储时间的集合，如 mCacheHits = new long[N]; 其中N为执行次数。
     * @param aTotalInterval 总的执行间隔 单位毫秒
     * @return 单位时间内执行N次时返回true
     */
    public static boolean nExecute(long[] aCacheHits, long aTotalInterval) {
        System.arraycopy(aCacheHits, 1, aCacheHits, 0, aCacheHits.length - 1);
        aCacheHits[aCacheHits.length - 1] = SystemClock.uptimeMillis();//获取手机开机时间
        return aCacheHits[aCacheHits.length - 1] - aCacheHits[0] < aTotalInterval;
    }

    public static Long toLong(Object aNum) {
        String aStrNum = Util.toString(aNum);
        if (!TextUtils.isEmpty(aStrNum)) {
            try {
                return Long.parseLong(aStrNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Integer toInt(Object aNum) {
        String aStrNum = Util.toString(aNum);
        if (!TextUtils.isEmpty(aStrNum)) {
            try {
                return Integer.parseInt(aStrNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
