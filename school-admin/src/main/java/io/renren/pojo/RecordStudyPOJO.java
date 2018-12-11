package io.renren.pojo;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Map;

import io.renren.utils.DateUtils;
  
/**
 * 学员档案学习信息
 * @author lintf
 *
 */
public class RecordStudyPOJO {
	 
	
	
	
	
	
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
	public int getInClass() {
		return inClass;
	}
	public void setInClass(int inClass) {
		this.inClass = inClass;
	}
	public Long getRecordStudyId() {
		return recordStudyId;
	}
	public void setRecordStudyId(Long recordStudyId) {
		this.recordStudyId = recordStudyId;
	}
	private Long userId;
	
	
	/**
	 * 学员名称
	 */
	private String name; 
	
	private String mobile;
	
	private Long orderId;
	private String orderNo;
	
	
	/**
	 * 课程id 
	 */
	private Long courseId;
	/***
	 * 课程名称
	 */
	private  String courseName;
	/**
	 * 授课老师
	 */
	private  String teacherName;
	/**
	 * 	授课老师ID
	 */
	private String  teacherId;
	/**
	 * 	商品名称
	 */
	private  String commodityName;
	/**
	 * 商品id
	 */
	private Long commodityId;
	/**
	 * 排课计划id
	 */
	private String classplanId;
	
	/**
	 * 排课计划名称
	 */
	private  String classplanName;
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
	 * 班级名称
	 */
	private  String className;
	/**
	 * 班主任老师id
	 */
	private Long classTeacherId;
	
	
	/**
	 * 听课完成率
	 */
	private Double classCompletionRate;
	/**
	 * 听课完成率文字显示
	 */
	private String classCompletionRateStr;
	
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
	 * 统考成绩 (整型,产品说没有小数点)
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
	  * 是否删除 1 是 0 否
	  */
      private  int dr;
	 
	 /**
	  * 开课时间
	  */
	 private Date startTime;
	 
	 /**
	  * 是否排课
	  */
	 private int inClass;
	 
		/**
		 * 学员学习信息主键
		 */
		private Long recordStudyId;
		
	 
	 
	 
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
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
	 
 
 
	public Integer getUnExaminationScore() {
		return unExaminationScore;
	}
	public void setUnExaminationScore(Integer unExaminationScore) {
		this.unExaminationScore = unExaminationScore;
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
	
	 
 
	public String getClassplanId() {
		return classplanId;
	}
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	public String getClassplanName() {
		return classplanName;
	}
	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Long getClassTeacherId() {
		return classTeacherId;
	}
	public void setClassTeacherId(Long classTeacherId) {
		this.classTeacherId = classTeacherId;
	}
	public String getClassTeacherName() {
		return classTeacherName;
	}
	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}
	/**
	 * 班主任老师名称
	 */
	private  String classTeacherName;

	
	
	
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTeacherId() {
		return teacherId;
	}
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
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
	public Long getUserplanClassId() {
		return userplanClassId;
	}
	public void setUserplanClassId(Long userplanClassId) {
		this.userplanClassId = userplanClassId;
	}	

	
	
	
	
	
	
	
	public String getClassCompletionRateStr() {
		
		return this.DoubleToPercents(this.classCompletionRate);
	}
	public void setClassCompletionRateStr(String classCompletionRateStr) {
		this.classCompletionRateStr = classCompletionRateStr;
	}
	public void StudyUpdate(RecordStudyPOJO oldE,RecordStudyPOJO newE) {
	//	oldE.setClassCompletionRate (newE.getClassCompletionRate());
		oldE.setClassId (newE.getClassId());
		oldE.setClassName (newE.getClassName());
		oldE.setClassplanId (newE.getClassplanId());
		oldE.setClassplanName (newE.getClassplanName());
		oldE.setClassTeacherId (newE.getClassTeacherId());
		oldE.setClassTeacherName (newE.getClassTeacherName());
		oldE.setCommodityId (newE.getCommodityId());
		oldE.setCommodityName (newE.getCommodityName());
		oldE.setCourseId (newE.getCourseId());
		oldE.setCourseName (newE.getCourseName());
		// oldE.setDr (newE.getDr());
	//	oldE.setFinalExamScore (newE.getFinalExamScore());
	//	oldE.setInClass (newE.getInClass());
	//	oldE.setKaobaOneScore (newE.getKaobaOneScore());
	//	oldE.setKaobaTwoScore (newE.getKaobaTwoScore());
	//	oldE.setMidtermExamScore (newE.getMidtermExamScore());
		oldE.setMobile (newE.getMobile());
		oldE.setName (newE.getName());
		oldE.setOrderId (newE.getOrderId());
		oldE.setOrderNo (newE.getOrderNo());
	//	oldE.setRecordStudyId (newE.getRecordStudyId());
	//	oldE.setScoreType (newE.getScoreType());
		oldE.setStartTime (newE.getStartTime());
	//	oldE.setStudyPart (newE.getStudyPart());
		oldE.setTeacherId (newE.getTeacherId());
		oldE.setTeacherName (newE.getTeacherName());
	//	oldE.setTikuHomeWorkCompletionRate (newE.getTikuHomeWorkCompletionRate());
	//	oldE.setUnExaminationScore (newE.getUnExaminationScore());
		oldE.setUserId (newE.getUserId());
	 	oldE.setUserplanClassId (newE.getUserplanClassId());
		oldE.setUserplanDetailId (newE.getUserplanDetailId());
		oldE.setUserplanId (newE.getUserplanId());
	//	oldE.setZeroBasisPracticeScore (newE.getZeroBasisPracticeScore());

	}
	
	public Date  ScheduleDateToDate(String scheduleDate) {
		try {
		 if (scheduleDate.contains("/")) {
		//	 scheduleDate= scheduleDate.replace("/", "-");
			return  DateUtils.parse(scheduleDate,"yyyy/MM");
			 
		 }
		 return  DateUtils.parse("1990-01","yyyy-MM");
		}catch(Exception es) {
			
		}
		 return  DateUtils.parse("1990-01","yyyy-MM");
	 }
	
	

	private String DoubleToPercents(Double rate) {
		try {
		 
		
		if (rate==0){
			return "0.00%";
		}else {
			
			 
			
			 BigDecimal p = new BigDecimal(rate);//保存4位小数
	         BigDecimal h = new BigDecimal(100);
	         

			
			
		DecimalFormat df=new DecimalFormat("#.00");
	     return   (df.format( p.multiply(h))+"%");
		}
		}catch(Exception es) {
			es.printStackTrace();
		 return "0.00%";
		}
		 
	}
	
	
}
