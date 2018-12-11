package com.izhubo.credit.init;

 

import org.hibernate.SessionFactory;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.izhubo.credit.schedule.SyncTikuScoreTaskSchedule;
import com.izhubo.credit.service.CreditService;
import com.izhubo.credit.service.CreditTaskLogService;
 
/**
 *学分中每天取题库的作业完成率和实操结课作业 	 
 *@author lintf
 */
@Service
@Transactional
public class SyncTikuScoreTaskInit  implements ApplicationListener<ContextRefreshedEvent> {
	private static Scheduler scheduler;
	private static Trigger trigger;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			return;
		}
		SessionFactory sessionFactory = (SessionFactory)event.getApplicationContext().getBean("sessionFactory");
		//操作日志
		CreditTaskLogService creditTaskLogService = (CreditTaskLogService)event.getApplicationContext().getBean("creditTaskLogService");
	//	CreditService creditService=(CreditService)event.getApplicationContext().getBean("creditService");
		
		
		if ("Root WebApplicationContext".equals(event.getApplicationContext().getParent().getDisplayName())) {// root  
				try {
					scheduler = StdSchedulerFactory.getDefaultScheduler();
					
					
					 
					if(trigger == null){
						trigger =(CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity("simpleTrigger3", "triggerGroup3")  
					              .withSchedule(CronScheduleBuilder.cronSchedule("0 0 4 * * ?")) // 每天4点运算
								  //   .withSchedule(CronScheduleBuilder.cronSchedule("0 34 15 * * ?")) // 每天2点运算

								.startNow().build();   
					}
					//定义一个JobDetail
			         JobDetail job = JobBuilder.newJob(SyncTikuScoreTaskSchedule.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
			             .withIdentity("job3", "group3") //定义name/group
			             .usingJobData("name", "quartz") //定义属性
			             .build();
			         
			         if( scheduler.getJobDetail(job.getKey()) != null){
				        	//避免重复创建
				        	return;
				        }
			         
					 //加入这个调度
			         scheduler.scheduleJob(job, trigger);
			         scheduler.getContext().put("sessionFactory", sessionFactory);
			         scheduler.getContext().put("creditTaskLogService", creditTaskLogService);
			       //  scheduler.getContext().put("creditService", creditService);
			         
			         //启动之
			         scheduler.start();
				} catch (SchedulerException e) {
					System.out.println("scheduler异常："+e.getMessage());
					e.printStackTrace();
				}
	        }else{
	        	System.out.println("Root WebApplicationContext为空!");
	        }
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}


	public static Trigger getTrigger() {
		return trigger;
	}


}
