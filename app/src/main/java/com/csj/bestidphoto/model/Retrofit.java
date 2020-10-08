package com.csj.bestidphoto.model;


import com.csj.bestidphoto.model.response.EditPhotoResp;
import com.lamfire.json.JSON;
import com.maoti.lib.net.RetrofitUtil;
import com.maoti.lib.net.interceptor.DefaultObserver;
import com.maoti.lib.utils.LogUtil;

import java.io.File;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Retrofit extends RetrofitUtil {
    private static final String TAG = Retrofit.class.getSimpleName();

    public static void cutPhoto(String path, int width, int height, String color, DefaultObserver<EditPhotoResp> observer) {
//        JSON json = new JSON();
//        json.put("com_id", com_id);
        File file=new File(path);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);//表单类型
        LogUtil.i(TAG,"file.getName() = " + file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("imagefilename", file.getName(), requestBody);

        getGsonRetrofitNoCache()
                .create(ApiService.class)
                .cutPhoto(width,height,color,part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    public static void beautyPhoto(String type, String value, String imgPath,DefaultObserver<EditPhotoResp> observer) {
        JSON json = new JSON();
        json.put("ImageURL", imgPath);

        getGsonRetrofitNoCache()
                .create(ApiService.class)
                .beautyPhoto(type,value,getJsonBody(json.toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
