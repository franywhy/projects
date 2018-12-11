package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 站内消息
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 17:41:49
 */
public class MsgContentCommonEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long commonId;
	//用户ID
	private Long userId;
	//消息PK
	private Long contentId;
	//平台
	private String schoolId;

	/**
	 * 设置：主键
	 */
	public void setCommonId(Long commonId) {
		this.commonId = commonId;
	}
	/**
	 * 获取：主键
	 */
	public Long getCommonId() {
		return commonId;
	}
	/**
	 * 设置：用户ID
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户ID
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：消息PK
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	/**
	 * 获取：消息PK
	 */
	public Long getContentId() {
		return contentId;
	}
	/**
	 * 设置：平台
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台
	 */
	public String getSchoolId() {
		return schoolId;
	}
}
