package com.csj.bestidphoto.model;

import com.csj.bestidphoto.model.response.EditPhotoResp;
import com.maoti.lib.net.ResponseResult;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {

    @Multipart
    @POST("upload/{width}/{height}/{color}")
    Observable<ResponseResult<EditPhotoResp>> uploadPhoto(@Path("width") int width, @Path("height") int height, @Path("color") String color, @Part MultipartBody.Part file);
}
