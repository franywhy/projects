package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.service.CourseClassplanLivesService;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("io.renren.modules.job.task.UpdateLiveBackTask")
public class UpdateLiveBackTask {
 
	public void execute(Map<String,Object> params) throws JobExecutionException {
		System.out.println("======================执行up定时任务========================");
//		HttpServletRequest request = null ;
		CourseClassplanLivesService courseClassplanLivesService = (CourseClassplanLivesService) SpringContextUtils.getBean("courseClassplanLivesService");
		String datePickerStr = (String) params.get("datePicker");
		String startCountTime = (String) params.get("startCountTime");
		String endCountTime = (String) params.get("endCountTime");
		Integer datePicker=Integer.parseInt(datePickerStr);
		courseClassplanLivesService.updataPlaybackData(datePicker,startCountTime,endCountTime);
		
	}
}
