package com.endlesscreator.titoollib.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.endlesscreator.tibaselib.frame.TApp;

import java.lang.reflect.Method;


/**
 * 屏幕管理，提供屏幕宽高像素，并可选是否包含虚拟键高度，由于数据缓存（初始宽高获取时为当前屏幕方向），所以也提供获取长边和短边方法，以免屏幕旋转时产生不必要的混淆
 */
public class ScreenManager {

    private static final String TAG = ScreenManager.class.getName();
    private static ScreenManager mScreenManager;
    private int mWidth, mHeight, mWidthHasVirtualKey, mHeightHasVirtualKey, mStatusBarHeight;

    private boolean mInitCompleteAll = false;

    private ScreenManager() {
    }

    public static ScreenManager getInstance() {
        if (mScreenManager == null) {
            mScreenManager = new ScreenManager();
        }
        return mScreenManager;
    }

    private void checkInit() {
        if (!mInitCompleteAll) {
            Context lContext = TApp.getInstance().getApplicationContext();
            DisplayMetrics lDM = new DisplayMetrics();
            Object lWindowManager = lContext.getSystemService(Context.WINDOW_SERVICE);
            Display display = null;
            if (lWindowManager instanceof WindowManager) {
                display = ((WindowManager) lWindowManager).getDefaultDisplay();
                display.getMetrics(lDM);
            } else {
                lDM = lContext.getResources().getDisplayMetrics();
            }
            mWidth = lDM.widthPixels;
            mHeight = lDM.heightPixels;

            if (display != null) {
                mInitCompleteAll = true;
                try {//通过反射，获取包含虚拟键的整体屏幕高度
                    Class c = Class.forName("android.view.Display");
                    @SuppressWarnings("unchecked")
                    Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                    method.invoke(display, lDM);
                    mWidthHasVirtualKey = lDM.widthPixels;
                    mHeightHasVirtualKey = lDM.heightPixels;
                } catch (Exception e) {
                    LogUtil.e(TAG, e);
                }
            }
            if (mWidthHasVirtualKey <= 0) {
                mWidthHasVirtualKey = mWidth;
            }
            if (mHeightHasVirtualKey <= 0) {
                mHeightHasVirtualKey = mHeight;
            }
        }
    }


    public int getWidth() {
        checkInit();
        return mWidth;
    }

    public int getHeight() {
        checkInit();
        return mHeight;
    }

    public int getWidthHasVirtualKey() {
        checkInit();
        return mWidthHasVirtualKey;
    }

    public int getHeightHasVirtualKey() {
        checkInit();
        return mHeightHasVirtualKey;
    }

    public int getLongHasVirtualKey() {
        int lHeight = getHeightHasVirtualKey();
        int lWidth = getWidthHasVirtualKey();
        return lHeight > lWidth ? lHeight : lWidth;
    }

    public int getShortHasVirtualKey() {
        int lHeight = getHeightHasVirtualKey();
        int lWidth = getWidthHasVirtualKey();
        return lHeight > lWidth ? lWidth : lHeight;
    }

    /**
     * 获得状态栏的高度
     */
    public int getStatusBarHeight() {
        if (mStatusBarHeight == 0) {
            Resources lResources = TApp.getInstance().getResources();
            int lResourceId = lResources.getIdentifier("status_bar_height", "dimen", "android");
            if (lResourceId > 0) {
                mStatusBarHeight = lResources.getDimensionPixelSize(lResourceId);
            }
        }
        return mStatusBarHeight;
    }
}
