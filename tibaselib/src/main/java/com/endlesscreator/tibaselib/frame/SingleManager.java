package com.endlesscreator.tibaselib.frame;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleManager {

    // 用于子线程向主线程发送消息，解决线程间通讯及延迟消息
    public static final Handler POST_DELAYED_HANDLER;
    // 用线程池管理程序中所有子线程，避免直接使用 new Thread
    public static final ExecutorService FIXED_THREAD_EXECUTOR;

    static {
        POST_DELAYED_HANDLER = new Handler(Looper.getMainLooper());
        FIXED_THREAD_EXECUTOR = Executors.newFixedThreadPool(10);
    }

}
