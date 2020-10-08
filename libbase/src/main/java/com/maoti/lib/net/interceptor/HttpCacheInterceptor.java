package com.maoti.lib.net.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.internal.cache.CacheStrategy;

/**
 * Created by csy on 2018/3/21.
 */

public class HttpCacheInterceptor implements Interceptor {
    int cacheTime=0;
    public HttpCacheInterceptor() {
    }

    public HttpCacheInterceptor(int cacheTime){
        this.cacheTime=cacheTime;//秒
    }

    //  配置缓存的拦截器
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", "private, max-age=" + cacheTime)
                .build();
    }
}
