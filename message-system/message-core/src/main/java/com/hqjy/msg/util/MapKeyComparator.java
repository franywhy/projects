package com.hqjy.msg.util;

import java.util.Comparator;

class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        //  升序排序
        //return str1.compareTo(str2);
        //  降序排序
        return str2.compareTo(str1);
    }
}
