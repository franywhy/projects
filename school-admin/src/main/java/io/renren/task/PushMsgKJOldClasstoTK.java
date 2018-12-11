package io.renren.task;

import io.renren.service.MessageKJOldClassService;
import io.renren.utils.SpringContextUtils;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("io.renren.task.PushMsgKJOldClasstoTK")
public class PushMsgKJOldClasstoTK {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
        MessageKJOldClassService messageKJOldClassService = (MessageKJOldClassService)SpringContextUtils.getBean("messageKJOldClassService");
        messageKJOldClassService.pushToMessageQueueClass();
	}

}
