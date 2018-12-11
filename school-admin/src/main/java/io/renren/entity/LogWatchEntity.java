package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看直播录播日志-考勤记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-18 14:43:29
 */
public class LogWatchEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long logWatchId;
	//直播录播id
	private String lvId;
	//学员id
	private Long userId;
	//业务id
	private String businessId;
	//观看时长
	private Long watchDuration;
	//直播开始时间
	private Date liveStartTime;
	//直播结束时间
	private Date liveEndTime;
	//直播录播时长
	private Long lvDuration;
	//出席超过30分钟 0:未出勤 1:出勤
	private Integer attend30;
	//版本号
	private Integer versionCode;
	//平台
	private String schoolId;
	//mongoDB平台标记
	private String mId;
	//日记同步时间
	private Date createTime;
	//日志类型 0:直播  1:录播
	private Integer logType;

	/**
	 * 设置：主键ID
	 */
	public void setLogWatchId(Long logWatchId) {
		this.logWatchId = logWatchId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getLogWatchId() {
		return logWatchId;
	}
	/**
	 * 设置：直播录播id
	 */
	public void setLvId(String lvId) {
		this.lvId = lvId;
	}
	/**
	 * 获取：直播录播id
	 */
	public String getLvId() {
		return lvId;
	}
	/**
	 * 设置：学员id
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：业务id
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * 获取：业务id
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * 设置：观看时长
	 */
	public void setWatchDuration(Long watchDuration) {
		this.watchDuration = watchDuration;
	}
	/**
	 * 获取：观看时长
	 */
	public Long getWatchDuration() {
		return watchDuration;
	}
	/**
	 * 设置：直播开始时间
	 */
	public void setLiveStartTime(Date liveStartTime) {
		this.liveStartTime = liveStartTime;
	}
	/**
	 * 获取：直播开始时间
	 */
	public Date getLiveStartTime() {
		return liveStartTime;
	}
	/**
	 * 设置：直播结束时间
	 */
	public void setLiveEndTime(Date liveEndTime) {
		this.liveEndTime = liveEndTime;
	}
	/**
	 * 获取：直播结束时间
	 */
	public Date getLiveEndTime() {
		return liveEndTime;
	}
	/**
	 * 设置：直播录播时长
	 */
	public void setLvDuration(Long lvDuration) {
		this.lvDuration = lvDuration;
	}
	/**
	 * 获取：直播录播时长
	 */
	public Long getLvDuration() {
		return lvDuration;
	}
	/**
	 * 设置：出席超过30分钟 0:未出勤 1:出勤
	 */
	public void setAttend30(Integer attend30) {
		this.attend30 = attend30;
	}
	/**
	 * 获取：出席超过30分钟 0:未出勤 1:出勤
	 */
	public Integer getAttend30() {
		return attend30;
	}
	/**
	 * 设置：版本号
	 */
	public void setVersionCode(Integer versionCode) {
		this.versionCode = versionCode;
	}
	/**
	 * 获取：版本号
	 */
	public Integer getVersionCode() {
		return versionCode;
	}
	/**
	 * 设置：平台
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：mongoDB平台标记
	 */
	public void setMId(String mId) {
		this.mId = mId;
	}
	/**
	 * 获取：mongoDB平台标记
	 */
	public String getMId() {
		return mId;
	}
	/**
	 * 设置：日记同步时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：日记同步时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：日志类型 0:直播  1:录播
	 */
	public void setLogType(Integer logType) {
		this.logType = logType;
	}
	/**
	 * 获取：日志类型 0:直播  1:录播
	 */
	public Integer getLogType() {
		return logType;
	}
}
