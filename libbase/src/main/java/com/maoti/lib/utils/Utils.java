package com.maoti.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLDecoder;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.core.app.ActivityCompat;

import com.lamfire.code.AES;
import com.lamfire.utils.StringUtils;
import com.maoti.lib.BaseApplication;
import com.maoti.lib.net.NetConstant;

import org.json.JSONObject;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@SuppressLint("MissingPermission")
public class Utils {
    private final static String TAG = Utils.class.getSimpleName();
    public static String id2;
    public static File file;
    private static UUID deviceUuid;


    /**
     * 拼接
     * @param imgPath
     * @param Size
     * @return
     */
    public String getImgPathSize(String imgPath,int Size){
        String SplicingPath = "?x-oss-process=image/resize,w_";
        return imgPath+SplicingPath+Size;
    }

    /**
     * 获取(版本/系统和版本/手机型号/网络类型)
     * @return
     */
    public  static String getUserAgent(){
        String UserAgent = "Android(v";
        UserAgent ="v"+ UserAgent+getBaseAppVersionName()+"/";
        UserAgent = UserAgent+"Android "+getSystemVersion()+"/";
        UserAgent = UserAgent +getSystemModel()+"/";
        UserAgent = UserAgent + NetworkUtils.getNetworkType()+")";
        return UserAgent;
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }
    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


