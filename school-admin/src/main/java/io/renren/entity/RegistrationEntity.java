package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 报考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-14 17:21:05
 */
public class RegistrationEntity implements Serializable {
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
	//课程
	private String course;
	//考试时间
	private Date examinationTime;
	//登记时间
	private Date registrationTime;
	//报名省份
	private String applyProvince;
	//报考省份
	private String registrationProvince;
	//备注
	private String remark;
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
	 * 设置：课程
	 */
	public void setCourse(String course) {
		this.course = course;
	}
	/**
	 * 获取：课程
	 */
	public String getCourse() {
		return course;
	}
	/**
	 * 设置：考试时间
	 */
	public void setExaminationTime(Date examinationTime) {
		this.examinationTime = examinationTime;
	}
	/**
	 * 获取：考试时间
	 */
	public Date getExaminationTime() {
		return examinationTime;
	}
	/**
	 * 设置：登记时间
	 */
	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}
	/**
	 * 获取：登记时间
	 */
	public Date getRegistrationTime() {
		return registrationTime;
	}
	/**
	 * 设置：报名省份
	 */
	public void setApplyProvince(String applyProvince) {
		this.applyProvince = applyProvince;
	}
	/**
	 * 获取：报名省份
	 */
	public String getApplyProvince() {
		return applyProvince;
	}
	/**
	 * 设置：报考省份
	 */
	public void setRegistrationProvince(String registrationProvince) {
		this.registrationProvince = registrationProvince;
	}
	/**
	 * 获取：报考省份
	 */
	public String getRegistrationProvince() {
		return registrationProvince;
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
