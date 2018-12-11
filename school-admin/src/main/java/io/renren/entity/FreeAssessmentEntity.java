package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 免考核档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-02-26 16:02:47
 */
public class FreeAssessmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//学员名称
	private String studentName;
	//手机号
	private String studentMobile;
	//班级名称
	private String className;
	//班主任
	private String teacherName;
	//免考核课程
	private String course;
	//申请时间
	private Date applicationTime;
	//截止时间
	private Date deadlineTime;
	//申请原因
	private String reason;
	//申请状态:0.审核中,1.通过,2.取消
	private Integer applicationStatus;
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
	 * 设置：学员名称
	 */
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	/**
	 * 获取：学员名称
	 */
	public String getStudentName() {
		return studentName;
	}
	/**
	 * 设置：手机号
	 */
	public void setStudentMobile(String studentMobile) {
		this.studentMobile = studentMobile;
	}
	/**
	 * 获取：手机号
	 */
	public String getStudentMobile() {
		return studentMobile;
	}
	/**
	 * 设置：班级名称
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * 获取：班级名称
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * 设置：班主任
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	/**
	 * 获取：班主任
	 */
	public String getTeacherName() {
		return teacherName;
	}
	/**
	 * 设置：免考核课程
	 */
	public void setCourse(String course) {
		this.course = course;
	}
	/**
	 * 获取：免考核课程
	 */
	public String getCourse() {
		return course;
	}
	/**
	 * 设置：申请时间
	 */
	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
	}
	/**
	 * 获取：申请时间
	 */
	public Date getApplicationTime() {
		return applicationTime;
	}
	/**
	 * 设置：截止时间
	 */
	public void setDeadlineTime(Date deadlineTime) {
		this.deadlineTime = deadlineTime;
	}
	/**
	 * 获取：截止时间
	 */
	public Date getDeadlineTime() {
		return deadlineTime;
	}
	/**
	 * 设置：申请原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：申请原因
	 */
	public String getReason() {
		return reason;
	}
	/**
	 * 设置：申请状态:0.审核中,1.通过,2.取消
	 */
	public void setApplicationStatus(Integer applicationStatus) {
		this.applicationStatus = applicationStatus;
	}
	/**
	 * 获取：申请状态:0.审核中,1.通过,2.取消
	 */
	public Integer getApplicationStatus() {
		return applicationStatus;
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
}
