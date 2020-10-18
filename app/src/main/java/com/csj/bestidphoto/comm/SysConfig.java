package com.csj.bestidphoto.comm;

import com.csj.bestidphoto.utils.PrefManager;
import com.google.gson.Gson;
import com.lamfire.utils.StringUtils;

import static com.csj.bestidphoto.comm.SPKey._AD_CONFIG;

public class SysConfig {

    private static SysConfig instance;
    private AdConfig adConfig;

    public static SysConfig getInstance(){
        if(instance == null){
            instance = new SysConfig();
        }

        return instance;
    }

    public void setAdConfig(AdConfig adConfig) {
        this.adConfig = adConfig;
        PrefManager.setPrefString(_AD_CONFIG,new Gson().toJson(adConfig));
    }

    public AdConfig getAdConfig() {
        if(adConfig == null){
            String adJson = PrefManager.getPrefString(_AD_CONFIG,null);
            if(!StringUtils.isEmpty(adJson)){
                adConfig = new Gson().fromJson(adJson,AdConfig.class);
            }
        }
        return adConfig;
    }
}
