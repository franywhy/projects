package com.izhubo.model.node;


public class ChatBaseMsg extends MessageBase {
	
	private static final long serialVersionUID = 8728693322047945189L;

	private String topic_id;
	
	private Long timestamp;

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "ChatMsg [topic_id=" + topic_id + ", timestamp=" + timestamp + "]";
	}
	
	
	
}
