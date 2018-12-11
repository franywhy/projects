package com.hq.answerapi.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * app常量记录表
 * 
 * @author zhaownwei
 * @date 2018-02-26 14:58:44
 */
public class AppConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//创建者
	private Long creator;
	//创建时间
	private Date creationTime;
	//
	private Long key;
	//内容
	private String value;
	//
	private String url;
	//版本号
	private Integer version;
	//设备
	private String facility;
	//备注
	private String remark;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建者
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：
	 */
	public void setKey(Long key) {
		this.key = key;
	}
	/**
	 * 获取：
	 */
	public Long getKey() {
		return key;
	}
	/**
	 * 设置：内容
	 */
	public void setValue(String value) {
		this.value = value;
	}
	/**
	 * 获取：内容
	 */
	public String getValue() {
		return value;
	}
	/**
	 * 设置：
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	public String getFacility() {
		return facility;
	}
	public void setFacility(String facility) {
		this.facility = facility;
	}
	
	
}
