package com.sowell.mvpbase.mvputils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by：kenny
 * Created Time：2020/5/13
 */
public class TUtil {
    @SuppressWarnings("unchecked")
    public static <T> T getT(Object o, int i) {
        try {
            if(o == null) return null;

            Type superclass = o.getClass().getGenericSuperclass();
            if(superclass instanceof ParameterizedType) {
                return ((Class<T>)((ParameterizedType) superclass).getActualTypeArguments()[i]).newInstance();
            }
//            return ((Class<T>) ((ParameterizedType) (o.getClass().getGenericSuperclass())).getActualTypeArguments()[i]).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Class<?> forName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}