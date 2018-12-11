package io.renren.task;

import java.util.Map;

import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.service.MessageKJClassService;
import io.renren.utils.SpringContextUtils;
@Component("io.renren.task.PushMsgKJClasstoTK")
public class PushMsgKJClasstoTK  {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
		MessageKJClassService messageKJClassService = (MessageKJClassService)SpringContextUtils.getBean("messageKJClassService");
		messageKJClassService.pushToMessageQueueClass();
	}

}
