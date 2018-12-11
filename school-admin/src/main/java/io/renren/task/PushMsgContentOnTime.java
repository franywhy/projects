package io.renren.task;

import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.service.MessageProductorService;
import io.renren.utils.SpringContextUtils;
//@Component("PushMsgContentOnTime")
@Component("io.renren.task.PushMsgContentOnTime")
public class PushMsgContentOnTime  {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
//		System.out.println("xxxxxxxxxxxxxxx");
		//实例化MsgContentService
		MessageProductorService messageProductorCourseClassplanServiceImpl = (MessageProductorService)SpringContextUtils.getBean("messageProductorCourseClassplanServiceImpl");
		messageProductorCourseClassplanServiceImpl.pushToMessageQueue();
		
		
	}

}
