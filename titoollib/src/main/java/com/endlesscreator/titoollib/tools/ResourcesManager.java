package com.endlesscreator.titoollib.tools;

import android.content.res.Resources;

import com.endlesscreator.tibaselib.frame.TApp;
import com.endlesscreator.titoollib.utils.SystemUtil;

public class ResourcesManager {

    private static ResourcesManager instance;
    private Resources mResources;

    private ResourcesManager() {
        mResources = TApp.getInstance().getResources();
    }

    public static ResourcesManager getInstance() {
        if (instance == null) {
            synchronized (ResourcesManager.class) {
                if (instance == null) instance = new ResourcesManager();
            }
        }
        return instance;
    }

    public Resources getResources() {
        return mResources;
    }

    public int getResID(String aType, String aName, String aPackage) {
        try {
            return mResources.getIdentifier(aName, aType, aPackage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getResID(String aType, String aName) {
        return getResID(aType, aName, SystemUtil.getPackName());
    }

    public int getDimen(String aName) {
        return getResID("dimen", aName);
    }


    public int getAnim(String aName) {
        return getResID("anim", aName);
    }

    public int getXml(String aName) {
        return getResID("xml", aName);
    }

    public int getStyleable(String aName) {
        return getResID("styleable", aName);
    }

    public int getStyle(String aName) {
        return getResID("style", aName);
    }

    public int getString(String aName) {
        return getResID("string", aName);
    }

    public int getRaw(String aName) {
        return getResID("raw", aName);
    }

    public int getMipmap(String aName) {
        return getResID("mipmap", aName);
    }

    public int getMenu(String aName) {
        return getResID("menu", aName);
    }

    public int getLayout(String aName) {
        return getResID("layout", aName);
    }

    public int getID(String aName) {
        return getResID("id", aName);
    }

    public int getDrawable(String aName) {
        return getResID("drawable", aName);
    }

    public int getColor(String aName) {
        return getResID("color", aName);
    }

    public int getFont(String aName) {
        return getResID("font", aName);
    }

    public int getBool(String aName) {
        return getResID("bool", aName);
    }

    public int getAttr(String aName) {
        return getResID("attr", aName);
    }

    public int getArray(String aName) {
        return getResID("array", aName);
    }

    public int getAnimator(String aName) {
        return getResID("animator", aName);
    }


}
