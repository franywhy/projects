package io.renren.entity;

import java.io.Serializable;

public class TimeTableDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long   id;
	//主表pk
	private Long   number;
	private Integer week;
	private Integer timeBucket;
	private String startTime;
	private String endTime;
	private String comments;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getNumber() {
		return number;
	}
	public void setNumber(Long number) {
		this.number = number;
	}
	public Integer getWeek() {
		return week;
	}
	public void setWeek(Integer week) {
		this.week = week;
	}
	public Integer getTimeBucket() {
		return timeBucket;
	}
	public void setTimeBucket(Integer timeBucket) {
		this.timeBucket = timeBucket;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	
	
}
