package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 录播信息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-04 17:08:20
 */
public class VideoInfoEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long videoInfoId;
	//录播id
	private String videoId;
	//直播id
	private String liveId;
	//业务id
	private String businessId;
	//业务类型 0:排课 1:公开课
	private Integer businessType;
	//录制开始时间
	private Long startTime;
	//录制结束时间
	private Long endTime;
	//录播件时长
	private Long videoDuration;
	//录播地址
	private String url;
	//平台
	private String schoolId;
	//日记同步时间
	private Date createTime;

	/**
	 * 设置：主键ID
	 */
	public void setVideoInfoId(Long videoInfoId) {
		this.videoInfoId = videoInfoId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getVideoInfoId() {
		return videoInfoId;
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
	 * 设置：直播id
	 */
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	/**
	 * 获取：直播id
	 */
	public String getLiveId() {
		return liveId;
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
	 * 设置：业务类型 0:排课 1:公开课
	 */
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
	/**
	 * 获取：业务类型 0:排课 1:公开课
	 */
	public Integer getBusinessType() {
		return businessType;
	}
	/**
	 * 设置：录制开始时间
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：录制开始时间
	 */
	public Long getStartTime() {
		return startTime;
	}
	/**
	 * 设置：录制结束时间
	 */
	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：录制结束时间
	 */
	public Long getEndTime() {
		return endTime;
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
	 * 设置：录播地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：录播地址
	 */
	public String getUrl() {
		return url;
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
