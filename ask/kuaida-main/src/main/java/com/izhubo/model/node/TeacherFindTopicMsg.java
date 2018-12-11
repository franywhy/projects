package com.izhubo.model.node;

import com.izhubo.model.MessageBase;

/**
 * 老师抢答问题后通知学员的消息体
 * 
 * @author shihongjie
 * 
 */
public class TeacherFindTopicMsg extends MessageBase {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6924956337564687349L;

	private String topic_id;
	private String teacher_pic;
	private String teacher_id;
	private String teacher_name;

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public String getTeacher_pic() {
		return teacher_pic;
	}

	public void setTeacher_pic(String teacher_pic) {
		this.teacher_pic = teacher_pic;
	}

	public String getTeacher_id() {
		return teacher_id;
	}

	public void setTeacher_id(String teacher_id) {
		this.teacher_id = teacher_id;
	}

	public String getTeacher_name() {
		return teacher_name;
	}

	public void setTeacher_name(String teacher_name) {
		this.teacher_name = teacher_name;
	}

	@Override
	public String toString() {
		return "TeacherFindTopicMsg [topic_id=" + topic_id + ", teacher_pic=" + teacher_pic + ", teacher_id="
				+ teacher_id + ", teacher_name=" + teacher_name + "]";
	}
	
	

}
