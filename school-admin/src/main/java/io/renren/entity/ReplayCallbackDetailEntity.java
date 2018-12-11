package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 视频直播回放观看明细表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-04-19 17:24:44
 */
public class ReplayCallbackDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long replayCallbackDetailId;
	//回放视频id
	private String videoId;
	//学员id
	private Long userId;
	//观看直播回放-加入时间
	private Long joinTime;
	//观看直播回放-离开时间
	private Long leaveTime;
	//日记同步时间
	private Date createTime;
	//视频进度开始时间
	private Long videoStartTime;
	//视频进度结束时间
	private Long videoEndTime;
	//视频总时长
	private Long videoTotalTime;
	//是否离线(缓存):0离线1回放回调
	private Integer isOfflive;
    //产品线
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
	public void setReplayCallbackDetailId(Long replayCallbackDetailId) {
		this.replayCallbackDetailId = replayCallbackDetailId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getReplayCallbackDetailId() {
		return replayCallbackDetailId;
	}
	/**
	 * 设置：回放视频id
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：回放视频id
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
	 * 设置：观看直播回放-加入时间
	 */
	public void setJoinTime(Long joinTime) {
		this.joinTime = joinTime;
	}
	/**
	 * 获取：观看直播回放-加入时间
	 */
	public Long getJoinTime() {
		return joinTime;
	}
	/**
	 * 设置：观看直播回放-离开时间
	 */
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
	/**
	 * 获取：观看直播回放-离开时间
	 */
	public Long getLeaveTime() {
		return leaveTime;
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
	/**
	 * 设置：是否离线(缓存):0离线1回放回调
	 */
	public void setIsOfflive(Integer isOfflive) {
		this.isOfflive = isOfflive;
	}
	/**
	 * 获取：是否离线(缓存):0离线1回放回调
	 */
	public Integer getIsOfflive() {
		return isOfflive;
	}

	public Float getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Float coefficient) {
		this.coefficient = coefficient;
	}
	
}
