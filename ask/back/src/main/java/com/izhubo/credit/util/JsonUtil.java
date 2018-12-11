package com.izhubo.credit.util;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * JSON工具类
 *
 */
public class JsonUtil {

	static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();;
	
	/**
	 * 对象转JSON字符串
	 * @param object
	 * @return
	 */
	public static String toJson(Object object){
		return gson.toJson(object).toString();
	}

    public static <T> T fromJson(String json,Class<T> tClass){
        return gson.fromJson(json,tClass);
    }

    public static <T> T fromJson(String json, Type t){
        return gson.fromJson(json,t);
    }
	
}
