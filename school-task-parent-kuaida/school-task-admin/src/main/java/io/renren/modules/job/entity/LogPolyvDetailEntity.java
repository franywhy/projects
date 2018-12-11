package io.renren.modules.job.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-16 14:16:36
 */
public class LogPolyvDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long logId;
	//录播课次id
	private String recordId;
	//课程id
	private Long courseId;
	//学员id
	private String userId;
	//录播视频id
	private String vid;
	//观看录播视频的客户端
	private String client;
	//用户观看视频时长/毫秒
	private Long playDuration;
	//录播视频总时长/毫秒
	private Long duration;
	//创建时间
	private Date createTime;
	//保利威视平台产生日志时间
	private Date polyvCreateTime;

	/**
	 * 设置：
	 */
	public void setLogId(Long logId) {
		this.logId = logId;
	}
	/**
	 * 获取：
	 */
	public Long getLogId() {
		return logId;
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
	 * 设置：观看录播视频的客户端
	 */
	public void setClient(String client) {
		this.client = client;
	}
	/**
	 * 获取：观看录播视频的客户端
	 */
	public String getClient() {
		return client;
	}
	/**
	 * 设置：用户观看视频时长/s
	 */
	public void setPlayDuration(Long playDuration) {
		this.playDuration = playDuration;
	}
	/**
	 * 获取：用户观看视频时长/s
	 */
	public Long getPlayDuration() {
		return playDuration;
	}
	/**
	 * 设置：录播视频总时长/s
	 */
	public void setDuration(Long duration) {
		this.duration = duration;
	}
	/**
	 * 获取：录播视频总时长/s
	 */
	public Long getDuration() {
		return duration;
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
	 * 设置：保利威视平台产生日志时间
	 */
	public void setPolyvCreateTime(Date polyvCreateTime) {
		this.polyvCreateTime = polyvCreateTime;
	}
	/**
	 * 获取：保利威视平台产生日志时间
	 */
	public Date getPolyvCreateTime() {
		return polyvCreateTime;
	}


    @Override
    public String toString() {
        return "LogPolyvDetailEntity{" +
                "logId=" + logId +
                ", recordId=" + recordId +
                ", courseId=" + courseId +
                ", userId=" + userId +
                ", vid='" + vid + '\'' +
                ", client='" + client + '\'' +
                ", playDuration=" + playDuration +
                ", duration=" + duration +
                ", createTime=" + createTime +
                ", polyvCreateTime=" + polyvCreateTime +
                '}';
    }
}
