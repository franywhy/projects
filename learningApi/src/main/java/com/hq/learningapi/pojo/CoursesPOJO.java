package com.hq.learningapi.pojo;


public class CoursesPOJO {
	//课程主键
	private Long courseId;
	//课程名称
	private String courseName;
	//课程代码
	private String courseNo;

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

	public String getCourseNo() {
		return courseNo;
	}

	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
}
