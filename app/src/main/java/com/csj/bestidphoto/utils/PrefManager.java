package com.csj.bestidphoto.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.csj.bestidphoto.MApp;


public class PrefManager {

    /**
     * 统一的SharedPreferences接口
     *
     * @return SharedPreferences
     */
    public static SharedPreferences getSharedPreferences() {
        return MApp.getInstance().getSharedPreferences("settings", 0);
    }

    /**
     * 统一的Editor接口
     *
     * @return Editor
     */
    public static Editor getSharedPreferencesEditor() {

        return getSharedPreferences().edit();
    }

    /**
     * 读取Preferences的int型数据
     *
     * @param strKey   关键字
     * @param nDefault 默认值
     * @return 返回读取的数据
     */
    public static int getPrefInt(String strKey, int nDefault) {
        SharedPreferences preference = getSharedPreferences();
        int nRet = preference.getInt(strKey, nDefault);
        return nRet;
    }

    /**
     * 设置Preferences的int型数据
     *
     * @param strKey 关键字
     * @param nValue 值
     */
    public static void setPrefInt(String strKey, int nValue) {
        SharedPreferences preference = getSharedPreferences();
        preference.edit().putInt(strKey, nValue).commit();
    }

    /**
     * 读取Preferences的String型数据
     *
     * @param strKey     关键字
     * @param strDefault 默认值
     * @return 返回读取的数据
     */
    public static String getPrefString(String strKey, String strDefault) {
        SharedPreferences preference = getSharedPreferences();
        String strRet = preference.getString(strKey, strDefault);
        return strRet;
    }

    /**
     * 设置Preferences的String型数据
     *
     * @param strKey   关键字
     * @param strValue 值
     */
    public static void setPrefString(String strKey, String strValue) {
        SharedPreferences preference = getSharedPreferences();
        preference.edit().putString(strKey, strValue).commit();
    }

    /**
     * 读取Preferences的boolean型数据
     *
     * @param strKey   关键字
     * @param bDefault 默认值
     * @return 返回读取的数据
     */
    public static boolean getPrefBoolean(String strKey, boolean bDefault) {
        SharedPreferences preference = getSharedPreferences();
        boolean bRet = preference.getBoolean(strKey, bDefault);
        return bRet;
    }

    /**
     * 设置Preferences的boolean型数据
     *
     * @param strKey 关键字
     * @param bValue 值
     */
    public static void setPrefBoolean(String strKey, boolean bValue) {
        SharedPreferences preference = getSharedPreferences();
        preference.edit().putBoolean(strKey, bValue).commit();
    }

    /**
     * 读取Preferences的float型数据
     *
     * @param strKey   关键字
     * @param fDefault 默认值
     * @return 返回读取的数据
     */
    public static float getPrefFloat(String strKey, float fDefault) {
        SharedPreferences preference = getSharedPreferences();
        float fRet = preference.getFloat(strKey, fDefault);
        return fRet;
    }

    /**
     * 设置Preferences的float型数据
     *
     * @param strKey 关键字
     * @param fValue 值
     */
    public static void setPrefFloat(String strKey, float fValue) {
        SharedPreferences preference = getSharedPreferences();
        preference.edit().putFloat(strKey, fValue).commit();
    }

    /**
     * 读取Preferences的long型数据
     *
     * @param strKey   关键字
     * @param lDefault 默认值
     * @return 返回读取的数据
     */
    public static long getPrefLong(String strKey, long lDefault) {
        SharedPreferences preference = getSharedPreferences();
        long lRet = preference.getLong(strKey, lDefault);
        return lRet;
    }

    /**
     * 设置Preferences的long型数据
     *
     * @param strKey 关键字
     * @param lValue 值
     */
    public static void setPrefLong(String strKey, long lValue) {
        SharedPreferences preference = getSharedPreferences();
        preference.edit().putLong(strKey, lValue).commit();
    }

    public static boolean containKey(String strKey) {
        SharedPreferences preference = getSharedPreferences();
        return preference.contains(strKey);
    }


}
