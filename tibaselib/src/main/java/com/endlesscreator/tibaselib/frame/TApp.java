package com.endlesscreator.tibaselib.frame;

/**
 * 必须初始化
 */
public class TApp {

    private static TiApp mApp;

    public static TiApp getInstance() {
        return mApp;
    }

    public static void onCreate(TiApp aApp) {
        mApp = aApp;
    }

}
