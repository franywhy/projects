package io.renren.service;

/**
 * 会计班级消息推送到题库
 * @class io.renren.service.MessageKJClassService.java
 * @Description:
 * @author shihongjie
 * @dete 2017年10月13日
 */
public interface MessageKJClassService {
	
	void pushToMessageQueueClass();

    void pushToMessageQueueClass(String startDate, String endDate, String commodityIds);
}
