package com.endlesscreator.tibaselib.frame;

import android.app.Application;

/**
 * 必须初始化
 */
public class TApp {

    private static Application mApp;

    public static Application getInstance() {
        return mApp;
    }

    public static void onCreate(Application aApp) {
        mApp = aApp;
    }

}
