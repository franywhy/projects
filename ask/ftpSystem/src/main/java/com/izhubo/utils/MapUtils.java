package com.izhubo.utils;

import java.util.HashMap;
import java.util.Map;

import com.izhubo.rest.common.util.JSONUtil;

public class MapUtils {

    public static <K,V> Map<K, V> toMap(K k, V v){
    	Map<K, V> map = new HashMap<K, V>();
    	map.put(k, v);
    	return map;
    }
    
    public static void main(String[] args) {
		String s1 = "{a:1 , b: 'b' , c:{c1:'c1'}}";
		Map m1 = JSONUtil.jsonToMap(s1);
		System.out.println(m1);
		System.out.println(m1.get("a"));
		System.out.println(m1.get("c"));
		System.out.println(m1.get("c").getClass());
	}
}
