package io.renren.task;

import java.util.Map;

import org.springframework.stereotype.Component;

import io.renren.service.MessageDivideClassesService;
import io.renren.utils.SpringContextUtils;

@Component("io.renren.task.PushMsgDivideClassesToTk")
public class PushMsgDivideClassesToTk {
	
	public void execute(Map<String,Object> params) {
		
		String startDate = (String) params.get("startDate");
		String endDate = (String) params.get("endDate");
		String commodityIds = (String) params.get("commodityIds");
		String productIds = (String) params.get("productIds");
		String watchType = (String) params.get("watchType");//请输入0或1或2 观看类型:直播+录播:0,直播:1,录播:2
		
		MessageDivideClassesService messageDivideClassesService = (MessageDivideClassesService) SpringContextUtils.getBean("messageDivideClassesService");
		messageDivideClassesService.pushToTkDivideClassesMessageQueue(startDate, endDate, commodityIds,productIds,watchType);
	}
	
}
