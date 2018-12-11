package com.izhubo.credit.schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Resource;

import org.apache.axis2.client.Options;
import org.apache.axis2.transport.http.HTTPConstants;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

//import com.izhubo.credit.runnable.TikuOldRunnable;
import com.izhubo.credit.service.CreditService;
import com.izhubo.credit.service.CreditTaskLogService;
import com.izhubo.credit.service.impl.CreditServiceImpl;
import com.izhubo.credit.service.impl.CreditTaskLogServiceImpl;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.NcSyncConstant; 
import com.izhubo.credit.util.SyncUtil;
import com.izhubo.credit.vo.CreditOperationParameterVO;
import com.izhubo.schedule.PushMsgMainRemainSchedule;
import com.mysqldb.model.CreditStandard;

 
 
/**
 * 每天定时运行取题库中当月的作业完成率和实操结课考试</p>
 * 说明：先以前一天取得题库月份，再以题库月份开始时间到前一天的时间按10天为单位去取得题库作业和结课考试分</p>
 * 取得分数后保存到学分成绩单中同时更新学分档案中的分数为合格。
 * @author lintf
 *
 */
 
public class SyncTikuScoreTaskSchedule extends QuartzJobBean  {
	private static Logger logger = LoggerFactory.getLogger(PushMsgMainRemainSchedule.class);
	 
	
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		 work(context,null, null,null);
	}
	
  
  @SuppressWarnings("unchecked")
  
