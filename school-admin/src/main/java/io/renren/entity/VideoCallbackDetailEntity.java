package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看回放回调信息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-28 17:04:20
 */
public class VideoCallbackDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long videoCallbackDetailId;
	//录播id
	private String videoId;
	//学员id
	private Long userId;
	//观看录播-加入时间
	private Long startTime;
	//观看录播-离开时间
	private Long leaveTime;
	//日记同步时间
	private Date createTime;
	
	public Long getVideoCallbackDetailId() {
		return videoCallbackDetailId;
	}
	public void setVideoCallbackDetailId(Long videoCallbackDetailId) {
		this.videoCallbackDetailId = videoCallbackDetailId;
	}
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public Long getLeaveTime() {
		return leaveTime;
	}
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
