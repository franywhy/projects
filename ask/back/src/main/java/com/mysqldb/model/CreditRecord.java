package com.mysqldb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
/**
 * 学分记录表
 * @author yanzhicheng 
 * @Time 2017年2月23日22:32:16
 */
@Entity
@Table(name = "credit_record")
@org.hibernate.annotations.Table(appliesTo = "credit_record", comment = "credit_record")
public class CreditRecord implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CreditRecord(){
		
	}
	/**
	 * 老师界面查询用到的构造方法
	 */
	public CreditRecord(Integer id, String studentName, String orgName,String orgCode, String className, String subjectName,
			 Integer attendanceActualScore,Integer attendanceClaimScore,
			 Integer workActualScore,Integer workClaimScore,
			 Integer examActualScore,Integer examClaimScore,
			Integer totalScore, Integer isPass,String signDate,
			String ncSubjectId,String ncUserId,String teacherId) {
		super();
		this.id = id;
		this.subjectName = subjectName;
		this.orgName = orgName;
		this.orgCode = orgCode;
		this.className = className;
		this.attendanceClaimScore = attendanceClaimScore;
		this.attendanceActualScore = attendanceActualScore;
		this.workClaimScore = workClaimScore;
		this.workActualScore = workActualScore;
		this.examClaimScore = examClaimScore;
		this.examActualScore = examActualScore;
		this.totalScore = totalScore;
		this.isPass = isPass;
		this.studentName = studentName;
		this.signDate = signDate;
		this.subjectId = ncSubjectId;
		this.studentId = ncUserId;
		this.teacherId = teacherId;
	}

public CreditRecord(CreditRecordSignCVO cvo,CreditStandard st){

	this.classId=cvo.getClassId() ;
	this.studentId=cvo.getStudentId();
	this.signId=cvo.getSignId() ;
	this.signIdc= cvo.getSignIdc() ;
	this.signDate=cvo.getSignDate().substring(0,10);
	this.signCode=cvo.getSignCode();
	this.subjectCode=cvo.getSubjectCode();
	this.subjectName=cvo.getSubjectName();
	this.subjectType=cvo.getSubjectType();	
	this.isCheck=cvo.getIsCheck();
	this.orgId=cvo.getOrgId();
	this.idCard=cvo.getIdCard();
	this.phone=cvo.getPhone();
	this.studentName=cvo.getStudentName();
 	if(st!=null){
	this.attendanceClaimScore=st.getAttendance_score() ;	 
	this.workClaimScore=st.getActivity_fraction() ; 
	this.examClaimScore=st.getGraduation_examination_score() ;	
	this.claimScore=(st.getAttendance_score()+st.getActivity_fraction()+st.getGraduation_examination_score());
	this.examType=st.getExam_type();
	this.subjectType=st.getSubject_type();
	
 	}
	}
	
 
	
	
	
	



	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 科目名称
	 */
	private String subjectName;
	
	/**
	 * 科目编码
	 */
	private String subjectCode;
	
	/**
	 * 科目id
	 */
	private String subjectId;
	
	
	
	/**
	 * 班型id
	 */
	private String classId;
	/**
	 * 报读班型 
	 */
	private String className;
	
	/**
	 * 学员NCID
	 */
	private String studentId;
	
	/**
	 * 出勤应休学分
	 */
	private Integer attendanceClaimScore =0;
	
	/**
	 * 出勤实休学分
	 */
	private Integer attendanceActualScore=0;
	
	/**
	 * 作业应休学分
	 */
	private Integer workClaimScore=0;
	
	/**
	 * 作业实休学分
	 */
	private Integer workActualScore=0;
	
	/**
	 * 考试应休学分
	 */
	private Integer examClaimScore=0;
	
	
	/**
	 * 考试实休学分
	 */
	private Integer examActualScore=0;
	
	
	
	/**
	 * 每一个科目合计总学分
	 */
	private Integer totalScore=0;
	
	
	/**
	 * 是否通过
	 */
	private Integer isPass = 1;
	
	/**
	 * 报名日期
	 */
	private String signDate;
	
	/**
	 * 时间戳
	 */
	private Date ts;
	
	/**
	 * 是否获取现金卷
	 */
	private String isGainCash;
	
	/**
	 * 授课老师主键
	 */
	private String teacherId;
	
	/**
	 * 授课老师名称
	 */
	private String teacherName;
	
	/**
	 * 校区名称
	 */
	private String orgName;
	
	/**
	 * 校区编码
	 */
	private String orgCode;
	
	/**
	 * 大区名称
	 */
	private String largeAreaName;

	/**
	 * 学生名称
	 */
	private String studentName;

	private String subjectType; //科目类型
	private String examType; //考试类型
	private String signId; //报名表hid
	private String signIdc;//报名表cid
	private Integer isEnable=1;//是否启用 0为启用1 为不启用
 
	 
	private String orgId;//校区pk
	private String arrName; //班级名称
	private String arrDate; //结课时间
	private String createDate; //创建时间
	private String creater; //创建者 admin\sync\ncws\
	private Integer isObsoleted=1;//是否是旧数据 0 是 1 否
	/**
	* 应考勤次数
	 */
	 private Integer rS;
	/**
	 * 已经签到的次数
	 */
	 private Integer kQ;
	/**
	 * 出勤率 0-100;
	 */
	 private Integer kqV;
	
	/**
	 * 应修总分
	 */
	private Integer claimScore;
