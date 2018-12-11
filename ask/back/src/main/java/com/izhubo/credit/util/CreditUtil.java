package com.izhubo.credit.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.mysqldb.model.CreditDef;
import com.mysqldb.model.CreditPerMon;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordNcSore;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditRecordTiKuScore;
import com.mysqldb.model.Creditpercent;

/**
 * 学分制中业务工具类
 * @author lintf	
 *
 */
public class CreditUtil {
	
	 
 
 
    
 
    /**  
     * 生成主键(20位数字) 
     * 主键生成方式,年月日时分秒毫秒的时间戳+四位随机数保证不重复 
     */    
    public static  String getId() {  
        //当前系统时间戳精确到毫秒  
        Long simple=System.currentTimeMillis();  
        //三位随机数  
        int random=new Random().nextInt(900000)+1000000;//为变量赋随机值100-999;  
        return simple.toString()+random;    
    }    
    /**
     * 生成单据号  
     * @param billkey 前缀
     * @return
     */
    public static  String getVbillcode(String billkey) {  
        //当前系统时间戳精确到毫秒  
     //   Long simple=System.currentTimeMillis();  
        String date=DateUtil.DateToString(new Date()).substring(0,10);     
        String[] datelist=date.split("-");
        String datenum="";
        for (String x:datelist){
        	datenum=datenum+x;
        }
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY)*1000000;
        int minute = cal.get(Calendar.MINUTE)*10000;
        int second = cal.get(Calendar.SECOND)*100;
        int mm = cal.get(Calendar.MILLISECOND);
        String  all=String.valueOf( hour+minute+second+mm);
        if (all.length()<8){
        	all="0"+all;
        }
        
