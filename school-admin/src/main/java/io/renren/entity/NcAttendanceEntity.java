package io.renren.entity;

import java.util.Date;

public class NcAttendanceEntity {
	private Long id;
	private String startChapter;
	private String startSection;
	private String endChapter;
	private String endSection;
	private Date attendanceTime;
	private String classPlanId;
	
	public String getClassPlanId() {
		return classPlanId;
	}
	public void setClassPlanId(String classPlanId) {
		this.classPlanId = classPlanId;
	}
	private String courseCode;
	private Date syncTime;
	public Date getSyncTime() {
		return syncTime;
	}
	public void setSyncTime(Date syncTime) {
		this.syncTime = syncTime;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	
	
	public String getStartChapter() {
		return startChapter;
	}
	public void setStartChapter(String startChapter) {
		this.startChapter = startChapter;
	}
	public String getStartSection() {
		return startSection;
	}
	public void setStartSection(String startSection) {
		this.startSection = startSection;
	}
	public String getEndChapter() {
		return endChapter;
	}
	public void setEndChapter(String endChapter) {
		this.endChapter = endChapter;
	}
	public String getEndSection() {
		return endSection;
	}
	public void setEndSection(String endSection) {
		this.endSection = endSection;
	}
	public Date getAttendanceTime() {
		return attendanceTime;
	}
	public void setAttendanceTime(Date attendanceTime) {
		this.attendanceTime = attendanceTime;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getId() {
		return id;
	}
	
}
