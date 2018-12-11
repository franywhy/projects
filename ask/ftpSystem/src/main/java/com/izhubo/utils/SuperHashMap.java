package com.izhubo.utils;

import java.util.HashMap;

public class SuperHashMap<K, V> extends HashMap<K, V> implements SuperMap<K, V> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public SuperMap<K, V> putVal(K key, V val) {
		this.put(key, val);
		return this;
	}
	
	public static <K, V> SuperMap<K, V> toSuperMap(K key, V val){
		SuperMap<K, V> map = new SuperHashMap<>();
		map.put(key, val);
		return map;
	}

}
