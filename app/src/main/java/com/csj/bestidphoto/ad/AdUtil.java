package com.csj.bestidphoto.ad;


import com.csj.bestidphoto.comm.SysConfig;

public class AdUtil {
    private final static String TAG = AdUtil.class.getSimpleName();

    public static boolean canShowAd(){
        return (SysConfig.getInstance().getAdConfig() != null && SysConfig.getInstance().getAdConfig().isAdvertising());
    }
}
