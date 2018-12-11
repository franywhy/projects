package io.renren.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 直播录播观看记录-时间求和
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-21 10:33:05
 */
public class LogGenseeWatchEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long logWatchId;
	//录播ID
	private String videoId;
	//直播id
	private String liveId;
	//学员id
	private Long userId;
	//业务id
	private String businessId;
	//应出勤时长(毫秒)
	private Long fullDur;
	//直播和录播总时长(毫秒)
	private Long watchDur;
	//录播总时长(毫秒)
	private Long videoDur;
	//直播总时长(毫秒)
	private Long liveDur;
	//出勤率
	private BigDecimal attendPer;
	//mongoDB平台标记
	private String mId;
	//创建时间
	private Date createTime;
	//ts
	private Date ts;
	//产品线ID
	private Long productId;

	
	
	public String getmId() {
		return mId;
	}
	public void setmId(String mId) {
		this.mId = mId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
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
	 * 设置：录播ID
	 */
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	/**
	 * 获取：录播ID
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
	 * 设置：应出勤时长(毫秒)
	 */
	public void setFullDur(Long fullDur) {
		this.fullDur = fullDur;
	}
	/**
	 * 获取：应出勤时长(毫秒)
	 */
	public Long getFullDur() {
		return fullDur;
	}
	/**
	 * 设置：直播和录播总时长(毫秒)
	 */
	public void setWatchDur(Long watchDur) {
		this.watchDur = watchDur;
	}
	/**
	 * 获取：直播和录播总时长(毫秒)
	 */
	public Long getWatchDur() {
		return watchDur;
	}
	/**
	 * 设置：录播总时长(毫秒)
	 */
	public void setVideoDur(Long videoDur) {
		this.videoDur = videoDur;
	}
	/**
	 * 获取：录播总时长(毫秒)
	 */
	public Long getVideoDur() {
		return videoDur;
	}
	/**
	 * 设置：直播总时长(毫秒)
	 */
	public void setLiveDur(Long liveDur) {
		this.liveDur = liveDur;
	}
	/**
	 * 获取：直播总时长(毫秒)
	 */
	public Long getLiveDur() {
		return liveDur;
	}
	/**
	 * 设置：出勤率
	 */
	public void setAttendPer(BigDecimal attendPer) {
		this.attendPer = attendPer;
	}
	/**
	 * 获取：出勤率
	 */
	public BigDecimal getAttendPer() {
		return attendPer;
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
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：ts
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：ts
	 */
	public Date getTs() {
		return ts;
	}
}
