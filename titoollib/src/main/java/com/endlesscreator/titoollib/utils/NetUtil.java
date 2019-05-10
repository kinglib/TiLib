package com.endlesscreator.titoollib.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.endlesscreator.tibaselib.frame.TApp;


public class NetUtil {
    private static final String TAG = NetUtil.class.getName();

    /**
     * @return 返回是否联网的状态，返回null为获取失败，失败可能会是权限（读取网络状态）问题
     */
    public static Boolean isConnected() {
        try {
            ConnectivityManager lConnectivityManager = (ConnectivityManager) TApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (lConnectivityManager != null) {
                NetworkInfo lNetworkInfo = lConnectivityManager.getActiveNetworkInfo();
                return lNetworkInfo != null && lNetworkInfo.isAvailable(); // lNetworkInfo为null说明无网
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    /**
     * 一般权限问题会导致 {@link #isConnected()} 返回null，此时不能当作无网处理，应该尝试请求。
     *
     * @return 成功获取网络状态为未联网 则返回 true, 否则 返回 false
     */
    public static boolean isNotConnected() {
        Boolean lNetworkConnected = isConnected();
        return lNetworkConnected != null && !lNetworkConnected;
    }

    /**
     * 是否已连接网络且连接类型不是wifi，一般用来判断是否为使用移动网络
     *
     * @return 若无法获取网络状态，则当作wifi已连接处理
     */
    public static boolean isConnectedNotWifi() {
        try {
            ConnectivityManager lConnectivityManager = (ConnectivityManager) TApp.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
            if (lConnectivityManager != null) {
                NetworkInfo lNetworkInfo = lConnectivityManager.getActiveNetworkInfo();
                return lNetworkInfo != null && lNetworkInfo.isAvailable() && lNetworkInfo.getType() != ConnectivityManager.TYPE_WIFI;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return false;
    }
}
