package com.maoti.lib.net.interceptor.converter;

import android.util.Log;

import com.google.gson.Gson;
import com.maoti.lib.net.ResponseResult;
import com.maoti.lib.net.interceptor.exception.ApiException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;


    public GsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String json = value.string();
        //第一次解析
        ResponseResult obj = GsonUtils.GsonToBean(json, ResponseResult.class);
        if (!obj.isSuccess()) {
            //如果是服务端返回的错误码，则抛出自定义异常
            throw new ApiException(obj.getRet(), obj.getMsg());
        }

        //第二次解析
        //预防有些数据虽然返回了成功,但是data是[]
        //如果data是[],那么单独解析,先验证是否满足单独解析的要求
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.get("data") instanceof JSONArray) {
                if (jsonObject.getJSONArray("data").length() == 0) {
                    jsonObject.remove("data");
                    json = jsonObject.toString();
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }

        T t = gson.fromJson(json, type);
        value.close();
        return t;

    }
}

