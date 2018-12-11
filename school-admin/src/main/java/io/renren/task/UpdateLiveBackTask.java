package io.renren.task;

import java.util.Map;

import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.service.CourseClassplanLivesService;
import io.renren.utils.SpringContextUtils;
@Component("io.renren.task.UpdateLiveBackTask")
public class UpdateLiveBackTask {
 
	public void execute(Map<String,Object> params) throws JobExecutionException{
//		System.out.println("======================执行up定时任务========================");
//		HttpServletRequest request = null ;
		CourseClassplanLivesService courseClassplanLivesService = (CourseClassplanLivesService) SpringContextUtils.getBean("courseClassplanLivesService");
		String datePickerStr = (String) params.get("datePicker");
		String startCountTime = (String) params.get("startCountTime");
		String endCountTime = (String) params.get("endCountTime");
		Integer datePicker=Integer.parseInt(datePickerStr);
		courseClassplanLivesService.updataPlaybackData(datePicker,startCountTime,endCountTime);
		
	}
}
