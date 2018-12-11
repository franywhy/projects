package io.renren.modules.job.service;

/**
 * 会计班级消息推送到题库
 * @class io.renren.modules.job.service.MessageKJClassService.java
 * @Description:
 * @author shihongjie
 * @dete 2017年10月13日
 */
public interface MessageClassByProductIdService {
	
	void pushToMessageQueueClass();
}
