package com.endlesscreator.titoollib.utils;

import java.math.BigDecimal;

/**
 * Created by 75244 on 2018/6/7.
 * <p>
 * 数学运算的工具类
 */

public class MathUtil {

    public static final String TAG = MathUtil.class.getName();

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
     * 转 int
     */
    public static int toInt(Object aValue, int aDefaultValue) {
        String lStr = Util.toString(aValue);
        if (lStr.length() > 0) {
            try {
                return Integer.parseInt(lStr);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return aDefaultValue;
    }

    public static int toInt(Object aValue) {
        return toInt(aValue, 0);
    }

    /**
     * 转 long
     */
    public static long toLong(Object aValue, long aDefaultValue) {
        String lStr = Util.toString(aValue);
        if (lStr.length() > 0) {
            try {
                return Long.parseLong(lStr);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return aDefaultValue;
    }

    public static long toLong(Object aValue) {
        return toLong(aValue, 0);
    }

}
