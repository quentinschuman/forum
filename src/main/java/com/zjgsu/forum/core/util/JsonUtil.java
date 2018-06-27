package com.zjgsu.forum.core.util;

import com.google.gson.Gson;
import com.zjgsu.forum.module.user.model.User;

/**
 * Created by qianshu on 2018/6/27.
 */
public class JsonUtil {
    public final static Gson gson = new Gson();
    public static String objectToJson(Object object) {
        return gson.toJson(object);
    }
    public static <T> T jsonToObject(String json, Class<T> object) {
        return gson.fromJson(json, object);
    }
}