        int random=new Random().nextInt(9999999)+100000000;//为变量赋随机值100-999;  
        return billkey+datenum+all+random;    
    }    
    
 
    /**
     * 某个月份到某个月份的目标完成率 
     * @param session
     * @param start_dbilldate   当前计算月份_开始
     * @param end_dbilldate    当前计算月份_结束
     
     * @return  key:dbilldate_months </p> value:Double 
     */
    public static Map<String,Double> getPercentMonth(Session session,String start_dbilldate,String end_dbilldate ){
    Map<String,Double>  PerMon=new HashMap<String,Double> ();
	 
	
	String qslq = " from CreditPerMon where dr =0  and dbilldate >= ? and dbilldate<=?";
	Query qs = session.createQuery(qslq);
	qs.setString(0, start_dbilldate.substring(0,7));
	qs.setString(1, end_dbilldate.substring(0,7));
	 
	List<CreditPerMon> pvo = qs.list();
	 
	
	if (pvo!=null&&pvo.size()>0){
		for (CreditPerMon p:pvo){
			PerMon.put(p.getDbilldate()+"_"+p.getMonths(), p.getMbpercent().doubleValue());
		}
	} 
    
    return PerMon;
    }
    
    
    /**
     * 某个月份到某个月份的排课目标完成率 
     * @param session
     * @param start_dbilldate   当前计算月份
     * @param end_dbilldate    报名表的开始月份
     
     * @return  key:dbilldate_months </p> value:Double 
     */
    public static Map<String,Double> getPaikePercentMonth(Session session,String start_dbilldate,String end_dbilldate ){
    Map<String,Double>  PerMon=new HashMap<String,Double> ();
	 
	
	String qslq = " from CreditPerMon where dr =0  and dbilldate >= ? and dbilldate<=?";
	Query qs = session.createQuery(qslq);
	qs.setString(0, start_dbilldate.substring(0,7));
	qs.setString(1, end_dbilldate.substring(0,7));
	 
	List<CreditPerMon> pvo = qs.list();
	 
	
	if (pvo!=null&&pvo.size()>0){
		for (CreditPerMon p:pvo){
			PerMon.put(p.getDbilldate()+"_"+p.getMonths(), p.getPkpercent().doubleValue());
		}
	} 
    
    return PerMon;
    }
    
    
    
    /**
     * 四舍五入
     * @param f
     * @return
     */
    public static  double Double_half_up(double f){
  
    BigDecimal   b   =   new   BigDecimal(f);
  return b.setScale(2,   RoundingMode.HALF_UP).doubleValue();
    }
    /**
     * 日期比较，精确到日。当date1大于date2 时返回true; 其他时间返回false 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean Compare_date(String date1,String date2){
    	  DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
          try {
              Date dt1 = df.parse(date1);
              Date dt2 = df.parse(date2);
              if (dt1.getTime() > dt2.getTime()) {
                   return true;
                 
              } else if (dt1.getTime() < dt2.getTime()) {
                  return false;
              } else {
                 return false;
              }
          } catch (Exception exception) {
        	  return false;
          }
              }
    
    /**
     * 日期比较，精确到月。当date1大于date2 时返回1; 相同时返回0 小于时返回-1,错误时 -9
     * @param date1
     * @param date2
     * @return
     */
    public static int Compare_month(String date1,String date2){
    	  DateFormat df = new SimpleDateFormat("yyyy-MM");
          try {
              Date dt1 = df.parse(date1);
              Date dt2 = df.parse(date2);
              if (dt1.getTime() > dt2.getTime()) {
                   return 1;
                 
              } else if (dt1.getTime() == dt2.getTime()) {
                  return 0;
              } else if (dt1.getTime() < dt2.getTime()) {
                 return -1;
              }
          } catch (Exception exception) {
        	  return -9;
          }
		      return -9;
              }
    
    
    
    
    
    /**
     * 取得总实修学分TotalScore 和总应修学分 Claimscore 判断相等时通过 ispass=0
     * @param d
     */
    public static void  CheckCreditRecordPass(CreditRecord d){
    	 if (d!=null){   		 
    	 
    	int AttendanceActualScore=	d.getAttendanceActualScore()==null?0:d.getAttendanceActualScore();
    	int AttendanceClaimScore=		d.getAttendanceClaimScore()==null?0:d.getAttendanceClaimScore();
    	int WorkActualScore=	d.getWorkActualScore()==null?0:d.getWorkActualScore();
    	int WorkClaimScore=	d.getWorkClaimScore()==null?0:d.getWorkClaimScore();
    	int ExamActualScore=	d.getExamActualScore()==null?0:d.getExamActualScore();
    	int ExamClaimScore=	d.getExamClaimScore()==null?0:d.getExamClaimScore();
    	d.setTotalScore( AttendanceActualScore+WorkActualScore+ExamActualScore);//总实修学分
    	d.setClaimScore(AttendanceClaimScore+WorkClaimScore+ExamClaimScore);//总应修学分
    	 if (d.getTotalScore()==d.getClaimScore()){
    		 d.setIsPass(0);
    	 }else {
    		 d.setIsPass(1);
    	 }
    	}
    	
    }
     
    /**
     * fenzi/fenmu的百分比 0.00%
     * @param fenzi 分子
     * @param fenmu 分母
     * @return
     */
    public static String getPercentString(int fenzi,int fenmu){
    	if (fenzi==0||fenmu==0){
    		return "0.00%";
    	}else{
    		 double parate = (fenzi/fenmu)*100;
			  DecimalFormat df=new DecimalFormat("0.00");
			 return   (df.format(parate)+"%");
    	}
    	
    }
    /**
     * 比转是否通过 a/b>=per 则为true
     * @param a 
     * @param b
     * @param per
     * @return
     */
    public static boolean isPass(Double a,Double b,Double per){
    	 BigDecimal dataa = new BigDecimal(a);  
    	 BigDecimal datab = new BigDecimal(b);  
    	 BigDecimal datap = new BigDecimal(per);  
    	 BigDecimal temp=dataa.divide(datab,2,BigDecimal.ROUND_HALF_UP);
    	 if ( temp.compareTo(datap) >= 0) { 
    			return true;
    	 }else {
		return false;
    	 }
    }
    
    
    
    
  
	  
	 
	 
	  
	  
	    
	 /**
	  * 从Map<String, CreditRecord> 中取得hid
	  * @param d
	  * @return
	  */
	 public static List<String> getCreditSignHid(Map<String, CreditRecord> d){
		
		 Map<String,String> hidmap= new HashMap<String,String>();
		 for(Entry<String, CreditRecord> entry:d.entrySet()){
			 if (entry.getValue()!=null&&entry.getValue().getIsEnable()==0&&entry.getValue().getSignId()!=null){
				 hidmap.put(entry.getValue().getSignId(), "");
			 }
		 }
		 List<String> hids=new ArrayList<String>(hidmap.keySet());
		 return hids; 
		 
	 }
	 
		
		/**
		 * xy中的最大数
		 * @param x
		 * @param y
		 * @return
		 */
		public static int getMaxNum(int x,int y){
			 if (y>x){
				 return y;
			 }else {
				 return x;
			 }
			
		}
		/**
		 * 当Str1为空时取str2
		 * @param str1
		 * @param str2
		 * @return
		 */
		public static String getStringBlank(String str1,String str2){
			 
				
				if (str1==null&&str2!=null){
					return str2;
				}else {
					return str1;
				}
				
			 
		}
		
		
		/**
		 * 两个对比，学分的取最大值，老师和班级的空的取有的   
		 * @param old 旧的档案
		 * @param d 新的档案
		 */
		public static void CompareSameRecord(CreditRecord  old,CreditRecord d){
			if (old!=null&&d!=null){
				old.setAttendanceActualScore(
						getMaxNum(old.getAttendanceActualScore(),d.getAttendanceActualScore())
						);
				old.setWorkActualScore(
						getMaxNum(old.getWorkActualScore(),d.getWorkActualScore())
						);
				old.setExamActualScore (
						getMaxNum(old.getExamActualScore(),d.getExamActualScore())
						);
				old.setTotalScore  (
						getMaxNum(old.getTotalScore(),d.getTotalScore())
						);
				old.setClaimScore(
						getMaxNum(old.getClaimScore(),d.getClaimScore())
						);
				
				
				old.setTeacherId(getStringBlank(old.getTeacherId(),d.getTeacherId()));
				old.setClassName(getStringBlank(old.getClassName(),d.getClassName()));
				old.setOrgCode(getStringBlank(old.getOrgCode(),d.getOrgCode()));
				old.setExamType(getStringBlank(old.getExamType(),d.getExamType()));
				
			  
				old.setkQ(d.getkQ());
				old.setrS(d.getrS());
				old.setkqV(d.getkqV()); 
				
			}
			
		}
		
		
		
		
		
		
		
}
