package com.hqjy.msg.util;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class MapUtils {
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(
                new MapKeyComparator());

        sortMap.putAll(map);

        return sortMap;
    }
}
