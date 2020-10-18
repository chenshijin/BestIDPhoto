package com.csj.bestidphoto.model;

import com.csj.bestidphoto.comm.AdConfig;
import com.csj.bestidphoto.model.response.EditPhotoResp;
import com.maoti.lib.net.ResponseResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Multipart
    @POST("upload/{width}/{height}/{color}")
    Observable<ResponseResult<EditPhotoResp>> cutPhoto(@Path("width") int width, @Path("height") int height, @Path("color") String color, @Part MultipartBody.Part file);

    @PUT("texiao/{type}/{value}")
    Observable<ResponseResult<EditPhotoResp>> beautyPhoto(@Path("type") String type, @Path("value") String value, @Body RequestBody requestBody);

    @GET("guanggao")
    Observable<ResponseResult<AdConfig>> getConfig();
}
