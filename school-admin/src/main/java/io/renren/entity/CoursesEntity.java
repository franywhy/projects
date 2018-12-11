package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.utils.Constant;



/**
 * 课程档案
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 14:06:37
 */
public class CoursesEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//课程主键
	private Long courseId;
	//课程编号
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
	//排课时间是否可冲突
	private Integer courseEq = Constant.CourseEQ.NO.getValue();
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
	private Integer dr = Constant.DR.NORMAL.getValue();
	//平台ID
	private String schoolId;
	
	//修改用户
	private String modifiedName;
	//创建用户
	private String creationName;
	private String mId;
	
	//产品线PK
	private Long productId;
	//产品线名称
	private String productName;
	//Long
	private Long courseFk;
	//是否为双师课程0：不是  1：是
	private Integer status;
	

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
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
	
	public Integer getCourseEq() {
		return courseEq;
	}
	public void setCourseEq(Integer courseEq) {
		this.courseEq = courseEq;
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
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	
	public String getModifiedName() {
		return modifiedName;
	}
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	public String getCreationName() {
		return creationName;
	}
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	public Long getCourseFk() {
		return courseFk;
	}
	public void setCourseFk(Long courseFk) {
		this.courseFk = courseFk;
	}
	@Override
	public String toString() {
		return "CoursesEntity [courseId=" + courseId + ", courseNo=" + courseNo + ", courseName=" + courseName
				+ ", courseLb=" + courseLb + ", courseType=" + courseType + ", examType=" + examType + ", courseCredit="
				+ courseCredit + ", courseEq=" + courseEq + ", listenUrl=" + listenUrl + ", creator=" + creator
				+ ", creationTime=" + creationTime + ", modifier=" + modifier + ", modifiedTime=" + modifiedTime
				+ ", dr=" + dr + ", schoolId=" + schoolId + ", modifiedName=" + modifiedName + ", creationName="
				+ creationName + ", mId=" + mId + ", productId=" + productId + ", productName=" + productName
				+ ", courseFk=" + courseFk + ", status=" + status + "]";
	}
	
	
	
	
}
