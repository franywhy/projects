package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-04-18 14:52:55
 */
public class LcOffliveLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//学员id
	private Long userId;
	//视频id
	private String videoId;
	//开始观看时间
	private Date lookStartTime;
	//结束观看时间
	private Date lookEndTime;
	//是否在线:0离线1在线
	private Integer isOfflive;
	//视频进度开始时间
	private Long videoStartTime;
	//视频进度结束时间
	private Long videoEndTime;
	//视频总时长
	private Long videoTotalTime;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：学员id
	 */
	public void setUserId(Long useId) {
		this.userId = useId;
	}
	/**
	 * 获取：学员id
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：视频id
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：视频id
	 */
	public String getVideoId() {
		return videoId;
	}
	/**
	 * 设置：开始观看时间
	 */
	public void setLookStartTime(Date lookStartTime) {
		this.lookStartTime = lookStartTime;
	}
	/**
	 * 获取：开始观看时间
	 */
	public Date getLookStartTime() {
		return lookStartTime;
	}
	/**
	 * 设置：结束观看时间
	 */
	public void setLookEndTime(Date lookEndTime) {
		this.lookEndTime = lookEndTime;
	}
	/**
	 * 获取：结束观看时间
	 */
	public Date getLookEndTime() {
		return lookEndTime;
	}
	/**
	 * 设置：是否在线:0离线1在线
	 */
	public void setIsOfflive(Integer isOfflive) {
		this.isOfflive = isOfflive;
	}
	/**
	 * 获取：是否在线:0离线1在线
	 */
	public Integer getIsOfflive() {
		return isOfflive;
	}
	/**
	 * 设置：视频进度开始时间
	 */
	public void setVideoStartTime(Long videoStartTime) {
		this.videoStartTime = videoStartTime;
	}
	/**
	 * 获取：视频进度开始时间
	 */
	public Long getVideoStartTime() {
		return videoStartTime;
	}
	/**
	 * 设置：视频进度结束时间
	 */
	public void setVideoEndTime(Long videoEndTime) {
		this.videoEndTime = videoEndTime;
	}
	/**
	 * 获取：视频进度结束时间
	 */
	public Long getVideoEndTime() {
		return videoEndTime;
	}
	/**
	 * 设置：视频总时长
	 */
	public void setVideoTotalTime(Long videoTotalTime) {
		this.videoTotalTime = videoTotalTime;
	}
	/**
	 * 获取：视频总时长
	 */
	public Long getVideoTotalTime() {
		return videoTotalTime;
	}
}
