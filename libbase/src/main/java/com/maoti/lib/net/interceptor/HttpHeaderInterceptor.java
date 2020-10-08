package com.maoti.lib.net.interceptor;

import android.util.Log;

import com.maoti.lib.BaseApplication;
import com.maoti.lib.utils.Utils;
import com.maoti.lib.base.ROMTools;
import com.maoti.lib.db.SPFKey;
import com.maoti.lib.db.SPfUtil;
import com.maoti.lib.net.NetConstant;

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by csy on 2018/3/21.
 */

public class HttpHeaderInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        //  配置请求头
        String token = SPfUtil.getInstance().getString(SPFKey.TOKEN);
        String mIMEI = Utils.getIMEI();
        Log.d(NetConstant.logTag,token +"  "+mIMEI);
        Request request = chain.request().newBuilder()
                .addHeader("Web-Token", token)
                .addHeader("version",Utils.getBaseAppVersionName())//获取版本号 1.0.7
                .addHeader("version-name",  Utils.getAppVersionCode(BaseApplication.getContext()))//获取版本号  5
                .addHeader("device","Android")
                .addHeader("unique-device",mIMEI)
                .addHeader("Device-Model", ROMTools.getName())
                .addHeader("Content-Type", "application/json;charset=utf-8")
                .addHeader("Accept","application/json;charset=utf-8")
                .addHeader("Accept-Encoding", "identity")
                .addHeader("User-Agent", Utils.getUserAgent())
                .build();
        return chain.proceed(request);
    }
}
