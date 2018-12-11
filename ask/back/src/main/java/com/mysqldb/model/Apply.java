package com.mysqldb.model;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools Date: 2015-08-07 20:20:34
 */
@Entity
@Table(name = "apply")
@org.hibernate.annotations.Table(appliesTo = "apply", comment = "apply")
public class Apply implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * apply
	 */
	public static final String REF = "Apply";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID = "userId";

	/**
	 * 昵称 的属性名
	 */
	public static final String PROP_NICKNAME = "nickName";

	/**
	 * 真实姓名 的属性名
	 */
	public static final String PROP_REALNAME = "realName";

	/**
	 * 支付宝账号 的属性名
	 */
	public static final String PROP_ALIPAYACCOUNT = "alipayAccount";

	/**
	 * 申请单流水号 的属性名
	 */
	public static final String PROP_APPLYFLOWID = "applyFlowId";

	/**
	 * apply_money 的属性名
	 */
	public static final String PROP_APPLYMONEY = "applyMoney";

	/**
	 * update_time 的属性名
	 */
	public static final String PROP_UPDATETIME = "updateTime";

	/**
	 * create_time 的属性名
	 */
	public static final String PROP_CREATETIME = "createTime";

	/**
	 * 申请单状态0为申请中1为结算成功 的属性名
	 */
	public static final String PROP_APPLYSTATE = "applyState";

	/**
	 * 提交申请单月份 的属性名
	 */
	public static final String PROP_APPLYYEARMONTH = "applyYearMonth";

	/**
	 * payment_flow_id 的属性名
	 */
	public static final String PROP_PAYMENTFLOWID = "paymentFlowId";

	/**
	 * 失败代码 的属性名
	 */
	public static final String PROP_ERRCODE = "errCode";

	/**
	 * 失败原因 的属性名
	 */
	public static final String PROP_ERRMEMO = "errMemo";

	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * user_id
	 */
	private java.lang.Integer userId;

	/**
	 * 昵称
	 */
	private java.lang.String nickName;

	/**
	 * 真实姓名
	 */
	private java.lang.String realName;

	/**
	 * 支付宝账号
	 */
	private java.lang.String alipayAccount;

	/**
	 * 申请单流水号
	 */
	private java.lang.String applyFlowId;

	/**
	 * apply_money
	 */
	private java.lang.Float applyMoney;

	/**
	 * update_time
	 */
	private java.sql.Timestamp updateTime;

	/**
	 * create_time
	 */
	private java.sql.Timestamp createTime;

	/**
	 * 申请单状态0为申请中1为结算成功
	 */
	private java.lang.Integer applyState;

	/**
	 * 提交申请单月份
	 */
	private java.lang.String applyYearMonth;

	/**
	 * payment_flow_id
	 */
	private java.lang.String paymentFlowId;

	/**
	 * 失败代码
	 */
	private java.lang.String errCode;

	/**
	 * 失败原因
	 */
	private java.lang.String errMemo;

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
	 * Author name@mail.com 获取 user_id 的属性值
	 * 
	 * @return userId : user_id
	 */
	@Column(name = "USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUserId() {
		return this.userId;
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
	 * Author name@mail.com 获取 昵称 的属性值
	 * 
	 * @return nickName : 昵称
	 */
	@Column(name = "NICK_NAME", columnDefinition = "VARCHAR")
	public java.lang.String getNickName() {
		return this.nickName;
	}

	/**
	 * Author name@mail.com 设置 昵称 的属性值
	 * 
	 * @param nickName
	 *            : 昵称
	 */
	public void setNickName(java.lang.String nickName) {
		this.nickName = nickName;
	}

	/**
	 * Author name@mail.com 获取 真实姓名 的属性值
	 * 
	 * @return realName : 真实姓名
	 */
	@Column(name = "REAL_NAME", columnDefinition = "VARCHAR")
	public java.lang.String getRealName() {
		return this.realName;
	}

	/**
	 * Author name@mail.com 设置 真实姓名 的属性值
	 * 
	 * @param realName
	 *            : 真实姓名
	 */
	public void setRealName(java.lang.String realName) {
		this.realName = realName;
	}

	/**
	 * Author name@mail.com 获取 支付宝账号 的属性值
	 * 
	 * @return alipayAccount : 支付宝账号
	 */
	@Column(name = "ALIPAY_ACCOUNT", columnDefinition = "VARCHAR")
	public java.lang.String getAlipayAccount() {
		return this.alipayAccount;
	}

	/**
	 * Author name@mail.com 设置 支付宝账号 的属性值
	 * 
	 * @param alipayAccount
	 *            : 支付宝账号
	 */
	public void setAlipayAccount(java.lang.String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}

	/**
	 * Author name@mail.com 获取 申请单流水号 的属性值
	 * 
	 * @return applyFlowId : 申请单流水号
	 */
	@Column(name = "APPLY_FLOW_ID", columnDefinition = "INT")
	public java.lang.String getApplyFlowId() {
		return this.applyFlowId;
	}

	/**
	 * Author name@mail.com 设置 申请单流水号 的属性值
	 * 
	 * @param applyFlowId
	 *            : 申请单流水号
	 */
	public void setApplyFlowId(java.lang.String applyFlowId) {
		this.applyFlowId = applyFlowId;
	}

	/**
	 * Author name@mail.com 获取 apply_money 的属性值
	 * 
	 * @return applyMoney : apply_money
	 */
	@Column(name = "APPLY_MONEY", columnDefinition = "FLOAT")
	public java.lang.Float getApplyMoney() {
		return this.applyMoney;
	}

	/**
	 * Author name@mail.com 设置 apply_money 的属性值
	 * 
	 * @param applyMoney
	 *            : apply_money
	 */
	public void setApplyMoney(java.lang.Float applyMoney) {
		this.applyMoney = applyMoney;
	}

	/**
	 * Author name@mail.com 获取 update_time 的属性值
	 * 
	 * @return updateTime : update_time
	 */
	@Column(name = "UPDATE_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateTime() {
		return this.updateTime;
	}

	/**
	 * Author name@mail.com 设置 update_time 的属性值
	 * 
	 * @param updateTime
	 *            : update_time
	 */
	public void setUpdateTime(java.sql.Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * Author name@mail.com 获取 create_time 的属性值
	 * 
	 * @return createTime : create_time
	 */
	@Column(name = "CREATE_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getCreateTime() {
		return this.createTime;
	}

	/**
	 * Author name@mail.com 设置 create_time 的属性值
	 * 
	 * @param createTime
	 *            : create_time
	 */
	public void setCreateTime(java.sql.Timestamp createTime) {
		this.createTime = createTime;
	}

	/**
	 * Author name@mail.com 获取 申请单状态0为申请中1为结算成功 的属性值
	 * 
	 * @return applyState : 申请单状态0为申请中1为结算成功
	 */
	@Column(name = "APPLY_STATE", columnDefinition = "INT")
	public java.lang.Integer getApplyState() {
		return this.applyState;
	}

	/**
	 * Author name@mail.com 设置 申请单状态0为申请中1为结算成功 的属性值
	 * 
	 * @param applyState
	 *            : 申请单状态0为申请中1为结算成功
	 */
	public void setApplyState(java.lang.Integer applyState) {
		this.applyState = applyState;
	}

	/**
	 * Author name@mail.com 获取 提交申请单月份 的属性值
	 * 
	 * @return applyYearMonth : 提交申请单月份
	 */
	@Column(name = "APPLY_YEAR_MONTH", columnDefinition = "VARCHAR")
	public java.lang.String getApplyYearMonth() {
		return this.applyYearMonth;
	}

	/**
	 * Author name@mail.com 设置 提交申请单月份 的属性值
	 * 
	 * @param applyYearMonth
	 *            : 提交申请单月份
	 */
	public void setApplyYearMonth(java.lang.String applyYearMonth) {
		this.applyYearMonth = applyYearMonth;
	}

	/**
	 * Author name@mail.com 获取 payment_flow_id 的属性值
	 * 
	 * @return paymentFlowId : payment_flow_id
	 */
	@Column(name = "PAYMENT_FLOW_ID", columnDefinition = "VARCHAR")
	public java.lang.String getPaymentFlowId() {
		return this.paymentFlowId;
	}

	/**
	 * Author name@mail.com 设置 payment_flow_id 的属性值
	 * 
	 * @param paymentFlowId
	 *            : payment_flow_id
	 */
	public void setPaymentFlowId(java.lang.String paymentFlowId) {
		this.paymentFlowId = paymentFlowId;
	}

	/**
	 * Author name@mail.com 获取 失败代码 的属性值
	 * 
	 * @return errCode : 失败代码
	 */
	@Column(name = "ERR_CODE", columnDefinition = "VARCHAR")
	public java.lang.String getErrCode() {
		return this.errCode;
	}

	/**
	 * Author name@mail.com 设置 失败代码 的属性值
	 * 
	 * @param errCode
	 *            : 失败代码
	 */
	public void setErrCode(java.lang.String errCode) {
		this.errCode = errCode;
	}

	/**
	 * Author name@mail.com 获取 失败原因 的属性值
	 * 
	 * @return errMemo : 失败原因
	 */
	@Column(name = "ERR_MEMO", columnDefinition = "VARCHAR")
	public java.lang.String getErrMemo() {
		return this.errMemo;
	}

	/**
	 * Author name@mail.com 设置 失败原因 的属性值
	 * 
	 * @param errMemo
	 *            : 失败原因
	 */
	public void setErrMemo(java.lang.String errMemo) {
		this.errMemo = errMemo;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=Apply" + ",id=" + id + ",userId=" + userId + ",nickName=" + nickName + ",realName=" + realName
				+ ",alipayAccount=" + alipayAccount + ",applyFlowId=" + applyFlowId + ",applyMoney=" + applyMoney
				+ ",updateTime=" + updateTime + ",createTime=" + createTime + ",applyState=" + applyState
				+ ",applyYearMonth=" + applyYearMonth + ",paymentFlowId=" + paymentFlowId + ",errCode=" + errCode
				+ ",errMemo=" + errMemo + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'Apply'" + ",id:'" + id + "'" + ",userId:'" + userId + "'" + ",nickName:'" + nickName + "'"
				+ ",realName:'" + realName + "'" + ",alipayAccount:'" + alipayAccount + "'" + ",applyFlowId:'"
				+ applyFlowId + "'" + ",applyMoney:'" + applyMoney + "'" + ",updateTime:'" + updateTime + "'"
				+ ",createTime:'" + createTime + "'" + ",applyState:'" + applyState + "'" + ",applyYearMonth:'"
				+ applyYearMonth + "'" + ",paymentFlowId:'" + paymentFlowId + "'" + ",errCode:'" + errCode + "'"
				+ ",errMemo:'" + errMemo + "'" + "}";
	}
}
