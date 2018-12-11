package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 成绩登记pojo
 * @author Administrator
 *
 */
public class ExaminationResultPOJO extends CourseAbnormalRegistrationPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	 
	//pk
	private Long examId;
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
	//班极id
	private Long classId; 
	//修改时间
	private Date ModifyTime;
	
	 
	public Long getExamId() {
		return examId;
	}
	public void setExamId(Long examId) {
		this.examId = examId;
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
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	} 
	public Date getModifyTime() {
		return ModifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		ModifyTime = modifyTime;
	}
	
	
}
