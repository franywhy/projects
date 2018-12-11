package com.hq.learningapi.util;

import java.awt.*;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

	/**
	 * 日期转字符串
	 * @param date
	 * @param pattern
	 * @return
	 */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    /** 
     * 日期字符串转时间戳 
     * @param dateStr 
     * @return 
     */  
    public static Integer transForMilliSecond(String dateStr){  
        Date date = DateUtils.formatDate(dateStr);  
        return date == null ? null : DateUtils.transForMilliSecond(date);  
    }
    /**
     * 日期字符串转日期
     * @param string
     * @param pattern
     * @return
     */
    public static Date formatDate(String string , String pattern){
    	if(StringUtils.isNotBlank(string)){
    		try {
				return new SimpleDateFormat(pattern).parse(string);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
    	}
    	return null;
    }
    /**
     * 字符串转日期
     * @param string
     * @return
     */
    public static Date formatDate(String string){
    	return formatDate(string, DATE_PATTERN);
    }
    /** 
     * 日期转时间戳 (秒)
     * @param date 
     * @return 
     */  
    public static Integer transForMilliSecond(Date date){  
        if(date==null) return null;  
        return (int)(date.getTime()/1000);  
    }
    /**
     * 时间戳转日期字符串"yyyy-MM-dd"
     * @param ms
     * @return
     */
    public static String transForDate(Integer ms){  
        String str = "";  
        if(ms!=null){  
            long msl=(long)ms*1000;  
            SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");  
              
            if(ms!=null){  
                try {  
                    str=sdf.format(msl);  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }         
        return str;  
    } 
    /**
	   * 获取现在时间
	   * 
	   * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	   */
	public static Date getNowDate() {
	   Date currentTime = new Date();
	   SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	   String dateString = formatter.format(currentTime);
	   ParsePosition pos = new ParsePosition(8);
	   Date currentTime_2 = formatter.parse(dateString, pos);
	   return currentTime_2;
	}
	
	/**
	   * 得到现在时间
	   * 
	   * @return
	   */
	public static Date getNow() {
	   Date currentTime = new Date();
	   return currentTime;
	}

	/*
	* 把时间戳装换为时间字符串
	*
	* */
    public static String getDateString4LongTime(Long timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return  formatter.format(new Date(timeStamp));
    }

//	public static void main(String[] args) {
//		System.out.println(transForMilliSecond("2018-01-27"));
//		System.out.println(transForDate(1516809600+86400));
//		Integer i = 1516809600;
//			
//		Integer j = 1516982400;
//		while(i <= j){
//			System.out.println(i);
//			i+=86400;
//		}
//	}
}
