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
 * 学分和排课完成率表中的详细学员数据 
 *  
 * @author lintf 
 * @Time 2017年11月13日14:23:32
 */
@Entity
@Table(name = "credit_percent_detail")
@org.hibernate.annotations.Table(appliesTo = "credit_percent_detail", comment = "credit_percent_detail")
public class CreditPercentDetail implements Serializable {



	private static final long serialVersionUID = 1L;

 
	
	public CreditPercentDetail(){
		
	}
	 

	/**
	 *从学员档案构建
	 * @param dvo
	 */
public CreditPercentDetail(CreditRecord dvo){
	this.attendanceActualScore=dvo.getAttendanceActualScore();
	this.attendanceClaimScore=dvo.getAttendanceClaimScore();
	this.workActualScore=dvo.getWorkActualScore();
	this.workClaimScore=dvo.getWorkClaimScore();
	this.examActualScore=dvo.getExamActualScore();
	this.examClaimScore=dvo.getExamClaimScore();
	this.classId=dvo.getClassId();
	this.signDate=dvo.getSignDate();
	this.signID=dvo.getSignId();
	this.signIDC=dvo.getSignIdc();
	this.ncUserId=dvo.getStudentId();
	this.ncSubjectId=dvo.getSubjectId();
	this.isPass=dvo.getIsPass();
	this.totalScore=dvo.getTotalScore();
	this.claimScore=dvo.getClaimScore();
	this.orgId=dvo.getorgId();
	this.orgCode=dvo.getOrgCode();
	this.className=dvo.getArrName();
	this.teacherId=dvo.getTeacherId();
	this.SubjectType=dvo.getSubjectType();
	this.months=dvo.getSignDate().substring(0,7);
	if (dvo.getArrName()!=null){
		this.isPaike=1;
	}else {
		this.isPaike=0;
	}
	
 
	}


	/**
	 * id
	 */
	private Integer id;
	 
	private Date ts;
	private String pid;

      
	private String orgId; 

	/**
	 * 校区编码
	 */
	private String orgCode;
	
	/**
	 * 月份
	 */
	private String months;	
	private String dbilldate;
	/**
	 * 总应修
	 */
	private Integer claimScore;	
	private String ncSubjectId;
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
	
	
	private String signID; //报名表hid
	private String signIDC;//报名表cid
 
	
	
	/**
	 * 是否通过 0为通过 1为不通过
	 */
	private Integer isPass = 1;
	/**
	 * 是否排课 1 为排 0为未排
	 */
	private Integer isPaike =0;
	
	
	/**
	 * 是否合格的报表显示
	 */
	private String  passRemark;
	/**
	 * 是否排课的报表显示
	 */
	private String  paikeRemark;
	/**
	 * 授课老师主键
	 */
	private String teacherId;
	
	/**
	 * 授课老师名称
	 */
	private String teacherName;
	/**
	 * 班级名称-(和班型名称是不同的)
	 */
	private String className;
	/**
	 * 报名日期
	 */
	private String signDate;
	/**
	 * 总已收
	 */
	private Integer totalScore;	
 
 
	/**
	 * 1 是删除 0是不删除
	 */
	private Integer dr=1;
	/**
	 * 报读班型
	 */

	private String classId;	
 
 
	private String SubjectName;
	private String SubjectType;
	private String SubjectCode;
	private String StudentName;
	
	private String SequenceName;

	/**
	 * 大区名称
	 */
	private String largeAreaName;

	/**
	 * 校区名称
	 */
	private String orgName;

	private String phone;

	private String signCode;

	/**
	 * 老师编号
	 */
	private String teacherCode;
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
	@Column(name = "pid",columnDefinition = "VARCHAR") 
	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	 
	
	@Column(name = "sign_id",columnDefinition = "VARCHAR")
	public String getsignID() {
		return signID;
	}

	public void setsignID(String SignID) {
		this.signID = SignID;
	}
	
