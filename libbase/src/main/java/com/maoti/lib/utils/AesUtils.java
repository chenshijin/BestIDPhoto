package com.maoti.lib.utils;


import com.lamfire.code.Base64;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author kenny
 * @date 2020/6/6
 */
public class AesUtils {

    /**
     * 加密
     *
     * @param content  待加密内容
     * @param password 加密密钥
     * @return
     */
    public static byte[] encrypt(String content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] result = cipher.doFinal(byteContent);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解密
     *
     * @param content  待解密内容
     * @param password 解密密钥
     * @return
     */
    public static byte[] decrypt(byte[] content, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128, new SecureRandom(password.getBytes()));
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] result = cipher.doFinal(content);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * AES加密
     *
     * @param plaintext   明文
     * @param Key         密钥
     * @param EncryptMode AES加密模式，CBC或ECB
     * @return 该字符串的AES密文值
     */
    public static String AES_Encrypt(Object plaintext, String Key, String EncryptMode) {
        String PlainText = null;
        try {
            PlainText = plaintext.toString();
            if (Key == null) {
                return null;
            }
            byte[] raw = Key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/" + EncryptMode + "/PKCS5Padding");
            if (EncryptMode == "ECB") {
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            } else {
                IvParameterSpec iv = new IvParameterSpec(Key.getBytes("utf-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
            }
            byte[] encrypted = cipher.doFinal(PlainText.getBytes("utf-8"));
            String encryptedStr = new String(Base64.encode(encrypted));
            return encryptedStr;
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    /**
     * AES解密
     *
     * @param cipertext   密文
     * @param Key         密钥
     * @param EncryptMode AES加密模式，CBC或ECB
     * @return 该密文的明文
     */
    public static String AES_Decrypt(Object cipertext, String Key, String EncryptMode) {
        String CipherText = null;
        try {
            CipherText = cipertext.toString();
            byte[] raw = Key.getBytes("utf-8");
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/" + EncryptMode + "/PKCS5Padding");
            if (EncryptMode == "ECB") {
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            } else {
                IvParameterSpec iv = new IvParameterSpec(Key.getBytes("utf-8"));//使用CBC模式，需要一个向量iv，可增加加密算法的强度
                cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            }
            byte[] encrypted1 = Base64.decode(CipherText);//先用base64解密
            try {
                byte[] original = cipher.doFinal(encrypted1);
                String originalString = new String(original, "utf-8");
                return originalString;
            } catch (Exception e) {
                System.out.println(e.toString());
                return null;
            }
        } catch (Exception ex) {
            System.out.println(ex.toString());
            return null;
        }
    }

    //编码函数
    public static String encode(String content, String key) throws Exception {
        byte[] encrypt = encrypt(content, key);
        return Base64.encode(encrypt);
    }

    //解码函数
    public static String decode(String encode, String key) throws Exception {
        byte[] encrypt = Base64.decode(encode);
        byte[] content = decrypt(encrypt, key);
        return new String(content);
    }

    //0-正常使用
    public static void main(String[] args) throws Exception {
        String content = "PrM2plKq4ZnW1l0YgrvSPlOjpnBPikkSdQYFcaXtTLJjDTfdzK0XpMZKV/le8KlXnYZhVW8hFL/jSafDycilJvbyfi70nqSqh9dlMEwiAtMR7QlL8SX3RneC/rm/30BN6vle3u19QTLlczKHCVYl98S9cJ4+RGYPZYUFOkIsahg9xFQm7vzesyV+zfcuUG3+HoPSORQt1aU/q5fU9GH6EP1t2C9AUDA0eXdnUyIVZHigzjigc98mXK5K9VxFZrxRHXfpaQH1M107PUkLcLOlq6cl6WWNZn327ItqxDzy+ssEzdA2NU9S4HEbEEuqxI375/GaM6gkTI9mpQryDCtMugJ6BFaXsklTfVkdHF+oYTqvywiOEUfGUpjrTZayRMWjLzz+Ru1sYBQBdNnW1+PDgzqjTHZz/D/bREN4bqtm2mWUPOdpeGdOUDleuUtouxknURoCmJ53YajsG5pb6nqT3pWHrn0UA1rEUSBFbhcTeW4QTwcsn7tBJ8Tv9ylyFlHcHOwm1/F5U9UpX7035ZpS335EjeAL0iJu45lSeec8UAGt9FKp9mOOMnikopDT/FNRB3NBWUX10Npcp4B8hSBpwsOpTgZHsyllxkmYFfiK02G1YpWXUn1SEJsoGNZsMnH6AmMZPZepl7Vm+e0WXV8lfoKOXAzS1tIMnGFgA2+bXNFMIVQ6OzxmRaxZ3picVkitDTLZJFH0sFLAU1ifuwMn9q4PdVPcG7pE43ZgtamQacJZiBs1G7rlXFyMAAXo8d6cfS5s+rf3npDzf+O/6dMusNbuMcv2tvdjE4Q+wRftjHVx7G+PM7IbffmoJY6nAAwyG0/ZVsptBsyFd5mYYcYl3lvmYpeL6+m7IN3T+AZ0KRDnliUjHzuhhNr106LNi0CHKQJmVVEWNZzVYQxiFmZgvh2A6PV0vWJimBykHFDib05ajUa/zR2B5j4OaCr/R/yxdsu9lnCTSWwEvbEHoNLHt9CnanGSuvtWsNwBwdXYTYhliEjyuda538iMXs6AdWA4+ekPDpRdhok9sGEuryNM+gupcNjB3+DuviITNt28kxg=";
        String password = "sQhc_W-.b17*!ken";

//        System.out.println("加密前1：" + content);
//        byte[] encryptResult1 = encrypt(content, password); //普通加密
//        byte[] decryptResult1 = decrypt(encryptResult1,password);   //普通解密
//        System.out.println("解密后1：" + new String(decryptResult1));

        //System.out.println("\n加密前2：" + content);
        //String encryptResult2 = encode(content, password);  //先编码再加密
        //String decryptResult2 = decode(content, password);   //先解码再解密
        //System.out.println("解密后2：" + decryptResult2);



        System.out.println("\n加密前2：" + content);
        String encryptResult2 = AES_Encrypt(content, password, "ECB");  //先编码再加密
        String decryptResult2 = AES_Decrypt(encryptResult2, password, "ECB");   //先解码再解密
        System.out.println("解密后2：" + decryptResult2);

    }
}
