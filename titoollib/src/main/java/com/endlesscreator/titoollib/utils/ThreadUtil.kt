package com.endlesscreator.titoollib.utils

import android.os.Looper

object ThreadUtil {

    /**
     * 判断是否在当前主线程
     */
    val isMainThread: Boolean get() = Looper.myLooper() == Looper.getMainLooper()
}
