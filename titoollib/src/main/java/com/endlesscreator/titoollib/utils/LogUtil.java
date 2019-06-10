package com.endlesscreator.titoollib.utils;

import android.text.TextUtils;
import android.util.Log;

public class LogUtil {

    private static final String TAG = LogUtil.class.getSimpleName();
    public static boolean isOutShow = false;

    public static void i(String msg) {
        i(null, msg);
    }

    public static void i(String tag, String msg) {
        if (!isOutShow) return;
        if (msg != null) Log.i(TextUtils.isEmpty(tag) ? TAG : tag, msg);
    }

    public static void e(Throwable tr) {
        if (tr != null) e(null, tr.getMessage(), tr);
    }

    public static void e(String tag, Throwable tr) {
        if (tr != null) e(tag, tr.getMessage(), tr);
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (!isOutShow) return;
        if (msg != null) Log.e(TextUtils.isEmpty(tag) ? TAG : tag, msg, tr);
    }
}
