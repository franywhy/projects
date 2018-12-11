package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 学员规划-课程子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:39
 */
public class CourseUserplanDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long userplanDetailId;
	//学员规划PK
	private Long userplanId;
	//课程PK
	private Long courseId;
	//考试情况:0.未通过;1.通过
	private Integer isPass;
	//被替代课程
	private Integer isSubstituted;
	//代替课程
	private Integer isSubstitute;
	//是否统考
	private Integer isUnitedExam;
	//专业不对口课程
	private Integer isSuitable;
	//省份ID
	private Long areaId;
	
	private String schoolId;
	
	

	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 设置：主键
	 */
	public void setUserplanDetailId(Long userplanDetailId) {
		this.userplanDetailId = userplanDetailId;
	}
	/**
	 * 获取：主键
	 */
	public Long getUserplanDetailId() {
		return userplanDetailId;
	}
	/**
	 * 设置：学员规划PK
	 */
	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}
	/**
	 * 获取：学员规划PK
	 */
	public Long getUserplanId() {
		return userplanId;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：考试情况:0.未通过;1.通过
	 */
	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}
	/**
	 * 获取：考试情况:0.未通过;1.通过
	 */
	public Integer getIsPass() {
		return isPass;
	}
	/**
	 * 设置：被替代课程
	 */
	public void setIsSubstituted(Integer isSubstituted) {
		this.isSubstituted = isSubstituted;
	}
	/**
	 * 获取：被替代课程
	 */
	public Integer getIsSubstituted() {
		return isSubstituted;
	}
	/**
	 * 设置：代替课程
	 */
	public void setIsSubstitute(Integer isSubstitute) {
		this.isSubstitute = isSubstitute;
	}
	/**
	 * 获取：代替课程
	 */
	public Integer getIsSubstitute() {
		return isSubstitute;
	}
	/**
	 * 设置：是否统考
	 */
	public void setIsUnitedExam(Integer isUnitedExam) {
		this.isUnitedExam = isUnitedExam;
	}
	/**
	 * 获取：是否统考
	 */
	public Integer getIsUnitedExam() {
		return isUnitedExam;
	}
	/**
	 * 设置：专业不对口课程
	 */
	public void setIsSuitable(Integer isSuitable) {
		this.isSuitable = isSuitable;
	}
	/**
	 * 获取：专业不对口课程
	 */
	public Integer getIsSuitable() {
		return isSuitable;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	@Override
	public String toString() {
		return "CourseUserplanDetailEntity [userplanDetailId=" + userplanDetailId + ", userplanId=" + userplanId + ", courseId=" + courseId
				+ ", isPass=" + isPass + ", isSubstituted=" + isSubstituted + ", isSubstitute=" + isSubstitute + ", isUnitedExam=" + isUnitedExam
				+ ", isSuitable=" + isSuitable + ", areaId=" + areaId + ", schoolId=" + schoolId + "]";
	}
	
}
