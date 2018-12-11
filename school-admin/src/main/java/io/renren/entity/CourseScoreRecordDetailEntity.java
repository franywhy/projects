package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 分数登记子表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-05 09:42:28
 */
public class CourseScoreRecordDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long scoreRecordDetailId;
	//
	private Long scoreRecordId;
	//学员PK
	private Long userId;
	//课程PK
	private Long courseId;
	//分数
	private Double score;
	//状态:0.通过;1.未通过
	private Integer passed;

	/**
	 * 设置：主键
	 */
	public void setScoreRecordDetailId(Long scoreRecordDetailId) {
		this.scoreRecordDetailId = scoreRecordDetailId;
	}
	/**
	 * 获取：主键
	 */
	public Long getScoreRecordDetailId() {
		return scoreRecordDetailId;
	}
	/**
	 * 设置：
	 */
	public void setScoreRecordId(Long scoreRecordId) {
		this.scoreRecordId = scoreRecordId;
	}
	/**
	 * 获取：
	 */
	public Long getScoreRecordId() {
		return scoreRecordId;
	}
	/**
	 * 设置：学员PK
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员PK
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：分数
	 */
	public void setScore(Double score) {
		this.score = score;
	}
	/**
	 * 获取：分数
	 */
	public Double getScore() {
		return score;
	}
	/**
	 * 设置：状态:0.通过;1.未通过
	 */
	public void setPassed(Integer passed) {
		this.passed = passed;
	}
	/**
	 * 获取：状态:0.通过;1.未通过
	 */
	public Integer getPassed() {
		return passed;
	}
}
