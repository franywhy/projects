package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看直播回到信息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-12-28 17:04:20
 */
public class LiveCallbackDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long liveCallbackDetailId;
	//直播id
	private String liveId;
	//学员id
	private Long userId;
	//观看直播-加入时间
	private Long joinTime;
	//观看直播-离开时间
	private Long leaveTime;
	//日记同步时间
	private Date createTime;
	
	public Long getLiveCallbackDetailId() {
		return liveCallbackDetailId;
	}
	public void setLiveCallbackDetailId(Long liveCallbackDetailId) {
		this.liveCallbackDetailId = liveCallbackDetailId;
	}
	public String getLiveId() {
		return liveId;
	}
	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getJoinTime() {
		return joinTime;
	}
	public void setJoinTime(Long joinTime) {
		this.joinTime = joinTime;
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
