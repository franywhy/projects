package com.hq.adaptive.pojo.query;
/**  
 * 类说明   
 *  
 * @author shihongjie
 * @email  shihongjie@hengqijy.com
 * @date 2017年11月8日
 */
public class AdlCourseQuery extends BaseQuery {
	
	// 自增id
	private Long courseId;
	// 课程名称
	private String courseName;
	// 课程编码
	private String courseNo;
	// 来源ID
	private Integer sourceId;
	public Long getCourseId() {
		return courseId;
	}
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}
	public Integer getSourceId() {
		return sourceId;
	}
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}
	@Override
	public String toString() {
		return "AdlCourseQuery [courseId=" + courseId + ", courseName=" + courseName + ", courseNo=" + courseNo
				+ ", sourceId=" + sourceId + "]";
	}
	
	
}
