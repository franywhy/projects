package com.izhubo.common.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 报表工具类
 * @author yy
 *
 */
public class DataUtils {
	
	public static final Long DAY_MI = 86400000L;
	
	/**
	 * 初始的时间
	 * @param time
	 * @return
	 */
	public static Long beginTime(Long time){
		//2015-09-20
//		Long stime = 0L;
		Long stime = 1442678400000L;
		if(time < stime){
			return stime;
		}
		return time;
	}
	
	private static SimpleDateFormat SDF_DATETOSTRING=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	
	/**
	 * Long转Date转String
	 * @param time
	 * @return
	 */
	public static String dateToString(Long time){
		return SDF_DATETOSTRING.format(new java.util.Date(time));
	}
	
	
	
	
	/**
	 * java精度运算double 相加
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static double addBigDecimal(double d1,Float d2){
		BigDecimal bd1 = new BigDecimal(Double.toString(d1));
		BigDecimal bd2 = new BigDecimal(Float.toString(d2));
		return bd1.add(bd2).doubleValue();
	}
	
}
