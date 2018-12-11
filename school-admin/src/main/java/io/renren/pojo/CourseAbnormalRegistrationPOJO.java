package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 报考档案表
 * 
 * @author vince
 * @date 2018-03-28 09:21:52
 */
public class CourseAbnormalRegistrationPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//单据号
	private String registrationNo;
	//订单编号
	private String orderNo;
	//班型名称
	private String classtypeName;
	//课程科目
	private String courseName;
	//报考省份
	private String bdAreaName;
	//报读省份
	private String bkAreaName;
	//专业
	private String professionName;
	//层次
	private String levelName;
	//班级名称
	private String className;
	//班主任
	private String sysUserName;
	//昵称
	private String nickName;
	//手机号
	private String mobile;
	//报考时间段名称
	private String scheduleName;
	//报考时间
	private String scheduleDate;
	//产品线
	private String productName;
	//产品线Id
	private Long productId;
	//报考登记号
	private String registrationNumber;
	//提交时间
	private String registrationTime;
	//状态
	private String status;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getClasstypeName() {
		return classtypeName;
	}
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public String getBdAreaName() {
		return bdAreaName;
	}
	public void setBdAreaName(String bdAreaName) {
		this.bdAreaName = bdAreaName;
	}
	public String getBkAreaName() {
		return bkAreaName;
	}
	public void setBkAreaName(String bkAreaName) {
		this.bkAreaName = bkAreaName;
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
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getSysUserName() {
		return sysUserName;
	}
	public void setSysUserName(String sysUserName) {
		this.sysUserName = sysUserName;
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
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	public String getRegistrationTime() {
		return registrationTime;
	}
	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	@Override
	public String toString() {
		return "CourseAbnormalRegistrationPOJO [id=" + id + ", registrationNo=" + registrationNo + ", orderNo="
				+ orderNo + ", classtypeName=" + classtypeName + ", courseName=" + courseName + ", bdAreaName="
				+ bdAreaName + ", bkAreaName=" + bkAreaName + ", professionName=" + professionName + ", levelName="
				+ levelName + ", className=" + className + ", sysUserName=" + sysUserName + ", nickName=" + nickName
				+ ", mobile=" + mobile + ", scheduleName=" + scheduleName + ", scheduleDate=" + scheduleDate
				+ ", productName=" + productName + ", productId=" + productId + ", registrationNumber="
				+ registrationNumber + ", registrationTime=" + registrationTime + ", status=" + status + "]";
	}
	
	
}
