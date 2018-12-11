package io.renren.modules.job.task;

import io.renren.common.utils.SpringContextUtils;
import io.renren.modules.job.service.MessageClassByProductIdService;
import io.renren.modules.job.service.MessageKJClassService;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component("io.renren.modules.job.task.PushMsgClasstoTKByProductId")
public class PushMsgClasstoTKByProductId {
	
	public void execute(Map<String,Object> params) throws JobExecutionException {
		MessageClassByProductIdService messageClassServiceByProductId = (MessageClassByProductIdService) SpringContextUtils.getBean("messageClassServiceByProductId");
        messageClassServiceByProductId.pushToMessageQueueClass();
	}

}
