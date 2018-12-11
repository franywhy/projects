package com.izhubo.utils;

import java.util.Map;

public interface SuperMap<K, V> extends Map<K, V> {
	SuperMap<K,V> putVal(K key, V val);
}
