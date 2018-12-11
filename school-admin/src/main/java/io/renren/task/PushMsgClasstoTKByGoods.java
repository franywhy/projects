package io.renren.task;

import java.util.Map;

import io.renren.service.MessageKJClassService;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import io.renren.service.MessageXLXWClassService;
import io.renren.utils.SpringContextUtils;
@Component("io.renren.task.PushMsgClasstoTKByGoods")
public class PushMsgClasstoTKByGoods {

	public void execute(Map<String,Object> params) throws JobExecutionException {
        //获取时间参数
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        String commodityIds = (String) params.get("commodityIds");
        MessageKJClassService messageKJClassService = (MessageKJClassService)SpringContextUtils.getBean("messageKJClassService");
        messageKJClassService.pushToMessageQueueClass(startDate, endDate,commodityIds);
	}

}
