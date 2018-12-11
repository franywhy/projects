package com.hq.learningcenter.contract.entity;

import java.util.Date;
import java.util.List;

public class ContractRecordEntity {
	private Long id;//pk
	private Long orderId;//订单pk
	private Long userId;//学员pk
	private String realName;//学员姓名
	private String idCard;//学员身份证
	private String mobile;//手机
	private int status;//状态(0-未签订 1-已签订)
	private Long contractId;//在线协议id
	private String templateId;//模板id
	private Long signerId;//在线用户pk
	private String teacherName;//课程顾问老师
	private String ncId;//订单主键
	private int sex;//性别
	private String bdyx;//报读院校
	private int record;//学历
	private String zy;//专业
	private String qq;//QQ
	private String emergencyPro;//紧急联系人
	private String emergencyCall;//紧急联系电话
	private String provinceName;//报考省份
	private String vbillCode;//报名表号
	private String className;//班级名称
	private String regdate;//报读日期
	private int dr;//是否删除0 为未删除, 1 为删除
	private Date createTime;
	private Date ts;
	private Double regMoney;//报名表金额
	
	private int idcardLocation;
	private int mobileLocation;
	//产品线PK
 	private Long productId; 
	private List<ContractDetailEntity> contractDetail;
	
	
	
	
	public Double getRegMoney() {
		return regMoney;
	}
	public void setRegMoney(Double regMoney) {
		this.regMoney = regMoney;
	}
	public List<ContractDetailEntity> getContractDetail() {
		return contractDetail;
	}
	public void setContractDetail(List<ContractDetailEntity> contractDetail) {
		this.contractDetail = contractDetail;
	}
	public String getNcId() {
		return ncId;
	}
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
 
	public int getIdcardLocation() {
		return idcardLocation;
	}
	public void setIdcardLocation(int idcardLocation) {
		this.idcardLocation = idcardLocation;
	}
	public int getMobileLocation() {
		return mobileLocation;
	}
	public void setMobileLocation(int mobileLocation) {
		this.mobileLocation = mobileLocation;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public int getDr() {
		return dr;
	}
	public void setDr(int dr) {
		this.dr = dr;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
 
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
 
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Long getContractId() {
		return contractId;
	}
	public void setContractId(Long contractId) {
		this.contractId = contractId;
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
	public String getBdyx() {
		return bdyx;
	}
	public void setBdyx(String bdyx) {
		this.bdyx = bdyx;
	}
	public int getRecord() {
		return record;
	}
	public void setRecord(int record) {
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
	 

}
