package com.izhubo.credit.vo;

 
import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.google.gson.annotations.SerializedName;


/**
 * 报名表中每天同步信息专用 
 * @author lintf 
 * @date 2017年9月15日20:23:38
 *
 */
 
 
public class ArrangedSYNCVO {
 

	/**
	 * 班级名称
	 */
	@SerializedName("className") private String className;
	/**
	 * 是否删除
	 */
	@SerializedName("dR") private String dR;
	/**
	 * 同步时间
	 */
	@SerializedName("syTs") private String syTs;
	/**
	 * 班级名称ID
	 */
	@SerializedName("arrangedName") private String arrangedName;
	/**
	 * 老师ID
	 */
	@SerializedName("teacherId") private String teacherId;
	/**
	 * 学员ID
	 */
	@SerializedName("studentId") private String studentId;
	/**
	 * 科目ID
	 */
	@SerializedName("subjectId") private String subjectId;
	/**
	 * 应考勤次数
	 */
	@SerializedName("rS") private Integer rS;
	/**
	 * 已经签到的次数
	 */
	@SerializedName("kQ") private Integer kQ;
	/**
	 * 出勤率 0-100;
	 */
	@SerializedName("kqV") private Integer kqV;
	public String getClassName() {
		return className;
	}
	public String getdR() {
		return dR;
	}
	public String getSyTs() {
		return syTs;
	}
	public String getArrangedName() {
		return arrangedName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public String getStudentId() {
		return studentId;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public Integer getrS() {
		return rS;
	}
	public Integer getkQ() {
		return kQ;
	}
	public Integer getKqV() {
		return kqV;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public void setdR(String dR) {
		this.dR = dR;
	}
	public void setSyTs(String syTs) {
		this.syTs = syTs;
	}
	public void setArrangedName(String arrangedName) {
		this.arrangedName = arrangedName;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public void setrS(Integer rS) {
		this.rS = rS;
	}
	public void setkQ(Integer kQ) {
		this.kQ = kQ;
	}
	public void setKqV(Integer kqV) {
		this.kqV = kqV;
	}
	 

	 


	 
	
}
