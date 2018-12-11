package com.hqjy.pay;

import java.io.Serializable;
import java.math.BigDecimal;
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
	// id
	private Integer id;
	// 订单号
	private String orderNo;
	// 订单名称
	private String orderName;
	// 支付状态：0：成功 1：代付款
	private Integer state;
	// 创建时间
	private String createTime;
	//修改时间
	private String updateTime;
	// 交易订单号
	private String tradeNo;
	// 交易金额
	private BigDecimal tradeMoney;
	// 支付方式
	private Integer payMode;
	// 创建支付下订单IP
	private String createOrderIp;
	// 下单时间戳
	private String orderTimestamp;
	// 支付完成时间
	private Date orderPaySucceedTime;
	// 支付平台订单号
	private String payOrderNo;
	// 双方相互密文
	private String ciphertext;
	// 平台标志
	private String terrace;
	// 支付成功IP
	private String payCallbackIp;
	// 第三方平台返回信息
	private String payCallbackInfo;
	// 日志信息
	private String payLog;


	/**
	 * 课程专业
	 */
	private String courseMajor;

	/**
	 * 学历层次
	 */
	private String eduLevel;

	/**
	 * 班型名称
	 */
	private String classTypeName;

	/**
	 * 课程原价
	 */
	private BigDecimal coursePrice;

	/**
	 * 优惠折扣
	 */
	private BigDecimal discount;

	/**
	 * 本次需支付
	 */
	private BigDecimal currentPayMoney;

	/**
	 * 剩余应支付
	 */
	private BigDecimal overPayMoney;

	/**
	 * 保存自考订单构造函数
	 */
	public PayOrderEntity(String orderNo, String orderName,
							BigDecimal tradeMoney, String createOrderIp, String orderTimestamp, String payOrderNo, String ciphertext,
							String terrace, String courseMajor, String eduLevel, String classTypeName, BigDecimal coursePrice,
							BigDecimal discount, BigDecimal currentPayMoney, BigDecimal overPayMoney) {
		this(orderNo, orderName, tradeMoney, createOrderIp, orderTimestamp, payOrderNo, ciphertext, terrace);
		this.courseMajor = courseMajor;
		this.eduLevel = eduLevel;
		this.classTypeName = classTypeName;
		this.coursePrice = coursePrice;
		this.discount = discount;
		this.currentPayMoney = currentPayMoney;
		this.overPayMoney = overPayMoney;
	}

	public PayOrderEntity() {
	}
	//下订单时判断订单号是否存在
    public PayOrderEntity(String tradeNo,String payOrderNo,String updateTime) {
		super();
		this.tradeNo = tradeNo;
		this.updateTime = updateTime;
		this.payOrderNo = payOrderNo;
	}

	//下订单保存报错构造函数
	public PayOrderEntity(String orderNo, BigDecimal tradeMoney,String createTime, String terrace, String createOrderIp,
			String payOrderNo, String payLog) {
		super();
		this.orderNo = orderNo;
		this.tradeMoney = tradeMoney;
		this.createTime=createTime;
		this.terrace = terrace;
		this.createOrderIp = createOrderIp;
		this.payOrderNo = payOrderNo;
		this.payLog = payLog;
	}
	 //回调接口保存日志构造函数
	public PayOrderEntity(String orderNo, BigDecimal tradeMoney, String terrace, Integer payMode,
			String payCallbackIp, String payOrderNo, String payCallbackInfo, String payLog, String tradeNo) {
		super();
		this.orderNo = orderNo;
		this.tradeMoney = tradeMoney;
		this.terrace = terrace;
		this.payMode = payMode;
		this.payCallbackIp = payCallbackIp;
		this.payOrderNo = payOrderNo;
		this.payCallbackInfo = payCallbackInfo;
		this.payLog = payLog;
		this.tradeNo = tradeNo;
	}
	//支付成功修改订单信息构造函数
    public PayOrderEntity(String tradeNo, Date orderPaySucceedTime, String payOrderNo) {
		super();
		this.tradeNo = tradeNo;
		this.orderPaySucceedTime = orderPaySucceedTime;
		this.payOrderNo = payOrderNo;
	}
	//保存订单构造函数
	public PayOrderEntity(String orderNo, String orderName, 
			BigDecimal tradeMoney, String createOrderIp, String orderTimestamp, String payOrderNo, String ciphertext,
			String terrace) {
		super();
		this.orderNo = orderNo;
		this.orderName = orderName;
		this.state = state;
		this.tradeNo = tradeNo;
		this.tradeMoney = tradeMoney;
		this.createOrderIp = createOrderIp;
		this.orderTimestamp = orderTimestamp;
		this.payOrderNo = payOrderNo;
		this.ciphertext = ciphertext;
		this.terrace = terrace;
	}

	public String getPayCallbackIp() {
		return payCallbackIp;
	}

	public void setPayCallbackIp(String payCallbackIp) {
		this.payCallbackIp = payCallbackIp;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public String getPayLog() {
		return payLog;
	}

	public void setPayLog(String payLog) {
		this.payLog = payLog;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public BigDecimal getTradeMoney() {
		return tradeMoney;
	}

	public void setTradeMoney(BigDecimal tradeMoney) {
		this.tradeMoney = tradeMoney;
	}

	public Integer getPayMode() {
		return payMode;
	}

	public void setPayMode(Integer payMode) {
		this.payMode = payMode;
	}

	public String getCreateOrderIp() {
		return createOrderIp;
	}

	public void setCreateOrderIp(String createOrderIp) {
		this.createOrderIp = createOrderIp;
	}

	public String getOrderTimestamp() {
		return orderTimestamp;
	}

	public void setOrderTimestamp(String orderTimestamp) {
		this.orderTimestamp = orderTimestamp;
	}

	public Date getOrderPaySucceedTime() {
		return orderPaySucceedTime;
	}

	public void setOrderPaySucceedTime(Date orderPaySucceedTime) {
		this.orderPaySucceedTime = orderPaySucceedTime;
	}

	public String getPayOrderNo() {
		return payOrderNo;
	}

	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public String getCiphertext() {
		return ciphertext;
	}

	public void setCiphertext(String ciphertext) {
		this.ciphertext = ciphertext;
	}

	public String getTerrace() {
		return terrace;
	}

	public void setTerrace(String terrace) {
		this.terrace = terrace;
	}

	public String getPayCallbackInfo() {
		return payCallbackInfo;
	}

	public void setPayCallbackInfo(String payCallbackInfo) {
		this.payCallbackInfo = payCallbackInfo;
	}

	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getCourseMajor() {
		return courseMajor;
	}

	public void setCourseMajor(String courseMajor) {
		this.courseMajor = courseMajor;
	}

	public String getEduLevel() {
		return eduLevel;
	}

	public void setEduLevel(String eduLevel) {
		this.eduLevel = eduLevel;
	}

	public String getClassTypeName() {
		return classTypeName;
	}

	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}

	public BigDecimal getCoursePrice() {
		return coursePrice;
	}

	public void setCoursePrice(BigDecimal coursePrice) {
		this.coursePrice = coursePrice;
	}

	public BigDecimal getDiscount() {
		return discount;
	}

	public void setDiscount(BigDecimal discount) {
		this.discount = discount;
	}

	public BigDecimal getCurrentPayMoney() {
		return currentPayMoney;
	}

	public void setCurrentPayMoney(BigDecimal currentPayMoney) {
		this.currentPayMoney = currentPayMoney;
	}

	public BigDecimal getOverPayMoney() {
		return overPayMoney;
	}

	public void setOverPayMoney(BigDecimal overPayMoney) {
		this.overPayMoney = overPayMoney;
	}

	@Override
	public String toString() {
		return "PayOrderEntity [id=" + id + ", orderNo=" + orderNo + ", orderName=" + orderName + ", state=" + state
				+ ", createTime=" + createTime + ", tradeNo=" + tradeNo + ", tradeMoney=" + tradeMoney + ", payMode="
				+ payMode + ", createOrderIp=" + createOrderIp + ", orderTimestamp=" + orderTimestamp
				+ ", orderPaySucceedTime=" + orderPaySucceedTime + ", payOrderNo=" + payOrderNo + ", ciphertext="
				+ ciphertext + ", terrace=" + terrace + "]";
	}
}
