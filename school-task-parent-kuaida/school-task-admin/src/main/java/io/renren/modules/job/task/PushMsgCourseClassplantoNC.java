package io.renren.modules.job.task;

import java.util.Map;

import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.service.MessageCourseClassplanService;
/**
 * 新增或更新双师课程排课计划推送到nc定时任务
 * @author liuhai 2018/8/31
 *
 */
@Component("io.renren.modules.job.task.PushMsgCourseClassplantoNC")
public class PushMsgCourseClassplantoNC {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
		
		MessageCourseClassplanService messageCourseClassplanService = (MessageCourseClassplanService)SpringContextUtils.getBean("messageCourseClassplanService");
		messageCourseClassplanService.pushToNCCourseClassplanMessageQueue();
	}
	
	
}
