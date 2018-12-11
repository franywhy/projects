package com.hq.learningcenter.school.pojo;

import java.util.List;

public class CoursesPOJO {
	//课程主键
	private Long courseId;
	//课程名称
	private String courseName;
	//课程业务主键FK
    private Long courseFk;

    public Long getCourseFk() {
        return courseFk;
    }

    public void setCourseFk(Long courseFk) {
        this.courseFk = courseFk;
    }

    private List<CourseRecordDetailPOJO> CourseRecordDetailList;
	
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
	
	public List<CourseRecordDetailPOJO> getCourseRecordDetailList() {
		return CourseRecordDetailList;
	}
	public void setCourseRecordDetailList(List<CourseRecordDetailPOJO> courseRecordDetailList) {
		CourseRecordDetailList = courseRecordDetailList;
	}
	
}
