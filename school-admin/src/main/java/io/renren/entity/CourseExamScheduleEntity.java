package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 课程考试时段档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-24 18:55:01
 */
public class CourseExamScheduleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键id
	private Long courseExamId;
	//平台id
	private String schoolId;
	//是否删除0：否  1：是
	private Integer dr;
	//创建人
	private Long createPerson;
	//创建时间
	private Date createTime;
	//修改人
	private Long modifyPerson;
	//修改时间
	private Date modifyTime;
	//状态0：禁用  1：启用
	private Integer status;
	//备注
	private String remark;
	//课程pk
	private Long courseId;
	//省份pk
	private Long areaId;
	//考试时段pk
	private Long examScheduleId;
	//考试日期
	private Date examDate;
	//考试时段
	private Integer examBucket;

	/**
	 * 设置：主键id
	 */
	public void setCourseExamId(Long courseExamId) {
		this.courseExamId = courseExamId;
	}
	/**
	 * 获取：主键id
	 */
	public Long getCourseExamId() {
		return courseExamId;
	}
	/**
	 * 设置：平台id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台id
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：是否删除0：否  1：是
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除0：否  1：是
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：状态0：禁用  1：启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态0：禁用  1：启用
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
	 * 设置：课程pk
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程pk
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：省份pk
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：省份pk
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：考试时段pk
	 */
	public void setExamScheduleId(Long examScheduleId) {
		this.examScheduleId = examScheduleId;
	}
	/**
	 * 获取：考试时段pk
	 */
	public Long getExamScheduleId() {
		return examScheduleId;
	}
	/**
	 * 设置：考试日期
	 */
	public void setExamDate(Date examDate) {
		this.examDate = examDate;
	}
	/**
	 * 获取：考试日期
	 */
	public Date getExamDate() {
		return examDate;
	}
	/**
	 * 设置：考试时段
	 */
	public void setExamBucket(Integer examBucket) {
		this.examBucket = examBucket;
	}
	/**
	 * 获取：考试时段
	 */
	public Integer getExamBucket() {
		return examBucket;
	}
}
