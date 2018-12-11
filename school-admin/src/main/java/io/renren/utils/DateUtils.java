package io.renren.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

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
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN_FIXEDCRON = "s m H d M ? y";
	
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
	/**
	 * 加减分钟
	 * @param minute
	 * @param date
	 * @return
	 */
	public static Date addMinute(int minute, Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, minute);
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
		
		if (pushType==Constant.MaterialDetailPushType.ONE_PUSH.getValue()) {
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
	 * 将日期转化成spring quartz的时间格式
	 * @param date 日期
	 * @param pushType 类型 0：一次 1：每天
	 * @param date 格式 若为空，则是yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCronByDate(String strdate,Integer pushType,String pattern){
		Date date = null;
		if (null==pattern || pattern.trim().equals("")) {
			date= parse(strdate,DATE_TIME_PATTERN);
		}else{
			date= parse(strdate,pattern);
		}
		if (null==date) {
			return "";
		}
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		String pa = "";
		pa = pa + c.get(Calendar.SECOND) + " ";
		pa = pa + c.get(Calendar.MINUTE) + " ";
		pa = pa + c.get(Calendar.HOUR_OF_DAY) + " ";
		
		if (pushType==Constant.MaterialDetailPushType.ONE_PUSH.getValue()) {
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
	 * 时长转毫秒  时间格式:HH:mm:ss
	 * @param duration	HH:mm:ss
	 * @return	毫秒数
	 */
	public static Long videDuration(String duration){
		long second = 0;
		if(StringUtils.isNotBlank(duration)){
			try {
//				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");  
//				second = sdf.parse(duration).getTime();
		        String[] my =duration.split(":");
		        int hour =Integer.parseInt(my[0]);
		        int min =Integer.parseInt(my[1]);
		        int sec =Integer.parseInt(my[2]);
		        second =(hour*3600+min*60+sec)*1000;
//		        System.out.println(second);
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		
        return second;
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
		
//		System.out.println(getDateBeforeMinute(new Date(), 30));
		
//		System.out.println(videDuration("02:26:51"));
//		System.out.println(videDuration("00:00:01"));
//		System.out.println(new Date(1502675732000L));
		
		
		
	}
	
//	public static Long videDuration(String time){
////		String time ="01:22:12";
//        String[] my =time.split(":");
//        int hour =Integer.parseInt(my[0]);
//        int min =Integer.parseInt(my[1]);
//        int sec =Integer.parseInt(my[2]);
//        long totalMs =(hour*3600+min*60+sec)*1000;
//        System.out.println(totalMs);
//        return totalMs;
//	}
    
	public static Date getDateBeforeMinute(Date d, int minute){
		Calendar now = Calendar.getInstance();  
        now.setTime(d);  
        now.set(Calendar.MINUTE, now.get(Calendar.MINUTE) - minute);
        return now.getTime();
	}
	
	/**
	 * 验证日期格式是否为yyyy-MM-dd
	 * @param string
	 * @return
	 */
	public static boolean matchDateString(String string) {
        Pattern pattern = Pattern.compile("(19|20)[0-9][0-9]-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])");
        Matcher matcher = pattern.matcher(string);
        return matcher.find();
    }
	/**
	 * 订单有效期,获取当前时间完后day天后的时间
	 * @param day	推移时间
	 * @return		推移后的时间
	 */
	public static Date orderDateValidity(Long day){
		return getDateAfter(new Date(), day.intValue());
	}
	/**
	 * 同步订单有效期,获取当前时间完后day天后的时间
	 * @param day	推移时间
	 * @return		推移后的时间
	 */
	public static Date orderDateValiditySync(Date date, Long day){
		return getDateAfter(date, day.intValue());
	}
	
	
	
	/**
	 * 获取起始时间戳,如果传入时间为空，则获取当天
	 * @return
	 * @throws ParseException 
	 */
	public static Long getStartTimeLong(String time_param) throws ParseException{
		Calendar todayStart = Calendar.getInstance();
		if(StringUtils.isNotBlank(time_param)){
			SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
			String time = time_param;
			Date date = format.parse(time);
			todayStart.setTime(date);
		}
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime().getTime();
	}
	
	/**
	 * 获取结束时间戳,如果传入时间为空，则获取当天
	 * @return
	 * @throws ParseException 
	 */
	public static Long getEndTimeLong(String time_param) throws ParseException{
		Calendar todayEnd = Calendar.getInstance();
		if(StringUtils.isNotBlank(time_param)){
			SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN);
			String time = time_param;
			Date date = format.parse(time);
			todayEnd.setTime(date);
			}
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime().getTime();
	}
	
	/**
	   * 获取现在时间
	   * 
	   * @return 返回时间类型 yyyy-MM-dd
	   */
	public static String getStringDate() {
		   Date currentTime = new Date();
		   SimpleDateFormat formatter = new SimpleDateFormat(DATE_PATTERN);
		   String dateString = formatter.format(currentTime);
		   return dateString;
		}
	
	/**
	 * 日期增加一天
	 */
	public static String addOneday(String today){
		SimpleDateFormat f =  new SimpleDateFormat(DATE_PATTERN);
		try   {
			Date  d  =  new Date(f.parse(today).getTime()+24*3600*1000);
			return  f.format(d);
		}
		catch(Exception ex) {
			return   "输入格式错误";
		}
	}
	/**
	 * 日期减少一天
	 */
	public static String subtractOneday(String today){
		SimpleDateFormat f =  new SimpleDateFormat(DATE_PATTERN);
		try   {
			Date  d  =  new Date(f.parse(today).getTime()-24*3600*1000);
			return  f.format(d);
		}
		catch(Exception ex) {
			return   "输入格式错误";
		}
	}
		/**
	 * 验证日期格式是否为yyyy/MM/dd
	 * @param string
	 * @return
	 */
	public static boolean checkDateString(String string) {
		Pattern pattern = Pattern.compile("(19|20)[0-9][0-9]/(0[1-9]|1[0-2])/(0[1-9]|[12][0-9]|3[01])");
		Pattern pattern1 = Pattern.compile("(19|20)[0-9][0-9]/([1-9]|1[0-2])/([1-9]|[12][0-9]|3[01])");
		Matcher matcher = pattern.matcher(string);
		Matcher matcher1 = pattern1.matcher(string);
		return matcher.find() || matcher1.find();
	}
	
	/**
	 * 时间戳转化为Sting
	 * @param param_time
	 * @return
	 */
	public static String longFormatString(Long paramTime){
		SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long time = new Long(paramTime);
		String d = format.format(time);
		return d;
	}
	
	
}
