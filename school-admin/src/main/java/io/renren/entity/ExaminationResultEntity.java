package io.renren.entity;

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
	//成绩
	private Integer score;
	//图片
	private String img;
	//考试类型（0-非补考 1-补考）
	private Integer examType;
	//创建时间
	private Date createTime;
	//创建者
	private Long creater;
	//班极id
	private Long classId; 
	//修改时间
	private Date ModifyTime;
	//0正常1删除
	private Integer dr;
	  
	public Long getCreater() {
		return creater;
	}
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	public Date getModifyTime() {
		return ModifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		ModifyTime = modifyTime;
	} 
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 设置：pk
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：pk
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：报考表pk
	 */
	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	/**
	 * 获取：报考表pk
	 */
	public Long getRegistrationId() {
		return registrationId;
	}
	/**
	 * 设置：学员users表pk
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员users表pk
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：成绩
	 */
	public void setScore(Integer score) {
		this.score = score;
	}
	/**
	 * 获取：成绩
	 */
	public Integer getScore() {
		return score;
	}
	/**
	 * 设置：图片
	 */
	public void setImg(String img) {
		this.img = img;
	}
	/**
	 * 获取：图片
	 */
	public String getImg() {
		return img;
	}
	/**
	 * 设置：考试类型（0-非补考 1-补考）
	 */
	public void setExamType(Integer examType) {
		this.examType = examType;
	}
	/**
	 * 获取：考试类型（0-非补考 1-补考）
	 */
	public Integer getExamType() {
		return examType;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：班极id
	 */
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	/**
	 * 获取：班极id
	 */
	public Long getClassId() {
		return classId;
	}
}
