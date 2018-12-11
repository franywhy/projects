package com.izhubo.credit.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 这个是学分制中取得时间的工具类
 * @author Administrator
 *
 */
public class DateUtil {
	
/**
 * 根据当前日期取得昨天
 * @param date
 * @return
 */
	public static Date getYesterDay(Date date){
		Calendar calendar = Calendar.getInstance();  
        calendar.setTime(date);  
        calendar.add(Calendar.DAY_OF_MONTH, -1);  
        date = calendar.getTime();  
        return date;  
		
	}
	/**
	 * 取未天几天
	 * @param date
	 * @param nextnum
	 * @return
	 */
	public static Date getNextDay(Date date,int nextnum){
		 
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        calendar.add(Calendar.DAY_OF_MONTH, +nextnum);//+当天加nextnum天
		        date = calendar.getTime();
		        return date;
		  
	}
	/**
	 * 取得befornum月前
	 * @param date 
	 * @param befornum 往前几个月
	 * @return
	 */
	public static Date getBeforMonth(Date date,int befornum){
		 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -befornum);//+当天加nextnum天
        date = calendar.getTime();
        return date;
  
}
	
	/**
	 * 取未来几个月
	 * @param date
	 * @param nextnum 未来几个月
	 * @return
	 */
	public static Date getNextMonth(Date date,int nextnum){
		 
		        Calendar calendar = Calendar.getInstance();
		        calendar.setTime(date);
		        calendar.add(Calendar.MONTH, +nextnum);//+当天加nextnum天
		        date = calendar.getTime();
		        return date;
		  
	}
	/**
	 * 取得befornum天前
	 * @param date 
	 * @param befornum 往前几天
	 * @return
	 */
	public static Date getBeforDay(Date date,int befornum){
		 
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -befornum);//+当天加nextnum天
        date = calendar.getTime();
        return date;
  
}
	
	
	
	
	
	
	
	
	/**
	 *  yyyy-MM-dd HH:mm:ss 转成 Date
	 * @param datestr
	 * @return
	 */
	 public static Date StringToDate(String  datestr)  {
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			 
				try {
					return dateFormat.parse (datestr);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			 
			 
		} 
	 
	 
	 
	 
	 /**
		 *  yyyy-MM 转成 Date
		 * @param datestr
		 * @return
		 */
		 public static Date MonthToDate(String  datestr)  {
				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
				 
					try {
						return dateFormat.parse (datestr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return null;
				 
				 
			} 
	/**
	 * 将日期转换成yyyy-MM-dd HH:mm:ss格式的文本
	 * @param date
	 * @return
	 */
	public static String DateToString(Date date){
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return dateFormat.format(date);
	}
	
	/**
	 * 题库中存的日期格式为 2017/9/8 0:00:00 要转换为date
	 * @param datestr
	 * @return
	 */
	 public static Date StringToDateByTiku(String  datestr)  {
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		 
			try {
				return dateFormat.parse (datestr);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		 
		 
	} 
	
	public static String Befor30MM(Date date){
		Date beforTre = new Date(date.getTime() - 1800000); //10分钟前的时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//可以方便地修改日期格式
		return   dateFormat.format(beforTre);
	}
	/**
	 * 取得前30分钟到现在的时间范围
	 * @param date
	 * @return
	 */
	public static Map<String,String> GetBefor30MM(Date date){
		Map<String,String> map= new HashMap<String,String>();
		map.put("StartTime",Befor30MM(date));
		map.put("EndTime",  DateToString(date));
		return map;
	}
	
	
	/**
	 * 根据当前时间取所属题库取数范围
	 * @param date
	 * @return StartTime EndTime

	 */
	public static Map<String,String> GetTikuMonths(Date date){
		
		
		Map<String,String> map= new HashMap<String,String>();
		Map<String,Integer>  datelist= GetDateToNumber(date);
		int startdate=27; //月初日期
		int enddate=26; //月结日期
		int t_year=datelist.get("year");
		int t_month=datelist.get("month");
		int t_day=datelist.get("day");
		Date StartTime= new Date();
		Date EndTime= new Date();
		if (t_day>enddate){//当今天的日期大于当月结束时  范围为当月的月结到本月的月初到下个月的月结 即 大于26时 就是当月的27号到下个朋的26号
              if (t_month==12){
            	  
            	  StartTime=GetNumberToDate(t_year,t_month,27);            	 
            	  t_year=t_year+1;
            	  t_month=1;
            	  EndTime=GetNumberToDate(t_year,t_month,26);
              }else {
                StartTime=GetNumberToDate(t_year,t_month,27);
            	EndTime=GetNumberToDate(t_year,t_month+1,26);
              }

			
		}else {//当今天的日期小于 等于月结日期时  范围为前一月的月结到本月的月初 即 如果 小于等于26时 则为前一个月的27到本月的26
			if (t_month==1){
				  EndTime=GetNumberToDate(t_year,t_month,26);
				  t_month=12;
				  t_year=t_year-1;
				  StartTime=GetNumberToDate(t_year,t_month,27);
			}else {
				  EndTime=GetNumberToDate(t_year,t_month,26);
				  StartTime=GetNumberToDate(t_year,t_month-1,27);	
			}
			
		}
		
		map.put("StartTime",DateToString(StartTime));
		map.put("EndTime",  DateToString(EndTime));
		return map;
		
	}
	 
	/**
	 * 将年月日 转成 date
	 * @param year 2017
	 * @param month 1
	 * @param day 1
	 * @return
	 */
	public static Date GetNumberToDate(int year,int month,int day){
		 Calendar ca=Calendar.getInstance(); 
		 ca.set(year, month-1, day);
		
		
		return ca.getTime();
		 
	}
	/**
	 * 将Date转成整形数字的年月日
	 * @param date
	 * @return
	 */
	public static Map<String,Integer> GetDateToNumber(Date date){
		Map<String,Integer> map = new HashMap<String,Integer>();
		 Calendar ca=Calendar.getInstance();  
		    ca.setTime(date); 
		     int year =ca.get(Calendar.YEAR);    
			 int month=ca.get(Calendar.MONTH)+1;
			 int day=ca.get(Calendar.DAY_OF_MONTH); 
			 map.put("year", year);
			 map.put("month", month);
			 map.put("day", day);
		return map;
	}
	/**
	 * 取当天是星期几 
	 * @param date
	 * @return 0 星期天、1 星期一
	 */
	public static int GetWeekDay(Date date){
	Calendar cal=Calendar.getInstance();
	cal.setTime(date); 
	 return cal.get(Calendar.DAY_OF_WEEK)-1;
	}
	/**
	 * 得到本月的第一天   
	 * @return   
	 */    
	public static String getMonthFirstDay(Date date) {     
	    Calendar calendar = Calendar.getInstance();  
	    calendar.setTime(date); 
	     
	    calendar.set(Calendar.DAY_OF_MONTH, calendar     
	            .getActualMinimum(Calendar.DAY_OF_MONTH));     
	    
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
		return   dateFormat.format(calendar.getTime());
	    
	}     
	    
	/**   
	 * 得到本月的最后一天   
	 *    
	 * @return   
	 */    
	public static String getMonthLastDay(Date date) {     
	    Calendar calendar = Calendar.getInstance();    
	    calendar.setTime(date); 
	    calendar.set(Calendar.DAY_OF_MONTH, calendar     
	            .getActualMaximum(Calendar.DAY_OF_MONTH));     
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");//可以方便地修改日期格式
	  		return   dateFormat.format(calendar.getTime()); 
	}     
	
	/**
	 * 根据开始日期       到结束日期  取得范围内的所有月份列表 map<"2017-01","2017-01">
	 * @param startday YYYY-MM-DD 超过7位
	 * @param endday YYYY-MM-DD 超过7位
	 * @return key为"YYYY-MM"
	 */
	public static Map<String,String> getMonthList(Date startday,Date endday){
		Map<String,String> root= new HashMap<String,String> ();
		
 
		  
		  String end_month= DateToString(endday).substring(0,7);	 
		 
		 
		  for (String  begin_month= DateToString(startday).substring(0,7);
				  CreditUtil.Compare_month(end_month, begin_month)>=0;  begin_month=DateToString( getNextMonth(MonthToDate(begin_month),1)).substring(0,7)
								  ){
			  root.put(begin_month, begin_month);
			

			 
		  }
		
		
		return root;
	}
	
}
