package com.hqjy.pay.utils;

import java.math.BigDecimal;

/**
 *
 */
public class NumberUtils {

    public static int parseInt(long l){
        return BigDecimal.valueOf(l).intValue();
    }

    public static long parseLong(String s) {
        return Long.parseLong(s.trim());
    }

    public static BigDecimal getBigDecimal(String s) {
        BigDecimal bigDecimal = new BigDecimal(s.trim());
        bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
        return bigDecimal;
    }

}
