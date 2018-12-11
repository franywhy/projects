package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 弹窗消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-19 14:12:25
 */
public class MsgContentTopEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long topId;
	//用户主键
	private Long userId;
	//弹出类型:0.未弹出,1.弹出,2.弹出过期
	private Integer type;
	//消息主键
	private Long contentId;
	//校区PK
	private String schoolId;
	//弹出过期时间type=1,2时记录
	private Date timestamp;

	/**
	 * 设置：主键
	 */
	public void setTopId(Long topId) {
		this.topId = topId;
	}
	/**
	 * 获取：主键
	 */
	public Long getTopId() {
		return topId;
	}
	/**
	 * 设置：用户主键
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户主键
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：弹出类型:0.未弹出,1.弹出,2.弹出过期
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：弹出类型:0.未弹出,1.弹出,2.弹出过期
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：消息主键
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	/**
	 * 获取：消息主键
	 */
	public Long getContentId() {
		return contentId;
	}
	/**
	 * 设置：校区PK
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：校区PK
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：弹出过期时间type=1,2时记录
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * 获取：弹出过期时间type=1,2时记录
	 */
	public Date getTimestamp() {
		return timestamp;
	}
}
