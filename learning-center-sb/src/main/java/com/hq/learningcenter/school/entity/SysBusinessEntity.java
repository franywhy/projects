package com.hq.learningcenter.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 业务线
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-09-01 10:46:50
 */
public class SysBusinessEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private String businessId;
	//
	private String name;
	//短信签名
	private String smsSign;
	//首页地址
	private String homeUrl;
	//类型0.PC网站 1.移动端站点
	private Integer type;
	
	private String jsonSetting;

	/**
	 * 设置：
	 */
	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}
	/**
	 * 获取：
	 */
	public String getBusinessId() {
		return businessId;
	}
	/**
	 * 设置：
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * 获取：
	 */
	public String getName() {
		return name;
	}
	/**
	 * 设置：短信签名
	 */
	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
	/**
	 * 获取：短信签名
	 */
	public String getSmsSign() {
		return smsSign;
	}
	/**
	 * 设置：首页地址
	 */
	public void setHomeUrl(String homeUrl) {
		this.homeUrl = homeUrl;
	}
	/**
	 * 获取：首页地址
	 */
	public String getHomeUrl() {
		return homeUrl;
	}
	/**
	 * 设置：类型0.PC网站 1.移动端站点
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：类型0.PC网站 1.移动端站点
	 */
	public Integer getType() {
		return type;
	}
	
	public String getJsonSetting() {
		return jsonSetting;
	}
	public void setJsonSetting(String jsonSetting) {
		this.jsonSetting = jsonSetting;
	}
}
