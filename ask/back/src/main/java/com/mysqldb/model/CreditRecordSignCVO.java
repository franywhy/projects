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
 * NC报名表中的C表　从NC中同步过来的
 * @author lintf 
 * @Time 2017年9月24日13:46:18
 */
@Entity
@Table(name = "credit_record_sign_c")
@org.hibernate.annotations.Table(appliesTo = "credit_record_sign_c", comment = "credit_record_sign_c")
public class CreditRecordSignCVO {
	/**
	 * id
	 */
	private Integer id;
	 
	/**
	 * 时间戳
	 */
	private Date ts;
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
	 
	 
	private String signId; //报名表HID
	private String signIdc;//报名表CID
	private String studentId;//学员主键商机pk
	private String studentName;//学员名称
	private String subjectId;//科目ID
	private String subjectCode;
	private String subjectName;
	private String subjectType;//科目类型
	private Integer creditRecordId;//关联的档案单biD
	private Integer isEnable;//是否启用 1为停用 0为启用
	private String replaceSubject; //替换的科目ID 
	private Integer kcStatus;//报名表中的排课状态
	private Integer kcKs;//报名表中的课次
	private Integer DR;//报名表中是否删除 1 为删除 0 为正常
	private Date SYTS;//同步的时间
	private String signDate;//报名表的报名时间 
	private String classId;
	private String orgId;//校区pk
	private Integer isCheck=0; //是否考核 0为考核 1为不考核

	private String signCode;//报名表号

	private String phone;//电话

	private String idCard;  
	
/***************************** 报名表C表的构造************************************/
	 public CreditRecordSignCVO(){}
	 
	 public CreditRecordSignCVO(RegistrationSYNCVO sync){
		 
			this.signId=sync.getSIGN_ID();
			this.signIdc=(sync.getNC_REG_C());
			this.signCode=sync.getSIGN_CODE();
			this.signDate=sync.getSIGN_DATE().substring(0, 10);			
			this.orgId=sync.getORG_ID();			
			this.classId=sync.getCLASS_ID();			
			this.studentId=sync.getSTUDENT_ID();
			this.studentName=sync.getSTUDENT_NAME();
			this.phone=sync.getPHONE();
			this.idCard=sync.getID_CARD();
			this.SYTS=sync.getSYTS();
			this.DR=sync.getRH_DR();		
			this.isEnable=sync.getENABLE();			 
		 	this.subjectId=sync.getNC_SUBJECT_ID() ;
			this.subjectCode=sync.getSUBJECT_CODE() ;
	    	this.subjectName=sync.getSUBJECT_NAME() ;
			this.replaceSubject=sync.getREPLACESUBJECT();
		    this.kcKs=sync.getKC_KS();		
			this.kcStatus=sync.getKC_STATUS() ; 
			this.orgId= sync.getORG_ID() ;
					
		 
	 }
	
	
	
	
	
	@Column(name = "sign_id",columnDefinition = "VARCHAR")
	public String getSignId() {
		return signId;
	}
	@Column(name = "sign_id_c",columnDefinition = "VARCHAR")
	public String getSignIdc() {
		return signIdc;
	}
	@Column(name = "sign_date",columnDefinition = "VARCHAR")
	public String getSignDate() {
		return signDate;
	}
	@Column(name = "student_id",columnDefinition = "VARCHAR")
	public String getStudentId() {
		return studentId;
	}
	@Column(name = "student_name",columnDefinition = "VARCHAR")
	public String getStudentName() {
		return studentName;
	}
	@Column(name = "subject_id",columnDefinition = "VARCHAR")
	public String getSubjectId() {
		return subjectId;
	}
	@Column(name = "subject_code",columnDefinition = "VARCHAR")
	public String getSubjectCode() {
		return subjectCode;
	}
	@Column(name = "subject_name",columnDefinition = "VARCHAR")
	public String getSubjectName() {
		return subjectName;
	}
	@Column(name = "subject_type",columnDefinition = "VARCHAR")
	public String getSubjectType() {
		return subjectType;
	}
	@Column(name = "credit_id",columnDefinition = "INT")
	public Integer getCreditRecordId() {
		return creditRecordId;
	}
	@Column(name = "is_enable",columnDefinition = "INT")
	public Integer getIsEnable() {
		return isEnable;
	}
	@Column(name = "replace_subject",columnDefinition = "VARCHAR")
	public String getReplaceSubject() {
		return replaceSubject;
	}
	@Column(name = "kc_status",columnDefinition = "INT")
	public Integer getkcStatus() {
		return kcStatus;
	}
	@Column(name = "kc_ks",columnDefinition = "INT")
	public Integer getkcKs() {
		return kcKs;
	}
	@Column(name = "DR",columnDefinition = "INT")
	public Integer getDR() {
		return DR;
	}
	@Column(name = "SYTS",columnDefinition = "DATETIME")
	public Date getSYTS() {
		return SYTS;
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

	
	
	
	
	@Column(name = "org_id",columnDefinition = "VARCHAR")
	public String getOrgId() {
		return orgId;
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
	
	
	/**
	 * 设置 校区
	 */
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	
	
	
	public void setSignId(String signId) {
		this.signId = signId;
	}

	public void setSignIdc(String signIdc) {
		this.signIdc = signIdc;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}
	public void setSubjectCode(String subjectCode) {
		this.subjectCode = subjectCode;
	}
	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public void setSubjectType(String subjectType) {
		this.subjectType = subjectType;
	}

	public void setCreditRecordId(Integer creditRecordId) {
		this.creditRecordId = creditRecordId;
	}

	public void setIsEnable(Integer isEnable) {
		this.isEnable = isEnable;
	}

	public void setReplaceSubject(String replaceSubject) {
		this.replaceSubject = replaceSubject;
	}

	public void setkcStatus(Integer kcStatus) {
		this.kcStatus = kcStatus;
	}

	public void setkcKs(Integer kcKs) {
		this.kcKs = kcKs;
	}

	public void setDR(Integer DR) {
		this.DR = DR;
	}

	public void setSYTS(Date SYTS) {
		this.SYTS = SYTS;
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

	 
	
	
 

	
	
	
	

	
	 
	
	
}
