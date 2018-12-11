package com.mysqldb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.izhubo.credit.vo.RegistrationSYNCVO;

/**
 * 学分计算的报名表信息
 * @author yanzhicheng 
 * @Time 2017年2月23日22:32:16
 */
@Entity
@Table(name = "credit_record_sign")
@org.hibernate.annotations.Table(appliesTo = "credit_record_sign", comment = "credit_record_sign")
public class CreditRecordSign {
	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * 报名表主键
	 */
	private String signId;
	
	/**
	 * 报名表编码
	 */
	private String signCode;
	
	/**
	 * 身份证号
	 */
	private String idCard;
	
	/**
	 * 电话
	 */
	private String phone;
	
	/**
	 * 班型主键
	 */
	private String classId;
	
	/**
	 * 班型编码
	 */
	private String classCode;
	
	/**
	 * 班型名称
	 */
	private String className;
	
	/**
	 * 校区主键
	 */
	private String orgId;
	
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
	 * 时间戳
	 */
	
	private Date ts;
	/**
	 * 学员主键
	 */
	private String studentId;
	
	/**
	 * 学员名称
	 */
	private String studentName;
	
	/**
	 * 报名日期
	 */
	private String signDate;
	
	/**
	 * 班级应修总分
	 */
	private Integer classClaimScore;
	
	/**
	 * 班级实修总分
	 */
	private Integer classActualScore;
	
	
	/**
	 * 班级是否合格
	 */
	private Integer ispass;
	
	
	/**
	 * 学分完成率
	 */
	private String creditCompletionRate;
	
	
	/**
	 * 老师id
	 */
	private String teacherId;
	private Date SYTS;
	private Integer DR;
	private Integer registStatus;//报名表状态
	private Integer vbillStatus; //报名表单据状态
	private Integer isEnable; //是否启用 1为不启用 0为启用
	private String  createDate;  //创建时间
	private String 	creater; //创建来源
	/**
	 * 是否交齐款
	 */
	private String  isFullPay; 
	/**
	 * 是否排课
	 */
	private String  isPaike;
	
	
	/* *******************************报名表h表的构造 ***************** */
	public   CreditRecordSign(){
		
	}
	/**
	 * 从同步的接口处得到的每天定时来源
	 * @param sync
	 */
	public   CreditRecordSign(RegistrationSYNCVO sync){
		this.isFullPay=sync.getFULLPAY();
		this.isPaike=sync.getIS_PAIKE();
		this.signId=sync.getSIGN_ID();
		this.signCode=sync.getSIGN_CODE();
		this.signDate=sync.getSIGN_DATE().substring(0, 10);
		this.orgCode=sync.getORG_CODE();
		this.orgName=sync.getORG_NAME();
		this.orgId=sync.getORG_ID();
		this.className=sync.getCLASS_NAME();
		this.classId=sync.getCLASS_ID();
		this.classCode=sync.getCLASS_CODE();
		this.studentId=sync.getSTUDENT_ID();
		this.studentName=sync.getSTUDENT_NAME();
		this.phone=sync.getPHONE();
		this.idCard=sync.getID_CARD();
		this.SYTS=sync.getSYTS();
		this.DR=sync.getRH_DR();
		this.registStatus=sync.getREGISTSTATUS();
		this.vbillStatus=sync.getVBILLSTATUS();
		this.isEnable=sync.getENABLE();
				
				
		
	}
	
	
	/* *******************************下面是 get 和set ***************** */
	@Column(name = "sign_date",columnDefinition = "VARCHAR")
	public String getSignDate() {
		return signDate;
	}

	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}

	@Column(name = "student_id",columnDefinition = "VARCHAR")
	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	@Column(name = "student_name",columnDefinition = "VARCHAR")
	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
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
	 * 获取 时间戳
	 */
 
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
	@Column(name = "SYTS",  columnDefinition = "DATETIME")
	public Date getSYTS() {
		return SYTS;
	}

	public void setSYTS(Date sYTS) {
		SYTS = sYTS;
	}
