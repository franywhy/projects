package io.renren.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.renren.service.NcAttendanceService;
import io.renren.utils.DateUtils;
import io.renren.utils.SpringContextUtils;
import io.renren.utils.TaskManager;

public class AttendanceJob implements Job {
	Logger log = LoggerFactory.getLogger(AttendanceJob.class);
	private static String JOB_GROUP_NAME = "ATTENDANCE_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "ATTENDANCE_TRIGGERGROUP_NAME";
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		// TODO Auto-generated method stub
		NcAttendanceService ncAttendanceService = (NcAttendanceService) SpringContextUtils
				.getBean("ncAttendanceService");

		final List list = ncAttendanceService.getAttendanceData();
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		try {
			cachedThreadPool.execute(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					job(list);
				}
			});

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void job(List list) {

		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			for (int i = 0; i < list.size(); i++) {
				// data
				Map map = (Map) list.get(i);
				if (null != map) {
					String time = (String)map.get("time");
					String classId = (String) map.get("class_plan");
					List datas = (List) map.get("data");

					// Scheduler
					String jobName = "AttendanceData" + (i);
					time = DateUtils.format(DateUtils.getDateBeforeMinute(DateUtils.parse(time, "yyyy-MM-dd HH:mm:ss"), 40), "yyyy-MM-dd HH:mm:ss");
					// String time = "2017-06-13 16:00:00";
					scheAdd(sched, jobName, DateUtils.getCronByDate(time, 0, ""), datas, classId);
				}

			}

			// 启动
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void scheAdd(Scheduler sched, String jobName, String time, Object object, String classId) {
		try {
			JobDetailImpl jobDetail = new JobDetailImpl();// 任务名，任务组，任务执行类
			jobDetail.setName(jobName);
			jobDetail.setGroup(JOB_GROUP_NAME);
			jobDetail.setJobClass(AttendancePushJob.class);
			jobDetail.getJobDataMap().put("datas", object);
			jobDetail.getJobDataMap().put("class_id", classId);
			// 触发器
			CronTriggerImpl trigger = new CronTriggerImpl();// 触发器名,触发器组
			trigger.setName(jobName);
			trigger.setGroup(TRIGGER_GROUP_NAME);

			trigger.setCronExpression(time);
			// 触发器时间设定
			sched.scheduleJob(jobDetail, trigger);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
