package com.izhubo.model.node;

import com.izhubo.model.MessageBase;

/**
 * 
 * @author Administrator
 *
 */
public class TopicChatEvalutaionMsg extends MessageBase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2938920059289201712L;
	
	private String topic_id;
	private int user_id;
	private String user_name;
	private String user_pic;
	
	private int score;

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "TopicChatEvalutaionMsg [topic_id=" + topic_id + ", user_id="
				+ user_id + ", user_name=" + user_name + ", user_pic="
				+ user_pic + ", score=" + score + "]";
	}
	
	
	
	
}
