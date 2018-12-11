package com.izhubo.userSystem.utils;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class JSONUtil {
	
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final JsonFactory JSONFACTORY =new JsonFactory();
    
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
    
    public static Map<String, Object> beanToMap(Object o) {
        try {
            return (Map) MAPPER.readValue(beanToJson(o), HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }
    
    public static Map<String, Object> jsonToMap(String json) {
        try {
            return (Map) MAPPER.readValue(json, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException("转换失败", e);
        }
    }
}
