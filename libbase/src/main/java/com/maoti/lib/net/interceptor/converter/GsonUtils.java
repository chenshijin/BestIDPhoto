package com.maoti.lib.net.interceptor.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public class GsonUtils {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final JsonParser PARSER = new JsonParser();

    public static Gson getGSON() {
        return GSON;
    }

    /**
     * Object 转 json
     * @param object object
     * @return object
     */
    public static String GsonString(Object object){
        return GSON.toJson(object);
    }

    public static <T> T GsonToBean(String gsonString, Class<T> cls) {
        return GSON.fromJson(gsonString, cls);
    }

    /**
     * 转成map的
     *
     * @param gsonString str
     * @return map
     */
    public static Map<String, Object> GsonToMaps(String gsonString) {
        return GSON.fromJson(gsonString, new TypeToken<Map<String, Object>>() {}.getType());
    }

    /**
     * 将json 格式化输出
     * @param str str
     * @return str
     */
    public static String GsonToString(String str){
        try {
            return GSON.toJson(PARSER.parse(str));
        } catch (JsonSyntaxException e){
            e.printStackTrace();
            return str;
        }
    }

}
