package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 录播课观看记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-11 16:49:57
 */
public class LogWatchRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long logWatchRecordId;
	//用户ID
	private Long userId;
	//录播档案ID
	private Long recordId;
	//是否已经观看
	private Integer attend;
	//更新时间
	private Date ts;
	
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
	 * 设置：是否已经观看
	 */
	public void setAttend(Integer attend) {
		this.attend = attend;
	}
	/**
	 * 获取：是否已经观看
	 */
	public Integer getAttend() {
		return attend;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getTs() {
		return ts;
	}
	/**
	 * 设置：更新时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	
}
