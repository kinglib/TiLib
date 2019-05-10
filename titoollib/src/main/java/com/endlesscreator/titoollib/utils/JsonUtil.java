package com.endlesscreator.titoollib.utils;

import android.content.res.AssetManager;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.endlesscreator.tibaselib.frame.TApp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class JsonUtil {
    private static final String TAG = JsonUtil.class.getName();

    /**
     * 解析到实体类
     */
    public static <T> T parse(String aJson, Class<T> aClazz) {
        if (!TextUtils.isEmpty(aJson)) {
            try {
                return JSON.parseObject(aJson, aClazz);
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return null;
    }

    /**
     * 解析Json类型为List嵌套实体类的
     */
    public static <T> List<T> parseList(String aJson, Class<T> aClazz) {
        try {
            return JSON.parseArray(aJson, aClazz);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    /**
     * 获取assets目录下的json文件数据
     */
    public static String getAssetsJson(String aFileName) {
        StringBuilder lSB = new StringBuilder();
        try {
            AssetManager assetManager = TApp.getInstance().getAssets();
            BufferedReader lBuffer = new BufferedReader(new InputStreamReader(assetManager.open(aFileName)));
            String lLine;
            while ((lLine = lBuffer.readLine()) != null) {
                lSB.append(lLine);
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return lSB.toString();
    }

    public static String toJson(Object aBean) {
        try {
            return JSON.toJSONString(aBean);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }
}
