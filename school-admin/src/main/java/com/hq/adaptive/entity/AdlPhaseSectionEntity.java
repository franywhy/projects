package com.hq.adaptive.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 评测阶段包含节关系表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhaseSectionEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//课程PK
	private Long courseId;
	//阶段PK
	private Long phaseId;
	//节PK
	private Long sectionId;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;

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
	/**
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
