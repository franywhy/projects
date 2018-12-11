package io.renren.entity;

import java.util.Date;

public class NcCoursesEntity {
	private Long id;
	private String courseCode;
	private String courseName;
	private String charpterCode;
	private String charpterName;
	private String sectionCode;
	private String sectionName;
	
	private String code;
	
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public void setId(Long id) {
		this.id = id;
	}

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
	
	public Long getId() {
		return id;
	}
	
	private Long chapterIdx;
	private Long sectionIdx;


	public Long getChapterIdx() {
		return chapterIdx;
	}
	public void setChapterIdx(Long chapterIdx) {
		this.chapterIdx = chapterIdx;
	}
	public Long getSectionIdx() {
		return sectionIdx;
	}
	public void setSectionIdx(Long sectionIdx) {
		this.sectionIdx = sectionIdx;
	}


}
