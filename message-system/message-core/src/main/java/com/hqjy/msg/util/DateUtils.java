package com.hqjy.msg.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils extends org.apache.commons.lang.time.DateUtils{

	public final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DATE_FORMAT_DAY = "yyyy-MM-dd";

	public final static String DATE_FORMAT_MOUNTH = "yyyyMM";

	public static  String dateToString(Date date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.format(date);
	}

	public static String dateToString(Date date) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
		return sdf.format(date);
	}
	
	public static Date stringToDate(String date,String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Date();
	}

	public static Date getNowDate(){
		
		return new Date();
	}



	
	/**
	 * 将日期转化成spring quartz的时间格式
	 * @param date 日期
	 * @param pushType 类型 0：一次 1：每天
	 * @return
	 */
	public static String getCronByDate(Date date,Integer pushType){
		
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		String pa = "";
		pa = pa + c.get(Calendar.SECOND) + " ";
		pa = pa + c.get(Calendar.MINUTE) + " ";
		pa = pa + c.get(Calendar.HOUR_OF_DAY) + " ";
		
		if (pushType==0) {
			pa = pa + c.get(Calendar.DAY_OF_MONTH) + " ";
			pa = pa + (c.get(Calendar.MONTH) + 1) + " ? ";
			pa = pa + c.get(Calendar.YEAR) + " ";
		}else{
			pa = pa + " *";
			pa = pa + " *";
			pa = pa + " ?";
			pa = pa + " *";
		}
		return pa;
	}

	/**
	 * 返回两个日期之差的分钟数
	 * @param date1
	 * @param date2
	 * @return
	 */
	public static int compare(Date date1,Date date2){
		Calendar cal = Calendar.getInstance();
		long time1 = 0 ;long time2 = 0;
		try {
			cal.setTime(date1);
			time1 = cal.getTimeInMillis();
			cal.setTime(date2);
			time2 = cal.getTimeInMillis();
		} catch (Exception e) {
			// TODO: handle exception
		}

		long between_days = (time2 - time1 )/(1000*60);
		//return Math.abs(Integer.parseInt(String.valueOf(between_days)));
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 判断时间格式 格式必须为“YYYY-MM-dd HH:mm:ss”
	 * 2004-2-30 是无效的
	 * 2003-2-29 是无效的
	 * @param str
	 * @return
	 */
	public static boolean isValidDate(String str) {
		//String str = "2007-01-02";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{
			Date date = (Date)formatter.parse(str);

			return str.equals(formatter.format(date));
		}catch(Exception e){
			return false;
		}
	}

	public static void main(String[] args) {
		String url = "" + "?"
				+ "category=65&category_parameter="

				+ "&tab=video&timestamp= &ac=WIFI&app_version=4.7.1"
				+ "&channel=App%20Store"
				+ "&device_platform=iphone&device_type=iPhone8%2C2&language=en"
				+ "&os=iOS&os_version=11.2.2"
				+ "&region=US&sys_language=zh-Hans&sys_region=MO&tz_name=Asia/Shanghai"
				//+ "&radio=CTRadioAccessTechnologyLTE&tz_offset=28800"
				//+"&sys_language=en&sys_region=CN"
				;
		System.out.println(url);
	}




}
