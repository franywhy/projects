package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 报考登记学员course_exam_record_user
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-06 14:19:44
 */
public class CourseExamRecordDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long examRecordDetailId;
	//报考登记PK
	private Long examRecordId;
	//
	private Long userplanDetailId;
	//备注
	private String remark;
	//dr
	private Integer dr;
	//平台ID
	private String schoolId;
	//课程PK
	private Long courseId;
	//报名省份PK
	private Long areaId;
	//报考省份PK
	private Long examareaId ;
	//学员ID
	private Long userId;


    public Long getExamareaId() {
        return examareaId;
    }

    public void setExamareaId(Long examareaId) {
        this.examareaId = examareaId;
    }

    /**
	 * 设置：主键
	 */
	public void setExamRecordDetailId(Long examRecordDetailId) {
		this.examRecordDetailId = examRecordDetailId;
	}
	/**
	 * 获取：主键
	 */
	public Long getExamRecordDetailId() {
		return examRecordDetailId;
	}
	/**
	 * 设置：报考登记PK
	 */
	public void setExamRecordId(Long examRecordId) {
		this.examRecordId = examRecordId;
	}
	/**
	 * 获取：报考登记PK
	 */
	public Long getExamRecordId() {
		return examRecordId;
	}
	/**
	 * 设置：
	 */
	public void setUserplanDetailId(Long userplanDetailId) {
		this.userplanDetailId = userplanDetailId;
	}
	/**
	 * 获取：
	 */
	public Long getUserplanDetailId() {
		return userplanDetailId;
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
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
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
	 * 设置：省份PK
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：省份PK
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：学员ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员ID
	 */
	public Long getUserId() {
		return userId;
	}
}
