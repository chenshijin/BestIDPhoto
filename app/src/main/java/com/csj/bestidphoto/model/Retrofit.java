package com.csj.bestidphoto.model;


import com.csj.bestidphoto.model.response.EditPhotoResp;
import com.lamfire.json.JSON;
import com.maoti.lib.net.RetrofitUtil;
import com.maoti.lib.net.interceptor.DefaultObserver;
import com.maoti.lib.utils.LogUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.QueryMap;

public class Retrofit extends RetrofitUtil {
    private static final String TAG = Retrofit.class.getSimpleName();

    public static void uploadPhoto(String path,int width,int height,String color,DefaultObserver<EditPhotoResp> observer) {
//        JSON json = new JSON();
//        json.put("com_id", com_id);
        File file=new File(path);
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),file);//表单类型
        LogUtil.i(TAG,"file.getName() = " + file.getName());
        MultipartBody.Part part = MultipartBody.Part.createFormData("imagefilename", file.getName(), requestBody);

        getGsonRetrofitNoCache()
                .create(ApiService.class)
                .uploadPhoto(width,height,color,part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
