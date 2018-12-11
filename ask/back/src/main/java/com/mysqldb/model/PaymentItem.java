package com.mysqldb.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Author: 汤垲峰-MagicalTools Date: 2015-08-18 19:00:49
 */
@Entity
@Table(name = "payment_item")
@org.hibernate.annotations.Table(appliesTo = "payment_item", comment = "payment_item")
public class PaymentItem implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * payment_item
	 */
	public static final String REF = "PaymentItem";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * 付款单id 的属性名
	 */
	public static final String PROP_PAYMENTID = "paymentId";

	/**
	 * pay_flow_id 的属性名
	 */
	public static final String PROP_PAYFLOWID = "payFlowId";

	/**
	 * alipay_account 的属性名
	 */
	public static final String PROP_ALIPAYACCOUNT = "alipayAccount";

	/**
	 * alipay_code 的属性名
	 */
	public static final String PROP_ALIPAYCODE = "alipayCode";

	/**
	 * apply_flow_id 的属性名
	 */
	public static final String PROP_APPLYFLOWID = "applyFlowId";

	/**
	 * err_code 的属性名
	 */
	public static final String PROP_ERRCODE = "errCode";

	/**
	 * err_memo 的属性名
	 */
	public static final String PROP_ERRMEMO = "errMemo";

	/**
	 * 支付宝完成时间 的属性名
	 */
	public static final String PROP_ALIFINISHTIME = "aliFinishTime";

	/**
	 * real_name 的属性名
	 */
	public static final String PROP_REALNAME = "realName";

	/**
	 * return_Memo 的属性名
	 */
	public static final String PROP_RETURNMEMO = "returnMemo";

	/**
	 * return_state 的属性名
	 */
	public static final String PROP_RETURNSTATE = "returnState";

	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID = "userId";

	/**
	 * 申请金额 的属性名
	 */
	public static final String PROP_APPLYMONEY = "applyMoney";

	
	/**
	 * 提交申请单月份 的属性名
	 */
	public static final String PROP_APPLYYEARMONTH="applyYearMonth";
	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * 付款单id
	 */
	private java.lang.Integer paymentId;

	/**
	 * pay_flow_id
	 */
	private java.lang.String payFlowId;

	/**
	 * alipay_account
	 */
	private java.lang.String alipayAccount;

	/**
	 * alipay_code
	 */
	private java.lang.String alipayCode;

	/**
	 * apply_flow_id
	 */
	private java.lang.String applyFlowId;

	/**
	 * err_code
	 */
	private java.lang.String errCode;

	/**
	 * err_memo
	 */
	private java.lang.String errMemo;

	/**
	 * 支付宝完成时间
	 */
	private java.lang.String aliFinishTime;

	/**
	 * real_name
	 */
	private java.lang.String realName;

	/**
	 * return_Memo
	 */
	private java.lang.String returnMemo;

	/**
	 * return_state
	 */
	private java.lang.Integer returnState;

	/**
	 * user_id
	 */
	private java.lang.Integer userId;

	/**
	 * main_id
	 */
	private Payment payment;
	
	/**
	 * 提交申请单月份
	 */
	private java.lang.String applyYearMonth;

	/**
	 * 申请金额
	 */
	private java.math.BigDecimal applyMoney;

	private java.lang.Integer _id;

	@Transient
	public java.lang.Integer get_id() {
		return this.id;
	}

	public void set_id(java.lang.Integer _id) {
		this._id = _id;
	}

	/**
	 * Author name@mail.com 获取 id 的属性值
	 * 
	 * @return id : id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID", columnDefinition = "INT")
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * Author name@mail.com 设置 id 的属性值
	 * 
	 * @param id
	 *            : id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * Author name@mail.com 获取 付款单id 的属性值
	 * 
	 * @return paymentId : 付款单id
	 */
	@Column(name = "PAYMENT_ID", columnDefinition = "INT", insertable = false, updatable = false)
	public java.lang.Integer getPaymentId() {
		return this.paymentId;
	}

	/**
	 * Author name@mail.com 设置 付款单id 的属性值
	 * 
	 * @param paymentId
	 *            : 付款单id
	 */
	public void setPaymentId(java.lang.Integer paymentId) {
		this.paymentId = paymentId;
	}

	/**
	 * Author name@mail.com 获取 pay_flow_id 的属性值
	 * 
	 * @return payFlowId : pay_flow_id
	 */
	@Column(name = "PAY_FLOW_ID", columnDefinition = "VARCHAR")
	public java.lang.String getPayFlowId() {
		return this.payFlowId;
	}

	/**
	 * Author name@mail.com 设置 pay_flow_id 的属性值
	 * 
	 * @param payFlowId
	 *            : pay_flow_id
	 */
	public void setPayFlowId(java.lang.String payFlowId) {
		this.payFlowId = payFlowId;
	}

	/**
	 * Author name@mail.com 获取 alipay_account 的属性值
	 * 
	 * @return alipayAccount : alipay_account
	 */
	@Column(name = "ALIPAY_ACCOUNT", columnDefinition = "VARCHAR")
	public java.lang.String getAlipayAccount() {
		return this.alipayAccount;
	}

	/**
	 * Author name@mail.com 设置 alipay_account 的属性值
	 * 
	 * @param alipayAccount
	 *            : alipay_account
	 */
	public void setAlipayAccount(java.lang.String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	/**
	 * Author name@mail.com 获取 alipay_code 的属性值
	 * 
	 * @return alipayCode : alipay_code
	 */
	@Column(name = "ALIPAY_CODE", columnDefinition = "VARCHAR")
	public java.lang.String getAlipayCode() {
		return this.alipayCode;
	}

	/**
	 * Author name@mail.com 设置 alipay_code 的属性值
	 * 
	 * @param alipayCode
	 *            : alipay_code
	 */
	public void setAlipayCode(java.lang.String alipayCode) {
		this.alipayCode = alipayCode;
	}

	/**
	 * Author name@mail.com 获取 apply_flow_id 的属性值
	 * 
	 * @return applyFlowId : apply_flow_id
	 */
	@Column(name = "APPLY_FLOW_ID", columnDefinition = "VARCHAR")
	public java.lang.String getApplyFlowId() {
		return this.applyFlowId;
	}

	/**
	 * Author name@mail.com 设置 apply_flow_id 的属性值
	 * 
	 * @param applyFlowId
	 *            : apply_flow_id
	 */
	public void setApplyFlowId(java.lang.String applyFlowId) {
		this.applyFlowId = applyFlowId;
	}

	/**
	 * Author name@mail.com 获取 err_code 的属性值
	 * 
	 * @return errCode : err_code
	 */
	@Column(name = "ERR_CODE", columnDefinition = "VARCHAR")
	public java.lang.String getErrCode() {
		return this.errCode;
	}

	/**
	 * Author name@mail.com 设置 err_code 的属性值
	 * 
	 * @param errCode
	 *            : err_code
	 */
	public void setErrCode(java.lang.String errCode) {
		this.errCode = errCode;
	}

	/**
	 * Author name@mail.com 获取 err_memo 的属性值
	 * 
	 * @return errMemo : err_memo
	 */
	@Column(name = "ERR_MEMO", columnDefinition = "VARCHAR")
	public java.lang.String getErrMemo() {
		return this.errMemo;
	}

	/**
	 * Author name@mail.com 设置 err_memo 的属性值
	 * 
	 * @param errMemo
	 *            : err_memo
	 */
	public void setErrMemo(java.lang.String errMemo) {
		this.errMemo = errMemo;
	}

	/**
	 * Author name@mail.com 获取 支付宝完成时间 的属性值
	 * 
	 * @return aliFinishTime : 支付宝完成时间
	 */
	@Column(name = "ALI_FINISH_TIME", columnDefinition = "VARCHAR")
	public java.lang.String getAliFinishTime() {
		return this.aliFinishTime;
	}

	/**
	 * Author name@mail.com 设置 支付宝完成时间 的属性值
	 * 
	 * @param aliFinishTime
	 *            : 支付宝完成时间
	 */
	public void setAliFinishTime(java.lang.String aliFinishTime) {
		this.aliFinishTime = aliFinishTime;
	}

	/**
	 * Author name@mail.com 获取 real_name 的属性值
	 * 
	 * @return realName : real_name
	 */
	@Column(name = "REAL_NAME", columnDefinition = "VARCHAR")
	public java.lang.String getRealName() {
		return this.realName;
	}

	/**
	 * Author name@mail.com 设置 real_name 的属性值
	 * 
	 * @param realName
	 *            : real_name
	 */
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}

	/**
	 * Author name@mail.com 获取 return_Memo 的属性值
	 * 
	 * @return returnMemo : return_Memo
	 */
	@Column(name = "RETURN_MEMO", columnDefinition = "VARCHAR")
	public java.lang.String getReturnMemo() {
		return this.returnMemo;
	}

	/**
	 * Author name@mail.com 设置 return_Memo 的属性值
	 * 
	 * @param returnMemo
	 *            : return_Memo
	 */
	public void setReturnMemo(java.lang.String returnMemo) {
		this.returnMemo = returnMemo;
	}

	/**
	 * Author name@mail.com 获取 return_state 的属性值
	 * 
	 * @return returnState : return_state
	 */
	@Column(name = "RETURN_STATE", columnDefinition = "VARCHAR")
	public java.lang.Integer getReturnState() {
		return this.returnState;
	}

	/**
	 * Author name@mail.com 设置 return_state 的属性值
	 * 
	 * @param returnState
	 *            : return_state
	 */
	public void setReturnState(java.lang.Integer returnState) {
		this.returnState = returnState;
	}

	/**
	 * Author name@mail.com 获取 user_id 的属性值
	 * 
	 * @return userId : user_id
	 */
	@Column(name = "USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUserId() {
		return this.userId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PAYMENT_ID")
	@JsonIgnore
	public Payment getPayment() {
		return payment;
	}

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	/**
	 * Author name@mail.com 设置 user_id 的属性值
	 * 
	 * @param userId
	 *            : user_id
	 */
	public void setUserId(java.lang.Integer userId) {
		this.userId = userId;
	}

	/**
	 * Author name@mail.com 获取 申请金额 的属性值
	 * 
	 * @return applyMoney : 申请金额
	 */
	@Column(name = "APPLY_MONEY", columnDefinition = "DECIMAL")
	public java.math.BigDecimal getApplyMoney() {
		return this.applyMoney;
	}

	/**
	 * Author name@mail.com 设置 申请金额 的属性值
	 * 
	 * @param applyMoney
	 *            : 申请金额
	 */
	public void setApplyMoney(java.math.BigDecimal applyMoney) {
		this.applyMoney = applyMoney;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 提交申请单月份 的属性值
     *
	 * @return applyYearMonth :  提交申请单月份 
	 */
	@Column(name = "APPLY_YEAR_MONTH",columnDefinition = "VARCHAR")
	public java.lang.String getApplyYearMonth(){
		return this.applyYearMonth;
	}

	/**
	 * Author name@mail.com
	 * 设置 提交申请单月份 的属性值
	 *		
	 * @param applyYearMonth :  提交申请单月份 
	 */
	public void setApplyYearMonth(java.lang.String applyYearMonth){
		this.applyYearMonth	= applyYearMonth;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=PaymentItem" + ",id=" + id + ",paymentId=" + paymentId + ",payFlowId=" + payFlowId
				+ ",alipayAccount=" + alipayAccount + ",alipayCode=" + alipayCode + ",applyFlowId=" + applyFlowId
				+ ",errCode=" + errCode + ",errMemo=" + errMemo + ",aliFinishTime=" + aliFinishTime + ",realName="
				+ realName + ",returnMemo=" + returnMemo + ",returnState=" + returnState + ",userId=" + userId
				+ ",applyMoney=" + applyMoney + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'PaymentItem'" + ",id:'" + id + "'" + ",paymentId:'" + paymentId + "'" + ",payFlowId:'"
				+ payFlowId + "'" + ",alipayAccount:'" + alipayAccount + "'" + ",alipayCode:'" + alipayCode + "'"
				+ ",applyFlowId:'" + applyFlowId + "'" + ",errCode:'" + errCode + "'" + ",errMemo:'" + errMemo + "'"
				+ ",aliFinishTime:'" + aliFinishTime + "'" + ",realName:'" + realName + "'" + ",returnMemo:'"
				+ returnMemo + "'" + ",returnState:'" + returnState + "'" + ",userId:'" + userId + "'" + ",applyMoney="
				+ applyMoney + "}";
	}
}
