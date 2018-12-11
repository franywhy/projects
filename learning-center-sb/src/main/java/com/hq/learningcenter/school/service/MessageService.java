package com.hq.learningcenter.school.service;

import java.util.Map;

/**
 * 推送消息服务
 * @dete 2017年12月05日
 */
public interface MessageService {
	
	void pushLiveMessageToQueue(Map<String,Object> messageMap);
	
	void pushReplayMessageToQueue(Map<String,Object> messageMap);
}
