package com.school.pojo;

import java.util.Date;

public class LogWatchRecordPOJO {
	//pk
	private Long logWatchRecordId;
	//用户ID
	private Long userId;
	//录播档案ID
	private Long recordId;
	//是否已经观看 0：没观看  1：观看
	private Integer attend;
	//最近更新时间
	private Date ts;
	
	//录播课名称
	private String recordName;
	//录播课时长
	private String duration;

	/**
	 * 设置：pk
	 */
	public void setLogWatchRecordId(Long logWatchRecordId) {
		this.logWatchRecordId = logWatchRecordId;
	}
	/**
	 * 获取：pk
	 */
	public Long getLogWatchRecordId() {
		return logWatchRecordId;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：录播档案ID
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：录播档案ID
	 */
	public Long getRecordId() {
		return recordId;
	}
	/**
	 * 设置：是否已经观看 0：没观看  1：观看
	 */
	public void setAttend(Integer attend) {
		this.attend = attend;
	}
	/**
	 * 获取：是否已经观看 0：没观看  1：观看
	 */
	public Integer getAttend() {
		return attend;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
}
