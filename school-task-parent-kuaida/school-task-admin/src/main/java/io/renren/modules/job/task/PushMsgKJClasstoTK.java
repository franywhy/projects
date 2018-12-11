package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.service.MessageKJClassService;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("io.renren.modules.job.task.PushMsgKJClasstoTK")
public class PushMsgKJClasstoTK {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
		MessageKJClassService messageKJClassService = (MessageKJClassService) SpringContextUtils.getBean("messageKJClassService");
		messageKJClassService.pushToMessageQueueClass();
	}

}
