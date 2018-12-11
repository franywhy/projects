package io.renren.entity.manage;

public class MessageCard {
	//消息id
	private String msgId;
	//消息类型（1:今日学习；2巩固学习；3:公开课；4:自定义卡片；9:系统消息）	
	private int msgType;
	//卡片标题
	private String title;
	//卡片副标题
	private String describe;
	//推送文案
	private String pushText;
	//消息体
	private Object msgData;
	//推送时间
	private Long pushTime;

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescribe() {
		return describe;
	}

	public void setDescribe(String describe) {
		this.describe = describe;
	}

	public String getPushText() {
		return pushText;
	}

	public void setPushText(String pushText) {
		this.pushText = pushText;
	}

	public Object getMsgData() {
		return msgData;
	}

	public void setMsgData(Object msgData) {
		this.msgData = msgData;
	}

	public Long getPushTime() {
		return pushTime;
	}

	public void setPushTime(Long pushTime) {
		this.pushTime = pushTime;
	}
}
