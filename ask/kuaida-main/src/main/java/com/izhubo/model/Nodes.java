package com.izhubo.model;

public interface Nodes {
	
	/** node*/
	//问题聊天-模块
	public static final String NODE_MODULE_TOPIC_CHAT = "topics";
	//聊天
	public static final String NODE_ACTION_TOPIC_CHAT = "chat";
	//问题评价成功
	public static final String NODE_ACTION_TOPIC_EVALUATION = "topic_evaluation";
	 //聊天结束
	public static final String NODE_ACTION_TOPIC_CHAT_END = "topic_end";
	//系统文字消息
	public static final String NODE_ACTION_TOPIC_SYS_TEXT = "topic_sys_text";
	//系统红包消息
	public static final String NODE_ACTION_TOPIC_SYS_RED = "topic_sys_red";
	//问答抢答成功
	public static final String NODE_ACTION_TOPIC_CHAT_WAITING_SUCCESS = "chat_waiting_success";
	//问题超时
	public static final String NODE_ACTION_TOPIC_CHAT_WAITING_TIMEOUT = "chat_waiting_timeout";
}
