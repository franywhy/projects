package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 观看直播详细日志
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-04 17:08:10
 */
public class LiveLogDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long liveLogDetailId;
	//直播id
	private String liveId;
	//直播房间号
	private String liveNum;
	//学员id
	private Long userId;
	//观看直播-加入时间
	private Long joinTime;
	//观看直播-离开时间
	private Long leaveTime;
	//观看直播-加入类型
	private Integer joinType;
	//平台代号 gensee:1 
	private Integer platformCode;
	//日记同步时间
	private Date createTime;
	
	private Long productId;
	
	

	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 设置：主键ID
	 */
	public void setLiveLogDetailId(Long liveLogDetailId) {
		this.liveLogDetailId = liveLogDetailId;
	}
	/**
	 * 获取：主键ID
	 */
	public Long getLiveLogDetailId() {
		return liveLogDetailId;
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
	 * 设置：直播房间号
	 */
	public void setLiveNum(String liveNum) {
		this.liveNum = liveNum;
	}
	/**
	 * 获取：直播房间号
	 */
	public String getLiveNum() {
		return liveNum;
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
	 * 设置：观看直播-加入时间
	 */
	public void setJoinTime(Long joinTime) {
		this.joinTime = joinTime;
	}
	/**
	 * 获取：观看直播-加入时间
	 */
	public Long getJoinTime() {
		return joinTime;
	}
	/**
	 * 设置：观看直播-离开时间
	 */
	public void setLeaveTime(Long leaveTime) {
		this.leaveTime = leaveTime;
	}
	/**
	 * 获取：观看直播-离开时间
	 */
	public Long getLeaveTime() {
		return leaveTime;
	}
	/**
	 * 设置：观看直播-加入类型
	 */
	public void setJoinType(Integer joinType) {
		this.joinType = joinType;
	}
	/**
	 * 获取：观看直播-加入类型
	 */
	public Integer getJoinType() {
		return joinType;
	}
	/**
	 * 设置：平台代号 gensee:1 
	 */
	public void setPlatformCode(Integer platformCode) {
		this.platformCode = platformCode;
	}
	/**
	 * 获取：平台代号 gensee:1 
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
}
