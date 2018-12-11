package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-07 11:02:24
 */
public class PayOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Integer id;
	//订单号
	private String orderNo;
	//订单名称
	private String orderName;
	//支付状态：0：成功 1：代付款
	private Integer state;
	//创建时间
	private Date createTime;
	//交易订单号
	private String tradeNo;
	//交易金额
	private Double tradeMoney;
	//支付方式
	private Integer payMode;
	//创建支付下订单IP
	private String createOrderIp;
	//支付成功回调IP
	private String alipayIp;
	//商品图片
	private String goodsPic;
	//参数
	private String param;
	//下单时间戳
	private String orderTimestamp;
	//支付完成时间
	private Date orderPaySucceedTime;
	//第三方平台返回错误信息
	private String alipayReturnErrorCode;
	//支付平台订单号
	private String payOrderNo;
    //双方相互密文
	private String ciphertext;
	//平台标志
	private String terrace;
	
	public String getTerrace() {
		return terrace;
	}

	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	/**
	 * 获取：id
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
	/**
	 * 获取：订单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：订单名称
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	/**
	 * 获取：订单名称
	 */
	public String getOrderName() {
		return orderName;
	}
	/**
	 * 设置：支付状态：0：成功 1：失败
	 */
	public void setState(Integer state) {
		this.state = state;
	}
	/**
	 * 获取：支付状态：0：成功 1：失败
	 */
	public Integer getState() {
		return state;
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
	 * 设置：交易订单号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}
	/**
	 * 获取：交易订单号
	 */
	public String getTradeNo() {
		return tradeNo;
	}
	/**
	 * 设置：交易金额
	 */
	public void setTradeMoney(Double tradeMoney) {
		this.tradeMoney = tradeMoney;
	}
	/**
	 * 获取：交易金额
	 */
	public Double getTradeMoney() {
		return tradeMoney;
	}
	/**
	 * 设置：支付方式
	 */
	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}
	/**
	 * 获取：支付方式
	 */
	public Integer getPayMode() {
		return payMode;
	}
	/**
	 * 设置：创建支付下订单IP
	 */
	public void setCreateOrderIp(String createOrderIp) {
		this.createOrderIp = createOrderIp;
	}
	/**
	 * 获取：创建支付下订单IP
	 */
	public String getCreateOrderIp() {
		return createOrderIp;
	}
	/**
	 * 设置：支付成功回调IP
	 */
	public void setAlipayIp(String alipayIp) {
		this.alipayIp = alipayIp;
	}
	/**
	 * 获取：支付成功回调IP
	 */
	public String getAlipayIp() {
		return alipayIp;
	}
	/**
	 * 设置：商品图片
	 */
	public void setGoodsPic(String goodsPic) {
		this.goodsPic = goodsPic;
	}
	/**
	 * 获取：商品图片
	 */
	public String getGoodsPic() {
		return goodsPic;
	}
	/**
	 * 设置：参数
	 */
	public void setParam(String param) {
		this.param = param;
	}
	/**
	 * 获取：参数
	 */
	public String getParam() {
		return param;
	}
	/**
	 * 设置：下单时间戳
	 */
	public void setOrderTimestamp(String orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}
	/**
	 * 获取：下单时间戳
	 */
	public String getOrderTimestamp() {
		return orderTimestamp;
	}
	

	/**
	 * 设置：支付完成时间
	 */
	
	/**
	 * 设置：第三方平台返回错误信息
	 */
	public void setAlipayReturnErrorCode(String alipayReturnErrorCode) {
		this.alipayReturnErrorCode = alipayReturnErrorCode;
	}
	public Date getOrderPaySucceedTime() {
		return orderPaySucceedTime;
	}

	public void setOrderPaySucceedTime(Date orderPaySucceedTime) {
		this.orderPaySucceedTime = orderPaySucceedTime;
	}

	/**
	 * 获取：第三方平台返回错误信息
	 */
	public String getAlipayReturnErrorCode() {
		return alipayReturnErrorCode;
	}
	/**
	 * 设置：支付平台订单号
	 */
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}
	/**
	 * 获取：支付平台订单号
	 */
	public String getPayOrderNo() {
		return payOrderNo;
	}

	@Override
	public String toString() {
		return "PayOrderEntity [id=" + id + ", orderNo=" + orderNo + ", orderName=" + orderName + ", state=" + state
				+ ", createTime=" + createTime + ", tradeNo=" + tradeNo + ", tradeMoney=" + tradeMoney + ", payMode="
				+ payMode + ", createOrderIp=" + createOrderIp + ", alipayIp=" + alipayIp + ", goodsPic=" + goodsPic
				+ ", param=" + param + ", orderTimestamp=" + orderTimestamp + ", orderPaySucceedTime="
				+ orderPaySucceedTime + ", alipayReturnErrorCode=" + alipayReturnErrorCode + ", payOrderNo="
				+ payOrderNo + ", ciphertext=" + ciphertext + ", terrace=" + terrace + "]";
	}
	
}
