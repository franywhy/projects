package com.school.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.school.service.MessageService;

@Service("messageService")
public class MessageServiceImpl implements MessageService {

	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private AmqpTemplate amqpTemplate;
	/** 推送直播消息队列名 */
	private static String LIVE_CALLBACK_INFO = "";
	@Value("#{application['queue.live.callback.info']}")
	private void setLIVE_CALLBACK_INFO(String str){
		LIVE_CALLBACK_INFO = str;
	}
	/** 推送回放消息队列名 */
	private static String REPLAY_CALLBACK_INFO = "";
	@Value("#{application['queue.replay.callback.info']}")
	private void setREPLAY_CALLBACK_INFO(String str){
		REPLAY_CALLBACK_INFO = str;
	}
	
	@Override
	public void pushLiveMessageToQueue(Map<String, Object> messageMap) {
		Gson gson=new Gson();
		String json = gson.toJson(messageMap).toString();
		//logger.info("MessageVideoClassServiceImpl pushToMessageQueueClass json:{}",json);
		amqpTemplate.convertAndSend(LIVE_CALLBACK_INFO, json);
	}
	@Override
	public void pushReplayMessageToQueue(Map<String, Object> messageMap) {
		Gson gson=new Gson();
		String json = gson.toJson(messageMap).toString();
		amqpTemplate.convertAndSend(REPLAY_CALLBACK_INFO, json);
	}
}
