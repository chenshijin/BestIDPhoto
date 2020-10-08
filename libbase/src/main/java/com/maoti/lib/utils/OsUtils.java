package com.maoti.lib.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lamfire.utils.StringUtils;
import com.maoti.lib.BaseApplication;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class OsUtils {

    private static final String TAG = OsUtils.class.getSimpleName();

    public OsUtils() {
    }

    public static String getProcessName(Context cxt, int pid) {
        String def = "";
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return def;
        } else {
            Iterator var5 = runningApps.iterator();

            ActivityManager.RunningAppProcessInfo procInfo;
            do {
                if (!var5.hasNext()) {
                    return def;
                }

                procInfo = (ActivityManager.RunningAppProcessInfo) var5.next();
            } while (procInfo.pid != pid);

            return procInfo.processName;
        }
    }

    /**
     * 判断是否已安装指定包名的APP
     *
     * @param packagename
     * @return
     */
    public static boolean isAppInstalled(String packagename) {
        PackageManager pm = BaseApplication.getContext().getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }


    /**
     * 这是使用adb shell命令来获取mac地址的方式(通过eth)
     *
     * @return
     */

    public static String getMacEth() {

        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/eth0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }

        } catch (IOException ex) {

            // 赋予默认值
            ex.printStackTrace();

        }

        if (StringUtils.isEmpty(macSerial)) {
            macSerial = getMacWlan();
        }

        return macSerial.toUpperCase();

    }

    /**
     * 这是使用adb shell命令来获取mac地址的方式 (通过wlan)
     *
     * @return
     */
    public static String getMacWlan() {

        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec("cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }

        } catch (IOException ex) {

            // 赋予默认值
            ex.printStackTrace();

        }

        return macSerial;

    }

    /**
     * 2018年6月25日 作者：csj 方法描述：获取MAC地址 23 == 23 24==24
     *
     * @param context
     * @return
     */
    public static String getMac(Context context) {

        String strMac = null;

        if (Build.VERSION.SDK_INT < 23) {
            Log.e("=====", "6.0以下");
            strMac = getLocalMacAddressFromWifiInfo(context);
        } else if (Build.VERSION.SDK_INT < 24 && Build.VERSION.SDK_INT >= 23) {
            Log.e("=====", "6.0以上7.0以下");
            strMac = getMacAddress(context);
        } else if (Build.VERSION.SDK_INT >= 24) {
            Log.e("=====", "7.0以上");
            if (!StringUtils.isEmpty(getMacAddress())) {
                Log.e("=====", "7.0以上1");
                strMac = getMacAddress();
            } else if (!StringUtils.isEmpty(getMachineHardwareAddress())) {
                Log.e("=====", "7.0以上2");
                strMac = getMachineHardwareAddress();
            } else {
                Log.e("=====", "7.0以上3");
                strMac = getLocalMacAddressFromBusybox();
            }
        }

        return strMac;
    }

    // ******************************************************************************************************************

    /**
     * 根据wifi信息获取本地mac (适用6.0以下 需要权限 <uses-permission
     * android:name="android.permission.ACCESS_WIFI_STATE" />)
     *
     * @param context
     * @return
     */
    public static String getLocalMacAddressFromWifiInfo(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo winfo = wifi.getConnectionInfo();
        String mac = winfo.getMacAddress();
        return mac;
    }

    // **************************************************************************************************************************

    /**
     * android 6.0及以上、7.0以下 获取mac地址
     *
     * @param context
     * @return
     */
    public static String getMacAddress(Context context) {

        // 如果是6.0以下，直接通过wifimanager获取
        if (Build.VERSION.SDK_INT < 23) {
            String macAddress0 = getMacAddress0(context);
            if (!StringUtils.isEmpty(macAddress0)) {
                return macAddress0;
            }
        }

        String str = "";
        String macSerial = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (Exception ex) {
            Log.e("----->" + "NetInfoManager",
                    "getMacAddress:" + ex.toString());
        }
        if (macSerial == null || "".equals(macSerial)) {
            try {
                return loadFileAsString("/sys/class/net/eth0/address")
                        .toUpperCase().substring(0, 17);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("----->" + "NetInfoManager",
                        "getMacAddress:" + e.toString());
            }

        }
        return macSerial;
    }

    private static String getMacAddress0(Context context) {
        if (isAccessWifiStateAuthorized(context)) {
            WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = null;
            try {
                wifiInfo = wifiMgr.getConnectionInfo();
                return wifiInfo.getMacAddress();
            } catch (Exception e) {
                Log.e("----->" + "NetInfoManager", "getMacAddress0:" + e.toString());
            }

        }
        return "";

    }

    /**
     * Check whether accessing wifi state is permitted
     *
     * @param context
     * @return
     */
    private static boolean isAccessWifiStateAuthorized(Context context) {
        if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission("android.permission.ACCESS_WIFI_STATE")) {
            Log.e("----->" + "NetInfoManager", "isAccessWifiStateAuthorized:" + "access wifi state is enabled");
            return true;
        } else
            return false;
    }

    private static String loadFileAsString(String fileName) throws Exception {
        FileReader reader = new FileReader(fileName);
        String text = loadReaderAsString(reader);
        reader.close();
        return text;
    }

    private static String loadReaderAsString(Reader reader) throws Exception {
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[4096];
        int readLength = reader.read(buffer);
        while (readLength >= 0) {
            builder.append(buffer, 0, readLength);
            readLength = reader.read(buffer);
        }
        return builder.toString();
    }

    // *******************************************************************************************************************

    /**
     * 根据IP地址获取MAC地址
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static String getMacAddress() {
        String strMacAddr = null;
        try {
            // 获得IpD地址
            InetAddress ip = getLocalInetAddress();
            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }
                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {

        }

        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    private static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            // 列举
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {// 是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface
                        .nextElement();// 得到下一个元素
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();// 得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress()
                            && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }

    /**
     * 获取本地IP
     *
     * @return
     */
    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    // ***********************************************************************************************************************
    /**
     * android 7.0及以上 （2）扫描各个网络接口获取mac地址
     *
     */
    /**
     * 获取设备HardwareAddress地址
     *
     * @return
     */
    @SuppressLint("NewApi")
    public static String getMachineHardwareAddress() {
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        String hardWareAddress = null;
        NetworkInterface iF = null;
        if (interfaces == null) {
            return null;
        }
        while (interfaces.hasMoreElements()) {
            iF = interfaces.nextElement();
            try {
                hardWareAddress = bytesToString(iF.getHardwareAddress());
                if (hardWareAddress != null)
                    break;
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        return hardWareAddress;
    }

    /***
     * byte转为String
     *
     * @param bytes
     * @return
     */
    private static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        for (byte b : bytes) {
            buf.append(String.format("%02X:", b));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    // *********************************************************************************************************************
    /**
     * android 7.0及以上 （3）通过busybox获取本地存储的mac地址
     *
     */

    /**
     * 根据busybox获取本地Mac
     *
     * @return
     */
    public static String getLocalMacAddressFromBusybox() {
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig", "HWaddr");
        // 如果返回的result == null，则说明网络不可取
        if (result == null) {
            return "网络异常";
        }
        // 对该行数据进行解析
        // 例如：eth0 Link encap:Ethernet HWaddr 00:16:E8:3E:DF:67
        if (result.length() > 0 && result.contains("HWaddr") == true) {
            Mac = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            while ((line = br.readLine()) != null && line.contains(filter) == false) {
                result += line;
            }

            result = line;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 或取本机的ip地址  (适用无线网络WIFI)
     */
    public static String getlocalip() {
        WifiManager wifiManager = (WifiManager) BaseApplication.getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        // Log.d(Tag, "int ip "+ipAddress);
        if (ipAddress == 0)
            return null;
        return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
    }

    /**
     * 2018年6月27日
     * 作者：csj
     * 方法描述：获取当前连接网络的Ip地址
     *
     * @return
     * @throws SocketException
     */
    public static String getLocalIPAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.i(TAG, "ex" + ex.toString());
        }

        return "null";
    }

//	***********************************************************************************************************************************

    /**
     * 2018年6月27日
     * 作者：csj
     * 方法描述：获取当前连接网络的类型
     *
     * @return
     */
    public static NetState getCurrentNetWorkState() {
        ConnectivityManager mConnManager = (ConnectivityManager) BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mInfo = mConnManager.getActiveNetworkInfo();
        if (null == mInfo)
            return NetState.NULL;
        Log.e(TAG, mInfo.getTypeName());
        if (mInfo.isAvailable() && mInfo.isConnected() && mInfo.getTypeName().equals("WIFI"))
            return NetState.WIFI;
        else if (mInfo.isAvailable() && mInfo.isConnected() && mInfo.getTypeName().equals("ETHERNET"))// ETHERNET
            return NetState.ETH;
        return NetState.NULL;
    }

    public enum NetState {NULL, WIFI, ETH}

    /**
     * 2018年6月28日
     * 作者：csj
     * 方法描述：判断服务是否在运行
     *
     * @param context
     * @param ServiceName
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (("").equals(ServiceName) || ServiceName == null)
            return false;
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager.getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().contains(ServiceName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * (第一种，部分机型不可行)
     * 手机是否开启位置服务，如果没有开启那么所有app将不能使用定位功能
     */
    public static boolean isLocServiceEnable(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    /**
     * （第二种，兼容所有机型）
     * 判断位置服务是否打开
     *
     * @return
     */
    public static boolean isLocationEnabled() {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            try {
                locationMode = Settings.Secure.getInt(BaseApplication.getContext().getContentResolver(), Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } else {
            locationProviders = Settings.Secure.getString(BaseApplication.getContext().getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !StringUtils.isEmpty(locationProviders);
        }
    }

    /**
     * 直接跳转至位置信息设置界面
     */
    public static void openLocation(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 让用户去打开wifi
     */
    public static void openWifi1(Context context) {
        //第一种
//      Intent intent = new Intent();
//      intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
//      startActivity(intent);

        //第二种
//      Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
//      startActivity(wifiSettingsIntent);

        //第三种
//      Intent intent = new Intent();
//      if(android.os.Build.VERSION.SDK_INT >= 11){
//          //Honeycomb
//          intent.setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");
//       }else{
//          //other versions
//           intent.setClassName("com.android.settings", "com.android.settings.wifi.WifiSettings");
//       }
//       startActivity(intent);
        //第四种
        Intent wifiSettingsIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);//"android.settings.WIFI_SETTINGS"
        wifiSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
        context.startActivity(wifiSettingsIntent);
    }

    /**
     * 反射获取 getSubscriberId ，既imsi  (应该不适用于Android Q了)
     *
     * @param subId
     * @return
     */
    public static String getSubscriberId(int subId) {
        TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.getContext().getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
        Class<?> telephonyManagerClass = null;
        String imsi = null;
        try {
            telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", int.class);
                imsi = (String) method.invoke(telephonyManager, subId);
            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                Method method = telephonyManagerClass.getMethod("getSubscriberId", long.class);
                imsi = (String) method.invoke(telephonyManager, (long) subId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i(TAG, "IMSI==" + imsi);
        return imsi;
    }

    /**
     * 反射获取 getSubscriptionId ，既 subid  (应该不适用于Android Q了)
     *
     * @param slotId 卡槽位置（0，1）
     * @return
     */
    public static int getSubscriptionId(int slotId) {
        try {
            Method datamethod;
            int setsubid = -1;//定义要设置为默认数据网络的subid
            //获取默认数据网络subid   getDefaultDataSubId
            Class<?> SubscriptionManager = Class.forName("android.telephony.SubscriptionManager");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { // >= 24  7.0
                datamethod = SubscriptionManager.getDeclaredMethod("getDefaultDataSubscriptionId");
            } else {
                datamethod = SubscriptionManager.getDeclaredMethod("getDefaultDataSubId");
            }
            datamethod.setAccessible(true);
            int SubId = (int) datamethod.invoke(SubscriptionManager);


            Method subManagermethod = SubscriptionManager.getDeclaredMethod("from", Context.class);
            subManagermethod.setAccessible(true);
            Object subManager = subManagermethod.invoke(SubscriptionManager, BaseApplication.getContext());

            //getActiveSubscriptionInfoForSimSlotIndex  //获取卡槽0或者卡槽1  可用的subid
            Method getActivemethod = SubscriptionManager.getDeclaredMethod("getActiveSubscriptionInfoForSimSlotIndex", int.class);
            getActivemethod.setAccessible(true);
            Object msubInfo = getActivemethod.invoke(subManager, slotId);  //getSubscriptionId

            Class<?> SubInfo = Class.forName("android.telephony.SubscriptionInfo");

            //slot0   获取卡槽0的subid
            int subid = -1;
            if (msubInfo != null) {
                Method getSubId0 = SubInfo.getMethod("getSubscriptionId");
                getSubId0.setAccessible(true);
                subid = (int) getSubId0.invoke(msubInfo);
            }
            Log.i(TAG, "slotId=" + slotId + ", subid=" + subid);
            return subid;
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage());
        }
        return -1;
    }

    /**
     * 获取运营商 IMSI (应该不适用于Android Q了)
     * 默认为 IMEI1对应的 IMSI
     *
     * @return
     */
    public static String getSimOperator() {
        TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.getContext().getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
        return telephonyManager.getSimOperator();
    }

    /**
     * 根据卡槽位置 获取运营商 IMSI (应该不适用于Android Q了)
     *
     * @param slotId 卡槽位置（0，1）
     * @return
     */
    public static String getSimOperator(int slotId) {
        int subid = getSubscriptionId(slotId);
        if (subid == -1) {
            return "";
        }

        String imsi = getSubscriberId(subid);
        if (!StringUtils.isEmpty(imsi)) {
            return imsi;
        }

        return "";
    }

    /**
     * 通过卡槽位置拿 IMEI(应该不适用于Android Q了)
     *
     * @param slotId (0, 1卡槽位置）
     * @return
     */
    public static String getImei(int slotId) {
        if (slotId != 0 && slotId != 1) {
            return null;
        }

        TelephonyManager tm = (TelephonyManager) BaseApplication.getContext().getSystemService(TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(BaseApplication.getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            return tm.getDeviceId(slotId);

        } else if (slotId == 0){
            return tm.getDeviceId();

        } else {
            TelephonyManager telephonyManager = (TelephonyManager) BaseApplication.getContext().getSystemService(TELEPHONY_SERVICE);// 取得相关系统服务
            Class<?> telephonyManagerClass = null;
            String imei = null;

            try {
                telephonyManagerClass = Class.forName("android.telephony.TelephonyManager");
                Method method = telephonyManagerClass.getMethod("getImei", int.class);
                imei = (String) method.invoke(telephonyManager, slotId);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.i(TAG, "imei==" + imei);

            return imei;
        }
    }

    /**
     * 跳转系统设置页
     * @param ctx
     */
    public static void toSetting(Context ctx){
        Intent intent =  new Intent(Settings.ACTION_SETTINGS);
        if(!(ctx instanceof Activity)){
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ctx.startActivity(intent);
    }

}
