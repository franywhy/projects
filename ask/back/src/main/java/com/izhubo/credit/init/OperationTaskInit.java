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
import com.mysqldb.model.CreditOperationTask;
/**
 * 在服务器启动后获取数据库的设置时间，设置学分运算定时任务的启动时间。
 * 避免每次启动服务器要手工启动学分运算定时任务
 * @author 严志城
 * @time 2017年3月9日14:30:08
 */
@Service
public class OperationTaskInit  implements ApplicationListener<ContextRefreshedEvent> {
	private static Scheduler scheduler;
	private static Trigger trigger;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		/*不再使用本调度任务了 lintf 2017年11月1日10:32:43
		if(event.getApplicationContext().getParent() == null){
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
						trigger =(CronTriggerImpl) TriggerBuilder.newTrigger().withIdentity("simpleTrigger", "triggerGroup")  
					              .withSchedule(CronScheduleBuilder.cronSchedule("0 0 8 "+date+" * ?")) //每月date号8点0分运算 
					              .startNow().build();   
					}
					//定义一个JobDetail
			         JobDetail job = JobBuilder.newJob(OperationTaskSchedule.class) //定义Job类为HelloQuartz类，这是真正的执行逻辑所在
			             .withIdentity("job1", "group1") //定义name/group
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
	        }*/
	}

	public static Scheduler getScheduler() {
		return scheduler;
	}


	public static Trigger getTrigger() {
		return trigger;
	}


}
