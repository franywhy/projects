package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 统考成绩
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-07 09:21:13
 */
public class ExaminationResultEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long id;
	//报考表pk
	private Long registrationId;
	//学员users表pk
	private Long userId;
	//创建者
	private Long creater;
	//成绩
	private Integer score;
	//图片
	private String img;
	//考试类型（0-非补考 1-补考）
	private Integer examType;
	//创建时间
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public Integer getExamType() {
		return examType;
	}

	public void setExamType(Integer examType) {
		this.examType = examType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
