package com.izhubo.topics.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.izhubo.common.util.KeyUtils;
import com.izhubo.rest.common.util.BMatch;



/**
 * 日期工具
 *
 */
public class TopicsDataUtils {
	
	private static final SimpleDateFormat formatter1 = new SimpleDateFormat ("yy/MM/dd HH:mm"); 
	/** 1小时的毫秒数 */
	private static final Long ONE_HOURE_MILLISECOND = 3600000L;
//	private static final Long ONE_SECOND_MILLISECOND = 1000L;
	
	private static final String TIME_MINUTE_FORMAT = "分钟前";
	private static final String TIME_HOUR_FORMAT3 = "小时前";
	private static final String TIME_NULL = "";
	private static final String TIME_MINUTE_FORMAT1 = "分钟后失效";
	private static final String TIME_HOUR_FORMAT1 = "小时后失效";
	
	public static void main(String[] args) {
		try {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date d1 = sdf.parse("2016-06-15 17:00:00");
//			Date d2 = sdf.parse("2016-09-16 09:00:00");
//			Date d3 = sdf.parse("2016-09-19 11:00:00");
//			Date d4 = sdf.parse("2016-09-19 10:50:00");
//			
////			System.out.println(timeFormatter(d1));
////			System.out.println(timeFormatter(d2));
//			System.out.println(timeFormatter(d3));
//			System.out.println(timeFormatter(d4));
			
//			System.out.println(msTom(60000L));
//			System.out.println(msTom(11000L));
//			System.out.println(msTom(111000L));
//			System.out.println(msTom(121000L));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String msTomL(final Long ms){
		Integer m = 1;
		
		m = (int) Math.ceil((KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME - ms) / 60000.0);
//		m = (int) Math.ceil(ms / 60000.0);
		if(m <= 0){
			return "-";
		}else{
			return m+TIME_MINUTE_FORMAT1;
		}
	}
//	private static final Double TPICES_INDUSTRYS_TIME_SH = BMatch.mul(KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME ,  1);
//	private static final Double TPICES_INDUSTRYS_TIME_SH = BMatch.mul(KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME ,  ONE_HOURE_MILLISECOND);
	
	public static String msTomD(final Double ms){
		Integer m = 1;
		
//		m = (int) Math.ceil((KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME * ONE_HOURE_MILLISECOND - ms) / 60000.0);
		try {
			m = (int) Math.ceil(BMatch.div(BMatch.sub(KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME , ms), 60000, 2));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
//		m = (int) Math.ceil(ms / 60000.0);
		if(m <= 0){
			return "-";
		}else{
			return m+TIME_MINUTE_FORMAT1;
		}
	}
	
	public static String hsTomD(final Double ms){
		Integer m = 1;
		
		try {
			m = (int) Math.ceil(BMatch.div(BMatch.sub(KeyUtils.TOPICES.TPICES_INDUSTRYS_TIME_ZIKAO , ms), 3600000, 2));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if(m <= 0){
			return "-";
		}else{
			return (m-1)+TIME_HOUR_FORMAT1;
		}
	}
	
	public static String timeFormatter(final Long timestamp){
		if(timestamp == null){
			return null;
		}
		return timeFormatter(new Date(timestamp));
	}
	
	public static String timeFormatter(final Date date){
		if(date == null){
			return TIME_NULL;
		}else if(!isToday(date)){
			return outTodayToStr(date);
		}else if(isToHour(date)){
			return outOneHourToStr(date);
		}else{
			return outMinuteToStr(date);
		}
//		return TIME_NULL;
	}
	
	/**
	 * 日期转换成yy/MM/dd格式
	 * @param date
	 * @return
	 */
	public static String outTodayToStr(Date date){ 
	    String ctime = null;
		try {
			ctime = formatter1.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return ctime; 
	} 
	
	/**
	 * 时间格式--小时
	 * @param date
	 * @return
	 */
	public static String outOneHourToStr(Date date) {
		Calendar c0 = Calendar.getInstance();
		c0.setTime(date);
		int hour0 = c0.get(Calendar.HOUR_OF_DAY);//小时
		
		Calendar c1 = Calendar.getInstance();
		c1.setTime(new Date());
		int hour1 = c1.get(Calendar.HOUR_OF_DAY);//小时
		return (hour1 - hour0) + TIME_HOUR_FORMAT3;
	}
	
	/**
	 * 时间格式 分钟
	 * @param date
	 * @return
	 */
	public static String outMinuteToStr(Date date) {
		int d = (int) ((new Date().getTime() - date.getTime()) / (60000));
		
		if(d==0){
			d=1;
		}
		return d + TIME_MINUTE_FORMAT;
	}
//	public static String outMinuteToStr(Date date) {
//		Calendar c0 = Calendar.getInstance();
//		c0.setTime(date);
//		int m0 = c0.get(Calendar.MINUTE);//分    
//		
//		Calendar c1 = Calendar.getInstance();
//		c1.setTime(new Date());
//		int m1 = c1.get(Calendar.MINUTE);//分
//		int m2 = m1 - m0;
//		if(m2==0){
//			m2=1;
//		}
//		return m2 + TIME_MINUTE_FORMAT;
//	}
	
	
//	/**
//	 * 时间是否在一分钟以内
//	 * @param date
//	 * @return
//	 */
//	public static boolean isInMinute(Date date){
//		if(null != date && new Date().getTime() - date.getTime() > ONE_MINUTE_MILLISECOND){
//			return true;
//		}
//		return false;
//	}
	/**
	 * 时间是否在一个小时以内
	 * @param date
	 * @return
	 */
	public static boolean isToHour(Date date){
		if(null != date && new Date().getTime() - date.getTime() > ONE_HOURE_MILLISECOND){
			return true;
		}
		return false;
	}
	
	/**
	* 是否是今天
	* 
	* @param date
	* @return
	*/
	public static boolean isToday(final Date date) {
	        return isTheDay(date, new Date());
	}
	/**
	* 是否是指定日期
	* 
	* @param date
	* @param day
	* @return
	*/
	public static boolean isTheDay(final Date date, final Date day) {
	        return date.getTime() >= dayBegin(day).getTime()
	                        && date.getTime() <= dayEnd(day).getTime();
	}
	
	
	
	/**
	* 获取指定时间的那天 00:00:00.000 的时间
	* 
	* @param date
	* @return
	*/
	public static Date dayBegin(final Date date) {
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.set(Calendar.HOUR_OF_DAY, 0);
	        c.set(Calendar.MINUTE, 0);
	        c.set(Calendar.SECOND, 0);
	        c.set(Calendar.MILLISECOND, 0);
	        return c.getTime();
	}
	/**
	* 获取指定时间的那天 23:59:59.999 的时间
	* 
	* @param date
	* @return
	*/
	public static Date dayEnd(final Date date) {
	        Calendar c = Calendar.getInstance();
	        c.setTime(date);
	        c.set(Calendar.HOUR_OF_DAY, 23);
	        c.set(Calendar.MINUTE, 59);
	        c.set(Calendar.SECOND, 59);
	        c.set(Calendar.MILLISECOND, 999);
	        return c.getTime();
	}
	
}
