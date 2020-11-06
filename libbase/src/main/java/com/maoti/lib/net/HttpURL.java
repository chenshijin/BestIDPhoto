package com.maoti.lib.net;

import com.maoti.lib.utils.LogUtil;
import com.maoti.libbase.BuildConfig;
import com.maoti.lib.db.SPFKey;
import com.maoti.lib.db.SPfUtil;

public class HttpURL {

    private final static String TAG = HttpURL.class.getSimpleName();

    public static String getHtmlUrl(){
        return  getBaseUrl()  + "v1/agreement-html/"; //html 地址 访问;
    }

    /**
     * 分享成功后的url
     * @param type
     * @param ContentId
     * @return
     */
    public static String ShareUrl(String type,String ContentId){
        String mShareUrl =  BuildConfig.SHARE_URL;
        try{
            mShareUrl =  String.format(mShareUrl, type, ContentId);
        }catch (Exception e){
            e.printStackTrace();
        }
        return mShareUrl;
    }

    /**
     * 获取baseUrl
     * @return
     */
    public static String getBaseUrl() {
        //todo 等后台给地址
        String BASE_URL = BuildConfig.BASE_URL;
//        if (BuildConfig.IS_DEBUG) { //测试版
            try{
                String url= SPfUtil.getInstance().getString(SPFKey.http_url);
                if(url != null && url.startsWith("http")){
                    BASE_URL = url;
                }else{
                    //如果没有,默认给一个合法的测试服务器地址
                    BASE_URL = BuildConfig.BASE_URL;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
//        }

        return BASE_URL;
    }

    /**
     * IM服务
     * @return
     */
    public static String getWsUrl(){
        String mWsUrl = BuildConfig.IM;
//        if (BuildConfig.IS_DEBUG) {
            try{
                String url=SPfUtil.getInstance().getString(SPFKey.ws_url);
                if(url!=null&&url.startsWith("ws")){
                    mWsUrl=url;
                }else{
                    //本地开发环境
                    mWsUrl=BuildConfig.IM;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
//        }
        return mWsUrl;
    }



}
