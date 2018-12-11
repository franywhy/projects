package com.izhubo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RelativeDateFormat {

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_YEAR_AGO = "年前";

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
        Date date = format.parse("2017-8-26 14:20:35");
        System.out.println(format(date));
        System.out.println(toFormatDate(date));
    }

    public static String format(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            return 1 + ONE_MINUTE_AGO;
        }
        if (delta < 60L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 365L * ONE_DAY) {
            return toFormatDate(date);
        } else {
            return 1 + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }
    
    private static String toFormatDate(Date date) {
    	SimpleDateFormat formatter = new SimpleDateFormat("MM-dd");
    	String str = formatter.format(date);
        return str;
    }
}