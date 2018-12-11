package io.renren.utils;



/** 
 * @Description:  
 * 
 * @Title: QuartzManager.java 
 * @Package com.joyce.quartz 
 * @Copyright: Copyright (c) 2014 
 * 
 * @author Comsys-LZP 
 * @date 2014-6-26 下午03:15:52 
 * @version V2.0 
 */  
  
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;  
import org.quartz.SchedulerFactory;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;

import io.renren.entity.ScheduleJobEntity;  
  
/** 
 * @Description: 定时任务管理类 
 *  
 * @ClassName: QuartzManager 
 * @Copyright: Copyright (c) 2014 
 *  
 * @author Comsys-LZP 
 * @date 2014-6-26 下午03:15:52 
 * @version V2.0 
 */  
public class TaskManager {  
    private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();  
    private static String JOB_GROUP_NAME = "EXTJWEB_JOBGROUP_NAME";  
    private static String TRIGGER_GROUP_NAME = "EXTJWEB_TRIGGERGROUP_NAME";  
  
    /** 
     * @Description: 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名 
     *  
     * @param jobName 
     *            任务名 
     * @param cls 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:47:44 
     * @version V2.0 
     */  
//    @SuppressWarnings("unchecked")  
    public static void addJob(String jobName, Class cls, String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类  
            jobDetail.setName(jobName);
            jobDetail.setGroup(JOB_GROUP_NAME);
            jobDetail.setJobClass(cls);
            // 触发器  
            CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组  
            trigger.setName(jobName);
            trigger.setGroup(TRIGGER_GROUP_NAME);
            trigger.setCronExpression(time);// 触发器时间设定  
            sched.scheduleJob(jobDetail, trigger);  
            // 启动  
            if (!sched.isShutdown()) {  
                sched.start();  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    
    public static void addJob(ScheduleJobEntity jobEntity) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类  
            jobDetail.setName("Job" + jobEntity.getJobId());
            jobDetail.setGroup(jobEntity.getGroupName());
            jobDetail.setJobClass((Class) Class.forName(jobEntity.getBeanName())); 
            if(jobEntity.getParams()!=null && !jobEntity.getParams().equals("")){
            	String[] params = jobEntity.getParams().split(";");
            	for(String p : params){
            		String[] temps = p.split(":");
            		jobDetail.getJobDataMap().put(temps[0].trim(), temps[1].trim()); 
            	}
            }
            // 触发器  
            CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组  
            trigger.setName("Job" + jobEntity.getJobId());
            trigger.setGroup(jobEntity.getGroupName());
            trigger.setCronExpression(jobEntity.getCronExpression());// 触发器时间设定  
            sched.scheduleJob(jobDetail, trigger);  
            // 启动  
            if (!sched.isShutdown()) {  
                sched.start();   
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    
    /** 
     * @Description: 添加一个定时任务 
     *  
     * @param jobName 
     *            任务名 
     * @param jobGroupName 
     *            任务组名 
     * @param triggerName 
     *            触发器名 
     * @param triggerGroupName 
     *            触发器组名 
     * @param jobClass 
     *            任务 
     * @param time 
     *            时间设置，参考quartz说明文档 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:48:15 
     * @version V2.0 
     */  
//    @SuppressWarnings("unchecked")  
//    public static void addJob(String jobName, String jobGroupName,  
//            String triggerName, String triggerGroupName, Class jobClass,  
//            String time) {  
//        try {  
//            Scheduler sched = gSchedulerFactory.getScheduler();  
//            JobDetail jobDetail = new JobDetail(jobName, jobGroupName, jobClass);// 任务名，任务组，任务执行类  
//            // 触发器  
//            CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组  
//            trigger.setCronExpression(time);// 触发器时间设定  
//            sched.scheduleJob(jobDetail, trigger);  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        }  
//    }  
  
    /** 
     * @Description: 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     * @param time 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:49:21 
     * @version V2.0 
     */  
//    @SuppressWarnings("unchecked")  
    public static void modifyJobTime(String jobName, String time) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            TriggerKey triggerKey = new TriggerKey(jobName,TRIGGER_GROUP_NAME);
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return; 
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(time)) {
            	JobKey jobKey = new JobKey(jobName,JOB_GROUP_NAME);
                JobDetail jobDetail = sched.getJobDetail(jobKey);
                Class objJobClass = jobDetail.getJobClass();  
                removeJob(jobName);  
                addJob(jobName, objJobClass, time);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    
//    @SuppressWarnings("unchecked")  
    public static void modifyJobTime(ScheduleJobEntity entity) {  
        try {  
            Scheduler sched = gSchedulerFactory.getScheduler();  
            TriggerKey triggerKey = new TriggerKey("Job"+entity.getJobId(), entity.getGroupName());
            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerKey);
            if (trigger == null) {
                return;
            }
            String oldTime = trigger.getCronExpression();
            if (!oldTime.equalsIgnoreCase(entity.getCronExpression())) {
                removeJob(entity);  
                addJob(entity);  
            }  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
  
//    /** 
//     * @Description: 修改一个任务的触发时间 
//     *  
//     * @param triggerName 
//     * @param triggerGroupName 
//     * @param time 
//     *  
//     * @Title: QuartzManager.java 
//     * @Copyright: Copyright (c) 2014 
//     *  
//     * @author Comsys-LZP 
//     * @date 2014-6-26 下午03:49:37 
//     * @version V2.0 
//     */  
//    public static void modifyJobTime(String triggerName,  
//            String triggerGroupName, String time) {  
//        try {  
//            Scheduler sched = gSchedulerFactory.getScheduler();  
//            CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName,triggerGroupName);  
//            if (trigger == null) {  
//                return;  
//            }  
//            String oldTime = trigger.getCronExpression();  
//            if (!oldTime.equalsIgnoreCase(time)) {  
//                CronTrigger ct = (CronTrigger) trigger;  
//                // 修改时间  
//                ct.setCronExpression(time);  
//                // 重启触发器  
//                sched.resumeTrigger(triggerName, triggerGroupName);  
//            }  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        }  
//    }  
//  
    /** 
     * @Description: 移除一个任务(使用默认的任务组名，触发器名，触发器组名) 
     *  
     * @param jobName 
     *  
     * @Title: QuartzManager.java 
     * @Copyright: Copyright (c) 2014 
     *  
     * @author Comsys-LZP 
     * @date 2014-6-26 下午03:49:51 
     * @version V2.0 
     */  
    public static void removeJob(String jobName) {  
        try {  
        	TriggerKey triggerKey = new TriggerKey(jobName,TRIGGER_GROUP_NAME);
        	JobKey jobKey = new JobKey(jobName,JOB_GROUP_NAME);
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(jobKey);// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    public static void removeJob(ScheduleJobEntity jobEntity) {  
        try {  
        	TriggerKey triggerKey = new TriggerKey("Job"+jobEntity.getJobId(),jobEntity.getGroupName());
        	JobKey jobKey = new JobKey("Job"+jobEntity.getJobId(),jobEntity.getGroupName());
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.pauseTrigger(triggerKey);// 停止触发器  
            sched.unscheduleJob(triggerKey);// 移除触发器  
            sched.deleteJob(jobKey);// 删除任务  
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    }  
    
    
    //立即执行
    public static void runJob(ScheduleJobEntity jobEntity) {  
        try {  
        	TriggerKey triggerKey = new TriggerKey("Job"+jobEntity.getJobId(),jobEntity.getGroupName());
        	JobKey jobKey = new JobKey("Job"+jobEntity.getJobId(),jobEntity.getGroupName());
            Scheduler sched = gSchedulerFactory.getScheduler();  
            sched.triggerJob(jobKey);
        } catch (Exception e) {  
            throw new RuntimeException(e);  
        }  
    } 
    
    
//  
//    /** 
//     * @Description: 移除一个任务 
//     *  
//     * @param jobName 
//     * @param jobGroupName 
//     * @param triggerName 
//     * @param triggerGroupName 
//     *  
//     * @Title: QuartzManager.java 
//     * @Copyright: Copyright (c) 2014 
//     *  
//     * @author Comsys-LZP 
//     * @date 2014-6-26 下午03:50:01 
//     * @version V2.0 
//     */  
//    public static void removeJob(String jobName, String jobGroupName,  
//            String triggerName, String triggerGroupName) {  
//        try {  
//            Scheduler sched = gSchedulerFactory.getScheduler();  
//            sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器  
//            sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器  
//            sched.deleteJob(jobName, jobGroupName);// 删除任务  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        }  
//    }  
//  
//    /** 
//     * @Description:启动所有定时任务 
//     *  
//     *  
//     * @Title: QuartzManager.java 
//     * @Copyright: Copyright (c) 2014 
//     *  
//     * @author Comsys-LZP 
//     * @date 2014-6-26 下午03:50:18 
//     * @version V2.0 
//     */  
//    public static void startJobs() {  
//        try {  
//            Scheduler sched = gSchedulerFactory.getScheduler();  
//            sched.start();  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        }  
//    }  
//  
//    /** 
//     * @Description:关闭所有定时任务 
//     *  
//     *  
//     * @Title: QuartzManager.java 
//     * @Copyright: Copyright (c) 2014 
//     *  
//     * @author Comsys-LZP 
//     * @date 2014-6-26 下午03:50:26 
//     * @version V2.0 
//     */  
//    public static void shutdownJobs() {  
//        try {  
//            Scheduler sched = gSchedulerFactory.getScheduler();  
//            if (!sched.isShutdown()) {  
//                sched.shutdown();  
//            }  
//        } catch (Exception e) {  
//            throw new RuntimeException(e);  
//        }  
//    }  
   
    
} 