/**
 * 是否考核 0为考核 1为不考核
 */
	private Integer isCheck=0;
	
	private String signCode;//报名表号

	private String phone;//电话

	private String idCard;  
	@Column(name = "teacher_id",columnDefinition = "VARCHAR")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	@Column(name = "subject_type",columnDefinition = "VARCHAR")
	public String getSubjectType() {
		return subjectType;
	}

	public void setSubjectType(String SubjectType) {
		this.subjectType = SubjectType;
	}
	@Column(name = "exam_type",columnDefinition = "VARCHAR")
	public String getExamType() {
		return examType;
	}

	public void setExamType(String ExamType) {
		this.examType = ExamType;
	}
	@Column(name = "sign_id",columnDefinition = "VARCHAR")
	public String getSignId() {
		return signId;
	}

	public void setSignId(String SignId) {
		this.signId = SignId;
	}
	
	@Column(name = "sign_id_c",columnDefinition = "VARCHAR")
	public String getSignIdc() {
		return signIdc;
	}

	public void setSignIdc(String signIdc) {
		this.signIdc = signIdc;
	}
	
	@Column(name = "is_enable",columnDefinition = "INT")
	public Integer getIsEnable() {
		return isEnable;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
	
	@Column(name = "is_obsoleted",columnDefinition = "INT")
	public Integer getIsObsoleted() {
		return isObsoleted;
	}

 
	
	
	
	
	@Column(name = "rS",columnDefinition = "INT")
	public Integer getrS() {
		return rS;
	}

	public void setrS(Integer rS) {
		this.rS = rS;
	}
	@Column(name = "kQ",columnDefinition = "INT")
	public Integer getkQ() {
		return kQ;
	}

	public void setkQ(Integer kQ) {
		this.kQ = kQ;
	}
	@Column(name = "kqV",columnDefinition = "INT")
	public Integer getkqV() {
		return kqV;
	}

	public void setkqV(Integer kqV) {
		this.kqV = kqV;
	}
	
	
	
	@Column(name = "org_id",columnDefinition = "VARCHAR")
	public String getorgId() {
		return orgId;
	}

	public void setorgId(String orgId) {
		this.orgId = orgId;
	}
	
	/**
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId(){
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(Integer id){
		this.id	= id;
	}
	
	
	/**
	 * 获取科目名
	 */
	@Column(name = "subject_name",columnDefinition = "VARCHAR")
	public String getSubjectName() {
		return subjectName;
	}

	/**
	 * 设置科目名
	 */
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}
	
	
	
	/**
	 * 获取NC科目id
	 * @return
	 */
	@Column(name = "subject_id",columnDefinition = "VARCHAR")
	public String getSubjectId() {
		return subjectId;
	}
	/**
	 * 设置NC科目id
	 * @return
	 */
	public void setSubjectId(String ncSubjectId) {
		this.subjectId = ncSubjectId;
	}

	/**
	 * 获取学员NCID
	 */
	@Column(name = "student_id",columnDefinition = "VARCHAR")
	public String getStudentId() {
		return studentId;
	}

	/**
	 * 设置学员NCID
	 */
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取出勤应休学分
	 */
	@Column(name = "attendance_claim_score",columnDefinition = "INT")
	public Integer getAttendanceClaimScore() {
		return attendanceClaimScore;
	}
	/**
	 * 设置出勤应休学分
	 */
	public void setAttendanceClaimScore(Integer attendanceClaimScore) {
		this.attendanceClaimScore = attendanceClaimScore;
	}
	/**
	 * 获取出勤实休学分
	 */
	@Column(name = "attendance_actual_score",columnDefinition = "INT")
	public Integer getAttendanceActualScore() {
		return attendanceActualScore;
	}
	/**
	 * 设置出勤实休学分
	 */
	public void setAttendanceActualScore(Integer attendanceActualScore) {
		this.attendanceActualScore = attendanceActualScore;
	}
	/**
	 * 获取作业应休学分
	 */
	@Column(name = "work_claim_score",columnDefinition = "INT")
	public Integer getWorkClaimScore() {
		return workClaimScore;
	}
	/**
	 * 设置作业应休学分
	 */
	public void setWorkClaimScore(Integer workClaimScore) {
		this.workClaimScore = workClaimScore;
	}
	/**
	 * 获取作业实休学分
	 */
	@Column(name = "work_actual_score",columnDefinition = "INT")
	public Integer getWorkActualScore() {
		return workActualScore;
	}
	/**
	 * 设置作业实休学分
	 */
	public void setWorkActualScore(Integer workActualScore) {
		this.workActualScore = workActualScore;
	}
	/**
	 * 获取考试应休学分
	 */
	@Column(name = "exam_claim_score",columnDefinition = "INT")
	public Integer getExamClaimScore() {
		return examClaimScore;
	}
	/**
	 * 设置考试应休学分
	 */
	public void setExamClaimScore(Integer examClaimScore) {
		this.examClaimScore = examClaimScore;
	}
	/**
	 * 获取考试实休学分
	 */
	@Column(name = "exam_actual_score",columnDefinition = "INT")
	public Integer getExamActualScore() {
		return examActualScore;
	}
	/**
	 * 设置考试实休学分
	 */
	public void setExamActualScore(Integer examActualScore) {
		this.examActualScore = examActualScore;
	}
	/**
	 * 获取每一科目合计总分
	 */
	@Column(name = "total_score",columnDefinition = "INT")
	public Integer getTotalScore() {
		return totalScore;
	}

	/**
	 * 设置每一科目合计总分
	 */
	public void setTotalScore(Integer totalScore) {
		this.totalScore = totalScore;
	}
	/**
	 * 获取 是否通过
	 */
	@Column(name = "is_pass",columnDefinition = "VARCHAR")
	public Integer getIsPass() {
		return isPass;
	}

	/**
	 * 设置是否通过
	 * @param isPass
	 */
	public void setIsPass(Integer isPass) {
		this.isPass = isPass;
	}

	/**
	 * 获取 时间戳
	 */
	@Column(name = "TS",insertable = false,updatable=false,columnDefinition = "DATETIME")
	public Date getTs(){
		return this.ts;
	}

	/**
	 * 设置 时间戳
	 */
	public void setTs(Date ts){
		this.ts	= ts;
	}

	/**
	 * 获取 是否获取现金卷
	 */
	@Column(name = "is_gain_cash",columnDefinition = "VARCHAR")
	public String getIsGainCash() {
		return isGainCash;
	}

	/**
	 * 设置 是否获取现金卷
	 */
	public void setIsGainCash(String isGainCash) {
		this.isGainCash = isGainCash;
	}

	/**
	 * 获取科目编码
	 */
	@Column(name = "subject_code",columnDefinition = "VARCHAR")
	public String getSubjectCode() {
		return subjectCode;
	}
	/**
	 * 设置 科目编码
	 */
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	
	/**
	 * 获取班型主键
	 * @return
	 */
	@Column(name = "class_id",columnDefinition = "VARCHAR")
	public String getClassId() {
		return classId;
	}
	/**
	 * 设置 班型主键
	 */
	public void setClassId(String classId) {
		this.classId = classId;
	}

	@Column(name = "sign_date",columnDefinition = "VARCHAR")
	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	/**
	 * 获取 班级名称
	 */
	@Column(name = "class_name",columnDefinition = "VARCHAR")
	public String getClassName() {
		return className;
	}
	

	/**
	 * 设置 班级名称
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	
	@Column(name = "claim_score",columnDefinition = "INT")
	public Integer getClaimScore() {
		return claimScore;
	}

	public void setClaimScore(Integer claimScore) {
		this.claimScore = claimScore;
	}
	@Column(name = "student_name",columnDefinition = "VARCHAR")
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	
	/**
	 * 是否考核 0为考核 1为不考核
	 * @return
	 */
	@Column(name = "is_check",columnDefinition = "INT")
	public Integer getIsCheck() {
		return isCheck;
	}
	/**
	 * 是否考核 0为考核 1为不考核
	 * @return
	 */
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
	}

	@Column(name = "sign_code",columnDefinition = "VARCHAR")
	public String getSignCode() {
		return signCode;
	}

	/**
	 * 报名表号
	 * @param signCode
	 */
	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}
	/**
	 * 电话号
	 * @param idCard
	 */
	@Column(name = "phone",columnDefinition = "VARCHAR")
	public String getPhone() {
		return phone;
	}

	/**
	 * 电话号
	 * @param idCard
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}
	/**
	 * 身份证
	 * @param idCard
	 */
	@Column(name = "idcard",columnDefinition = "VARCHAR")
	public String getIdCard() {
		return idCard;
	}
