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

import com.izhubo.credit.schedule.SyncTikuScoreTaskSchedule;
import com.izhubo.credit.schedule.XfpercentTaskSchedule;
 
/**
 *定时生成学分完成率数据表
 *@author lintf
 */
@Service
public class XfPercentTaskInit  implements ApplicationListener<ContextRefreshedEvent> {
	private static Scheduler scheduler;
	private static Trigger trigger;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			return;
		}
		SessionFactory sessionFactory = (SessionFactory)event.getApplicationContext().getBean("sessionFactory");
	 		 if ("Root WebApplicationContext".equals(event.getApplicationContext().getParent().getDisplayName())) {// root  
				try {
					scheduler = StdSchedulerFactory.getDefaultScheduler();
					
					
					 
					if(trigger == null){
						trigger =(CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity("simpleTrigger8", "triggerGroup8")  
					              .withSchedule(CronScheduleBuilder.cronSchedule("0 0 10 4 * ?")) // 每月4号早上10点运算
 

								.startNow().build();   
					}
					//定义一个JobDetail
			         JobDetail job = JobBuilder.newJob(XfpercentTaskSchedule.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
			             .withIdentity("job8", "group8") //定义name/group
			             .usingJobData("name", "quartz") //定义属性
			             .build();
			         
			         if( scheduler.getJobDetail(job.getKey()) != null){
				        	//避免重复创建
				        	return;
				        }
			         
					 //加入这个调度
			         scheduler.scheduleJob(job, trigger);
			         scheduler.getContext().put("sessionFactory", sessionFactory);
				        
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
