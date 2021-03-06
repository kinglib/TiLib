package com.endlesscreator.tilib.base;

import android.app.Application;

import com.endlesscreator.tibaselib.frame.TApp;
import com.endlesscreator.titoollib.utils.ImageFrescoUtil;
import com.endlesscreator.titoollib.utils.LogUtil;


public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        TApp.onCreate(this);
        LogUtil.isOutShow = true;
        ImageFrescoUtil.init();
    }
}
