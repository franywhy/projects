package io.renren.pojo.log;

import java.util.Date;

/**
 * 考勤报表-按学员统计-学员详情
 * @class io.renren.pojo.log.LogStudentAttenDetailInfo.java
 * @Description:
 * @author shihongjie
 * @dete 2017年9月13日
 */
public class LogStudentAttenDetailInfo {
	/**
	 * 用户昵称
	 */
	private String userName;
	/**
	 * 直播课ID
	 */
	private String classplanLiveId;
	/**
	 * 直播课名称
	 */
	private String classplanLiveName;
	/**
	 * 观看时间
	 */
	private Date firstWatchTime;
	/**
	 * 时间范围-直播开始时间
	 */
	private Date startTime;
	/**
	 * 时间范围-直播结束时间
	 */
	private Date endTime;
	/**
	 * 观看毫秒数
	 */
	private Long watchDur;
	/**
	 * 录播视频ID
	 */
	private String backId;
	/**
	 * 直播视频ID
	 */
	private String liveId;

	/**
	 * 出勤百分比
	 */
	private Double attendPer;
	

	public String getBackId() {
		return backId;
	}

	public void setBackId(String backId) {
		this.backId = backId;
	}

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public String getClassplanLiveId() {
		return classplanLiveId;
	}

	public void setClassplanLiveId(String classplanLiveId) {
		this.classplanLiveId = classplanLiveId;
	}

	public String getClassplanLiveName() {
		return classplanLiveName;
	}

	public void setClassplanLiveName(String classplanLiveName) {
		this.classplanLiveName = classplanLiveName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getWatchDur() {
		return watchDur;
	}

	public void setWatchDur(Long watchDur) {
		this.watchDur = watchDur;
	}

	public Double getAttendPer() {
		return attendPer;
	}

	public void setAttendPer(Double attendPer) {
		this.attendPer = attendPer;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getFirstWatchTime() {
		return firstWatchTime;
	}

	public void setFirstWatchTime(Date firstWatchTime) {
		this.firstWatchTime = firstWatchTime;
	}

	@Override
	public String toString() {
		return "LogStudentAttenDetailInfo [userName=" + userName + ", classplanLiveId=" + classplanLiveId + ", classplanLiveName=" + classplanLiveName
				+ ", firstWatchTime=" + firstWatchTime + ", startTime=" + startTime + ", endTime=" + endTime + ", watchDur=" + watchDur + ", backId="
				+ backId + ", liveId=" + liveId + ", attendPer=" + attendPer + "]";
	}

}
