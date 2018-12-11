package com.mysqldb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 学分权限表
 * @author 严志城
 *
 */
@Entity
@Table(name = "credit_query_permission")
@org.hibernate.annotations.Table(appliesTo = "credit_query_permission", comment = "credit_query_permission")
public class CreditQueryPermission {
	/**
	 * id
	 */
	private Integer id;
	
	private String orgId;
	private String orgCode;
	private String orgName;
	
	private String teacherId;
	private String teacherName;
	private String teacherPhone;
	
	private String userId;
	private Integer type;
	
	/**
	 * 时间戳
	 */
	private Date ts;
	
	
	/**
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId(){
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(Integer id){
		this.id	= id;
	}
	/**
	 * 获取 时间戳
	 */
	@Column(name = "TS",insertable = false,updatable=false,columnDefinition = "DATETIME")
	public Date getTs(){
		return this.ts;
	}

	/**
	 * 设置 时间戳
	 */
	public void setTs(Date ts){
		this.ts	= ts;
	}

	/**
	 * 获取 组织id
	 */
	@Column(name = "org_id",columnDefinition = "VARCHAR")
	public String getOrgId() {
		return orgId;
	}
	/**
	 * 设置组织id
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	/**
	 * 获取 组织编码
	 */
	@Column(name = "org_code",columnDefinition = "VARCHAR")
	public String getOrgCode() {
		return orgCode;
	}

	/**
	 * 设置 组织编码
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	/**
	 * 获取 组织名称
	 */
	@Column(name = "org_name",columnDefinition = "VARCHAR")
	public String getOrgName() {
		return orgName;
	}
	/**
	 * 设置组织名称
	 */
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	/**
	 * 获取 老师id
	 */
	@Column(name = "teacher_id",columnDefinition = "VARCHAR")
	public String getTeacherId() {
		return teacherId;
	}
	/**
	 * 设置 老师id
	 */
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	/**
	 * 获取 老师名称
	 */
	@Column(name = "teacher_name",columnDefinition = "VARCHAR")
	public String getTeacherName() {
		return teacherName;
	}
	/**
	 * 设置老师名称
	 */
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	
	/**
	 * 获取老师电话
	 */
	@Column(name = "teacher_phone",columnDefinition = "VARCHAR")
	public String getTeacherPhone() {
		return teacherPhone;
	}
	/**
	 * 设置老师电话
	 */
	public void setTeacherPhone(String teacherPhone) {
		this.teacherPhone = teacherPhone;
	}

	/**
	 * 获取 用户id
	 */
	@Column(name = "user_id",columnDefinition = "VARCHAR")
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置 用户id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 *获取 用户类型
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置 用户类型
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	
	
}
