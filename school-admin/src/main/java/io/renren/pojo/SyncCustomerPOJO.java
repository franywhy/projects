package io.renren.pojo;

import java.util.Date;

public class SyncCustomerPOJO {
	private Long orderId;
	
	private String commodityName;
	
	private Date payTime;
	
	private String nickName;
	
	private String mobile;
	
	private String teacherName;
	
	private Integer ownerId;
	
	private String className;
	
	private Integer classId;
	
	private String areaName;
	
	private String professionName;
	
	private String levelName;
	
	private String deptName;

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public Integer getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Integer ownerId) {
		this.ownerId = ownerId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Integer getClassId() {
		return classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	@Override
	public String toString() {
		return "SyncCustomerPOJO [orderId=" + orderId + ", commodityName=" + commodityName + ", nickName=" + nickName
				+ ", mobile=" + mobile + ", teacherName=" + teacherName + ", ownerId=" + ownerId + ", className="
				+ className + ", classId=" + classId + ", areaName=" + areaName + ", professionName=" + professionName
				+ ", levelName=" + levelName + ", deptName=" + deptName + "]";
	}
	
	
}
