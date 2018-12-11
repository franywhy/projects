package com.hq.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Glenn on 2017/5/11 0011.
 */
public class CommonUtil {


    public static final Long day= 1000 * 60 * 60 * 24L;
    public static final Long hour = 1000 * 60 * 60L;
    public static final Long minute = 1000 * 60L;

    public static final Long oneDayTimeStamp = 24*60*60*1000L;

    public static boolean checkRegular(String regular, String value)
    {
        Pattern p = Pattern.compile(regular);
        Matcher m = p.matcher(value);
        return m.find();
    }
}
