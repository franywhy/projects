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
 * 学分报表中的用来存临时字段的 
 * @author lintf 
 * @Time 2017年9月21日9:51:54
 */
@Entity
@Table(name = "credit_record_reporttemp")
@org.hibernate.annotations.Table(appliesTo = "credit_record_reporttemp", comment = "credit_record_reporttemp")
public class CreditRecordReportTemp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CreditRecordReportTemp(){
		
	} 
	
	public CreditRecordReportTemp(CreditRecord d){
		this.signIDC=d.getSignIdc();
		this.attendanceActualScore=d.getAttendanceActualScore()==null?0:d.getAttendanceActualScore();
		this.attendanceClaimScore=d.getAttendanceClaimScore()==null?0:d.getAttendanceClaimScore();
		this.workActualScore=d.getWorkActualScore()==null?0:d.getWorkActualScore();
		this.workClaimScore=d.getWorkClaimScore()==null?0:d.getWorkClaimScore();
		this.examActualScore=d.getExamActualScore()==null?0:d.getExamActualScore();
		this.examClaimScore=d.getExamClaimScore()==null?0:d.getExamClaimScore();
		this.claimScore=d.getClaimScore()==null?0:d.getClaimScore();
		this.totalScore=d.getTotalScore()==null?0:d.getTotalScore();
		this.orgCode=d.getOrgCode();
		this.orgName=d.getOrgName();
		this.orgId=d.getorgId();
		this.isPass=d.getIsPass();
		this.ncUserId=d.getStudentId();
		this.signId=d.getSignId();
		this.signDate=d.getSignDate();
		this.teacherId=d.getTeacherId();
		this.arrName=d.getClassName();
		this.subjectName=d.getSubjectName();
		this.subjectCode=d.getSubjectCode();
		this.isCheck=d.getIsCheck();
		if (d.getClassName()!=null&&d.getClassName().length()>1){
			this.isPaike=1;
			this.paikeRemark="已排课";
		}else {
			this.isPaike=0;
			this.paikeRemark="未排课";
		}
		 if (d.getIsPass()==0){
			 this.passRemark="合格";
		 }else {
			 this.passRemark="不合格";
		 }
		 if (d.getIsCheck()==0){
			 this.checkRemark="考核";
		 }else {
			 this.checkRemark="不考核";
		 }
		
		
	} 
	
	
	

	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 报名表主键
	 */
	private String signId;
	private String signCode;
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
	private String ncSubjectId;
	
	
	
	/**
	 * 班型id
	 */
	private String classId;
	/**
	 * 班级名称-(和班型名称是不同的)
	 */
	private String className;
	/**
	 * 排课的班级名称
	 */
	private String arrName;
	
	private String phone;
	/**
	 * 学员NCID
	 */
	private String ncUserId;
	
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
	 * 是否排课 0是未排1是排
	 */
	private Integer isPaike = 1;
	
	/**
	 * 科目是否考核
	 */
	private Integer isCheck=0;
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
	 * 老师编号
	 */
	private String teacherCode;
	/**
	 * 校区名称
	 */
	private String orgName;
	
	private String orgId;
	/**
	 * 校区编码
	 */
	private String orgCode;
	private String signIDC;//报名表cid
	/**
	 * 大区名称
	 */
	private String largeAreaName;

	/**
	 * 学生名称
	 */
	private String studentName;
	
	/**
	 * 应修总分
	 */
	private Integer claimScore;
	
	/**
	 * 学员数量
	 */
	private Integer studentnumber;
	private Integer passrs; //通过人数
	private Integer pkrs; //排课人数
	private Integer nopkrs; //未排课人数
	private String creditcompletionrate;
	private String paikerate;
	private String passrate;
	private Integer issum=0;//是否合计1 为是否计 0为不是合计
	
	/**
	 * 是否合格 用来显示文字的
	 */
	private String passRemark;
	/**
	 * 是否不考核 用来显示文字的
	 */
	private String checkRemark;
	/**
	 * 是否排课 用来显示文字的
	 */
	private String paikeRemark;
	
	
	
	
	
	
	
	@Column(name = "teacher_id",columnDefinition = "VARCHAR")
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Column(name = "sign_id",columnDefinition = "VARCHAR")
	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
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
	@Column(name = "nc_subject_id",columnDefinition = "VARCHAR")
	public String getNcSubjectId() {
		return ncSubjectId;
	}
	/**
	 * 设置NC科目id
	 * @return
	 */
	public void setNcSubjectId(String ncSubjectId) {
		this.ncSubjectId = ncSubjectId;
	}

	/**
	 * 获取学员NCID
	 */
	@Column(name = "nc_user_id",columnDefinition = "VARCHAR")
	public String getNcUserId() {
		return ncUserId;
	}

	/**
	 * 设置学员NCID
	 */
	public void setNcUserId(String ncUserId) {
		this.ncUserId = ncUserId;
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
	 * 是否排课 1是排0是未排
	 * @return
	 */
	@Transient 	 
	public Integer getIsPaike() {
		return isPaike;
	}

	/**
	 * 是否排课 1是排0是未排
	 * @param isPaike
	 */
	public void setIsPaike(Integer isPaike) {
		this.isPaike = isPaike;
	}

	/**
	 * 是否考核 0是考核 1是不考核
	 * @return
	 */
	@Transient 	 
	public Integer getIsCheck() {
		return isCheck;
	}

	/**
	 *  是否考核 0是考核 1是不考核
	 * @param isCheck
	 */
	public void setIsCheck(Integer isCheck) {
		this.isCheck = isCheck;
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
	
	/****************老师界面查询需要临时字段 begin***************************/
	
	@Transient 
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
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
	@Transient 
	public Integer getClaimScore() {
		return claimScore;
	}

	public void setClaimScore(Integer claimScore) {
		this.claimScore = claimScore;
	}
	
	 
	
	
	
	
	
	
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public String getPassRemark() {
		return passRemark;
	}
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public void setPassRemark(String passRemark) {
		this.passRemark = passRemark;
	}
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public String getCheckRemark() {
		return checkRemark;
	}
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public void setCheckRemark(String checkRemark) {
		this.checkRemark = checkRemark;
	}
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public String getPaikeRemark() {
		return paikeRemark;
	}
	
	/**
	 * passRemark paikeRemark checkRemark 是合格 排课 考核的报表显示文字
	 *  
	 */
	public void setPaikeRemark(String paikeRemark) {
		this.paikeRemark = paikeRemark;
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
	@Transient
	public String getTeacherCode() {
		return teacherCode;
	}
	public void setTeacherCode(String teacherCode) {
		this.teacherCode = teacherCode;
	}
	
	@Transient
	public String getArrName() {
		return arrName;
	}
	public void setArrName(String arrName) {
		this.arrName = arrName;
	}
	@Transient
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Transient
	public String getSignCode() {
		return signCode;
	}
	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}
	@Column(name = "studentnumber",columnDefinition = "INT")
	public Integer getStudentnumber() {
		return studentnumber;
	}

	public void setStudentnumber(Integer studentnumber) {
		this.studentnumber = studentnumber;
	}
	@Column(name = "passrs",columnDefinition = "INT")
	public Integer getPassrs() {
		return passrs;
	}

	public void setPassrs(Integer passrs) {
		this.passrs = passrs;
	}
	@Column(name = "pkrs",columnDefinition = "INT")
	public Integer getPkrs() {
		return pkrs;
	}

	public void setPkrs(Integer pkrs) {
		this.pkrs = pkrs;
	}
	@Column(name = "nopkrs",columnDefinition = "INT")
	public Integer getNopkrs() {
		return nopkrs;
	}

	public void setNopkrs(Integer nopkrs) {
		this.nopkrs = nopkrs;
	}
	@Column(name = "issum",columnDefinition = "INT")
	public Integer getIssum() {
		return issum;
	}

	public void setIssum(Integer issum) {
		this.issum = issum;
	}
	 
	@Column(name = "creditcompletionrate",columnDefinition = "VARCHAR")
	public String getCreditcompletionrate() {
		return creditcompletionrate;
	}

	public void setCreditcompletionrate(String creditcompletionrate) {
		this.creditcompletionrate = creditcompletionrate;
	}
	@Column(name = "paikerate",columnDefinition = "VARCHAR")
	public String getPaikerate() {
		return paikerate;
	}

	public void setPaikerate(String paikerate) {
		this.paikerate = paikerate;
	}
	@Column(name = "passrate",columnDefinition = "VARCHAR")
	public String getPassrate() {
		return passrate;
	}

	public void setPassrate(String passrate) {
		this.passrate = passrate;
	}
	
	@Column(name = "org_id",columnDefinition = "VARCHAR")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	@Column(name = "sign_id_c",columnDefinition = "VARCHAR")
	public String getsignIDC() {
		return signIDC;
	}

	public void setsignIDC(String signIDC) {
		this.signIDC = signIDC;
	}
	
}
