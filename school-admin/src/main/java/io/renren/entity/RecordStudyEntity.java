package io.renren.entity;

import java.util.Date;
import java.util.Map;

import io.renren.pojo.RecordStudyPOJO;
import io.renren.utils.JSONUtil;
 


/**
 * 学员档案学习信息
 * @author lintf
 *
 */
public class RecordStudyEntity {
	 
	
	/**
	 * 学员学习信息主键
	 */
	private Long recordStudyId;
	
	
	private Long userId;
	
	
	 
	
	private Long orderId;
	private String orderNo;
	
	
	/**
	 * 课程id 
	 */
	private Long courseId;
	 
	/**
	 * 	授课老师ID
	 */
	private String  teacherId;
	 
	/**
	 * 商品id
	 */
	private Long commodityId;
	/**
	 * 排课计划id
	 */
	private String classplanId;
	
	/**
	 * 学习计划主表
	 */
	private  Long userplanClassId;
	

	/**
	 * 学习计划子表id
	 */
	private Long userplanDetailId;
	/**
	 * 学员规划主键
	 */
	private Long userplanId;
	/**
	 * 班级id
	 */
	private Long classId;
 
	/**
	 * 班主任老师id
	 */
	private Long classTeacherId;
	
	
	/**
	 * 听课完成率
	 */
	private Double classCompletionRate;
	
	/**
	 * 题库完成率
	 */
	private Double tikuHomeWorkCompletionRate;
	
	
	
  /**
  * 零基实战
 */
	private Double   zeroBasisPracticeScore;
	
	
	
/**
 * 期中考试
 */
	private Double   midtermExamScore;
	/**
	 * 期末考试
	 */
	private Double    finalExamScore ;
	/**
	 * 考霸1
	 */
	private Double    kaobaOneScore;
	/**
	 * 考霸2
	 */
	private Double    kaobaTwoScore;
	/**
	 * 统考成绩
	 */
	private Integer     unExaminationScore;
	/**
	 * 学段
	 */
	private String     studyPart;
	
	/**
	 * 成绩类型
	 */
	 private int scoreType;
	 /**
	  * 是否删除  1 是删除 0是未删除
	  */
	 private  int dr;
	 
	 
	 private Date startTime;
	 
	 
public RecordStudyEntity() {}


	/**
	  * 从pojo转为entity
	  *@param p
	  *@return
	  * @author lintf
	  * 2018年8月17日
	  */
public RecordStudyEntity RecordStudyEntityFromPOJO(RecordStudyPOJO p) {
	
	
	 return  JSONUtil.jsonToBean( JSONUtil.beanToJson(p),RecordStudyEntity.class);
	
}




public Long getRecordStudyId() {
		return recordStudyId;
	}


	public void setRecordStudyId(Long recordStudyId) {
		this.recordStudyId = recordStudyId;
	}


public Long getUserplanClassId() {
	return userplanClassId;
}

public void setUserplanClassId(Long userplanClassId) {
	this.userplanClassId = userplanClassId;
}

public int getDr() {
return dr;
}

public void setDr(int dr) {
this.dr = dr;
}

public Date getStartTime() {
return startTime;
}

public void setStartTime(Date startTime) {
this.startTime = startTime;
}
	 

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}

	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	public Long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}

	public String getClassplanId() {
		return classplanId;
	}

	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}

	public Long getUserplanDetailId() {
		return userplanDetailId;
	}

	public void setUserplanDetailId(Long userplanDetailId) {
		this.userplanDetailId = userplanDetailId;
	}

	public Long getUserplanId() {
		return userplanId;
	}

	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public Long getClassTeacherId() {
		return classTeacherId;
	}

	public void setClassTeacherId(Long classTeacherId) {
		this.classTeacherId = classTeacherId;
	}

	 

	public Double getClassCompletionRate() {
		return classCompletionRate;
	}


	public void setClassCompletionRate(Double classCompletionRate) {
		this.classCompletionRate = classCompletionRate;
	}


	public Double getTikuHomeWorkCompletionRate() {
		return tikuHomeWorkCompletionRate;
	}

	public void setTikuHomeWorkCompletionRate(Double tikuHomeWorkCompletionRate) {
		this.tikuHomeWorkCompletionRate = tikuHomeWorkCompletionRate;
	}

	public Double getZeroBasisPracticeScore() {
		return zeroBasisPracticeScore;
	}

	public void setZeroBasisPracticeScore(Double zeroBasisPracticeScore) {
		this.zeroBasisPracticeScore = zeroBasisPracticeScore;
	}

	public Double getMidtermExamScore() {
		return midtermExamScore;
	}

	public void setMidtermExamScore(Double midtermExamScore) {
		this.midtermExamScore = midtermExamScore;
	}

	public Double getFinalExamScore() {
		return finalExamScore;
	}

	public void setFinalExamScore(Double finalExamScore) {
		this.finalExamScore = finalExamScore;
	}

	public Double getKaobaOneScore() {
		return kaobaOneScore;
	}

	public void setKaobaOneScore(Double kaobaOneScore) {
		this.kaobaOneScore = kaobaOneScore;
	}

	public Double getKaobaTwoScore() {
		return kaobaTwoScore;
	}

	public void setKaobaTwoScore(Double kaobaTwoScore) {
		this.kaobaTwoScore = kaobaTwoScore;
	}

	 

	 

	 


	public Integer getUnExaminationScore() {
		return unExaminationScore;
	}


	public void setUnExaminationScore(Integer unExaminationScore) {
		this.unExaminationScore = unExaminationScore;
	}


	public String getStudyPart() {
		return studyPart;
	}


	public void setStudyPart(String studyPart) {
		this.studyPart = studyPart;
	}


	public int getScoreType() {
		return scoreType;
	}

	public void setScoreType(int scoreType) {
		this.scoreType = scoreType;
	}
	 
	 
	 
	  
	
	
	

}
