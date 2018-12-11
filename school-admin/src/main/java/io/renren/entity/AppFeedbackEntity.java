package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * APP意见反馈表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-06-20 14:27:17
 */
public class AppFeedbackEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键pk
	private Long feedbackId;
	//反馈内容
	private String content;
	//反馈时间
	private Date timestamp;
	//前台用户id
	private Long userId;
	//平台id
	private String schoolId;
	//客户端类型
	private Integer clientType;
	//客户端版本
	private Integer clientVersion;
	
	/**
	 * 设置：主键pk
	 */
	public void setFeedbackId(Long feedbackId) {
		this.feedbackId = feedbackId;
	}
	/**
	 * 获取：主键pk
	 */
	public Long getFeedbackId() {
		return feedbackId;
	}
	/**
	 * 设置：反馈内容
	 */
	public void setContent(String content) {
		this.content = content;
	}
	/**
	 * 获取：反馈内容
	 */
	public String getContent() {
		return content;
	}
	/**
	 * 设置：反馈时间
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * 获取：反馈时间
	 */
	public Date getTimestamp() {
		return timestamp;
	}
	/**
	 * 设置：前台用户id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：前台用户id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：平台id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台id
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：客户端类型
	 */
	public void setClientType(Integer clientType) {
		this.clientType = clientType;
	}
	/**
	 * 获取：客户端类型
	 */
	public Integer getClientType() {
		return clientType;
	}
	/**
	 * 设置：客户端版本
	 */
	public void setClientVersion(Integer clientVersion) {
		this.clientVersion = clientVersion;
	}
	/**
	 * 获取：客户端版本
	 */
	public Integer getClientVersion() {
		return clientVersion;
	}
}