    /**
     * 判断密码是否包含 数字跟字母一起
     * @param str
     * @return
     */
    public static boolean isPassword(String str) {
        boolean regex =true;
        String regex1 = ".*[a-zA-z].*";
        boolean result3 = str.matches(regex1);
        //【含有数字】true
        String regex2 = ".*[0-9].*";
        boolean result4 = str.matches(regex2);
        if (result3 && result4){
            regex = false;
        }
        return regex;
    }
    /**
     * 格式化手机号 170****9210
     * @param mobile
     * @return 170****9210
     */
    public static String getStarMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            if (mobile.length() >=11)
                return mobile.substring(0,3) +"****" + mobile.substring(7, mobile.length());
        }else {
            return "";
        }
        return mobile;
    }


    /**
     * 上传数据加密
     * @param json
     * @return
     */
    public static  String Encryption(JSONObject json){
        SimpleDateFormat sDateFormat   =   new   SimpleDateFormat("yyyy-MM-dd   HH:mm:ss");
        String date = sDateFormat.format(new Date());
        String mad5 = "Android"+ Utils.getBaseAppVersionName() + DateUtils.dateToStamp(date) + json.toString() + "$"+ NetConstant.Secret;
        String sign = EncryptionUtils.getMD5(EncryptionUtils.getMD5(mad5));
        String string = json.toString() +"@卍ⓛⓞⓥⓔ卐@"+sign+"@卍ⓛⓞⓥⓔ卐@"+DateUtils.dateToStamp(date);
        LogUtil.i("1111",string);
        LogUtil.i("11112",AesUtils.AES_Encrypt(string,"Dds#Safu3!4R5S*2","ECB"));

        String content =  AesUtils.AES_Encrypt(string,"Dds#Safu3!4R5S*2","ECB");
        return content;
    }


        /**
         * 正则验证手机号
         * @param mobileNums
         * @return
         */
    public static  boolean isMobileNO(String mobileNums){
        String telRegex = "^((13[0-9])|(14[0-9])|(15[0-9])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$";
        if (TextUtils.isEmpty(mobileNums))
            return false;
        else
            return mobileNums.matches(telRegex);
    }
    /**
     * nand ID
     * @return
     */
    public static String readCID() {
        String str1 = null;
        Object localOb;
        try {
            localOb = new FileReader("/sys/block/mmcblk0/device/type");
            localOb = new BufferedReader((Reader) localOb).readLine()
                    .toLowerCase().contentEquals("mmc");
            if (localOb != null) {
                str1 = "/sys/block/mmcblk0/device/";
            }
            localOb = new FileReader(str1 + "cid"); // nand ID
            str1 = new BufferedReader((Reader) localOb).readLine();
            Log.v("readDev1-->", "nid: " + str1);
        } catch (Exception e1) {
            try {
                localOb = new FileReader("/sys/ufs/ufsid");
                if (localOb != null) {
                    str1 = new BufferedReader((Reader) localOb).readLine();
                }
            } catch (Exception e2) {

            }
            e1.printStackTrace();
        }
        return str1;
    }

    /**
     * sdk卡CID
     *
     * @return
     */
    public static String readSD_CID() {
        String str1 = null;
        Object localOb;
        try {
            localOb = new FileReader("/sys/block/mmcblk1/device/type");
            localOb = new BufferedReader((Reader) localOb).readLine()
                    .toLowerCase().contentEquals("sd");
            if (localOb != null) {
                str1 = "/sys/block/mmcblk1/device/";
            }
            localOb = new FileReader(str1 + "cid"); // SD Card ID
            str1 = new BufferedReader((Reader) localOb).readLine();
            Log.v("readDev1-->", "cid: " + str1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return str1;
    }

    public static void installApk(Context context, String appPath) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        newIntent.setDataAndType(Uri.parse("file://" + appPath), "application/vnd.android.package-archive");
        context.startActivity(newIntent);

    }

    public static String getIMEI() {
//        try {
//            return ((TelephonyManager) MApp.getInstance().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "";
//        }

        String deviceId = null;
        try {
            TelephonyManager tm = (TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= 29) {//Build.VERSION_CODES.Q = 29
                deviceId = Settings.System.getString(BaseApplication.getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                // request old storage permission
                if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
//                    Toast.makeText(MApp.getInstance().getApplicationContext(),"请开启电话权限,否则部分功能无法使用",Toast.LENGTH_LONG).show();
//                    return null;
                }
                deviceId = tm.getDeviceId();
            }
            if (deviceId == null || "".equals(deviceId)) {
                return OsUtils.getMac(BaseApplication.getContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (deviceId == null || "".equals(deviceId)) {
                return OsUtils.getMac(BaseApplication.getContext());
            }
        }

        return deviceId;
    }


    public static String getIMSI() {
        String imsi = "";
        try {
            imsi = ((TelephonyManager) BaseApplication.getContext().getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(StringUtils.isEmpty(imsi)){
            imsi = OsUtils.getSimOperator(0);
        }
        if(StringUtils.isEmpty(imsi)){
            imsi = OsUtils.getSimOperator(1);
        }

        return imsi;
    }


    public static String getDeviceId() {
        String imei = ((TelephonyManager) BaseApplication.getContext().getSystemService(
                Activity.TELEPHONY_SERVICE)).getDeviceId();
        if (TextUtils.isEmpty(imei)) {
            WifiManager wifiManager = (WifiManager) BaseApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            imei = wifiManager.getConnectionInfo().getMacAddress();
        }
//		if (TextUtils.isEmpty(imei)) {
//			imei = FileUtil.getLocalDeviceIdUUID();
//		}
        return imei;
    }


    public static String getUserId() {
        String userId = "";
        String m_szDevIDShort = "";
        String serial = "";
        File file;
        try {
            m_szDevIDShort = "37" +
                    Build.BOARD.length() % 10 +
                    Build.BRAND.length() % 10 +
                    Build.CPU_ABI.length() % 10 +
                    Build.DEVICE.length() % 10 +
                    Build.DISPLAY.length() % 10 +
                    Build.HOST.length() % 10 +
                    Build.ID.length() % 10 +
                    Build.MANUFACTURER.length() % 10 +
                    Build.MODEL.length() % 10 +
                    Build.PRODUCT.length() % 10 +
                    Build.TAGS.length() % 10 +
                    Build.TYPE.length() % 10 +
                    Build.USER.length() % 10;
            serial = Build.class.getField("SERIAL").get(null).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String deviceUuid = new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (m != null) {
            m.update(deviceUuid.getBytes(), 0, deviceUuid.length());
        }
        byte p_md5Data[] = new byte[0];
        if (m != null) {
            p_md5Data = m.digest();
        }
        for (byte aP_md5Data : p_md5Data) {
            int b = (0xFF & aP_md5Data);
            if (b <= 0xF)
                userId += "0";
            userId += Integer.toHexString(b);
        }
        userId = userId.toUpperCase();
        return userId;
    }

    /**
     * 获取三段版本号
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String channelName = getApplicationMetaValue(context, "CHANNEL_NAME");
        return getBaseAppVersionName() + "." + channelName;

    }


    public static String getVersionChannel(Context context) {
        return getMetaData(context, "UMENG_CHANNEL");
    }

    /**
     * 获取app基础版本号，对应配置文件的versionName字段
     *
     * @return
     */
    public static String getBaseAppVersionName() {
        String version = "1.0.0";
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = BaseApplication.getContext().getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(BaseApplication.getContext().getPackageName(), 0);
            version = packInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取app基础版本号，对应配置文件的versionCode字段
     * @return
     */
    public static String getAppVersionCode(Context context) {
        int version = 0;
        try {
            // 获取packagemanager的实例
            PackageManager packageManager = context.getPackageManager();
            // getPackageName()是你当前类的包名，0代表是获取版本信息
            PackageInfo packInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);

            version = packInfo.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return version+"";
    }

    /***
     * 获取ip地址
     *
     * @return
     */
    public static String getLocalIpAddress() {
        String ipAddress = null;
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.getDisplayName().equals("eth0")) {
                    List<InetAddress> addresses = Collections.list(iface
                            .getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                } else if (iface.getDisplayName().equals("wlan0")) {
                    List<InetAddress> addresses = Collections.list(iface
                            .getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }

    /**
     * 获取Manifest文件中的meta信息
     */
    public static String getApplicationMetaValue(Context context,
                                                 String metaName) {
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            // return appInfo.metaData.getString(metaName);
            return appInfo.metaData.get(metaName) + "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMetaData(Context context, String metaName) {
        if (metaName == null || TextUtils.isEmpty(metaName)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            Log.d("channel1", "::packageManager:" + packageManager);
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Log.d("channel1", "::applicationInfo:" + applicationInfo);
                if (applicationInfo != null) {
                    Log.d("channel1", "::applicationInfo.metaData:" + applicationInfo.metaData);
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(metaName);
                        Log.d("channel1", "::resultData:" + resultData);
                    }
                }

            }
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return resultData;
    }

    /**
     * 跳去其他程序导航
     */
    public static void gotoOtherNavi(Context mContext, double lat, double lng) {
        try {
            Uri mUri = Uri.parse("geo:" + lat + "," + lng);
            Intent mIntent = new Intent(Intent.ACTION_VIEW, mUri);
            mContext.startActivity(mIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过uri获取文件路径
     *
     * @param mUri
     * @return
     */
    public static String getFilePath(Activity act, Uri mUri) {
        try {
            if (mUri.getScheme().equals("file")) {
                return mUri.getPath();
            } else {
                // 兼容不如编码

                String puriStr = mUri.toString();
                puriStr = URLDecoder.decode(puriStr, "utf-8");
                if ((puriStr != null) && puriStr.contains(":")) {
                    // puriStr=puriStr.replace(":", "/");
                    mUri = Uri.parse(puriStr);
                }
                return getFilePathByUri(act.getContentResolver(), mUri);
            }
        } catch (Exception ex) {
            return null;
        }
    }

    private static String getFilePathByUri(ContentResolver mContentResolver,
                                           Uri mUri) throws FileNotFoundException {
        String imgPath;
        Cursor cursor = mContentResolver
                .query(mUri, new String[]{MediaStore.Images.Media.DATA},
                        null, null, null);
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        imgPath = cursor.getString(index);
        return imgPath;
    }

    /**
     * 程序是否在前台运行
     *
     * @return boolean
     */
    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = context.getPackageName();

        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏输入框
     */
    public static void hideSoftInputMethod(View v) {
        if (v != null && v.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) BaseApplication.getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 显示输入框
     *
     * @param focusView
     */
    public static void showSoftInputMethod(View focusView) {
        focusView.requestFocus();
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(focusView, InputMethodManager.SHOW_FORCED);
    }

    private static String EmailPatter = "([a-zA-Z0-9.]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

    public static boolean isVaildEmail(String email) {
        Matcher m = Pattern.compile(EmailPatter).matcher(email);
        if (m.matches()) {
            return true;
        }
        // Pattern.matches(EmailPatter, email);
        return false;
    }

    /**
     * 判断是否安装
     * check the app is installed
     */
    public static boolean isAppInstalled(Context context, String packagename) {
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(packagename, 0);
        } catch (NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            //System.out.println("没有安装");
            return false;
        } else {
            //System.out.println("已经安装");
            return true;
        }
    }

    /**
     * 检查apk包是否可用
     *
     * @return
     */
    public static boolean isApkAvailable(String apkPath) {
        boolean result = false;
        try {
            PackageManager pm = BaseApplication.getContext().getPackageManager();
            PackageInfo info = pm.getPackageArchiveInfo(apkPath,
                    PackageManager.GET_ACTIVITIES);
            // String packageName = null;
            if (info != null) {
                result = true;
            }
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * in转px
     * @param context
     * @param inValue
     * @return
     */
    public static int inToPx(Context context, float inValue) {
        final float scale = context.getResources().getDisplayMetrics().xdpi;
        return (int) (inValue * scale);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxToDip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据年月 获取月份天数
     *
     * @param dyear
     * @param dmouth
     * @return
     */
    public static int calDayByYearAndMonth(String dyear, String dmouth) {
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy/MM");
        Calendar rightNow = Calendar.getInstance();
        try {
            rightNow.setTime(simpleDate.parse(dyear + "/" + dmouth));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 设置当前屏幕亮度值 0--255
     */
    public static void saveScreenBrightness(Context mContext, int paramInt) {
        try {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 设置当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static void setScreenMode(Context mContext, int paramInt) {
        try {
            Settings.System.putInt(mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE, paramInt);
        } catch (Exception localException) {
            localException.printStackTrace();
        }
    }

    /**
     * 获得当前屏幕亮度的模式 SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
     * SCREEN_BRIGHTNESS_MODE_MANUAL=0 为手动调节屏幕亮度
     */
    public static int getScreenMode(Context mContext) {
        int screenMode = 0;
        try {
            screenMode = Settings.System.getInt(mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Exception localException) {

        }
        return screenMode;
    }

    /**
     * 获得当前屏幕亮度值 0--255
     */
    public static int getScreenBrightness(Context mContext) {
        int screenBrightness = 255;
        try {
            screenBrightness = Settings.System.getInt(
                    mContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception localException) {

        }
        return screenBrightness;
    }


    /**
     * 打开网络
     */
    public static void openOnline() {
        // TODO Auto-generated method stub
        Intent intent = null;
        //判断手机系统的版本  即API大于10 就是3.0或以上版本
        if (Build.VERSION.SDK_INT > 10) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        BaseApplication.getContext().startActivity(intent);

    }

    /**
     * 获取apk文件路径
     *
     * @param url
     * @return
     */
    public static String getLocalPathByUrl(String url) {
        int index = url.lastIndexOf("/");
        if (index == -1)
            return null;
        String fileName = url.substring(index + 1);
        return Environment.getExternalStorageDirectory().getAbsolutePath() + "/.ab";
    }

    /***
     * 通过匹配获取是否符合正则的布尔值
     *
     * @param regular 匹配字段
     * @return
     */
    public static boolean getRegular(String regular) {
        String regEx = ".*\\d+\\D*\\d+.*";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(regular);
        return matcher.matches();
    }

    /***
     * 对比手机版本(低于421版本不执行)
     *
     * @return
     */
    public static boolean getPhoneVerson() {
        String phoneVerson = Build.VERSION.RELEASE;
        int p = Integer.parseInt(phoneVerson.replaceAll("\\.", ""));
        Log.i("phoneVerson", "------------------" + phoneVerson);
        if (p < 100) {
            p = p * 10;
        }
        if (p >= 500) {
            return true;
        }
        return false;
    }

    /**
     * 判断通知权限是否打开
     *
     * @return
     */
    public static boolean isNotificationListenEnabled(Activity activity) {
        try {
            String pkgName = activity.getPackageName();
            final String flat = Settings.Secure.getString(activity.getContentResolver(),
                    "enabled_notification_listeners");
            if (!TextUtils.isEmpty(flat)) {
                final String[] names = flat.split(":");
                for (int i = 0; i < names.length; i++) {
                    final ComponentName cn = ComponentName.unflattenFromString(names[i]);
                    if (cn != null) {
                        if (TextUtils.equals(pkgName, cn.getPackageName())) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
    //判断是否安装了微博
    public static boolean isWeiboAvilible(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.sina.weibo")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断是否安装了微信
    public static boolean isDoyinAvilible(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.ss.android.ugc.aweme")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断是否安装了微信
    public static boolean isWeixinAvilible(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mm")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //判断是否安装了qq
    public static boolean isQQAvilible(Context context) {
        try {
            final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
            List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
            if (pinfo != null) {
                for (int i = 0; i < pinfo.size(); i++) {
                    String pn = pinfo.get(i).packageName;
                    if (pn.equals("com.tencent.mobileqq")) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getDouyinVerstion() {
        try {
            if (isDoyinAvilible(BaseApplication.getContext())) {
                String pkName = BaseApplication.getContext().getPackageName();
                String versionName = BaseApplication.getContext().getPackageManager().getPackageInfo(
                        "com.ss.android.ugc.aweme", 0).versionName;
                int versionCode = BaseApplication.getContext().getPackageManager()
                        .getPackageInfo("com.ss.android.ugc.aweme", 0).versionCode;
                Log.i("getDouyinVerstion", "-pkName->" + pkName);
                Log.i("getDouyinVerstion", "-versionName->" + versionName);
                Log.i("getDouyinVerstion", "-versionCode->" + versionCode);
                return versionName;
            }
        } catch (Exception e) {
            Log.i("getDouyinVerstion", "-Exception->");
            e.printStackTrace();
            return "";
        }
        return "";
    }

    public static String getWeixinVerstion(Context context) {
        try {
            if (isWeixinAvilible(context)) {
                String pkName = context.getPackageName();
                String versionName = context.getPackageManager().getPackageInfo(
                        "com.tencent.mm", 0).versionName;
                int versionCode = context.getPackageManager()
                        .getPackageInfo("com.tencent.mm", 0).versionCode;
                Log.i("ApplicationInfo", "-pkName->" + pkName);
                Log.i("ApplicationInfo", "-versionName->" + versionName);
                Log.i("ApplicationInfo", "-versionCode->" + versionCode);
                return versionName;
            }
        } catch (Exception e) {
            Log.i("ApplicationInfo", "-Exception->");
            e.printStackTrace();
        }
        return null;
    }

    public static String getQQVerstion(Context context) {
        try {
            if (isQQAvilible(context)) {
                String pkName = context.getPackageName();
                String versionName = context.getPackageManager().getPackageInfo(
                        "com.tencent.mobileqq", 0).versionName;
                int versionCode = context.getPackageManager()
                        .getPackageInfo("com.tencent.mobileqq", 0).versionCode;
                Log.i("ApplicationInfoqq", "-pkName->" + pkName);
                Log.i("ApplicationInfoqq", "-versionName->" + versionName);
                Log.i("ApplicationInfoqq", "-versionCode->" + versionCode);
                return versionName;
            }
        } catch (Exception e) {
            Log.i("ApplicationInfo", "-Exception->");
            e.printStackTrace();
        }
        return null;
    }

    public static String getLollipopRecentTask(Context context) {
        final int PROCESS_STATE_TOP = 2;
        try {
            //通过反射获取私有成员变量processState，稍后需要判断该变量的值
            Field processStateField = RunningAppProcessInfo.class.getDeclaredField("processState");
            List<RunningAppProcessInfo> processes = ((ActivityManager) context.getSystemService(
                    Context.ACTIVITY_SERVICE)).getRunningAppProcesses();
            for (RunningAppProcessInfo process : processes) {
                //判断进程是否为前台进程
                if (process.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    int state = processStateField.getInt(process);
                    //processState值为2
                    if (state == PROCESS_STATE_TOP) {
                        String[] packname = process.pkgList;
                        return packname[0];
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String getTopActivity(Context context) {
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            //获取正在运行的task列表，其中1表示最近运行的task，通过该数字限制列表中task数目，最近运行的靠前
            List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

            if (runningTaskInfos != null && runningTaskInfos.size() != 0) {
                return (runningTaskInfos.get(0).baseActivity).getPackageName();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static int[] getWidth_Height(Activity context) {
        WindowManager manager = context.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;
        return new int[]{width, height};
    }

    public static int getWindowWidth(Context context) {
// 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenWidth = dm.widthPixels;
        return mScreenWidth;
    }

    public static int getWindowHeigh(Context context) {
// 获取屏幕分辨率
        WindowManager wm = (WindowManager) (context.getSystemService(Context.WINDOW_SERVICE));
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int mScreenHeigh = dm.heightPixels;
        return mScreenHeigh;
    }

    /**
     * 获取顶部statusBar高度
     * @param mActivity
     * @return
     */
    public static int getStatusBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("status_bar_height","dimen","android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.i("status bar>>>", "height:" + height);
        return height;
    }

    /**
     * 获取底部navigationBar高度
     * @param mActivity
     * @return
     */
    public static int getNavigationBarHeight(Activity mActivity) {
        Resources resources = mActivity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height","dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.i(TAG,"NavigationBarHeight = " + height);
        return height;
    }

    /**
     * 获取设备是否存在NavigationBar
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            //do something
        }
        return hasNavigationBar;
    }

}