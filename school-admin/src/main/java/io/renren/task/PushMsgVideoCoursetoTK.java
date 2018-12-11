package io.renren.task;

import java.util.Map;

import org.springframework.stereotype.Component;
import io.renren.service.MessageVideoCourseService;
import io.renren.utils.DateUtils;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.PushMsgVideoCoursetoTK")	
public class PushMsgVideoCoursetoTK  {
	
	public void execute(Map<String,Object> params) {
		//获取时间参数
		String startDate = (String) params.get("startDate");
		String endDate = (String) params.get("endDate");
		String commodityIds = (String) params.get("commodityIds");
		MessageVideoCourseService messageVideoCourseService = (MessageVideoCourseService)SpringContextUtils.getBean("messageVideoCourseService");
		messageVideoCourseService.pushToMessageQueueClass(startDate, endDate, null, commodityIds);
	}
	
}
