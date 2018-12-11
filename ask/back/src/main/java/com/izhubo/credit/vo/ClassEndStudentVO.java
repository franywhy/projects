package com.izhubo.credit.vo;

import com.google.gson.annotations.SerializedName;

/**
 * 结课的学员
 *</p>这个是精简的取法 跟别的student类不一样 所以建的新类
 * @author lintf 
 *
 */
public class ClassEndStudentVO {
	@SerializedName("arrId") private String arrId;//排课计划主键
	@SerializedName("subjectId") private String subjectId;//科目
	@SerializedName("regId") private String regId;//报名表主键
	@SerializedName("studentId") private String studentId;//学员主键
	@SerializedName("lastDate") private String lastDate;//结课日期
 public String getArrId() {
	return arrId;
}
public void setArrId(String arrId) {
	this.arrId = arrId;
}
public String getSubjectId() {
	return subjectId;
}
public void setSubjectId(String subjectId) {
	this.subjectId = subjectId;
}
public String getRegId() {
	return regId;
}
public void setRegId(String regId) {
	this.regId = regId;
}
public String getStudentId() {
	return studentId;
}
public void setStudentId(String studentId) {
	this.studentId = studentId;
}
public String getLastDate() {
	return lastDate;
}
public void setLastDate(String lastDate) {
	this.lastDate = lastDate;
}

}
