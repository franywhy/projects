package com.hqjy.msg.util;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.hqjy.msg.adapter.GsonTypeAdapter;
import org.apache.commons.collections.ListUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by baobao on 2017/11/16 0016.
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    public static String objToJsonStr(Object obj){
        /*JSONArray json = JSONArray.fromObject(obj);
        return json.toString();*/
        Gson gson = new Gson();
        return gson.toJson(obj);

    }

    public static Object strToObj(String str,Class cls){
        Gson gson = new Gson();
        return gson.fromJson(str,cls);

    }

    /**
     * json字符串转list或者map
     *
     * @param json
     * @param typeToken
     * @return
     */
    public static Object fromJson(String json, Class clz) {
        Gson gson = new GsonBuilder().registerTypeAdapter(new TypeToken<Map<String, Object>>()
        {
        }.getType(), new GsonTypeAdapter()).create();
        return gson.fromJson(json, clz);
    }

    /*方法二：推荐，速度最快
  * 判断是否为整数
  * @param str 传入的字符串
  * @return 是整数返回true,否则返回false
*/

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }



    public static String getUnquie(){
        String chars = "abcdefghijklmnopqrstuvwxyz1234567890";

        UUID uuid = UUID.randomUUID();
        String key = uuid.toString().replaceAll("-", String.valueOf(chars.charAt((int)(Math.random() * 36)))).toUpperCase();
        key = key + DateUtils.dateToString(DateUtils.getNowDate(), "yyyyMMddHHmmss");
        return key;

    }

    public static String getUUID(){

        UUID uuid = UUID.randomUUID();
        return  uuid.toString();


    }




}
