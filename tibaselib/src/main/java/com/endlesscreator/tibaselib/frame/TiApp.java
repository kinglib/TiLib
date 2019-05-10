package com.endlesscreator.tibaselib.frame;

import android.app.Application;

public class TiApp extends Application {

    public static Application getInstance() {
        return TApp.getInstance();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        TApp.onCreate(this);
    }

    /**
     * 提供业务处理的高层接口
     */
    public Object execBS(String aBS, Object... aParams) {
        return null;
    }

}
