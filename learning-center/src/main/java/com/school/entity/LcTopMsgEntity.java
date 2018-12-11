package com.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-30 11:44:03
 */
public class LcTopMsgEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long topMsgId;
	//业务id
	private String businessId;
	//消息内容
	private String msgContent;
	//跳转地址
	private String url;
	//开关:0:关闭 1:开启
	private Integer status;

	/**
	 * 设置：pk
	 */
	public void setTopMsgId(Long topMsgId) {
		this.topMsgId = topMsgId;
	}
	/**
	 * 获取：pk
	 */
	public Long getTopMsgId() {
		return topMsgId;
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
	 * 设置：消息内容
	 */
	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}
	/**
	 * 获取：消息内容
	 */
	public String getMsgContent() {
		return msgContent;
	}
	/**
	 * 设置：跳转地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	/**
	 * 获取：跳转地址
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * 设置：开关:0:关闭 1:开启
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：开关:0:关闭 1:开启
	 */
	public Integer getStatus() {
		return status;
	}
}
