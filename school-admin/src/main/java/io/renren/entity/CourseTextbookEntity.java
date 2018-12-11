package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 教材档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 16:09:29
 */
public class CourseTextbookEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long textbookId;
	//教材
	private String textbookName;
	//课程ID
	private Long courseId;
	//状态0.禁用1.启用
	private Integer status;
	//备注
	private String remark;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;

	/**
	 * 设置：主键
	 */
	public void setTextbookId(Long textbookId) {
		this.textbookId = textbookId;
	}
	/**
	 * 获取：主键
	 */
	public Long getTextbookId() {
		return textbookId;
	}
	/**
	 * 设置：教材
	 */
	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}
	/**
	 * 获取：教材
	 */
	public String getTextbookName() {
		return textbookName;
	}
	
	
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 设置：状态0.禁用1.启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态0.禁用1.启用
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：最近修改用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：最近修改日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近修改日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台PK
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台PK
	 */
	public String getSchoolId() {
		return schoolId;
	}
}
