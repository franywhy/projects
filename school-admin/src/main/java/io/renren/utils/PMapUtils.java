package io.renren.utils;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月8日
 */
public final class PMapUtils {
	
	public static final Object getObject(Map<String , Object> params , String key ) {
		return getObject(params, key, null);
	}
	public static final Object getObject(Map<String , Object> params , String key , Object defaultValue) {
		if(isNotNull(params, key)) {
			return params.get(key);
		}
		return defaultValue;
	}
	public static final Integer getInteger(Map<String , Object> params , String key ) {
		return getInteger(params, key, null);
	}
	public static final Integer getInteger(Map<String , Object> params , String key , Integer defaultValue) {
		if(isNotNull(params, key)) {
			String str = (String) params.getOrDefault(key, null);
			try {
				if(StringUtils.isNotBlank(str)){
					return Integer.valueOf(str);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	public static final Long getLong(Map<String , Object> params , String key) {
		return getLong(params, key, null);
	}
	public static final Long getLong(Map<String , Object> params , String key , Long defaultValue) {
		if(isNotNull(params, key)) {
			String str = (String) params.getOrDefault(key, null);
			try {
				if(StringUtils.isNotBlank(str)){
					return Long.valueOf(str);
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	public static final String getString(Map<String , Object> params , String key) {
		return getString(params, key, null);
	}
	public static final String getString(Map<String , Object> params , String key , String defaultValue) {
		if(isNotNull(params, key)) {
			String str = (String) params.getOrDefault(key, null);
			try {
				if(StringUtils.isNotBlank(str)){
					return str.trim();
				}
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		return defaultValue;
	}
	
	/**
	 * map中存在key值
	 * @param map
	 * @param key
	 * @return
	 */
	public static final boolean isNotNull(Map<String , Object> map , String key) {
		return null != map && !map.isEmpty() && map.containsKey(key);
	}
	
}
