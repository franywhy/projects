package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看录播日志-有效记录
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-04 17:08:20
 */
public class VideoLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long videoLogId;
	//录播id
	private String videoId;
	//学员id
	private Long userId;
	//观看录播-持续时长
	private Long watchDuration;
	//录播件时长
	private Long videoDuration;
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

	
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	/**
	 * 设置：主键ID
	 */
	public void setVideoLogId(Long videoLogId) {
		this.videoLogId = videoLogId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getVideoLogId() {
		return videoLogId;
	}
	/**
	 * 设置：录播id
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：录播id
	 */
	public String getVideoId() {
		return videoId;
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
	 * 设置：观看录播-持续时长
	 */
	public void setWatchDuration(Long watchDuration) {
		this.watchDuration = watchDuration;
	}
	/**
	 * 获取：观看录播-持续时长
	 */
	public Long getWatchDuration() {
		return watchDuration;
	}
	/**
	 * 设置：录播件时长
	 */
	public void setVideoDuration(Long videoDuration) {
		this.videoDuration = videoDuration;
	}
	/**
	 * 获取：录播件时长
	 */
	public Long getVideoDuration() {
		return videoDuration;
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
}
