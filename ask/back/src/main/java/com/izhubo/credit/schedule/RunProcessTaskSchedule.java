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
import com.izhubo.credit.service.CreditTaskLogService;
import com.izhubo.credit.service.impl.CreditTaskLogServiceImpl;
import com.izhubo.credit.util.CheckParameter;
import com.izhubo.credit.util.CreditDaoUtil;
import com.izhubo.credit.util.CreditUtil; 
import com.izhubo.credit.util.NcSyncConstant;
import com.izhubo.credit.util.SyncUtil;
import com.izhubo.credit.util.SyncUtilRest;
import com.izhubo.credit.vo.CreditOperationParameterVO;
import com.izhubo.schedule.PushMsgMainRemainSchedule;
import com.mysqldb.model.CreditOperationLog;
import com.mysqldb.model.CreditStandard;
 
 /**
  * 学分制中同步任务，本任务已经不再是自动执行的了只能在前端运算。自动运算的已经迁移到恒企同步平台sync中</p>
  
  * @author lintf 
  *
  */
public class RunProcessTaskSchedule extends QuartzJobBean implements org.quartz.StatefulJob {
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
	   	boolean attRun =false;//是否运算考勤
	   	boolean regRun =false;//是否运算报名表
	   	boolean tikuRun=false;//是否运算题库成绩单
	   	boolean ncexam=false;//是否运算NC中的考试成绩单（含学分单）
	   	String begin_reg_time =null;//报名表修改时间
	   	String end_reg_time=null;//
	   	
	   	String begin_att_time =null;//结课日期
	   	String end_att_time=null;
	   	
		String begin_exam_time =null;//NC成绩单修改日期
		String end_exam_time =null;
	 
