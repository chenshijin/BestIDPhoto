package com.maoti.lib.net.interceptor;

import android.annotation.SuppressLint;
import android.util.Log;
import com.maoti.lib.net.interceptor.cache.CacheManager;
import com.maoti.lib.utils.LogUtil;
import com.maoti.lib.utils.NetworkUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by csy on 2018/4/20.
 */

public class LoggingInterceptor implements Interceptor {
    private final Charset UTF8 = Charset.forName("UTF-8");
    private boolean offlineNoCache;

    public LoggingInterceptor() {

    }

    public LoggingInterceptor(boolean offlineNoCache) {
        this.offlineNoCache = offlineNoCache;
    }


    @SuppressLint("DefaultLocale")
    @Override
    public Response intercept(Chain chain) throws IOException {
        //这个chain里面包含了request和response，所以你要什么都可以从这里拿
        String cacheKye = "" + System.nanoTime();
        long t1 = System.nanoTime();//请求发起的时间

        Request request = chain.request();
        /**
         * 仅仅针对get 请求
         */
        if (!NetworkUtils.isConnected() && !offlineNoCache) {
            int offlineCacheTime = Integer.MAX_VALUE;//离线的时候的缓存的过期时间
            request = request.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "private,only-if-cached,max-stale=" + offlineCacheTime)
                    .build();
        }

        String body = null;
        try {
            RequestBody requestBody = request.body();
            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                body = buffer.readString(charset);
            }
            cacheKye = request.url()+body;
//            LogUtil.i("net", "发送请求\nmethod：%s\nurl：%s\nheaders: %sbody：%s",
//                    request.method(), request.url(), request.headers(), body);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Response response = chain.proceed(request);

        /**
         * 仅针对Post请求, 断网的时候读取缓存
         */
        if(request.method()=="POST"&&!NetworkUtils.isConnected() && !offlineNoCache){
            CacheManager cacheManager = CacheManager.getInstance();
            final String cacheData=cacheManager.getCache(cacheKye);
            if(cacheData==null){
                return response;
            }

            response= response.newBuilder()
                    .code(200)
                    .message("OK")
                    .body(ResponseBody.create(MediaType.get("application/json; charset=UTF-8"), cacheData.getBytes()))
                    .build();
        }
        LogUtil.i("net", "发送请求\nmethod：%s\nurl：%s\nheaders: %sbody：%s",
                request.method(), request.url(), request.headers(), body);

        //这里不能直接使用response.body().string()的方式输出日志
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        String requestUrl = request.url().encodedPath();
        String resultData = responseBody.string();
        if (!requestUrl.contains(".apk")
                && !requestUrl.contains(".png")
                && !requestUrl.contains(".jpg")
                && !requestUrl.contains(".gif")) {
            LogUtil.i("net", String.format("返回数据:%n url:%s %n 耗时:%s %n %s",
                    response.request().url(),
                    (System.nanoTime() - t1) / 1e6d+"",
                    resultData));
        }

        /**
         * 仅针对post请求,进行网络缓存
         */
        if (request.method()=="POST"
                &&NetworkUtils.isConnected()
                && !offlineNoCache
                &&!requestUrl.contains(".apk")
                && !requestUrl.contains(".png")
                && !requestUrl.contains(".jpg")
                && !requestUrl.contains(".gif")) {
            CacheManager cacheManager = CacheManager.getInstance();
            cacheManager.putCache(cacheKye, resultData);
        }

        return response;
    }

}
