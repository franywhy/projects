package io.renren.modules.job.task;

import java.util.Map;

import io.renren.common.utils.SpringContextUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.modules.job.service.MessageProductorService;
//@Component("PushMsgContentOnTime")
@Component("io.renren.modules.job.task.PushMsgContentOnTime")
public class PushMsgContentOnTime  {


	public void execute(Map<String,Object> params) throws JobExecutionException {
//		System.out.println("xxxxxxxxxxxxxxx");
		//实例化MsgContentService
		MessageProductorService messageProductorCourseClassplanServiceImpl = (MessageProductorService) SpringContextUtils.getBean("messageProductorCourseClassplanServiceImpl");
		messageProductorCourseClassplanServiceImpl.pushToMessageQueue();
		
		
	}

}