		String begin_tk_time =null;//题库月份
		String secretkey= NcSyncConstant.getNcSecretkey();
	   	String note="";
	  
  
	   	
	   	
		if(para!=null){
			
			begin_reg_time=para.getRegBeginDate() ;
			end_reg_time=para.getRegEndDate();
			begin_att_time=para.getAttendanceBeginDate();
			end_att_time=para.getAttendanceEndDate();
			begin_exam_time=para.getExamBeginDate();
			end_exam_time=para.getExamEndDate();
			begin_tk_time=para.getTikuMonth();
			  
			   	 attRun =para.isSyncatt();
			     regRun =para.isSyncreg();
			   	 tikuRun=para.isSynctk();
			     ncexam=para.isSyncexam();
		
	    Session session =  null;
	    CreditOperationLog log= new CreditOperationLog();
	    String logstr="";
		  try{
				session =  sessionFactory.openSession();
				 Map<String,CreditStandard> creditStandardmap= new HashMap<String,CreditStandard>();
			  
				List<CreditStandard> st = session.createQuery("from CreditStandard").list();
				 
				 for(CreditStandard s:st){
					 creditStandardmap.put(s.getNc_id(), s);
				 }
				 
				 Map<String,String> notcheckmap= new HashMap<String,String>();
				  List<String> subject=CreditDaoUtil.getDef(session, "DEFDEB201712110163888888");
					if (subject!=null&&subject.size()>0){
						for (String sub:subject){
							if (sub!=null){
								notcheckmap.put(sub, sub);
							}
						}
						
						
					}
				
				 
				 
		            /***********开始执行******************/		 
				 
					log.setStartTime(new Date());
					log.setTaskname("手动执行");
					 
			 
					 logstr="手动运算"
						+"【 运算考勤:"+attRun +","+begin_att_time+"-"+end_att_time+" 】"  
						+"【 运算题库成绩单:"+tikuRun +","+begin_tk_time+" 】" 
						+"【 运算NC成绩单:"+ncexam +","+begin_exam_time+"-"+end_exam_time+" 】" 
						+"【 运算报名表 :"+regRun +","+begin_reg_time+"-"+end_reg_time+" 】" ;
					 log.setLogstr(logstr);
					 session.save(log);
					// CreditTaskLogService tasklogService= new CreditTaskLogServiceImpl();	
					   CreditOperationLog runMsg= new CreditOperationLog();
					// String logid=tasklogService.BeginLog(session, "学分制手动运算", new Date() , null, logstr);
				     this.taskrun(session, creditStandardmap,notcheckmap, secretkey, attRun, regRun, tikuRun, ncexam, begin_reg_time, end_reg_time, begin_att_time, end_att_time, begin_exam_time, end_exam_time, begin_tk_time, note);
				 
				     runMsg.setLogstr(logstr+"   "+note);
				     session.save(runMsg);
				     
				     //	  String newlogstr="";
				 //    tasklogService.EndLog(session, new Date() , newlogstr, logid);
				     
				 
 
			} catch (Exception e) {
//				String info = "学分运算:通过Scheduler获取的SessionFactory异常："+e.getMessage();
//				System.out.println(info);
//				logger.info(info);
				e.printStackTrace();
				logstr+="运算错误: "+e.getMessage();
			}finally {
					if (session!=null){
						session.close();
					 
						 System.out.println("关掉了sesssion");
					}
					 
			
				}
	   	
	   	
	   	
		}
	   	
	   	
	   	
	   	
	   	
	   	
	   	
   } 
  
  void taskrun(Session session, Map<String,CreditStandard> creditStandardmap,Map<String,String> notcheckmapString,String secretkey,
		  
	boolean attRun,
 	boolean regRun,
 	boolean tikuRun,
 	boolean ncexam,
 	String begin_reg_time,
   	String end_reg_time,
   	
   	String begin_att_time,
   	String end_att_time,
   	
	String begin_exam_time,
	String end_exam_time,
 
	String begin_tk_time, String note
		  ){
		 
	  SyncUtilRest sync= new SyncUtilRest();
	  if (regRun&&CheckParameter.checkTimeFormat(begin_reg_time)&&CheckParameter.checkTimeFormat(end_reg_time)){ // 同步报名表
/*		  Transaction txreg = session.beginTransaction();
		  List<String> studentidlists=SyncUtil.SyncReg(session, creditStandardmap,notcheckmapString, txreg, begin_reg_time, end_reg_time, secretkey, note);	
		  //重新运算学分档案	   
		  if (studentidlists!=null&studentidlists.size()>0){
		  Transaction txcord = session.beginTransaction();
		  SyncUtil.computeCreditRecordByStudentIdList(session, creditStandardmap, txcord, studentidlists, note); 
	  */
		 
		  sync.sync(begin_reg_time, end_reg_time, sync.CREDITSYNC_TYPE_REG);
		  
		  
		  
		//  }
		  
	  }
     
	  if (attRun&&CheckParameter.checkTimeFormat(begin_att_time)&&CheckParameter.checkTimeFormat(end_att_time)){
		  //同步考勤
		/*   Transaction txnckq = session.beginTransaction();
		 SyncUtil.SyncKQrate(session, creditStandardmap, txnckq, begin_att_time, end_att_time, secretkey, note);*/
		  sync.sync(begin_att_time, end_att_time, sync.CREDITSYNC_TYPE_ATTEND);
	  }
	  
	  if (ncexam&&CheckParameter.checkTimeFormat(begin_exam_time)&&CheckParameter.checkTimeFormat(end_exam_time)){
         //同步NC成绩单 
	/* Transaction txncscore = session.beginTransaction();
	 SyncUtil.SyncNCscore(session, creditStandardmap, txncscore, begin_exam_time, end_exam_time, secretkey, note);*/
		  sync.sync(begin_exam_time, end_exam_time, sync.CREDITSYNC_TYPE_SCORE);
	  }
	  
	  if (tikuRun&&CheckParameter.checkTimeFormat(begin_tk_time+"-01") ){
		  
		  SyncUtil.SyncCreditTikuMonth( session, creditStandardmap,
				  begin_tk_time,secretkey ,note);
	  
	  }
	  
	 /*	//重新运算学分制
	  Transaction txrule = session.beginTransaction();
	 SyncUtil.SyncCreditRecordRule(session, creditStandardmap, txrule, StartTime, EndTime, secretkey, note);
 	*/ 
	  
  			 System.out.println("运行完。");		
  }
  
  
   
  
}
