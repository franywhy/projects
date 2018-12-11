package com.izhubo.web.vo;

import java.util.Map;

import com.izhubo.model.FtpInfo;

/**
 * @ClassName: TopicReplyVO
 * @Description: 问题回复VO
 * @author shihongjie
 * @date 2015年5月25日 上午10:54:37
 * 
 */
public class TopicReplyVO implements BaseVO {

	/** 问题id */
	private String topic_id;
	/** 回复内容 */
	private String reply_content;
	/** 回复类型 */
	private String reply_type;
	/** 用户id */
	private String user_id;
	/** 用户昵称 */
	private String user_name;
	/** 用户头像 */
	private String user_pic;
	/** 时间戳 */
	private String timestamp;
	/** 音频时长毫秒 */
	private long duration;
	
	private int pic_height = 0;
	
	private int pic_width = 0;

	private String file_json;

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public String getReply_type() {
		return reply_type;
	}

	public void setReply_type(String reply_type) {
		this.reply_type = reply_type;
	}

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
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

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public String getFile_json() {
		return file_json;
	}

	public void setFile_json(String file_json) {
		this.file_json = file_json;
	}

	@Override
	public String toString() {
		return "TopicReplyVO [topic_id=" + topic_id + ", reply_content=" + reply_content + ", reply_type=" + reply_type
				+ ", user_id=" + user_id + ", user_name=" + user_name + ", user_pic=" + user_pic + ", timestamp="
				+ timestamp + ", duration=" + duration + ", file_json=" + file_json + "]";
	}

	public int getPic_height() {
		return pic_height;
	}

	public void setPic_height(int pic_height) {
		this.pic_height = pic_height;
	}

	public int getPic_width() {
		return pic_width;
	}

	public void setPic_width(int pic_width) {
		this.pic_width = pic_width;
	}

	


}
