package com.izhubo.utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 报表工具类
 * @author yy
 *
 */
public class DataUtils {
	
	public static final Long DAY_MI = 86400000L;
	private static final String SECRET_AESKEY = "%^$AF>.12*******";
//	private static final String SECRET_AESKEY = "%^\$AF>.12*******";
	public static void main(String[] args) throws ParseException {
//		System.out.println(SDF_yyyyMMdd.parse("2016-11-01").getTime());
//		System.out.println(SDF_yyyyMMdd.parse("2016-12-01").getTime());
//		System.out.println(SDF_DATETOSTRING.parse("2016-01-01 00:00:00").getTime());
//		System.out.println(SDF_DATETOSTRING.parse("2017-01-01 00:00:00").getTime());
//		System.out.println(UUID.randomUUID().toString());
//		System.out.println(MsgDigestUtil.MD5.digest2HEX("13541905982hqzxtk"));
//		System.out.println("ceb40ea85ade9d8715e41f3796c541b0");
		Date timestamp = new Date(1484120862164L);
		Date race_time = new Date(1484115267900L);
		Date update_at = new Date(1484118900325L);
		System.out.println(timestamp);
		System.out.println(race_time);
		System.out.println(update_at);
	/*	Long st = 1482890061810L;
		Date d = new Date(st);
		System.out.println(d);
		Long now = System.currentTimeMillis();
		Long mi = now - st;
		System.out.println(mi);
		System.out.println(TopicsDataUtils.msTomL(mi));
		try {
			Double mii = BMatch.sub(now, st);
			System.out.println(mii);
			System.out.println(TopicsDataUtils.msTomD(mii));
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
		
//		Long st = 1482830014509L;
//		Date d = new Date(st);
//		System.out.println(d);
//		Long now = System.currentTimeMillis();
//		Long mi = now - st;
//		System.out.println(mi);
//		System.out.println(TopicsDataUtils.msTom(mi));
//		
//		Long mi = (System.currentTimeMillis() - 1479131809569L);
//		System.out.println(mi);
//		String s = TopicsDataUtils.msTom(mi);
//		System.out.println(s);
//		http://nx.kjcity.com/Web/CourseByWap/List?phone=18620523707&k=51f7b085f57e78bbac680a6d9d05f5f4
		
		//用户手机号码
//				String username = "18620523707";
//				//加密后的结果
//				String  md5result = MsgDigestUtil.MD5.digest2HEX(username+SECRET_AESKEY).toLowerCase();
//				
//				System.out.print("http://nx.kjcity.com/Web/CourseByWap/List?phone="+username+"&k="+md5result);
//		String s = "123456789";
//		System.out.println(s.substring(0,5));
	}
	
	
	/**
	 * 初始的时间
	 * @param time
	 * @return
	 */
	public static Long beginTime(Long time){
		//2015-09-20
		Long stime = 0L;
//		Long stime = 1442678400000L;
		if(time < stime){
			return stime;
		}
		return time;
	}
	
	private static SimpleDateFormat SDF_DATETOSTRING=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	private static SimpleDateFormat SDF_DATETOSTRING_START=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
	
	
	private static SimpleDateFormat SDF_DATETOSTRING_END=new SimpleDateFormat("yyyy-MM-dd 23:59:59");
	
	private static SimpleDateFormat SDF_yyyyMMdd=new SimpleDateFormat("yyyy-MM-dd");  
	
	/**
	 * Long转Date转String
	 * @param time
	 * @return
	 */
	public static String dateToString(Long time){
		return SDF_yyyyMMdd.format(new java.util.Date(time));
	}
	
	public static String dateToString(String time) throws ParseException
	{
	  return	SDF_yyyyMMdd.format(SDF_yyyyMMdd.parse(time));
	}
	
	
	public static String dateToStringNormal(Long time) throws ParseException
	{
	  return	SDF_DATETOSTRING.format(new java.util.Date(time));
	}
	
	
	public static String dateToStringStart(Long time) {
		
		return	SDF_DATETOSTRING_START.format(new java.util.Date(time));
	}
	
      public static String dateToStringEnd(Long time) {
		
		return	SDF_DATETOSTRING_END.format(new java.util.Date(time));
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
	

	

	 // 获得当天0点时间  
  public static Date getTimesmorning() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(Calendar.HOUR_OF_DAY, 0);  
      cal.set(Calendar.SECOND, 0);  
      cal.set(Calendar.MINUTE, 0);  
      cal.set(Calendar.MILLISECOND, 0);  
      return cal.getTime();  


  }  
  // 获得昨天0点时间  
  public static Date getYesterdaymorning() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTimeInMillis(getTimesmorning().getTime()-3600*24*1000);  
      return cal.getTime();  
  }  
  // 获得当天近7天时间  
  public static Date getWeekFromNow() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTimeInMillis( getTimesmorning().getTime()-3600*24*1000*7);  
      return cal.getTime();  
  }  

  // 获得当天24点时间  
  public static Date getTimesnight() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(Calendar.HOUR_OF_DAY, 24);  
      cal.set(Calendar.SECOND, 0);  
      cal.set(Calendar.MINUTE, 0);  
      cal.set(Calendar.MILLISECOND, 0);  
      return cal.getTime();  
  }  

  // 获得本周一0点时间  
  public static Date getTimesWeekmorning() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
      cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);  
      return cal.getTime();  
  }  

  // 获得本周日24点时间  
  public static Date getTimesWeeknight() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTime(getTimesWeekmorning());  
      cal.add(Calendar.DAY_OF_WEEK, 7);  
      return cal.getTime();  
  }  

  // 获得本月第一天0点时间  
  public static Date getTimesMonthmorning() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));  
      return cal.getTime();  
  }  

  // 获得本月最后一天24点时间  
  public static Date getTimesMonthnight() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
      cal.set(Calendar.HOUR_OF_DAY, 24);  
      return cal.getTime();  
  }  

  public static Date getLastMonthStartMorning() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTime(getTimesMonthmorning());  
      cal.add(Calendar.MONTH, -1);  
      return cal.getTime();  
  }  

  public static Date getCurrentQuarterStartTime() {  
      Calendar c = Calendar.getInstance();  
      int currentMonth = c.get(Calendar.MONTH) + 1;  
      SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
      SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");  
      Date now = null;  
      try {  
          if (currentMonth >= 1 && currentMonth <= 3)  
              c.set(Calendar.MONTH, 0);  
          else if (currentMonth >= 4 && currentMonth <= 6)  
              c.set(Calendar.MONTH, 3);  
          else if (currentMonth >= 7 && currentMonth <= 9)  
              c.set(Calendar.MONTH, 4);  
          else if (currentMonth >= 10 && currentMonth <= 12)  
              c.set(Calendar.MONTH, 9);  
          c.set(Calendar.DATE, 1);  
          now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");  
      } catch (Exception e) {  
          e.printStackTrace();  
      }  
      return now;  
  }  

  /** 
   * 当前季度的结束时间，即2012-03-31 23:59:59 
   * 
   * @return 
   */  
  public static Date getCurrentQuarterEndTime() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTime(getCurrentQuarterStartTime());  
      cal.add(Calendar.MONTH, 3);  
      return cal.getTime();  
  }  


  public static Date getCurrentYearStartTime() {  
      Calendar cal = Calendar.getInstance();  
      cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);  
      cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.YEAR));  
      return cal.getTime();  
  }  

  public static Date getCurrentYearEndTime() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTime(getCurrentYearStartTime());  
      cal.add(Calendar.YEAR, 1);  
      return cal.getTime();  
  }  

  public static Date getLastYearStartTime() {  
      Calendar cal = Calendar.getInstance();  
      cal.setTime(getCurrentYearStartTime());  
      cal.add(Calendar.YEAR, -1);  
      return cal.getTime();  
  }  
	

	
}
