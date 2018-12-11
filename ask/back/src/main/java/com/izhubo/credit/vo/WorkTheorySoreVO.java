package com.izhubo.credit.vo;

import com.google.gson.annotations.SerializedName;



/**
 * 作业和考试的理论分的vo 对接题库
 * @author 严志城
 *
 */
public class WorkTheorySoreVO {
	@SerializedName("ClassNCCode")
	private String classNCCode;
	@SerializedName("CourseCode")
	private String courseCode;
	@SerializedName("StudentNCCode")
	private String studentNCCode;
	@SerializedName("StudentName")
	private String studentName;
	@SerializedName("StandarRate")
	private Double standarRate;
	//@SerializedName("CreateTime")
	//private String createTime;
	public String getClassNCCode() {
		return classNCCode;
	}
	public void setClassNCCode(String classNCCode) {
		this.classNCCode = classNCCode;
	}
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public String getStudentNCCode() {
		return studentNCCode;
	}
	public void setStudentNCCode(String studentNCCode) {
		this.studentNCCode = studentNCCode;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public Double getStandarRate() {
		return standarRate;
	}
	public void setStandarRate(Double standarRate) {
		this.standarRate = standarRate;
	}
	/*public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}*/
	
	

	
}
