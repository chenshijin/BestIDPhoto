package com.maoti.lib.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 */
public class EncryptionUtils {

    /**
     * MD5加密
     *
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(str.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    public static String md5(String inputStr) {
        String md5Str = inputStr;
        try {
            if(inputStr != null) {
                MessageDigest md = MessageDigest.getInstance("MD5");
                md.update(inputStr.getBytes());
                BigInteger hash = new BigInteger(1, md.digest());
                md5Str = hash.toString(16);
                if((md5Str.length() % 2) != 0) {
                    md5Str = "0" + md5Str;
                }
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return inputStr;
    }

    /**
     * 字符Base64加密
     *
     * @param str
     * @return
     */
    public static String encodeToString(String str) {
        try {
            return Base64.encodeToString(str.getBytes("UTF-8"), Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 字符Base64解密
     *
     * @param str
     * @return
     */
    public static String decodeToString(String str) {
        try {
            return new String(Base64.decode(str.getBytes("UTF-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "";
    }
}
