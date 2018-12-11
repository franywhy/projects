package com.izhubo.credit.init;

import java.util.Date;
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
import com.izhubo.credit.schedule.SyncRegTaskSchedule;
import com.mysqldb.model.CreditOperationTask;
import com.mysqldb.model.CreditRecord;
/**
 *  每天1点钟同步前一天的报名表信息
 * @author lintf
 * @time 2017年9月28日14:10:19
 */
@Service
public class SyncRegTaskInit  implements ApplicationListener<ContextRefreshedEvent> {
	private static Scheduler scheduler;
	private static Trigger trigger;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if(event.getApplicationContext().getParent() == null){
			return;
		}
/*		System.out.println("开始进入NC同步接口"+new Date());
		SessionFactory sessionFactory = (SessionFactory)event.getApplicationContext().getBean("sessionFactory");
		
      if ("Root WebApplicationContext".equals(event.getApplicationContext().getParent().getDisplayName())) {// root  
				try {
					scheduler = StdSchedulerFactory.getDefaultScheduler();
					int date = 3 ;//默认为3号
//					if(list != null && list.size() > 0){
//						date = list.get(0).getExecuteDate();
//					}else{
//						System.out.println("获取启动日期失败，查询数据为空!");
//					}
				 
					if(trigger == null){
						trigger =(CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity("simpleTrigger1", "triggerGroup1")  
					              .withSchedule(CronScheduleBuilder.cronSchedule("0 0/1 * * * ?")) //每天1点开始执行
					              .startNow().build();   
					}
					//定义一个JobDetail
			         JobDetail job = JobBuilder.newJob(SyncRegTaskSchedule.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
			             .withIdentity("job2", "group1") //定义name/group
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
			         System.out.println("启动job2"+new Date());
			 			       
				} catch (SchedulerException e) {
					System.out.println("scheduler异常："+e.getMessage());
					e.printStackTrace();
				}
	        }else{
	        	System.out.println("Root WebApplicationContext为空!");
	        }*/
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}


	public static Trigger getTrigger() {
		return trigger;
	}


}
