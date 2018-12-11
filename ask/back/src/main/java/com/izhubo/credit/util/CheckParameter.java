package com.izhubo.credit.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CheckParameter {
	
	/**判断输入的日期格式是否合法
	 * @param LastDateTime
	 * @return
	 */
	public static boolean checkTimeFormat(String dateTime) {
		boolean convertSuccess=true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if(dateTime != null && dateTime.length() != 10){
			convertSuccess = false;//时间格式严格按照    yyyy-MM-dd
		}
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，
			if(dateTime!=null){
				format.setLenient(false);
				format.parse(dateTime);
			}
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		} 
		return convertSuccess;
	}
	/**
	 * 校验字符串是否为空，
	 * @param str
	 * @return true代表空
	 */
	public static boolean checkString(String str){
		if(str == null || str.length() == 0){
			return true;
		}
		return false;
		
	}
	
	/**
	 * 检测月份是否合规则
	 * @param months
	 * @return
	 */
	public static boolean checkTimeFormatByMonths(String months) {
		boolean convertSuccess=true;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
		if(months != null && months.length() != 7){
			convertSuccess = false;//时间格式严格按照    yyyy-MM-dd
		}
		try {
			// 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，
			if(months!=null){
				format.setLenient(false);
				format.parse(months);
			}
		} catch (ParseException e) {
			// e.printStackTrace();
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			convertSuccess = false;
		} 
		return convertSuccess;
	}
	
	
	
}
