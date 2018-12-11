package com.hq.adaptive.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public class AdlKnowledgeContainEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//本级知识点pk
	private Long selfId;
	//后续知识点pk
	private Long childId;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：本级知识点pk
	 */
	public void setSelfId(Long selfId) {
		this.selfId = selfId;
	}
	/**
	 * 获取：本级知识点pk
	 */
	public Long getSelfId() {
		return selfId;
	}
	/**
	 * 设置：后续知识点pk
	 */
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	/**
	 * 获取：后续知识点pk
	 */
	public Long getChildId() {
		return childId;
	}
	/**
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
