package io.renren.entity;

import java.util.Date;

public class QuestionBankEntity {
	private Long id;
	private String courseCode;
	private String courseName;
	private String charpterCode;
	private String charpterName;
	private String sectionCode;
	private String sectionName;
	/*private String isDelete;
	private String isUse;*/
	
	private boolean IsDelete;
	private boolean IsUse;
	
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
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCharpterCode() {
		return charpterCode;
	}
	public void setCharpterCode(String charpterCode) {
		this.charpterCode = charpterCode;
	}
	public String getCharpterName() {
		return charpterName;
	}
	public void setCharpterName(String charpterName) {
		this.charpterName = charpterName;
	}
	public String getSectionCode() {
		return sectionCode;
	}
	public void setSectionCode(String sectionCode) {
		this.sectionCode = sectionCode;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public boolean getIsDelete() {
		return IsDelete;
	}
	public void setIsDelete(boolean isDelete) {
		this.IsDelete = isDelete;
	}
	public boolean getIsUse() {
		return IsUse;
	}
	public void setIsUse(boolean isUse) {
		this.IsUse = isUse;
	}
	public Long getId() {
		return id;
	}
	
}
