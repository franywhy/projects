package com.hq.learningcenter.contract.pojo;

public class ContractRecordPOJO {
	private Long id;//pk
	private String realName;//学员姓名
	private String idCard;//学员身份证
	private String mobile;//手机
	private String templateId;//模板id
	private Long signerId;//在线用户pk
	private String teacherName;//课程顾问老师
	private int sex;//性别
	private String sexName;//性别
	private String bdyx;//报读院校
	private String record;//学历
	private String zy;//专业
	private String qq;//QQ
	private String emergencyPro;//紧急联系人
	private String emergencyCall;//紧急联系电话
	private String provinceName;//报考省份
	private String coursename;//报读班型
	private String vbillCode;//报名表号
	private String className;//班级名称
	private String regdate;//报读日期
	private Double regMoney;//报名表金额
	private String payName;//支付方式

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Long getSignerId() {
		return signerId;
	}

	public void setSignerId(Long signerId) {
		this.signerId = signerId;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getSexName() {
		sexName = "保密";
		if (sex == 0) {
			sexName = "女";
		}else if(sex == 1){
			sexName = "男";
		}
		return sexName;
	}

	public void setSexName(String sexName) {
		this.sexName = sexName;
	}

	public String getBdyx() {
		return bdyx;
	}

	public void setBdyx(String bdyx) {
		this.bdyx = bdyx;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public String getZy() {
		return zy;
	}

	public void setZy(String zy) {
		this.zy = zy;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getEmergencyPro() {
		return emergencyPro;
	}

	public void setEmergencyPro(String emergencyPro) {
		this.emergencyPro = emergencyPro;
	}

	public String getEmergencyCall() {
		return emergencyCall;
	}

	public void setEmergencyCall(String emergencyCall) {
		this.emergencyCall = emergencyCall;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCoursename() {
		return coursename;
	}

	public void setCoursename(String coursename) {
		this.coursename = coursename;
	}

	public String getVbillCode() {
		return vbillCode;
	}

	public void setVbillCode(String vbillCode) {
		this.vbillCode = vbillCode;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRegdate() {
		return regdate;
	}

	public void setRegdate(String regdate) {
		this.regdate = regdate;
	}

	public Double getRegMoney() {
		return regMoney;
	}

	public void setRegMoney(Double regMoney) {
		this.regMoney = regMoney;
	}

	public String getPayName() {
		return payName;
	}

	public void setPayName(String payName) {
		this.payName = payName;
	}
}
