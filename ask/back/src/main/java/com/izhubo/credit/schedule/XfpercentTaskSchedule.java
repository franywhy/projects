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
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.google.gson.reflect.TypeToken;
import com.izhubo.credit.service.CreditTaskLogService;
import com.izhubo.credit.service.CreditXfpercentService;
import com.izhubo.credit.service.impl.CreditTaskLogServiceImpl;
import com.izhubo.credit.service.impl.CreditXfpercentServiceImpl;
import com.izhubo.credit.util.CheckParameter;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil;
import com.izhubo.credit.util.DateUtil;
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SyncUtil;
import com.izhubo.credit.vo.CreditOperationParameterVO;
import com.izhubo.schedule.PushMsgMainRemainSchedule;
import com.mysqldb.model.CreditDef;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditPercentDetail;
import com.mysqldb.model.CreditRecord;
import com.mysqldb.model.CreditRecordSign;
import com.mysqldb.model.CreditRecordSignCVO;
import com.mysqldb.model.CreditStandard;
import com.mysqldb.model.Creditpercent;
 
 /**
  * 学分制中生成学分单据</p>
  *1.保存学员的基础数据到credit_percent_detail中
  *2.根据credit_percent_detail生成汇总credit_percent
  * @author lintf 
  *
  */
public class XfpercentTaskSchedule extends QuartzJobBean implements org.quartz.StatefulJob {
	private static Logger logger = LoggerFactory.getLogger(PushMsgMainRemainSchedule.class);
	
	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
	//	System.out.println("调用了新的执行");
		 work(context,null, null,null);
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
						logger.info(info);
						return;
					}
					} catch (SchedulerException e) {
						String info = "学分运算:通过Scheduler获取的SessionFactory异常："+e.getMessage();
						System.out.println(info);
						logger.info(info);
						e.printStackTrace();
					}
				}
		}
	   	
	   	/**
	   	 * 开始定义变量
	   	 */
	    String dbilldate=null;
	    String s_months=null;
	    String e_months=null;
	   	String note=null;
	  
	   	
	  
			   	 
		
	    Session session =  null;
	    CreditOperationLog log= new CreditOperationLog();
	    String logstr="";
		  try{
				session =  sessionFactory.openSession();
				 
				  Transaction tx = session.beginTransaction();
				
		            /***********开始执行******************/		 
				 
				
			 	
				if(para!=null){
					
					s_months=para.getRegBeginDate() ;
					e_months=para.getRegEndDate(); 
					dbilldate=para.getTikuMonth();
				}else {
					
					
				//	如果是空的话说明是后台执行的 
					
				 
					
					dbilldate=DateUtil.DateToString(DateUtil.getBeforMonth(new Date(), 1)).substring(0,7);
					s_months="2017-01";
					e_months=dbilldate;
					
				}  
				
				
				
				
					log.setStartTime(new Date());
					log.setTaskname("手动生成学分单据");
					 
			 String pid=CreditUtil.getVbillcode("XFD");
					 logstr="手动生成学分单据"
						+"【 本月月份:"+dbilldate+"  】"  
						+"【 开始月份:"+s_months+" 】" 
						+"【 结束月份:"+e_months+" 】";  
					 
					 log.setLogstr(logstr);
					 log.setLogid(pid);
					 
					  
					 
					 
		 		// Transaction tx = session.beginTransaction();
		 		//  System.out.println("进入删除 ");
			  // SyncUtil.SyncDelSameRecordBill (session,tx);
  this.taskrun(session,tx,dbilldate,s_months,e_months, note,pid,log);
			 				 tx.commit();
 
			} catch (Exception e) {
//				String info = "学分运算:通过Scheduler获取的SessionFactory异常："+e.getMessage();
//				System.out.println(info);
//				logger.info(info);
				e.printStackTrace();
				if (e.getCause()!=null){
					logstr+="运算错误: "+e.getCause();
				}else {
					logstr+="运算错误: "+e.getMessage();
				}
				 
				 if (session!=null&&session.getTransaction()!=null){
					 
				 Transaction tx = session.getTransaction();
				 log.setLogstr(logstr);
				 log.setEndTime(new Date());
				 session.save(log);
				 tx.commit();
				 }
				 
			}finally {
				
				 
				
					if (session!=null){
						session.close();
					 
						 System.out.println("生成学分报表关掉了sesssion");
					}
					 
			
				}
	   	
	   	
	   	

	   	
	   	
	   	
	   	
	   	
	   	
	   	
   } 
  
  private void taskrun(Session session,   Transaction tx,String dbilldate, String s_months,
		String e_months, String note,String pid,CreditOperationLog log) throws Exception {
	    
	  
	
	  SyncUtil.SyncXfPercentreReport(session, tx, dbilldate, s_months, e_months, pid, note, log);
	  
	  
 
}

 
  
   
  
}
