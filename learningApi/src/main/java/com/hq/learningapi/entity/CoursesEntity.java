package com.hq.learningapi.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 课程档案
 * 
 * @author zhaowenwei
 * @date 2018-01-25 19:37:32
 */
public class CoursesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//课程主键
	private Long courseId;
	//
	private String courseNo;
	//课程名称
	private String courseName;
	//课程类别
	private String courseLb;
	//课程类型
	private String courseType;
	//考试方式
	private String examType;
	//学分
	private String courseCredit;
	//排课时间是否可冲突0：不可冲突 1：可冲突
	private Integer courseEq;
	//试听地址
	private String listenUrl;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	//删除标志
	private Integer dr;
	//平台ID
	private String schoolId;
	//
	private String mId;
	//产品线PK
	private Long productId;
	//
	private Integer valid;
	//课程的业务外键,推送接收存储
	private Long courseFk;
	//推送时间
	private Long pushTimestamp;
	//推送状态,0.作废;1.正常
	private Integer courseStatus;

	/**
	 * 设置：课程主键
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程主键
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：
	 */
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	/**
	 * 获取：
	 */
	public String getCourseNo() {
		return courseNo;
	}
	/**
	 * 设置：课程名称
	 */
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	/**
	 * 获取：课程名称
	 */
	public String getCourseName() {
		return courseName;
	}
	/**
	 * 设置：课程类别
	 */
	public void setCourseLb(String courseLb) {
		this.courseLb = courseLb;
	}
	/**
	 * 获取：课程类别
	 */
	public String getCourseLb() {
		return courseLb;
	}
	/**
	 * 设置：课程类型
	 */
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}
	/**
	 * 获取：课程类型
	 */
	public String getCourseType() {
		return courseType;
	}
	/**
	 * 设置：考试方式
	 */
	public void setExamType(String examType) {
		this.examType = examType;
	}
	/**
	 * 获取：考试方式
	 */
	public String getExamType() {
		return examType;
	}
	/**
	 * 设置：学分
	 */
	public void setCourseCredit(String courseCredit) {
		this.courseCredit = courseCredit;
	}
	/**
	 * 获取：学分
	 */
	public String getCourseCredit() {
		return courseCredit;
	}
	/**
	 * 设置：排课时间是否可冲突0：不可冲突 1：可冲突
	 */
	public void setCourseEq(Integer courseEq) {
		this.courseEq = courseEq;
	}
	/**
	 * 获取：排课时间是否可冲突0：不可冲突 1：可冲突
	 */
	public Integer getCourseEq() {
		return courseEq;
	}
	/**
	 * 设置：试听地址
	 */
	public void setListenUrl(String listenUrl) {
		this.listenUrl = listenUrl;
	}
	/**
	 * 获取：试听地址
	 */
	public String getListenUrl() {
		return listenUrl;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreator() {
		return creator;
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
	 * 设置：修改用户
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改用户
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：删除标志
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：
	 */
	public void setMId(String mId) {
		this.mId = mId;
	}
	/**
	 * 获取：
	 */
	public String getMId() {
		return mId;
	}
	/**
	 * 设置：产品线PK
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线PK
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：
	 */
	public void setValid(Integer valid) {
		this.valid = valid;
	}
	/**
	 * 获取：
	 */
	public Integer getValid() {
		return valid;
	}
	/**
	 * 设置：课程的业务外键,推送接收存储
	 */
	public void setCourseFk(Long courseFk) {
		this.courseFk = courseFk;
	}
	/**
	 * 获取：课程的业务外键,推送接收存储
	 */
	public Long getCourseFk() {
		return courseFk;
	}
	/**
	 * 设置：推送时间
	 */
	public void setPushTimestamp(Long pushTimestamp) {
		this.pushTimestamp = pushTimestamp;
	}
	/**
	 * 获取：推送时间
	 */
	public Long getPushTimestamp() {
		return pushTimestamp;
	}
	/**
	 * 设置：推送状态,0.作废;1.正常
	 */
	public void setCourseStatus(Integer courseStatus) {
		this.courseStatus = courseStatus;
	}
	/**
	 * 获取：推送状态,0.作废;1.正常
	 */
	public Integer getCourseStatus() {
		return courseStatus;
	}
}
