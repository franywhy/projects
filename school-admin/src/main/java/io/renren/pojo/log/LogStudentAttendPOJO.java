package io.renren.pojo.log;

import java.io.Serializable;
import java.math.BigDecimal;

public class LogStudentAttendPOJO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String userplanId;
	
	private String classTypeId;
	
	private String userId;
	
	private String userName;
	
	private String userStatus;
	
	private String startClassTime;
	
	private String mobile;
	
	private String areaName;
	
	private String className;
	
	private String teacherName;
	
	private BigDecimal livePer;
	
	private BigDecimal attendPer;
	
	private String livePerTxt;
	
	private String attendPerTxt;
	//实际出勤时长(min)
	private BigDecimal minWatchDur;
	//应出勤时长(min)
	private BigDecimal minFullDur;
	
	private String minWatchDurTxt;
	
	private String minFullDurTxt;
	
	private String classplanLiveName;

	public String getClassplanLiveName() {
		return classplanLiveName;
	}

	public void setClassplanLiveName(String classplanLiveName) {
		this.classplanLiveName = classplanLiveName;
	}

	public String getMinWatchDurTxt() {
		return minWatchDurTxt;
	}

	public void setMinWatchDurTxt(String minWatchDurTxt) {
		this.minWatchDurTxt = minWatchDurTxt;
	}

	public String getMinFullDurTxt() {
		return minFullDurTxt;
	}

	public void setMinFullDurTxt(String minFullDurTxt) {
		this.minFullDurTxt = minFullDurTxt;
	}

	public BigDecimal getMinWatchDur() {
		return minWatchDur;
	}

	public void setMinWatchDur(BigDecimal minWatchDur) {
		this.minWatchDur = minWatchDur;
	}

	public BigDecimal getMinFullDur() {
		return minFullDur;
	}

	public void setMinFullDur(BigDecimal minFullDur) {
		this.minFullDur = minFullDur;
	}

	public String getUserplanId() {
		return userplanId;
	}

	public void setUserplanId(String userplanId) {
		this.userplanId = userplanId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	
	public String getStartClassTime() {
		return startClassTime;
	}

	public void setStartClassTime(String startClassTime) {
		this.startClassTime = startClassTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public BigDecimal getLivePer() {
		return livePer;
	}

	public void setLivePer(BigDecimal livePer) {
		this.livePer = livePer;
	}

	public BigDecimal getAttendPer() {
		return attendPer;
	}

	public void setAttendPer(BigDecimal attendPer) {
		this.attendPer = attendPer;
	}

	public String getClassTypeId() {
		return classTypeId;
	}

	public void setClassTypeId(String classTypeId) {
		this.classTypeId = classTypeId;
	}

	public String getLivePerTxt() {
		return livePerTxt;
	}

	public void setLivePerTxt(String livePerTxt) {
		this.livePerTxt = livePerTxt;
	}

	public String getAttendPerTxt() {
		return attendPerTxt;
	}

	public void setAttendPerTxt(String attendPerTxt) {
		this.attendPerTxt = attendPerTxt;
	}

	@Override
	public String toString() {
		return "LogStudentAttendPOJO [userplanId=" + userplanId + ", classTypeId=" + classTypeId + ", userId=" + userId
				+ ", userName=" + userName + ", userStatus=" + userStatus + ", startClassTime=" + startClassTime
				+ ", mobile=" + mobile + ", areaName=" + areaName + ", className=" + className + ", teacherName="
				+ teacherName + ", livePer=" + livePer + ", attendPer=" + attendPer + ", livePerTxt=" + livePerTxt
				+ ", attendPerTxt=" + attendPerTxt + ", minWatchDur=" + minWatchDur + ", minFullDur=" + minFullDur
				+ ", minWatchDurTxt=" + minWatchDurTxt + ", minFullDurTxt=" + minFullDurTxt + ", classplanLiveName="
				+ classplanLiveName + "]";
	}
	
}
