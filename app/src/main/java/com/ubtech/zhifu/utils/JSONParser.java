package com.ubtech.zhifu.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;


/**
 * Created by lei on 2020/6/5
 * desc:
 */
public class JSONParser {
    protected static Gson gson = new Gson();

    public static String toString(Object obj) {
        return gson.toJson(obj);
    }

    public static Object toObject(String jsonString, Object type) {
        if (type instanceof Type) {
            try {
                return gson.fromJson(jsonString, (Type) type);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
                return null;
            }
        } else if (type instanceof Class) {
            try {
                return gson.fromJson(jsonString, (Class) type);
            } catch (JsonSyntaxException e2) {
                e2.printStackTrace();
                return null;
            }
        } else {
            throw new RuntimeException("只能是Class<?>或者通过TypeToken获取的Type类型");
        }
    }

}
