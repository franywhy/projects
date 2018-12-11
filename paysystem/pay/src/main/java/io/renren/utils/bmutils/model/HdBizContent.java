package io.renren.utils.bmutils.model;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;


public class HdBizContent implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JSONField(name="end_time")  
	private String endTime;

	@JSONField(name="id_name")  
	private String idName;

	@JSONField(name="id_no")  
	private String idNo;
	@JSONField(name="order_status")  
	private String orderStatus;
	
	@JSONField(name="phone_no")  
	private String phoneNo;

	@JSONField(name="start_time")  
	private String startTime;

	@JSONField(name="trade_no")  
	private String tradeNo;
	@JSONField(name="ciphertext") 
	private String ciphertext;
	
	
	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getIdName() {
		return idName;
	}

	public void setIdName(String idName) {
		this.idName = idName;
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	
	

	
}

