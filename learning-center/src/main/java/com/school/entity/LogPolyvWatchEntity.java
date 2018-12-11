package com.school.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 录播考勤汇总表
 * @author ouchujian
 *
 */
public class LogPolyvWatchEntity {
	
	private Long logWatchId;
	private Long recordId;
	private Long courseId;
	private Long userId;
	private String vid;
	private Long playDuration;
	private Long fullDuration;
	private BigDecimal attentPer;
	private Date createTime;
	private Date ts;
	
	public Long getLogWatchId() {
		return logWatchId;
	}
	public void setLogWatchId(Long logWatchId) {
		this.logWatchId = logWatchId;
	}
	public Long getRecordId() {
		return recordId;
	}
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getVid() {
		return vid;
	}
	public void setVid(String vid) {
		this.vid = vid;
	}
	public Long getPlayDuration() {
		return playDuration;
	}
	public void setPlayDuration(Long playDuration) {
		this.playDuration = playDuration;
	}
	public Long getFullDuration() {
		return fullDuration;
	}
	public void setFullDuration(Long fullDuration) {
		this.fullDuration = fullDuration;
	}
	public BigDecimal getAttentPer() {
		return attentPer;
	}
	public void setAttentPer(BigDecimal attentPer) {
		this.attentPer = attentPer;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
