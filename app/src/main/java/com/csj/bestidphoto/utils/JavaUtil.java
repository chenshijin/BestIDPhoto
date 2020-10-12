package com.csj.bestidphoto.utils;

import com.google.gson.Gson;

public class JavaUtil {
    private final static  String TAG = JavaUtil.class.getSimpleName();
    /**
     * 把modelA对象的属性值赋值给bClass对象的属性。
     *
     * @param modelA
     * @param bClass
     * @param <T>
     * @return
     */
    public static <A, T> T modelAconvertoB(A modelA, Class<T> bClass) {
        try {
            Gson gson = new Gson();
            String gsonA = gson.toJson(modelA);
            T instanceB = gson.fromJson(gsonA, bClass);
            return instanceB;
        } catch (Exception e) {
            return null;
        }
    }
}
