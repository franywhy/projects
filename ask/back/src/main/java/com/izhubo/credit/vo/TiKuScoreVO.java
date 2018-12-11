package com.izhubo.credit.vo;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.google.gson.annotations.SerializedName;

/**
 * 题库中的作业和考试的成绩单
 * 
 * @author lintf
 * 
 **/
 public class TiKuScoreVO {
 
	
	@SerializedName("id")
	 
	private String id;
	
	@SerializedName("ClassNCCode")
 
	private String classNCCode; 
	
	@SerializedName("CourseCode")
	 
	private String courseCode; 
	
	@SerializedName("ArrSubject")
 
	private String ArrSubject;
	
	@SerializedName("StudentNCCode")
	 
	private String studentNCCode;// 商机编号
	
	@SerializedName("StudentName")
	 
	private String studentName;
	
	@SerializedName("StandarRate")
	 
	private Double standarRate;
	
	@SerializedName("ThisRate")
	 
	private Double ThisRate;// 本次取得的成绩
	
	@SerializedName("BillType")
	 
	private String BillType;// 这个是单据类型 W为作业E为考试
	
	@SerializedName("BillKey")
	 
	private String BillKey;// 这个是商机主键_科目_单据类型
	
	@SerializedName("BillKeyList")
	 
	private List<String> BillKeyList;// 这个是商机主键_科目_单据类型 列表
	
	@SerializedName("LastTime") 
	// 最后更新时间
	private Date LastTime;
	
	@SerializedName("ExanTime") 
	// 作业和考试时间
	private Date ExanTime;
	@SerializedName("SubjectType") 
	// 科目类型
	private String SubjectType;
	
	@SerializedName("CreateTime") 
	private String CreateTime;
	
	public String getCreateTime() {
		return CreateTime;
	}


	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}


	public String getSubjectType() {
		return SubjectType;
	}
 

	public String getClassNCCode() {
		return classNCCode;
	}

	public String getCourseCode() {
		return courseCode;
	}

	public String getArrSubject() {
		return ArrSubject;
	}

	public String getStudentNCCode() {
		return studentNCCode;
	}

	public String getStudentName() {
		return studentName;
	}

	public Double getStandarRate() {
		return standarRate;
	}

	public String getBillType() {
		return BillType;
	}

	public String getBillKey() {
		return BillKey;
	}

	public Date getLastTime() {
		return LastTime;
	}

	public Date getExanTime() {
		return ExanTime;
	}

	public void setSubjectType(String SubjectType) {
		this.SubjectType = SubjectType;
	}
	public void setClassNCCode(String classNCCode) {
		this.classNCCode = classNCCode;
	}

	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	public void setArrSubject(String arrSubject) {
		ArrSubject = arrSubject;
	}

	public void setStudentNCCode(String studentNCCode) {
		this.studentNCCode = studentNCCode;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public void setStandarRate(Double standarRate) {
		this.standarRate = standarRate;
	}

	public void setBillType(String billType) {
		BillType = billType;
	}

	public void setBillKey(String billKey) {
		BillKey = billKey;
	}

	public void setLastTime(Date lastTime) {
		LastTime = lastTime;
	}

	public void setExanTime(Date exanTime) {
		ExanTime = exanTime;
	}
 
	public String getId() {
		return id;
	}
 
	public Double getThisRate() {
		return ThisRate;
	}
 
	public List<String> getBillKeyList() {
		return BillKeyList;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setThisRate(Double thisRate) {
		ThisRate = thisRate;
	}

	public void setBillKeyList(List<String> billKeyList) {
		BillKeyList = billKeyList;
	}

}
