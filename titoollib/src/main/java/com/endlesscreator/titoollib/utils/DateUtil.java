package com.endlesscreator.titoollib.utils;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT_PATTERN_ALL_DETAIL = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_PATTERN_ALL = "yyyy-MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN_ONLY_DAY = "yyyy-MM-dd";
    public static final String DATE_FORMAT_PATTERN_MONTH = "MM-dd HH:mm";
    public static final String DATE_FORMAT_PATTERN_TIME = "HH:mm";

    /**
     * 获取十位的时间戳（服务器专用）
     */
    public static String getTimeMillisTenBits() {
        return String.valueOf(System.currentTimeMillis()).substring(0, 10);
    }

    /**
     * 新闻、评论专用时间显示规则
     * *time < 1min             刚刚
     * *1min < time < 1h        n 分钟前
     * *1h < time < 昨天        n小时前
     * *昨天 < time < 前天      昨天HH:mm
     * *前天 < time < 去年      MM-dd HH:mm
     * *非今年                  yyyy-MM-dd HH:mm
     */
    public static String formatDate(long aTimeMillis) {
        long lTimeMillisThirteenBits = getTimeMillisThirteenBits(aTimeMillis);
        long lCurrentTimeMillis = System.currentTimeMillis();
        long lDifferenceSecond = (lCurrentTimeMillis - lTimeMillisThirteenBits) / 1000;
        if (lDifferenceSecond < 60) {//一分钟内
            return "刚刚";
        }
        if (lDifferenceSecond < 3600) {//一小时内
            return lDifferenceSecond / 60 + "分钟前";
        }

        long lTodayZeroMillis = getOneDayZeroMillis(lCurrentTimeMillis);// 今天零点的时间
        if (lTimeMillisThirteenBits >= lTodayZeroMillis) {// 今天
            return lDifferenceSecond / 3600 + "小时前";
        }
        if (lTimeMillisThirteenBits >= lTodayZeroMillis - 86400000) { // 昨天
            return "昨天" + formatDate(lTimeMillisThirteenBits, DATE_FORMAT_PATTERN_TIME);
        }
        String lOutAllTime = formatDate(lTimeMillisThirteenBits, DATE_FORMAT_PATTERN_ALL);
        String lCurrentAllTime = formatDate(lCurrentTimeMillis, DATE_FORMAT_PATTERN_ALL);
        String lThisYear = lCurrentAllTime.substring(0, 4);
        if (lOutAllTime.substring(0, 4).equals(lThisYear)) {// 今年
            return lOutAllTime.substring(5);
        }
        return lOutAllTime;
    }

    /**
     * 根据时间戳获取格式化的日期
     */
    public static String formatDate(long aTimeMillis, String aFormatPattern) {
        Date lDate = new Date(getTimeMillisThirteenBits(aTimeMillis));
        SimpleDateFormat lFormat = new SimpleDateFormat(aFormatPattern, Locale.getDefault());
        return lFormat.format(lDate);
    }

    /**
     * 将时间转换为时间戳
     */
    public static long unFormatDate(String aFormatDate, String aFormatPatter) {
        if (!TextUtils.isEmpty(aFormatDate)) {
            try {
                SimpleDateFormat lFormat = new SimpleDateFormat(aFormatPatter, Locale.getDefault());
                return lFormat.parse(aFormatDate).getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return 0;
    }

    /**
     * 某天零点的时间
     */
    public static long getOneDayZeroMillis(long aTimeMillis) {
        return aTimeMillis / 86400000 * 86400000 - TimeZone.getDefault().getRawOffset();
    }

    /**
     * 转换为十三位的时间戳（Java专用）
     */
    public static long getTimeMillisThirteenBits(long aTimeMillis) {
        int lLength = String.valueOf(aTimeMillis).length();
        if (lLength > 13) aTimeMillis = aTimeMillis / (long) Math.pow(10, lLength - 13);
        else if (lLength < 13) aTimeMillis = aTimeMillis * (long) Math.pow(10, 13 - lLength);
        return aTimeMillis;
    }

    /**
     * 将秒格式化为 分:秒
     */
    public static String formatTime(int aTimeSecond) {
        if (aTimeSecond <= 0) return "00:00";
        int lSecond = aTimeSecond < 60 ? aTimeSecond : aTimeSecond % 60;
        int lMinute = aTimeSecond < 60 ? 0 : aTimeSecond / 60;
        return formatDoubleUp(lMinute) + ":" + formatDoubleUp(lSecond);
    }

    public static String formatDoubleUp(int aTime) {
        return (aTime < 10 ? "0" : "") + aTime;
    }

    /**
     * 将秒时长格式化为 时:分:秒 数组
     */
    public static int[] getTimes(int aTimeSecond) {
        int[] lTimes = new int[3];
        if (aTimeSecond <= 0) return lTimes;
        lTimes[2] = aTimeSecond < 60 ? aTimeSecond : aTimeSecond % 60;
        int lMinute = aTimeSecond < 60 ? 0 : aTimeSecond / 60;
        lTimes[1] = lMinute < 60 ? lMinute : lMinute % 60;
        lTimes[0] = lMinute < 60 ? 0 : lMinute / 60;
        return lTimes;
    }

    /**
     * 将毫秒时长格式化为 时:分:秒
     */
    public static String formatTimeForMillis(long aTimeMillis) {
        int lTotalSeconds = (int) (aTimeMillis / 1000);
        int lSeconds = lTotalSeconds % 60;
        int lMinutes = (lTotalSeconds / 60) % 60;
        int lHours = lTotalSeconds / 3600;
        return lHours > 0 ? String.format("%02d:%02d:%02d", lHours, lMinutes, lSeconds) : String.format("%02d:%02d", lMinutes, lSeconds);
    }

}
