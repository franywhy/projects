package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看录播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-04 17:08:20
 */
public class VideoLogDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long videoLogDetailId;
	//录播id
	private String videoId;
	//学员id
	private Long userId;
	//观看录播-加入时间
	private Long startTime;
	//观看录播-离开时间
	private Long leaveTime;
	//观看录播-持续时长
	private Long duration;
	//观看录播-设备类型
	private Integer device;
	//平台代号
	private Integer platformCode;
	//日记同步时间
	private Date createTime;
	// 产品线ID
	private Long productId;
	//系数
	private Float coefficient;
	

	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 设置：主键ID
	 */
	public void setVideoLogDetailId(Long videoLogDetailId) {
		this.videoLogDetailId = videoLogDetailId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getVideoLogDetailId() {
		return videoLogDetailId;
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
	 * 设置：观看录播-加入时间
	 */
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：观看录播-加入时间
	 */
	public Long getStartTime() {
		return startTime;
	}
	/**
	 * 设置：观看录播-离开时间
	 */
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
	/**
	 * 获取：观看录播-离开时间
	 */
	public Long getLeaveTime() {
		return leaveTime;
	}
	/**
	 * 设置：观看录播-持续时长
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * 获取：观看录播-持续时长
	 */
	public Long getDuration() {
		return duration;
	}
	/**
	 * 设置：观看录播-设备类型
	 */
	public void setDevice(Integer device) {
		this.device = device;
	}
	/**
	 * 获取：观看录播-设备类型
	 */
	public Integer getDevice() {
		return device;
	}
	/**
	 * 设置：平台代号
	 */
	public void setPlatformCode(Integer platformCode) {
		this.platformCode = platformCode;
	}
	/**
	 * 获取：平台代号
	 */
	public Integer getPlatformCode() {
		return platformCode;
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
	public Float getCoefficient() {
		return coefficient;
	}
	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
	
}
