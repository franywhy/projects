package com.izhubo.credit.init;

import java.util.List;

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

import com.izhubo.credit.schedule.OperationTaskSchedule;
import com.izhubo.credit.schedule.RunProcessTaskSchedule;
import com.mysqldb.model.CreditOperationTask;
/**
 * 学分新的手动运算不再定时执行的了
 @author lintf 
 
 */
@Service
public class RunProcessTaskInit  implements ApplicationListener<ContextRefreshedEvent> {
	private static Scheduler scheduler;
	private static Trigger trigger;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
 /*		if(event.getApplicationContext().getParent() == null){
			return;
		}
		SessionFactory sessionFactory = (SessionFactory)event.getApplicationContext().getBean("sessionFactory");
		List<CreditOperationTask> list = sessionFactory.openSession().createQuery("from CreditOperationTask").list();
		 if ("Root WebApplicationContext".equals(event.getApplicationContext().getParent().getDisplayName())) {// root  
				try {
					scheduler = StdSchedulerFactory.getDefaultScheduler();
					int date = 3 ;//默认为3号
					if(list != null && list.size() > 0){
						date = list.get(0).getExecuteDate();
					}else{
						System.out.println("获取启动日期失败，查询数据为空!");
					}
					if(trigger == null){
						trigger =(CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity("simpleTrigger5", "triggerGroup5")  
					              .withSchedule(CronScheduleBuilder.cronSchedule("0 56 22 * * ?")) //每月date号8点0分运算 
					              .startNow().build();   
					}
					//定义一个JobDetail
			         JobDetail job = JobBuilder.newJob(RunProcessTaskSchedule.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
			             .withIdentity("job5", "group5") //定义name/group
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
	        } */
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}


	public static Trigger getTrigger() {
		return trigger;
	}


}