	@Column(name = "sign_id_c",columnDefinition = "VARCHAR")
	public String getsignIDC() {
		return signIDC;
	}

	public void setsignIDC(String signIDC) {
		this.signIDC = signIDC;
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
	 * 设置dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	/**
	 * 获取dr
	 */
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
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
	@Column(name = "sign_date",columnDefinition = "VARCHAR")
	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
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
 
	@Column(name = "orgId",columnDefinition = "VARCHAR") 
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	@Column(name = "orgCode",columnDefinition = "VARCHAR") 
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	@Column(name = "claim_score",columnDefinition = "INT")
	public Integer getClaimScore() {
		return claimScore;
	}

	public void setClaimScore(Integer claimScore) {
		this.claimScore = claimScore;
	}
	
	@Column(name = "dbilldate",columnDefinition = "VARCHAR")
	public String getDbilldate() {
		return dbilldate;
	}


	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}
	@Column(name = "months",columnDefinition = "VARCHAR")
	public String getMonths() {
		return months;
	}


	public void setMonths(String months) {
		this.months = months;
	}
	/**
	 * 是否排课0 为未排 1 为排
	 * @return
	 */
	@Column(name = "is_paike",columnDefinition = "INT")
	public Integer getIsPaike() {
		return isPaike;
	}

	/**
	 * 是否排课0 为未排 1 为排
	 * @return
	 */
	public void setIsPaike(Integer isPaike) {
		this.isPaike = isPaike;
	}

	@Column(name = "teacher_id",columnDefinition = "VARCHAR") 
	public String getTeacherId() {
		return teacherId;
	}


	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}

	@Column(name = "teacher_name",columnDefinition = "VARCHAR") 
	public String getTeacherName() {
		return teacherName;
	}


	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	@Column(name = "class_name",columnDefinition = "VARCHAR") 
	public String getClassName() {
		return className;
	}


	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * 学员名称
	 * @param StudentName
	 */
	@Transient
	public String getStudentName() {
		return StudentName;
	}

/**
 * 学员名称
 * @param StudentName
 */
	public void setStudentName(String StudentName) {
		this.StudentName = StudentName;
	}
	@Transient
	public String getSubjectName() {
		return SubjectName;
	}
	/**
	 * 科目名称
	 * @param Subject
	 */
		public void setSubjectName(String Subject) {
			this.SubjectName = Subject;
		}

/**
 * 科目编号
 * @param Subject
 */
	public void setSubjectCode(String SubjectCode) {
		this.SubjectCode = SubjectCode;
	}
	
	@Transient
	public String getSubjectCode() {
		return SubjectCode;
	}


	
	
	
	@Column(name = "subject_type",columnDefinition = "VARCHAR") 
	public String getSubjectType() {
		return SubjectType;
	}


	public void setSubjectType(String subjectType) {
		SubjectType = subjectType;
	}


	/**
	 *报读班型 
	 * @param StudentName
	 */
	@Transient
	public String getSequenceName() {
		return SequenceName;
	}

/**
 * 报读班型
 * @param  
 */
	public void setSequenceName(String SequenceName) {
		this.SequenceName = SequenceName;
	}

	@Transient
public String getLargeAreaName() {
	return largeAreaName;
}


public void setLargeAreaName(String largeAreaName) {
	this.largeAreaName = largeAreaName;
}

@Transient
public String getOrgName() {
	return orgName;
}


public void setOrgName(String orgName) {
	this.orgName = orgName;
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

@Transient
public String getTeacherCode() {
	return teacherCode;
}


public void setTeacherCode(String teacherCode) {
	this.teacherCode = teacherCode;
}

@Transient
public String getPassRemark() {
	return passRemark;
}


public void setPassRemark(String passRemark) {
	this.passRemark = passRemark;
}

@Transient
public String getPaikeRemark() {
	return paikeRemark;
}


public void setPaikeRemark(String paikeRemark) {
	this.paikeRemark = paikeRemark;
}
	
}
