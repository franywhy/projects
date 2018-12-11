package com.izhubo.model.node;

/**
 * 聊天
 * 
 * @author shihongjie
 * 
 */
public class Chat {

	private String _id;
	private String topic_id;
	/** 用户id */
	private Integer user_id;
	/** 用户id */
	private String user_name;

	/** 内容 */
	private String reply_content;
	/** 类型 */
	private Integer reply_type;
	/** 头像 */
	private String user_pic;
	/** 发送状态 */
	private Integer sendState;

	/** 时间 */
	private Long reply_time;
	/** 时间 */
	private Long timestamp;

	/** 文件缓存位置 */
	private FileInfo file_info;
	/** 音频时长毫秒 */
	private Long duration;

	public String getOriginal_file_name() {
		if (file_info != null) {
			return file_info.getOriginal_file_name();
		}
		return null;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTopic_id() {
		return topic_id;
	}

	public void setTopic_id(String topic_id) {
		this.topic_id = topic_id;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getReply_content() {
		return reply_content;
	}

	public void setReply_content(String reply_content) {
		this.reply_content = reply_content;
	}

	public Integer getReply_type() {
		return reply_type;
	}

	public void setReply_type(Integer reply_type) {
		this.reply_type = reply_type;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public Integer getSendState() {
		return sendState;
	}

	public void setSendState(Integer sendState) {
		this.sendState = sendState;
	}

	public Long getReply_time() {
		return reply_time;
	}

	public void setReply_time(Long reply_time) {
		this.reply_time = reply_time;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public FileInfo getFile_info() {
		return file_info;
	}

	public void setFile_info(FileInfo file_info) {
		this.file_info = file_info;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	@Override
	public String toString() {
		return "Chat [_id=" + _id + ", topic_id=" + topic_id + ", user_id=" + user_id + ", user_name=" + user_name
				+ ", reply_content=" + reply_content + ", reply_type=" + reply_type + ", user_pic=" + user_pic
				+ ", sendState=" + sendState + ", reply_time=" + reply_time + ", timestamp=" + timestamp
				+ ", file_info=" + file_info + ", duration=" + duration + "]";
	}

	class FileInfo {

		private String original_file_name;

		public String getOriginal_file_name() {
			return original_file_name;
		}

		public void setOriginal_file_name(String original_file_name) {
			this.original_file_name = original_file_name;
		}

		@Override
		public String toString() {
			return "file_info [original_file_name=" + original_file_name + "]";
		}

	}

}
