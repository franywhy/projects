package com.izhubo.credit.vo;


/**
 * 报名信息
 * @author 严志城
 *
 */
public class RegistrationInfoVO {
	private String studentId;
	private String studentName;
	private String classId;
	private String classCode;
	private String className;
	private String idCard;
	private String subjectilesid;
	private String signId;
	private String phone;
	private String orgId;
	private String orgName;
	private String orgCode;
	private String vbillcode;
	private String signDate;
	/**
	 * 被替换的科目主键
	 */
	private String def10;
	
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
	}
	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getSubjectilesid() {
		return subjectilesid;
	}
	public void setSubjectilesid(String subjectilesid) {
		this.subjectilesid = subjectilesid;
	}
	public String getVbillcode() {
		return vbillcode;
	}
	public void setVbillcode(String vbillcode) {
		this.vbillcode = vbillcode;
	}
	public String getSignId() {
		return signId;
	}
	public void setSignId(String signId) {
		this.signId = signId;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	public String getOrgCode() {
		return orgCode;
	}
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getDef10() {
		return def10;
	}
	public void setDef10(String def10) {
		this.def10 = def10;
	}
	
}
