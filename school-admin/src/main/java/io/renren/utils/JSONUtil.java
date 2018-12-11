package io.renren.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * json javabean 之间的转换工具;
 * servlet 输出工具
 *
 * @author yangyang.cong
 */
public abstract class JSONUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSONFACTORY = new JsonFactory();
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();

    public static<T> String objToGson(T t) {
        return GSON.toJson(t);
    }

    public static<T> T gsonToObj(String gson, Class<T> type) {
        return GSON.fromJson(gson,type);
    }

    /**
     * 转换Java Bean 为 json
     */
    public static String beanToJson(Object o) {
        StringWriter sw = new StringWriter(300);//300
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

    public static String beanToJson(ToJson o) {
        return o.toJsonString();
    }

    /**
     * 把对象转换为json格式字符串输出
     *
     * @param response 当前响应response
     * @param bean     要输出的对象
     */
//    public static void printObjAsJson(HttpServletResponse response, Object bean) {
//        printObj(response, beanToJson(bean));
//    }

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
     * 转换Json String 为 HashMap
     */
    public static Map<String, Object> jsonToMap(String json) {
        try {
        	//配置转义字符
        	MAPPER.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
            return (Map) MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
        	System.out.println(e.toString());
            throw new RuntimeException("转换失败", e);
        }
    }
    
    
 
    
    
    public static String hashMapToJson(Map<String, String> params) {  
    	String dot = "\"";
        String string = "{";  
        for (Iterator it = params.entrySet().iterator(); it.hasNext();) {  
            Entry e = (Entry) it.next();  
            string += dot + e.getKey() + dot+":";  
            string += dot + e.getValue() + dot+",";  
        }  
        string = string.substring(0, string.lastIndexOf(","));  
        string += "}";  
        return string;  
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