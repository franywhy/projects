package io.renren.constant;

import java.util.HashMap;

public class DateTimeConstant {
	public static final String WEEK[] = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"}; 
	public static final String TIME_BUCKET[] = {"上午","下午","晚上"};
	public static final HashMap<String,Integer> CONVERT_WEEK_MAP = new HashMap<String,Integer>();
	public static final HashMap<String,Integer> CONVERT_TIME_BUCKET_MAP = new HashMap<String,Integer>();
	static
	{
		CONVERT_WEEK_MAP.put("星期日", 0);
		CONVERT_WEEK_MAP.put("星期一", 1);
		CONVERT_WEEK_MAP.put("星期二", 2);
		CONVERT_WEEK_MAP.put("星期三", 3);
		CONVERT_WEEK_MAP.put("星期四", 4);
		CONVERT_WEEK_MAP.put("星期五", 5);
		CONVERT_WEEK_MAP.put("星期六", 6);
		
		CONVERT_TIME_BUCKET_MAP.put("上午", 0);
		CONVERT_TIME_BUCKET_MAP.put("下午", 1);
		CONVERT_TIME_BUCKET_MAP.put("晚上", 2);
	}
}
