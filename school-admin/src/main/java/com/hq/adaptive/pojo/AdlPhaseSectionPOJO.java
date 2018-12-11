package com.hq.adaptive.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 评测阶段包含节关系表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhaseSectionPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//课程PK
	private Long courseId;
	//阶段PK
	private Long phaseId;
	//节PK
	private Long sectionId;

	private String sectionName;

	private String sectionNo;

	@Override
	public String toString() {
		return "AdlPhaseSectionPOJO{" +
				"id=" + id +
				", courseId=" + courseId +
				", phaseId=" + phaseId +
				", sectionId=" + sectionId +
				", sectionName='" + sectionName + '\'' +
				", sectionNo='" + sectionNo + '\'' +
				'}';
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}

	public String getSectionNo() {
		return sectionNo;
	}

	public void setSectionNo(String sectionNo) {
		this.sectionNo = sectionNo;
	}

	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：阶段PK
	 */
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	/**
	 * 获取：阶段PK
	 */
	public Long getPhaseId() {
		return phaseId;
	}
	/**
	 * 设置：节PK
	 */
	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * 获取：节PK
	 */
	public Long getSectionId() {
		return sectionId;
	}
}