/**
 * 单据是否删除 1为删除 0为正常
 * @return
 */
	@Column(name = "DR", columnDefinition = "INT")
	public Integer getDR() {
		return DR;
	}
	/**
	 * 单据是否删除 1为删除 0为正常
	 * @return
	 */
	public void setDR(Integer dr) {
		DR = dr;
	}
	/**
	 * 报名表报读状态
	 * @param registStatus
	 */
	@Column(name = "registStatus", columnDefinition = "INT")
	public Integer getRegistStatus() {
		return registStatus;
	}
	/**
	 * 报名表报读状态
	 * @param registStatus
	 */
	public void setRegistStatus(Integer registStatus) {
		this.registStatus = registStatus;
	}
	/**
	 * 报名表单据审批状态 1为审批通过 -1为自由
	 * @return
	 */
	@Column(name = "vbillstatus", columnDefinition = "INT")
	public Integer getVbillStatus() {
		return vbillStatus;
	}
	/**
	 * 报名表单据审批状态 1为审批通过 -1为自由
	 * @return
	 */
	public void setVbillStatus(Integer vbillStatus) {
		this.vbillStatus = vbillStatus;
	}
	/**
	 * 是否启用 0启1不启
	 * @return
	 */
	@Column(name = "is_enable", columnDefinition = "INT")
	public Integer getIsEnable() {
		return isEnable;
	}
	/**
	 * 是否启用 0启1不启
	 * @return
	 */
	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}
	
	
	/**
	 * 是否交齐款 
	 * @return
	 */
	@Column(name = "is_fullpay", columnDefinition = "VARCHAR")
	public String getIsFullPay() {
		return isFullPay;
	}
	/**
	 * 是否交齐款 
	 * @return
	 */
	public void setIsFullPay(String isFullPay) {
		this.isFullPay = isFullPay;
	}
	
/**
 * 是否已经排课
 * @return
 */
	@Column(name = "is_paike", columnDefinition = "VARCHAR")
	public String getIsPaike() {
		return isPaike;
	}
	/**
	 * 是否已经排课
	 * @return
	 */
	public void setIsPaike(String IsPaike) {
		isPaike = IsPaike;
	}
	
	

	@Column(name = "sign_id",columnDefinition = "VARCHAR")
	public String getSignId() {
		return signId;
	}

	public void setSignId(String signId) {
		this.signId = signId;
	}
	@Column(name = "sign_code",columnDefinition = "VARCHAR")
	public String getSignCode() {
		return signCode;
	}

	public void setSignCode(String signCode) {
		this.signCode = signCode;
	}
	@Column(name = "id_card",columnDefinition = "VARCHAR")
	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	@Column(name = "phone",columnDefinition = "VARCHAR")
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	@Column(name = "class_id",columnDefinition = "VARCHAR")
	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}
	@Column(name = "class_code",columnDefinition = "VARCHAR")
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	@Column(name = "class_name",columnDefinition = "VARCHAR")
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	@Column(name = "org_id",columnDefinition = "VARCHAR")
	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	@Column(name = "org_name",columnDefinition = "VARCHAR")
	public String getOrgName() {
		return orgName;
	}
	@Column(name = "org_code",columnDefinition = "VARCHAR")
	public String getOrgCode() {
		return orgCode;
	}
	

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	@Transient
	public String getLargeAreaName() {
		return largeAreaName;
	}

	public void setLargeAreaName(String largeAreaName) {
		this.largeAreaName = largeAreaName;
	}
	
	@Transient
	public String getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	@Transient 
	public Integer getClassClaimScore() {
		return classClaimScore;
	}

	public void setClassClaimScore(Integer classClaimScore) {
		this.classClaimScore = classClaimScore;
	}
	@Transient 
	public Integer getClassActualScore() {
		return classActualScore;
	}

	public void setClassActualScore(Integer classActualScore) {
		this.classActualScore = classActualScore;
	}
	@Transient 
	public Integer getIspass() {
		return ispass;
	}

	public void setIspass(Integer ispass) {
		this.ispass = ispass;
	}
	@Transient 
	public String getCreditCompletionRate() {
		return creditCompletionRate;
	}

	public void setCreditCompletionRate(String creditCompletionRate) {
		this.creditCompletionRate = creditCompletionRate;
	}
	/**
	 * 创建时间
	 * @return
	 */
	@Column(name = "create_date",columnDefinition = "VARCHAR")
	public String getCreateDate() {
		return createDate;
	}
	/**
	 * 创建时间
	 * @return
	 */
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	/**
	 * 创建来源 
	 * @return
	 */
	@Column(name = "creater",columnDefinition = "VARCHAR")
	public String getCreater() {
		return creater;
	}
	/**
	 * 创建来源 
	 * @return
	 */
	public void setCreater(String creater) {
		this.creater = creater;
	}

	 

	
	
}
