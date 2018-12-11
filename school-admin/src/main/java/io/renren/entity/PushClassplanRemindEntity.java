package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 课次通知提醒表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:19:51
 */
public class PushClassplanRemindEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//排课计划ID
	private String courseClassplanId;
	//推送模板ID
	private Integer pushTimeTemplateId;
	//创建人
	private Long creater;
	//创建时间
	private Date createTime;
	//最近更新人
	private Long updater;
	//
	private Date updateTime;
	//是否删除 0：正常 1：删除
	private Integer dr;
	
	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：排课计划ID
	 */
	public void setCourseClassplanId(String courseClassplanId) {
		this.courseClassplanId = courseClassplanId;
	}
	/**
	 * 获取：排课计划ID
	 */
	public String getCourseClassplanId() {
		return courseClassplanId;
	}
	/**
	 * 设置：推送模板ID
	 */
	public void setPushTimeTemplateId(Integer pushTimeTemplateId) {
		this.pushTimeTemplateId = pushTimeTemplateId;
	}
	/**
	 * 获取：推送模板ID
	 */
	public Integer getPushTimeTemplateId() {
		return pushTimeTemplateId;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreater() {
		return creater;
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
	 * 设置：最近更新人
	 */
	public void setUpdater(Long updater) {
		this.updater = updater;
	}
	/**
	 * 获取：最近更新人
	 */
	public Long getUpdater() {
		return updater;
	}
	/**
	 * 设置：
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：是否删除 0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除 0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
}
