package com.hqjy.pay.weixin;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

public class JsonUtil {

    private static final JsonConfig EXCLUDE_NULL_PROPERTY_CONFIG = new JsonConfig(); // 用于过滤掉值为null的字段
    private static final ObjectMapper MAPPER = new ObjectMapper();
    //private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSONFACTORY =new JsonFactory();
    
    public static Object fromJSON(String json) {

        if (json == null || json.equals("")) {
            return null;
        } else {
            JSONObject jsono = JSONObject.fromObject(json, EXCLUDE_NULL_PROPERTY_CONFIG);
            return jsono;
        }
    }

    public static String toJSON(Object ext) {
        if (ext == null) {
            return null;
        } else {
            JSONObject jsono = JSONObject.fromObject(ext, EXCLUDE_NULL_PROPERTY_CONFIG);
            return jsono.toString();
        }
    }

    public static JSONObject toJSONObject(Object ext) {
        if (ext == null) {
            return null;
        } else {
            JSONObject jsono = JSONObject.fromObject(ext, EXCLUDE_NULL_PROPERTY_CONFIG);
            return jsono;
        }
    }
    /**
     * 转换Json String 为 HashMap
     */
    public static Map<String, Object> jsonToMap(String json) {
        try {
            return (Map) MAPPER.readValue(json, HashMap.class);
        } catch (Exception e) {
            throw new RuntimeException("转换失败", e);
        }
    }
    /**
     * 转换Json String 为 JavaBean
     */
    public static <T> T jsonToBean(String json, Class<T> type) {
        try {
            return MAPPER.readValue(json, type);
            //return map;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 转换Java Bean 为 HashMap
     */
    public static Map<String, Object> beanToMap(Object o) {
        try {
            return (Map) MAPPER.readValue(beanToJson(o), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }
    
    /**
     * 转换Java Bean 为 json
     */
    public static String beanToJson(Object o) {
        StringWriter sw = new StringWriter(300);
        JsonGenerator gen = null;
        try {
            gen = JSONFACTORY.createGenerator(sw);
            MAPPER.writeValue(gen, o);
            return sw.toString();
        } catch (Exception e) {
            throw new RuntimeException("JSON转换失败", e);
        } finally {
            if (gen != null) try {
                gen.close();
            } catch (IOException ignored) {
            }
        }
    }
    
    static {
        MAPPER.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        MAPPER.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);

        MAPPER.getSerializationConfig().withSerializationInclusion(JsonInclude.Include.NON_NULL);
    }


    public static void validateJSON(String json) throws IOException {
        try(JsonParser parser = JSONFACTORY.createParser(json)){
            while (parser.nextToken() != null) {
            }
        }
    }

    public interface ToJson{
        String toJsonString();
    }
}