public void work(JobExecutionContext context,
		SessionFactory sessionFactory,
		CreditTaskLogService	creditTaskLogService,List<Integer> ids ) {
 	 
	  
	  
	  
	 
	   	if(sessionFactory == null){
	  		//不等于null是直接调用运行，等于null是后台任务调用运行
				if(context != null){
					//后台任务启动时获取sessionFactory
					Session session=null;
					try {
						SchedulerContext skedCtx = context.getScheduler().getContext();
						sessionFactory = (SessionFactory) skedCtx.get("sessionFactory");
						creditTaskLogService = (CreditTaskLogService) skedCtx.get("creditTaskLogService");
						String logid=creditTaskLogService.newLog("题库取数运算", DateUtil.DateToString(new Date())+"开始执行");
					    
						Date today=DateUtil.getBeforDay(new Date(), 1);//取得昨天
				 
			
				 
				 Map<String, String> datelist= DateUtil.GetTikuMonths(today);
				String Startdate= datelist.get("StartTime"); //取得开始日期
				 String Enddate = datelist.get("EndTime");//取得结束日期
				 int day_num= DateUtil.GetDateToNumber(today).get("day");
				 if (day_num<4||day_num>25){  //当今天小于4号或者大于25号时 取得往前10天的日期 ，并将题库的开始时间做为上个月的时间
					 Date last_mon_day=DateUtil.getBeforDay(new Date(), 10);//
					 Startdate=DateUtil.GetTikuMonths(last_mon_day).get("StartTime");
				 }
					 
				  
				String secretkey= NcSyncConstant.getNcSecretkey();
				
				
	 
				 	String StartTime="";
				 	String EndTime="";
 
				 	
			 	    Date s_date= DateUtil.StringToDate(Startdate);//循环开始时间
				    Date e_date=  DateUtil.getNextDay(s_date, 10);//结束时间 为开始加10天
				    Date FE_date=  DateUtil.StringToDate(Enddate); //循环结束时间
				    //当结束日期大于昨天时 以昨天为最后时间
				    if (CreditUtil.Compare_date(Enddate, DateUtil.DateToString( today))				    		
				    		){
				    	FE_date= today;
				    }
				    creditTaskLogService.setLog(logid, "循环时间为：【"+Startdate.substring(0,7)+" "+DateUtil.DateToString(FE_date).substring(0,7)+"】");
				    
				    
				 for (int i=0;i<200;i++){ 
					  if (e_date.after(FE_date)){ //当超过最大时间时
						  StartTime= DateUtil.DateToString(s_date).substring(0,10);
						  
						  EndTime=DateUtil.DateToString(FE_date).substring(0,10);
					     taskrun(sessionFactory, StartTime,EndTime,secretkey );
						 break;
					  }else {
						      StartTime= DateUtil.DateToString(s_date).substring(0,10);
						 	  EndTime=DateUtil.DateToString(e_date).substring(0,10);
						 	  taskrun(sessionFactory,StartTime,	EndTime,secretkey);
						 s_date=DateUtil.getNextDay(e_date,1);
						 e_date=DateUtil.getNextDay(s_date, 11);
						 if (CreditUtil.Compare_date(DateUtil.DateToString(s_date),DateUtil.DateToString(FE_date))){//当开始时间大于原定的结束时间时 直接跳出
							 break;
						 }
					  }
					
					 
					
					 
				 } 
				 	//补考成绩检测 
			       	 bukao (sessionFactory,null,null,secretkey); 
					 creditTaskLogService.closeLog(logid, "题库取数执行完成。"+DateUtil.DateToString(new Date())); 
			 
					} catch (Exception e) {
						e.printStackTrace();
					 }finally {
						if (session!=null){
							session.close();
							 System.out.println("题库成绩单执行完并关掉了sesssion");
						}
						 
				
					}
				}
				
				
				
				
				
		} 
  } 
  /**
   * 补考的学员的成绩取数 
   * @param sessionFactory
   * @param StartTime
   * @param EndTime
   * @param secretkey
   */
  void bukao(SessionFactory sessionFactory,  String StartTime,
		  String EndTime,String secretkey){
          Session session=null;
	  
	  try {
	     session =sessionFactory.openSession();
			
			
	   Map<String,CreditStandard> creditStandardmap= new HashMap<String,CreditStandard>();

		List<CreditStandard> st = session.createQuery("from CreditStandard").list();
		 
		 for(CreditStandard s:st){
			 creditStandardmap.put(s.getNc_id(), s);
		 }
		 String note=null;
		//题库考试的补考
		  Transaction txtikuexamFail = session.beginTransaction();	
		 	 SyncUtil.SyncTiKuscoreExamFail(session, creditStandardmap, txtikuexamFail, null, null, secretkey,note   );
	  } catch (Exception e) {
			e.printStackTrace();
		 }finally {
			if (session!=null){
				session.close();
			//	 System.out.println("题库成绩单执行完并关掉了sesssion");
			}
			 
	
		}
	   
  }
  
  
  void taskrun(SessionFactory sessionFactory,  String StartTime,
		  String EndTime,String secretkey
		  )  {
	  /**
	   * 由于总是执行了两次 现在改为一每一次运行保存一次
	   */
	  Session session=null;
	  Transaction tx=null;
	  try {
	     session =sessionFactory.openSession();
			
			
	   Map<String,CreditStandard> creditStandardmap= new HashMap<String,CreditStandard>();

		List<CreditStandard> st = session.createQuery("from CreditStandard").list();
		 
		 for(CreditStandard s:st){
			 creditStandardmap.put(s.getNc_id(), s);
		 }
	 String note=null;
	 tx= session.beginTransaction();
	  
   	   //题库成绩单
	 Transaction txtikuwork = null;
	 SyncUtil.SyncTiKuscoreWork(session, creditStandardmap, txtikuwork, StartTime, EndTime, secretkey, note);
	 	 
	 
	 //题库考试单
	 Transaction txtikuexam =null;
	 SyncUtil.SyncTiKuscoreExam(session, creditStandardmap, txtikuexam, StartTime, EndTime, secretkey, note);
	 	 	  
	 // 2017年之前的作业完成率的取数   
	  Transaction t = null;
	  SyncUtil.CheckHomeWorkCompletion(session, t,StartTime, EndTime);
	  
	  } catch (Exception e) {
			e.printStackTrace();
		 }finally {
			 if (tx!=null){
				 tx.commit();
			 }
			if (session!=null){
				session.close();
			//	 System.out.println("题库成绩单执行["+StartTime+"   "+EndTime+"]完并关掉了sesssion");
			}
			 
	
		}
	   
  }
   
  
}
