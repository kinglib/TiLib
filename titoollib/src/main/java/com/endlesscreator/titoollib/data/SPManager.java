package com.endlesscreator.titoollib.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.endlesscreator.tibaselib.frame.TApp;


public class SPManager {
    private static final String DEFAULT_USE_INFO_NAME = "use_info";

    public static final int DEFAULT_INT = 0;
    public static final String DEFAULT_STRING = "";

    private SharedPreferences mSP;

    public SPManager() {
        this(DEFAULT_USE_INFO_NAME);
    }

    public SPManager(String aName) {
        mSP = TApp.getInstance().getSharedPreferences(aName, Context.MODE_PRIVATE);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_INT);
    }

    public int getInt(String key, int defValue) {
        return mSP.getInt(key, defValue);
    }

    public void applyInt(String key, int value) {
        edit().putInt(key, value).apply();
    }

    public void commitInt(String key, int value) {
        edit().putInt(key, value).commit();
    }

    public long getLong(String key) {
        return getLong(key, DEFAULT_INT);
    }

    public long getLong(String key, long defValue) {
        return mSP.getLong(key, defValue);
    }

    public void applyLong(String key, long value) {
        edit().putLong(key, value).apply();
    }

    public String getString(String key) {
        return getString(key, DEFAULT_STRING);
    }

    public String getString(String key, String defValue) {
        return mSP.getString(key, defValue);
    }

    public void applyString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public void commitString(String key, String value) {
        edit().putString(key, value).commit();
    }

    public boolean getBoolean(String key, boolean defValue) {
        return mSP.getBoolean(key, defValue);
    }

    public void commitBoolean(String key, boolean value) {
        edit().putBoolean(key, value).commit();
    }

    public void clearObject(String key) {
        edit().remove(key).apply();
    }

    /**
     * 可以获取edit并put多种类型
     */
    public SharedPreferences.Editor edit() {
        return mSP.edit();
    }

    public void destroy() {
        mSP = null;
    }

}
