package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 考试倒计时
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-27 09:48:19
 */
public class CourseExamTimeEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//考试时间
	private Date examTime;
	//备注
	private String remark;
	//创建用户
	private Long createPerson;
	//创建用户
	private String creationName;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改用户
		private String modifiedName;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;
    //   倒计时天使
	private Float countDays;
	public String getCreationName() {
		return creationName;
	}
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	public String getModifiedName() {
		return modifiedName;
	}
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	public Float getCountDays() {
		return countDays;
	}
	public void setCountDays(Float countDays) {
		this.countDays = countDays;
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
	 * 设置：考试时间
	 */
	public void setExamTime(Date examTime) {
		this.examTime = examTime;
	}
	/**
	 * 获取：考试时间
	 */
	public Date getExamTime() {
		return examTime;
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
