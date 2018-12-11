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
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SyncUtil;
import com.izhubo.credit.vo.CreditOperationParameterVO;
import com.izhubo.schedule.PushMsgMainRemainSchedule;
import com.mysqldb.model.CreditPerMon;
import com.mysqldb.model.CreditStandard;
 
 /**
  * 学分制中同步任务，本任务已经不再是自动执行的了只能在前端运算。自动运算的已经迁移到恒企同步平台sync中</p>
  
  * @author lintf 
  *
  */
public class SyncRegTaskSchedule extends QuartzJobBean implements org.quartz.StatefulJob {
	private static Logger logger = LoggerFactory.getLogger(PushMsgMainRemainSchedule.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
	//	 work(context,null, null,null);
	}
	
  
  @SuppressWarnings("unchecked")
public void work(JobExecutionContext context,
		SessionFactory sessionFactory,
		CreditOperationParameterVO para,List<Integer> ids ) { 
 	 
	  
	  
	  
	 
	   	if(sessionFactory == null){
	  		//不等于null是直接调用运行，等于null是后台任务调用运行
				if(context != null){
					//后台任务启动时获取sessionFactory
					try {
						SchedulerContext skedCtx = context.getScheduler().getContext();
						sessionFactory = (SessionFactory) skedCtx.get("sessionFactory");
					if(sessionFactory == null){
						String info = "学分运算:通过Scheduler获取的SessionFactory位空";
						System.out.println(info);
					//	logger.info(info);
						return;
					}

					Session session =sessionFactory.openSession();
					/*Map<String,Double>  PerMon=new HashMap<String,Double> ();

					String qslq = " from CreditPerMon ";
					Query qs = session.createQuery(qslq);
					 
					List<CreditPerMon> pvo = qs.list();
					 
					
					if (pvo!=null&&pvo.size()>0){
						for (CreditPerMon p:pvo){
							PerMon.put(p.getMonths(), p.getMbpercent().doubleValue());
						}
					}
					*/
					
					

					  Map<String,String> notcheckmap= new HashMap<String,String>();
					  List<String> subject=CreditDaoUtil.getDef(session, "DEFDEB201712110163888888");
						if (subject!=null&&subject.size()>0){
							for (String sub:subject){
								if (sub!=null){
									notcheckmap.put(sub, sub);
								}
							}
							
							
						}
					
					
					
					
		 	  
	 				 Map<String,CreditStandard> creditStandardmap= new HashMap<String,CreditStandard>();
				  
					List<CreditStandard> st = session.createQuery("from CreditStandard").list();
					 
					 for(CreditStandard s:st){
						 creditStandardmap.put(s.getNc_id(), s);
					 }
				 String note=null;
				String StartTime= DateUtil.DateToString( DateUtil.getYesterDay(new Date())).substring(0, 10);
				String EndTime= DateUtil.DateToString(new Date()).substring(0, 10);
				  
				
				
				String secretkey= NcSyncConstant.getNcSecretkey();
				 	
			 	 Date startdate= DateUtil.StringToDateByTiku("2017/1/1 0:00:00") ;
			 //Date startdate= DateUtil.StringToDateByTiku("2017/10/11 0:00:00") ;
				Date enddate=DateUtil.getNextDay(startdate, 2);
				 for (int i=0;i<20;i++){
					 
					
					  StartTime= DateUtil.DateToString(startdate).substring(0,10);

					  EndTime=DateUtil.DateToString(enddate).substring(0,10);
					 
					   
				//	 taskrun(session,creditStandardmap,notcheckmap,StartTime,	EndTime,secretkey,note);
					 startdate=enddate;
					 enddate=DateUtil.getNextDay(startdate, 2);
					 
				 }
				session.close();
				
				
					
					
					
					
					
					
					
					
					
					
					
					
					
					} catch (SchedulerException e) {
						String info = "学分运算:通过Scheduler获取的SessionFactory异常："+e.getMessage();
						System.out.println(info);
						logger.info(info);
						e.printStackTrace();
					}
				}
				
				
				
				
				
		} 
   } 
  
  void taskrun(Session session, Map<String,CreditStandard> creditStandardmap,Map<String,String> notcheckmap, String StartTime,
		  String EndTime,String secretkey,String note
		  ){
		 
	  System.out.println("开始进入调度任务。日期为【" +StartTime+"】 到 【"+EndTime+"】 ,启动时间为"+DateUtil.DateToString(new Date()));	
 // 同步报名表
	  Transaction txreg = session.beginTransaction();
	  List<String> studentidlists=SyncUtil.SyncReg(session, creditStandardmap, notcheckmap,txreg, StartTime, EndTime, secretkey, note);	
//重新运算学分档案	   
	  Transaction txcord = session.beginTransaction();
	 SyncUtil.computeCreditRecordByStudentIdList(session, creditStandardmap, txcord, studentidlists, note); 
  
  //同步NC成绩单 
	 Transaction txncscore = session.beginTransaction();
	 SyncUtil.SyncNCscore(session, creditStandardmap, txncscore, StartTime, EndTime, secretkey, note);
		
	 //题库成绩单
	 Transaction txtikuwork = session.beginTransaction();
	 SyncUtil.SyncTiKuscoreWork(session, creditStandardmap, txtikuwork, StartTime, EndTime, secretkey, note);
	 //题库考试单
	 Transaction txtikuexam = session.beginTransaction();	
	 SyncUtil.SyncTiKuscoreExam(session, creditStandardmap, txtikuexam, StartTime, EndTime, secretkey, note);
  
	 //同步考勤
	   Transaction txnckq = session.beginTransaction();
	 SyncUtil.SyncKQrate(session, creditStandardmap, txnckq, StartTime, EndTime, secretkey, note);
 	//重新运算学分制
	  Transaction txrule = session.beginTransaction();
	 SyncUtil.SyncCreditRecordRule(session, creditStandardmap, txrule, StartTime, EndTime, secretkey, note);
 	 
	  
  			 System.out.println("运行完。");		
  }
  
  
   
  
}
