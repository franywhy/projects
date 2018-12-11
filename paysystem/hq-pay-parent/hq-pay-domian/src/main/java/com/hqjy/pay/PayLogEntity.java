package com.hqjy.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-11 11:57:44
 */
public class PayLogEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// ID
	private Integer id;
	// 订单号
	private String orderNo;
	// 交易金额
	private BigDecimal tradeMoney;
	// 交易平台标识
	private String terrace;
	// 支付方式 0：支付宝 1：微信
	private Integer payMode;
	// 创建订单IP
	private String createOrderIp;
	// 支付成功IP
	private String payCallbackIp;
	//商户单号
	private String payOrderNo;
	// 第三方平台返回信息
	private String payCallbackInfo;
	// 日志信息
	private String payLog;
	// 时间戳
	private String createTime;
	// 交易单号
	private String tradeNo;

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayCallbackIp() {
		return payCallbackIp;
	}

	public void setPayCallbackIp(String payCallbackIp) {
		this.payCallbackIp = payCallbackIp;
	}

	/**
	 * 设置：ID
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：订单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取：订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}

	public BigDecimal getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(BigDecimal tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	/**
	 * 设置：交易平台标识
	 */
	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}

	/**
	 * 获取：交易平台标识
	 */
	public String getTerrace() {
		return terrace;
	}

	/**
	 * 设置：支付方式 0：支付宝 1：微信
	 */
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	/**
	 * 获取：支付方式 0：支付宝 1：微信
	 */
	public Integer getPayMode() {
		return payMode;
	}

	/**
	 * 设置：创建订单IP
	 */
	public void setCreateOrderIp(String createOrderIp) {
		this.createOrderIp = createOrderIp;
	}

	/**
	 * 获取：创建订单IP
	 */
	public String getCreateOrderIp() {
		return createOrderIp;
	}
	/**
	 * 设置：统一下订单单号
	 */
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	/**
	 * 获取：统一下订单单号
	 */
	public String getPayOrderNo() {
		return payOrderNo;
	}

	/**
	 * 设置：第三方平台返回信息
	 */
	public String getPayCallbackInfo() {
		return payCallbackInfo;
	}

	/**
	 * 设置：日志信息
	 */
	public String getPayLog() {
		return payLog;
	}

	public void setPayLog(String payLog) {
		this.payLog = payLog;
	}

	public void setPayCallbackInfo(String payCallbackInfo) {
		this.payCallbackInfo = payCallbackInfo;
	}

}
