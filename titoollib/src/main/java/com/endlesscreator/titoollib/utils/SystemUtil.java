package com.endlesscreator.titoollib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Surface;

import com.endlesscreator.tibaselib.frame.TApp;
import com.endlesscreator.titoollib.data.SPManager;

import java.lang.reflect.Method;
import java.util.List;

import static android.content.Context.ACTIVITY_SERVICE;

public class SystemUtil {

    private static final String TAG = SystemUtil.class.getName();
    private static String mOnlyFlag, mPackName, mIMEI0, mIMEI1;


    //获取屏幕方向
    public static int getScreenOrientation(Activity aActivity) {
        int lRotation = aActivity.getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics lDM = new DisplayMetrics();
        aActivity.getWindowManager().getDefaultDisplay().getMetrics(lDM);
        int lWidth = lDM.widthPixels;
        int lHeight = lDM.heightPixels;
        int lOrientation;
        // if the device's natural orientation is portrait:
        if ((lRotation == Surface.ROTATION_0 || lRotation == Surface.ROTATION_180) && lHeight > lWidth || (lRotation == Surface.ROTATION_90 || lRotation == Surface.ROTATION_270) && lWidth > lHeight) {
            switch (lRotation) {
                case Surface.ROTATION_0:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        } else {// if the device's natural orientation is landscape or if the device is square:
            switch (lRotation) {
                case Surface.ROTATION_0:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    lOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }
        return lOrientation;
    }

    /**
     * 设备标识：获取手机AndroidID
     * 在设备首次启动时，系统会随机生成一个64位的数字，并把这个数字以16进制字符串的形式保存下来。不需要权限，平板设备通用。
     * 获取成功率也较高，缺点是设备恢复出厂设置会重置。另外就是某些厂商的低版本系统会有bug，返回的都是相同的AndroidId。
     */
    public static String getAndroidID() {
        try {
            return Settings.System.getString(TApp.getInstance().getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    /**
     * 设备标识：获取手机SN（Serial Number）
     * Android系统2.3版本以上可以通过下面的方法得到Serial Number，且非手机设备也可以通过该接口获取。
     * 不需要权限，通用性也较高，但我测试发现红米手机返回的是 0123456789ABCDEF 明显是一个顺序的非随机字符串。也不一定靠谱。
     */
    public static String getSN() {
        try {
            return Build.SERIAL;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    /**
     * 设备唯一标识方案：
     * AndroidId 和 Serial Number 的通用性都较好，并且不受权限限制，
     * 如果刷机和恢复出厂设置会导致设备标识符重置这一点可以接受的话，
     * 那么将他们组合使用时，唯一性就可以应付绝大多数设备了。
     * 优化一下。直接暴露用户的设备信息并不是一个好的选择，既然需要的只是一个唯一标识，那么将他们转化成Md5即可，格式也更整齐。
     * <p>
     * 其他方法:经常被提到的还有下面几个
     * 1，手机IMEI号（DeviceId） – 需要动态权限: android.permission.READ_PHONE_STATE。 它有3个缺点:
     * 需要android.permission.READ_PHONE_STATE权限，它在6.0+系统中是需要动态申请的。如果需求要求App启动时上报设备标识符的话，那么第一会影响初始化速度，第二还有可能被用户拒绝授权。
     * android系统碎片化严重，有的手机可能拿不到DeviceId，会返回null或者000000。
     * 这个方法是只对有电话功能的设备有效的，在pad上不起作用。 可以看下方法注释
     * Returns the unique device ID, for example, the IMEI for GSM and the MEID
     * or ESN for CDMA phones. Return null if device ID is not available.
     * <p>Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE}
     * <p>
     * 2，Mac地址 – 属于Android系统的保护信息，高版本系统获取的话容易失败，返回0000000；
     * 3，SimSerialNum – 显而易见，只能用在插了Sim的设备上，通用性不好。而且需要android.permission.READ_PHONE_STATE权限
     */
    public static String getOnlyFlag() {
        if (TextUtils.isEmpty(mOnlyFlag)) {
            SPManager lSpManager = new SPManager();
            mOnlyFlag = lSpManager.getString("OnlyFlag");
            if (TextUtils.isEmpty(mOnlyFlag)) {
                String lSourceOnlyFlag = getAndroidID() + "_" + getSN();
                LogUtil.i(TAG, "source lSourceOnlyFlag = " + lSourceOnlyFlag);
                mOnlyFlag = EncodeUtil.encodeMD5(lSourceOnlyFlag);
                lSpManager.applyString("OnlyFlag", mOnlyFlag);
            }
            lSpManager.destroy();
        }
        return mOnlyFlag;
    }


    /**
     * @param slotId slotId为卡槽Id，值为 0、1
     * 手机IMEI号（DeviceId） – 需要动态权限: android.permission.READ_PHONE_STATE {@link android.Manifest.permission#READ_PHONE_STATE}。
     */
    public static String getIMEI(int slotId) {
        if (slotId == 0 && !TextUtils.isEmpty(mIMEI0)) {
            return mIMEI0;
        }
        if (slotId == 1 && !TextUtils.isEmpty(mIMEI1)) {
            return mIMEI1;
        }
        try {
            TelephonyManager manager = (TelephonyManager) TApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            Method method = manager.getClass().getMethod("getImei", int.class);
            String imei = (String) method.invoke(manager, slotId);
            switch (slotId) {
                case 0:
                    mIMEI0 = imei;
                    break;
                case 1:
                    mIMEI1 = imei;
                    break;
            }
            return imei;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getPackName() {
        if (TextUtils.isEmpty(mPackName)) {
            mPackName = TApp.getInstance().getPackageName();
        }
        return mPackName;
    }

    public static void initCamera() {
        // android 7.0系统解决拍照的问题
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }

    public static String CLIPBOARD_LABEL = "Label";
    public static String CLIPBOARD_PROMPT = "已复制到粘贴板";

    /**
     * 复制文本到粘贴板，并提示
     */
    public static boolean copyText(String aCopyText) {
        return copyText(CLIPBOARD_LABEL, aCopyText, CLIPBOARD_PROMPT);
    }

    /**
     * 复制文本到粘贴板，并提示
     */
    public static boolean copyText(String aCopyText, String aShowInfo) {
        return copyText(CLIPBOARD_LABEL, aCopyText, aShowInfo);
    }

    /**
     * 复制文本到粘贴板，不提示
     */
    public static boolean copyTextSilent(String aCopyText) {
        return copyText(CLIPBOARD_LABEL, aCopyText, null);
    }

    /**
     * 复制文本到粘贴板
     */
    public static boolean copyText(String aLabel, String aCopyText, String aShowInfo) {
        ClipboardManager lManager = (ClipboardManager) TApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
        if (lManager != null) {
            lManager.setPrimaryClip(ClipData.newPlainText(aLabel, aCopyText));
            if (!TextUtils.isEmpty(aShowInfo)) InfoUtil.INSTANCE.show(aShowInfo);
            return true;
        }
        return false;
    }

    /**
     * 读取粘贴板文本
     */
    public static CharSequence readClipboardText() {
        try {
            ClipboardManager lManager = (ClipboardManager) TApp.getInstance().getSystemService(Context.CLIPBOARD_SERVICE);
            if (lManager != null) {
                return lManager.getPrimaryClip().getItemAt(0).getText();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return null;
    }

    /**
     * 打开QQ临时会话（需要指定QQ开启临时会话权限）
     */
    public static boolean openQQTmpSession(String aQQNum) {
        if (TextUtils.isEmpty(aQQNum)) return false;
        return openUri("mqqwpa://im/chat?chat_type=wpa&uin=" + aQQNum);
    }

    /**
     * 打开QQ临时会话（需要指定QQ开启临时会话权限）
     */
    public static boolean openUri(String aUri) {
        if (!TextUtils.isEmpty(aUri)) {
            try {
                Intent lIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(aUri));
                lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                TApp.getInstance().startActivity(lIntent);
                return true;
            } catch (Exception e) {
                LogUtil.e(TAG, e);
            }
        }
        return false;
    }


    /**
     * 将本应用置顶到最前端
     */
    public static void setTopApp() {
        try {
            //获取ActivityManager
            ActivityManager activityManager = (ActivityManager) TApp.getInstance().getSystemService(ACTIVITY_SERVICE);
            //获得当前运行的task(任务)
            List<ActivityManager.RunningTaskInfo> taskInfoList = activityManager.getRunningTasks(100);
            String packageName = TApp.getInstance().getPackageName();
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                //找到本应用的 task，并将它切换到前台
                if (taskInfo.topActivity.getPackageName().equals(packageName)) {
                    activityManager.moveTaskToFront(taskInfo.id, 0);
                    break;
                }
            }
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
    }


    public static boolean shareText(Context aContext, String aText, String aActivityTitle) {
        if (TextUtils.isEmpty(aText)) {
            return false;
        }
        try {
            Intent lIntent = new Intent(Intent.ACTION_SEND);
            lIntent.setType("text/plain");
            lIntent.putExtra(Intent.EXTRA_TEXT, aText);
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            aContext.startActivity(Intent.createChooser(lIntent, aActivityTitle));
            return true;
        } catch (Exception e) {
            LogUtil.e(TAG, e);
        }
        return false;
    }


//    public static void openApp(String aPackName) {
//
//
//        //同AndroidManifest中主入口Activity一样
//        Intent intent = new Intent(Intent.ACTION_MAIN, null);
//        intent.addCategory(Intent.CATEGORY_LAUNCHER);
//
//        //得到一个PackageManager的对象
//        PackageManager packageManager = TApp.getInstance().getApplicationContext().getPackageManager();
//        //获取到主入口的Activity集合
//        List<ResolveInfo> mlist = packageManager.queryIntentActivities(intent, 0);
//
//        Collections.sort(mlist, new ResolveInfo.DisplayNameComparator(packageManager));
//        for (ResolveInfo res : mlist) {
//            String pkg = res.activityInfo.packageName;
//            String cls = res.activityInfo.name;
//            if (pkg.contains("com.tencent.mm")) {
//                ComponentName componentName = new ComponentName(pkg, cls);
//                Intent intent1 = new Intent();
//                intent1.setComponent(componentName);
//                intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                TApp.getInstance().startActivity(intent1);
//            }
//        }
//    }
}
