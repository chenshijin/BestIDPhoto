
package com.maoti.lib.net;


import com.google.gson.Gson;
import com.maoti.lib.BaseApplication;
import com.maoti.lib.net.interceptor.HttpCacheInterceptor;
import com.maoti.lib.net.interceptor.HttpHeaderInterceptor;
import com.maoti.lib.net.interceptor.LoggingInterceptor;
import com.maoti.lib.net.interceptor.converter.MyConverterFactory;
import com.maoti.lib.utils.LogUtil;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;


public class RetrofitUtil {


    public static RequestBody toJsonBody(Object body){
        return getJsonBody(new Gson().toJson(body));
    }

    public static RequestBody getJsonBody(String json){
        RequestBody requestBody=RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),json);
        return requestBody;
    }


    /**
     * 无网: 读取缓存
     * 有网: 不读取缓存,正常请求
     * 场景: 无特殊情况,默认用这个
     */
    private static Retrofit retrofit;
    public static Retrofit getGsonRetrofit() {
        if(retrofit==null){
            retrofit= baseRetrofit().build();
        }
        return retrofit;
    }

    /**
     * 无网: 不读取缓存
     * 有网: 不读取缓存
     * 场景: 登录信息\用户操作\敏感信息就用这个
     */
    private static Retrofit retrofitNoCache;
    public static Retrofit getGsonRetrofitNoCache(){
        if(retrofitNoCache==null){
            retrofitNoCache=baseRetrofitNoCache().build();
        }
        return retrofitNoCache;
    }

    /**
     * 无网: 读取缓存
     * 有网: 在有效时间内读取缓存
     * 场景: 不经常更新内容的时候可以用.
     * 意义: 减少用户访问服务器,降低压力.
     */
    private static Retrofit retrofitCache;
    public static Retrofit getGsonRetrofitCache() {
        if(retrofitCache==null){
            retrofitCache= baseRetrofitCache().build();
        }
        return retrofitCache;
    }




    public static Retrofit.Builder baseRetrofit() {
        return new Retrofit.Builder()
                .client(getOKhttpBuilder().build())
                .baseUrl(HttpURL.getBaseUrl())
                .addConverterFactory(MyConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());

    }

    /**
     *无网的时候,读取缓存,有网的时候正常请求网络
     */
    private static OkHttpClient.Builder getOKhttpBuilder() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpHeaderInterceptor())//顺序要对,先添加header拦截.
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(getCache());
        return okhttpClientBuilder;
    }




    public static Retrofit.Builder baseRetrofitCache() {
        return new Retrofit.Builder()
                .client(getOKhttpBuilderCache().build())
                .baseUrl(HttpURL.getBaseUrl())
                .addConverterFactory(MyConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    /**
     *有网的时候读取缓存,有效期为5分钟,
     * 期间不对服务器做请求,降低流量消耗和服务器压力
     */
    private static OkHttpClient.Builder getOKhttpBuilderCache() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpHeaderInterceptor())//顺序要对,先添加header拦截.
                .addInterceptor(new LoggingInterceptor())
                .addNetworkInterceptor(new HttpCacheInterceptor(60*55))
                .cache(getCache());
//
//                .sslSocketFactory(Objects.requireNonNull(SSLSocketFactoryUtils.getSSLSocketFactory()),
//                        SSLSocketFactoryUtils.createTrustManager())
//                .hostnameVerifier(new SSLSocketFactoryUtils.TrustAllHostnameVerifier()).cache(cache);
        return okhttpClientBuilder;
    }

    public static Retrofit.Builder baseRetrofitNoCache() {
        return new Retrofit.Builder()
                .client(getOKhttpBuilderNoCache().build())
                .baseUrl(HttpURL.getBaseUrl())
                .addConverterFactory(MyConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create());
    }

    private static OkHttpClient.Builder getOKhttpBuilderNoCache() {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .readTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .connectTimeout(NetConstant.DEFAULT_TIMEOUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new HttpHeaderInterceptor())//顺序要对,先添加header拦截.
                .addInterceptor(new LoggingInterceptor(true))
                .addNetworkInterceptor(new HttpCacheInterceptor())
                .cache(getCache());
        return okhttpClientBuilder;
    }

    private static Cache getCache(){
        File cacheFile = new File(BaseApplication.getContext().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100); //100Mb
        return cache;
    }

}
