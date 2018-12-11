package com.hq.learningcenter.utils;

import org.apache.commons.lang.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期处理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年12月21日 下午12:53:33
 */
public class DateUtils {
	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_HOUR_MIN_PATTERN = "yyyy-MM-dd HH:mm";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN_FIXEDCRON = "s m H d M ?";
	/** 时间格式(yyyy-MM-dd HH:mm) */
	public final static String DATE_HOUR_MINUTE_PATTERN = "yyyy-MM-dd HH:mm";
	/** 时间格式(HH:mm) */
	public final static String HOUR_MINUTE_PATTERN = "HH:mm";
	
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }
    
    public static Date parse(String string , String pattern){
    	if(StringUtils.isNotBlank(string)){
    		try {
				return new SimpleDateFormat(pattern).parse(string);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
    	}
    	return null;
    }
    public static Date parse(String string){
    	return parse(string, DATE_PATTERN);
    }
    public static Date parse1(String string){
    	return parse(string, DATE_TIME_PATTERN);
    }
    
    /*** 
     *  
     * @param d 
     *            :Base Date 
     * @param day 
     *            :Delayed days 
     * @return 
     */  
    public static Date getDateAfter(Date d, int day) {
        Calendar now = Calendar.getInstance();
        now.setTime(d);
        now.set(Calendar.DATE, now.get(Calendar.DATE) + day);
        return now.getTime();
    }

    /***
     *
     * @param d : 基准时间
     * @param day : 几天前
     * @return
     */
    public static Date getDateBefore(Date d, int day) {
        Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.DATE, now.get(Calendar.DATE) - day);  
        return now.getTime();  
    }  
    
	/**
	 * 根据日期取得星期几 String[] weeks = {"星期日","星期一","星期二","星期三","星期四","星期五","星期六"};
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeek(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (week_index < 0) {
			week_index = 0;
		}
		return week_index;
	}
	
	/**
	 * 设置小时 
	 * @param hour
	 * @param date
	 * @return
	 */
	public static Date setHour(int hour, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }

    //
    /**
     * 设置分钟
     * @param minute
     * @param date
     * @return
     */
	public static Date setMinute(int minute, Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, minute);
        return calendar.getTime();
    }
	
	public static Date dateClear(Date date){
		Calendar cal=Calendar.getInstance();  
	    cal.setTime(date);
	    cal.set(Calendar.HOUR_OF_DAY, 0);
	    cal.set(Calendar.MINUTE, 0);
	    cal.set(Calendar.SECOND, 0);
	    return cal.getTime();
	}
	/**
	 * 讲毫秒数转化为yyyy-MM-dd hh:mm:ss
	 * @param date
	 * @return
	 */
	public static Date getDate(long date,int flag){
		DateFormat formatter=null;
		if(flag==1){
		  formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		}else if(flag==2){
			formatter = new SimpleDateFormat("yyyy-MM-dd");
		}else if(flag==3){
			formatter = new SimpleDateFormat("HH:mm");
		}else if(flag==4){
			formatter = new SimpleDateFormat("HH");
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date);
		String d= formatter.format(calendar.getTime());
		Date da=null;
		try {
			da = formatter.parse(d);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return da;
	}
	
	public static void main(String[] args) {
		/*String datestr = "10:14:41";
		Date date = parse(datestr,"HH:mm:ss");
		System.out.println(getCronByDate(date, 0));
		System.out.println(getCronByDate(date, 1));*/
	
		//String datestr = "10:14:41";
		//Date date = parse(datestr,"HH:mm:ss");
		//System.out.println(getCronByDate(date, 0));
		//System.out.println(getCronByDate(date, 1));
		
		System.out.println(getDateBeforeMinute(new Date(), 30));
		
		
	}
    
	public static Date getDateBeforeHour(Date d, int hour){
		Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.HOUR, now.get(Calendar.HOUR) - hour);  
        return now.getTime();
	}
	
	public static Date getDateBeforeMinute(Date d, int minute){
		Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minute);  
        return now.getTime();
	}
	
	public static Date getDateAfterMonth(Date d, int month){
		Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.MONTH, now.get(Calendar.MONTH) + month);  
        return now.getTime();
	}
	
	/**
	 * 验证日期格式是否为yyyy-MM-dd
	 * @param string
	 * @return
	 */
	public static boolean matchDateString(String string) {
        Pattern pattern = Pattern.compile("(19|20)[0-9][0-9]-(0?[1-9]|1[0-2])-(0?[1-9]|[12][0-9]|3[01])");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
	
	/**
	 * 获得一天的开始时间
	 * @param date
	 * @return
	 */
	public static String getDayStart(Date date){
		if(null == date) return null;
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		return fm.format(date);
	}
    
	/**
	 * 获得一天的结束时间
	 * @param date
	 * @return
	 */
	public static String getDayEnd(Date date){
		if(null == date) return null;
		DateFormat fm = new SimpleDateFormat("yyyy-MM-dd 23:59:59");
		return fm.format(date);
		
	}
	
	/**
	 * 获得一个月第一天的开始时间
	 * @param date
	 * @return
	 */
	public static String getMonStart(Date date){
		DateFormat fm = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		return fm.format(date);
		
	}
    
	/**
	 * 获得一个月最后一天的结束时间
	 * @param date
	 * @return
	 */
	public static String getMonEnd(Date date){
		Date d = getDateAfterMonth(date,1);
		d = parse(format(d));
		d = new Date(d.getTime() - 1000);
		DateFormat fm = new SimpleDateFormat("yyyy-MM-01 00:00:00");
		return fm.format(d);
		
	}
}
