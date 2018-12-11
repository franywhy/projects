package io.renren.modules.job.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-16 11:02:21
 */
public class LogPolyvWatchEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long logWatchId;
	//录播课次id
	private String recordId;
	//课程id
	private Long courseId;
	//学员id
	private String userId;
	//录播视频id
	private String vid;
	//用户观看总时长/毫秒
	private Long playDuration;
	//录播视频总时长/毫秒
	private Long fullDuration;
	//录播出勤率
	private BigDecimal attentPer;
	//创建时间
	private Date createTime;
	//变动时间
	private Date ts;

	/**
	 * 设置：
	 */
	public void setLogWatchId(Long logWatchId) {
		this.logWatchId = logWatchId;
	}
	/**
	 * 获取：
	 */
	public Long getLogWatchId() {
		return logWatchId;
	}
	/**
	 * 设置：录播课次id
	 */
	public void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	/**
	 * 获取：录播课次id
	 */
	public String getRecordId() {
		return recordId;
	}
	/**
	 * 设置：课程id
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程id
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：学员id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员id
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * 设置：录播视频id
	 */
	public void setVid(String vid) {
		this.vid = vid;
	}
	/**
	 * 获取：录播视频id
	 */
	public String getVid() {
		return vid;
	}
	/**
	 * 设置：用户观看总时长/s
	 */
	public void setPlayDuration(Long playDuration) {
		this.playDuration = playDuration;
	}
	/**
	 * 获取：用户观看总时长/s
	 */
	public Long getPlayDuration() {
		return playDuration;
	}
	/**
	 * 设置：录播视频总时长/s
	 */
	public void setFullDuration(Long fullDuration) {
		this.fullDuration = fullDuration;
	}
	/**
	 * 获取：录播视频总时长/s
	 */
	public Long getFullDuration() {
		return fullDuration;
	}
	/**
	 * 设置：录播出勤率
	 */
	public void setAttentPer(BigDecimal attentPer) {
		this.attentPer = attentPer;
	}
	/**
	 * 获取：录播出勤率
	 */
	public BigDecimal getAttentPer() {
		return attentPer;
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
	 * 设置：变动时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：变动时间
	 */
	public Date getTs() {
		return ts;
	}

    @Override
    public String toString() {
        return "LogPolyvWatchEntity{" +
                "logWatchId=" + logWatchId +
                ", recordId=" + recordId +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", vid='" + vid + '\'' +
                ", playDuration=" + playDuration +
                ", fullDuration=" + fullDuration +
                ", attentPer=" + attentPer +
                ", createTime=" + createTime +
                ", ts=" + ts +
                '}';
    }
}
