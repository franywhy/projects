package io.renren.pojo.log;

import java.util.Date;

public class LogAttendaceClassTeacherVPOJO {

	private Long userPlanId;//学员规划ID
	private Long userId;//学员ID
	private Integer isRep;//
	private Integer status;//状态
	private Integer userplanStatus;//
	private Long orderId;//订单ID
	private Long classId;//班级ID
	private String orderNo;//订单号
	private Long classTypeId;//班型ID
	private Long commodityId;//商品ID

	private String userMobile;//手机
	private String userName;//昵称
	private String commodityName;//商品名称
	private String areaName;//省份
	private String classTypeName;//班型
	private String className;//班级
	private String levelName;//层次
	private String teacherName;//授课老师
	private String professionName;//专业
	private Date startTimeClassplanLive;//第一次直播课时间
	private String deptName;//校区名称
	
	/** 考勤次数 */
	private int liveNum;
	/** 考勤平均值 */
	private double logStudentAttenAvg ;

	
	
	public Date getStartTimeClassplanLive() {
		return startTimeClassplanLive;
	}

	public void setStartTimeClassplanLive(Date startTimeClassplanLive) {
		this.startTimeClassplanLive = startTimeClassplanLive;
	}

	public int getLiveNum() {
		return liveNum;
	}

	public void setLiveNum(int liveNum) {
		this.liveNum = liveNum;
	}

	public double getLogStudentAttenAvg() {
		return logStudentAttenAvg;
	}

	public void setLogStudentAttenAvg(double logStudentAttenAvg) {
		this.logStudentAttenAvg = logStudentAttenAvg;
	}

	public Long getUserPlanId() {
		return userPlanId;
	}

	public void setUserPlanId(Long userPlanId) {
		this.userPlanId = userPlanId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getIsRep() {
		return isRep;
	}

	public void setIsRep(Integer isRep) {
		this.isRep = isRep;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getUserplanStatus() {
		return userplanStatus;
	}

	public void setUserplanStatus(Integer userplanStatus) {
		this.userplanStatus = userplanStatus;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getClassId() {
		return classId;
	}

	public void setClassId(Long classId) {
		this.classId = classId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}

	public Long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}

	public String getUserMobile() {
		return userMobile;
	}

	public void setUserMobile(String userMobile) {
		this.userMobile = userMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getLevelName() {
		return levelName;
	}

	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getProfessionName() {
		return professionName;
	}

	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}

	@Override
	public String toString() {
		return "LogAttendaceClassTeacherVPOJO [userPlanId=" + userPlanId + ", userId=" + userId + ", isRep=" + isRep
				+ ", status=" + status + ", userplanStatus=" + userplanStatus + ", orderId=" + orderId + ", classId="
				+ classId + ", orderNo=" + orderNo + ", classTypeId=" + classTypeId + ", commodityId=" + commodityId
				+ ", userMobile=" + userMobile + ", userName=" + userName + ", commodityName=" + commodityName
				+ ", areaName=" + areaName + ", classTypeName=" + classTypeName + ", className=" + className
				+ ", levelName=" + levelName + ", teacherName=" + teacherName + ", professionName=" + professionName
				+ ", startTimeClassplanLive=" + startTimeClassplanLive + ", deptName=" + deptName + ", liveNum="
				+ liveNum + ", logStudentAttenAvg=" + logStudentAttenAvg + "]";
	}
	

}
