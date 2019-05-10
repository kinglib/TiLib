package com.endlesscreator.titoollib.utils;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;


import com.endlesscreator.tibaselib.frame.TApp;

import java.util.List;

public class AppUtil {
    public static final String TAG = AppUtil.class.getName();

    public static final String PACK_NAME_QQ = "com.tencent.mobileqq";
    public static final String PACK_NAME_WECHAT = "com.tencent.mm";
    public static final String PACK_NAME_SINA = "com.sina.weibo";

//    /**
//     * Application中调用，解决调用安装命令在7.0报错
//     */
//    public static void init() {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
//                StrictMode.setVmPolicy(builder.build());
//            }
//        } catch (Exception e) {
//            LogUtil.e(TAG, e);
//        }
//    }
//
//    public static void install(Context aContext, String aPath) {
//        try {
//            install(aContext, new File(aPath));
//        } catch (Exception e) {
//            LogUtil.e(TAG, e);
//        }
//    }
//
//    public static void install(Context aContext, File aFile) {
//        try {
//            Intent lIntent = new Intent(Intent.ACTION_VIEW);
//            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            lIntent.setDataAndType(Uri.fromFile(aFile), "application/vnd.android.package-archive");
//            aContext.startActivity(lIntent);
//        } catch (Exception e) {
//            LogUtil.e(TAG, e);
//        }
//    }

    public static boolean canOpenApp(String aPackName) {
        try {
            PackageManager lPackageManager = TApp.getInstance().getPackageManager();
            Intent lIntent = lPackageManager.getLaunchIntentForPackage(aPackName);
            return lIntent != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean checkApp(String aPackName) {
        try {
            TApp.getInstance().getPackageManager().getApplicationInfo(aPackName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean openApp(String aPackName) {
        try {
            Intent lIntent = new Intent(Intent.ACTION_MAIN, null);
            lIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            lIntent.setPackage(aPackName);
            PackageManager lPackageManager = TApp.getInstance().getPackageManager();
            List<ResolveInfo> lApps = lPackageManager.queryIntentActivities(lIntent, 0);
            ResolveInfo lResolveInfo = lApps.iterator().next();
            if (lResolveInfo != null) {
                String lPackageName = lResolveInfo.activityInfo.packageName;
                String lName = lResolveInfo.activityInfo.name;
                ComponentName lComponentName = new ComponentName(lPackageName, lName);
                lIntent.setComponent(lComponentName);
                lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TApp.getInstance().startActivity(lIntent);
                return true;
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return false;
    }

    public static boolean openApp(String aPackName, String aClassName) {
        if (TextUtils.isEmpty(aClassName)) return openApp(aPackName);
        try {
            Intent lIntent = new Intent(Intent.ACTION_MAIN);
            lIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName(aPackName, aClassName);
            lIntent.setComponent(cn);
            lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            TApp.getInstance().startActivity(lIntent);
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return false;
    }

    /**
     * 启动到应用商店app详情界面
     * @param aPackage    目标App的包名
     * @param aMarketPackage 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    public static boolean openAppMarket(String aPackage, String aMarketPackage) {
        if (!TextUtils.isEmpty(aPackage)) {

            try {
                Uri lUri = Uri.parse("market://details?id=" + aPackage);
                Intent lIntent = new Intent(Intent.ACTION_VIEW, lUri);
                if (!TextUtils.isEmpty(aMarketPackage)) lIntent.setPackage(aMarketPackage);
                lIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TApp.getInstance().startActivity(lIntent);
                return true;
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return false;
    }

//    public static boolean startActivityNewTask(Context aContext, Intent aIntent) {
//        try {
//            aIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            aContext.startActivity(aIntent);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//
//
//    public static void openAppMarket(Context aContext, String aTargetUri) {
//        try {
//            aContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(aTargetUri)));
//        } catch (Exception e) {
//            LogUtil.e(TAG, e);
//        }
//        //aContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));// 通过GooglePlay打开
//        //aContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));// 通过应用市场打开（手机多个应用市场会出现提示选择弹窗）
//    }
//
//    public static final String MARKET_PKG_GOOGLE_PLAY = "com.android.vending";
//    public static final String MARKET_PKG_HUA_WEI = "com.huawei.appmarket";
//    public static final String MARKET_PKG_YING_YONG_BAO = "com.tencent.android.qqdownloader";
//    public static final String MARKET_PKG_QIHOO_360 = "com.qihoo.appstore";
//    public static final String MARKET_PKG_BAI_DU = "com.baidu.appsearch";
//    public static final String MARKET_PKG_XIAO_MI = "com.xiaomi.market";
//    public static final String MARKET_PKG_WAN_DOU_JIA = "com.wandoujia.phoenix2";
//    public static final String MARKET_PKG_TAO_BAO = "com.taobao.appcenter";
//    public static final String MARKET_PKG_AN_ZHUO = "com.hiapk.marketpho";
//    public static final String MARKET_PKG_AN_ZHI = "cn.goapk.market";
//
}
