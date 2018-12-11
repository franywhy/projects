package io.renren.task;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.renren.service.NcAttendanceService;
import io.renren.service.NcCoursesService;
import io.renren.service.QuestionBankService;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.SyncDataJob")
public class SyncDataJob {

	Logger log = LoggerFactory.getLogger(SyncDataJob.class);

	public void execute(Map<String,Object> params){
		// TODO Auto-generated method stub
		ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
		
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					syncNcAttendance();
				}
			});
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					syncNcCourse();
				}
			});
			cachedThreadPool.execute(new Runnable() {
				public void run() {
					syncNcQuestionBank();
				}
			});
		
	}
	
	@Transactional
	private void syncNcAttendance(){
		NcAttendanceService ncAttendanceService = (NcAttendanceService) SpringContextUtils
				.getBean("ncAttendanceService");
		ncAttendanceService.syncData();
	}
	
	@Transactional
	private void syncNcCourse(){
		NcCoursesService ncCoursesService =(NcCoursesService) SpringContextUtils.getBean("ncCoursesService");
		ncCoursesService.syncData();
	}
	
	@Transactional
	private void syncNcQuestionBank(){
		QuestionBankService questionBankService =(QuestionBankService) SpringContextUtils.getBean("questionBankService");
		questionBankService.syncData();
	}
}
