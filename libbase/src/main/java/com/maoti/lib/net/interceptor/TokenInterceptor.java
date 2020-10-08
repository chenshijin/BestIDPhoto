package com.maoti.lib.net.interceptor;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;

public class TokenInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = chain.proceed(request);
//
//        //String url = request.url().scheme();//请求Url
//        String url = request.url().scheme() + "://" + request.url().host() + ":" + request.url().port();
//
//        //获取返回的json，response.body().string();只有效一次，对返回数据进行转换
//        ResponseBody responseBody = originalResponse.body();
//        assert responseBody != null;
//        BufferedSource source = responseBody.source();
//        source.request(Long.MAX_VALUE); // Buffer the entire body.
//        Buffer buffer = source.buffer();
//        Charset charset = Charset.forName("UTF-8");
//        MediaType contentType = responseBody.contentType();
//        if (contentType != null) {
//            charset = contentType.charset(Charset.forName("UTF-8"));
//        }
//        assert charset != null;
//        String bodyString = buffer.clone().readString(charset);//首次请求返回的结果
//
//        //  && !TextUtils.isEmpty(CServiceApi.getRefreshAcess_Token())
//        RequestInfo requestInfo = new Gson().fromJson(bodyString, RequestInfo.class);
//        if (requestInfo != null && requestInfo.getCode() == 300004) {//根据和服务端的约定判断token过期
//            //同步请求方式，获取最新的Token
//            TokenInfo tokenInfo = null;
//            try {
//                tokenInfo = getNewToken(url).getData();
//            } catch (Exception e) {
//                // refreshtoken失效，登出app
//                EventBusModel<String> eventBusModel = new EventBusModel<String>();
//                eventBusModel.tag = "logout";
//                EventBus.getDefault().post(eventBusModel);
//                throw new RuntimeException(e);
//            }
//            //Log.i("TokenInterceptor", "tokenInfo:" + tokenInfo.getToken());
//            if (tokenInfo != null && !TextUtils.isEmpty(tokenInfo.getToken())) {
//                SharedPreferenceUtil.getInstance(MyApplication.getApp()).saveToken(tokenInfo.getToken());
//            }
//            if (tokenInfo != null && !TextUtils.isEmpty(tokenInfo.getRefreshToken())) {
//                SharedPreferenceUtil.getInstance(MyApplication.getApp()).saveRefreshToken(tokenInfo.getRefreshToken());
//            }
//            //使用新的Token，创建新的请求
//            Request newRequest = chain.request()
//                    .newBuilder()
//                    .header("token", (tokenInfo == null || TextUtils.isEmpty(tokenInfo.getToken())) ? "" : tokenInfo.getToken())
//                    .build();
//            //重新请求
//            return chain.proceed(newRequest);
//        }
//        if(requestInfo != null && requestInfo.getCode() == 300009){//账户已在其他设备登陆了
//            EventBusModel<String> eventBusModel = new EventBusModel<String>();
//            eventBusModel.tag = Constant.LOGOUT_BY_MULTI_USER;
//            EventBus.getDefault().post(eventBusModel);
//        }

        return originalResponse;
    }

    /**
     * 根据Response约定，判断Token是否失效
     *
     * @param response
     */
    private boolean isTokenExpired(String response) {
//        RequestInfo requestInfo = new Gson().fromJson(response, RequestInfo.class);
//        if (requestInfo != null && requestInfo.getCode() == 300004) {
//            return true;
//        }
        return false;
    }

    /**
     * 同步请求方式，获取最新的Token
     */
//    private ResponseResult<TokenInfo> getNewToken(String url) throws IOException {
//        String refreshToken = SharedPreferenceUtil.getInstance(MyApplication.getApp()).getRefreshToken();
//
//        HashMap<String, String> hashMap = new HashMap<>();
//        hashMap.put("refreshToken", refreshToken);
//
//        OkHttpClient.Builder okhttpBuilder;
//        //if (Constant.isUseHttps) {
//            okhttpBuilder = RetrofitClient.OKHttpHolder.OK_HTTP_BUILDER
//                    .addInterceptor(new HttpHeaderInterceptor())
//                    .sslSocketFactory(Objects.requireNonNull(SSLSocketFactoryUtils.createSSLSocketFactory(MyApplication.getApp())),
//                            SSLSocketFactoryUtils.createTrustManager())
//                    .hostnameVerifier(new SSLSocketFactoryUtils.TrustAllHostnameVerifier());
////        } else {
////            okhttpBuilder = RetrofitClient.OKHttpHolder.OK_HTTP_BUILDER
////                    .addInterceptor(new HttpHeaderInterceptor());
////        }
//
//        Retrofit retrofit = RetrofitClient.RetrofitHolder.RETROFIT_BUILDER
//                .client(okhttpBuilder.build())
//                .baseUrl(url)
//                .build();
//        Call<ResponseResult<TokenInfo>> refreshCall = retrofit.create(RetrofitApiService.class).refreshToken(hashMap);
//
//        // 通过一个特定的接口获取新的token，此处要用到同步的retrofit请求
////        Call<ResponseResult<TokenInfo>> refreshCall = RetrofitApi.getApiServiceRefreshTolen(CommonService.class, url)
////                .refreshToken(hashMap);
//        return refreshCall.execute().body();
//    }
}