/**
 * 身份证
 * @param idCard
 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	 
	
	
	@Column(name = "arr_name",columnDefinition = "VARCHAR")
	public String getArrName() {
		return arrName;
	}
	public void setArrName(String arrName) {
		this.arrName = arrName;
	}
	@Column(name = "arr_date",columnDefinition = "VARCHAR")
	public String getArrDate() {
		return arrDate;
	}
	public void setArrDate(String arrDate) {
		this.arrDate = arrDate;
	}
	@Column(name = "create_date",columnDefinition = "VARCHAR")
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	@Column(name = "creater",columnDefinition = "VARCHAR")
	public String getCreater() {
		return creater;
	}
	public void setCreater(String creater) {
		this.creater = creater;
	}
	
	
	
	/****************老师界面查询需要临时字段 begin***************************/
	

	@Transient 
	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Transient
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	
	
	
	@Transient
	public String getLargeAreaName() {
		return largeAreaName;
	}
	public void setLargeAreaName(String largeAreaName) {
		this.largeAreaName = largeAreaName;
	}
	
	/****************老师界面查询需要临字段 end***************************/
	
	/**
	 * 学员端需要显示的班级学习建议
	 */
	private String learningTip;
	@Transient 
	public String getLearningTip() {
		return learningTip;
	}
	public void setLearningTip(String learningTip) {
		this.learningTip = learningTip;
	}
	/**
	 * 学分记录-教学老师学员显示的老师名称
	 */
	@Transient
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public void setIsObsoleted(Integer isObsoleted) {
		this.isObsoleted = isObsoleted;
	}

	
}
