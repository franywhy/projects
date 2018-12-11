package com.mysqldb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools Date: 2015-08-18 19:00:52
 */
@Entity
@Table(name = "payment")
@org.hibernate.annotations.Table(appliesTo = "payment", comment = "payment")
public class Payment implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * payment
	 */
	public static final String REF = "Payment";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * 付款单流水号 的属性名
	 */
	public static final String PROP_PAYFLOWID = "payFlowId";

	/**
	 * payment_month 的属性名
	 */
	public static final String PROP_PAYMENTMONTH = "paymentMonth";

	/**
	 * 总笔数 的属性名
	 */
	public static final String PROP_BATCHNO = "batchNo";

	/**
	 * 备注 的属性名
	 */
	public static final String PROP_MEMO = "memo";

	/**
	 * 付款账号 的属性名
	 */
	public static final String PROP_PAYACCOUNTNO = "payAccountNo";

	/**
	 * 付款用户对应的支付宝用户 的属性名
	 */
	public static final String PROP_PAYUSERID = "payUserId";

	/**
	 * 付款账号姓名 的属性名
	 */
	public static final String PROP_PAYUSERNAME = "payUserName";

	/**
	 * 总计金额 的属性名
	 */
	public static final String PROP_SUMMONEY = "sumMoney";

	/**
	 * audit_flag 的属性名
	 */
	public static final String PROP_AUDITFLAG = "auditFlag";

	/**
	 * audit_user_id 的属性名
	 */
	public static final String PROP_AUDITUSERID = "auditUserId";

	/**
	 * create_user_id 的属性名
	 */
	public static final String PROP_CREATEUSERID = "createUserId";

	/**
	 * timestamp 的属性名
	 */
	public static final String PROP_TIMESTAMP = "timestamp";

	/**
	 * update_date 的属性名
	 */
	public static final String PROP_UPDATEDATE = "updateDate";

	/**
	 * update_user_id 的属性名
	 */
	public static final String PROP_UPDATEUSERID = "updateUserId";

	/**
	 * upload_date 的属性名
	 */
	public static final String PROP_UPLOADDATE = "uploadDate";

	/**
	 * upload_flag 的属性名
	 */
	public static final String PROP_UPLOADFLAG = "uploadFlag";

	/**
	 * upload_user_id 的属性名
	 */
	public static final String PROP_UPLOADUSERID = "uploadUserId";

	/**
	 * 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功 的属性名
	 */
	public static final String PROP_ALIPAYSTATE = "alipayState";

	/**
	 * 支付宝申请信息 的属性名
	 */
	public static final String PROP_ALIPAYMSG = "alipayMsg";

	/**
	 * 付款当天日期 格式：年[4位]月[2位]日[2位]，如：20100801 的属性名
	 */
	public static final String PROP_ALIPAYPAYDATE = "alipayPayDate";

	/**
	 * 回调日期 的属性名
	 */
	public static final String PROP_ALIPAYNODIFYDATE = "alipayNodifyDate";

	/**
	 * 实际支付总笔数 的属性名
	 */
	public static final String PROP_ACTUALBATCHNO = "actualBatchNo";

	/**
	 * 实际付款总金额 的属性名
	 */
	public static final String PROP_ACTUALSUMMONEY = "actualSumMoney";

	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * 付款单流水号
	 */
	private String payFlowId;

	/**
	 * payment_month
	 */
	private java.lang.String paymentMonth;

	/**
	 * 总笔数
	 */
	private java.lang.Integer batchNo;

	/**
	 * 备注
	 */
	private java.lang.String memo;

	/**
	 * 付款账号
	 */
	private java.lang.String payAccountNo;

	/**
	 * 付款用户对应的支付宝用户
	 */
	private java.lang.Integer payUserId;

	/**
	 * 付款账号姓名
	 */
	private java.lang.String payUserName;

	/**
	 * 总计金额
	 */
	private java.math.BigDecimal sumMoney;

	/**
	 * audit_flag
	 */
	private java.lang.Integer auditFlag;

	/**
	 * audit_user_id
	 */
	private java.lang.Integer auditUserId;

	/**
	 * create_user_id
	 */
	private java.lang.Integer createUserId;

	/**
	 * timestamp
	 */
	private java.sql.Timestamp timestamp;

	/**
	 * update_date
	 */
	private java.sql.Timestamp updateDate;

	/**
	 * update_user_id
	 */
	private java.lang.Integer updateUserId;

	/**
	 * upload_date
	 */
	private java.sql.Timestamp uploadDate;

	/**
	 * upload_flag
	 */
	private java.lang.Integer uploadFlag;

	/**
	 * upload_user_id
	 */
	private java.lang.Integer uploadUserId;

	/**
	 * 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功
	 */
	private java.lang.Integer alipayState;

	/**
	 * 支付宝申请信息
	 */
	private java.lang.String alipayMsg;

	/**
	 * 实际支付总笔数
	 */
	private java.lang.Integer actualBatchNo;

	/**
	 * 实际付款总金额
	 */
	private java.math.BigDecimal actualSumMoney;

	private Set<PaymentItem> paymentItemList;

	/**
	 * 付款当天日期
	 */
	private java.sql.Timestamp alipayPayDate;

	/**
	 * 回调日期
	 */
	private java.sql.Timestamp alipayNodifyDate;

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
	 * Author name@mail.com 获取 付款单流水号 的属性值
	 * 
	 * @return payFlowId : 付款单流水号
	 */
	@Column(name = "PAY_FLOW_ID", columnDefinition = "VARCHAR")
	public String getPayFlowId() {
		return this.payFlowId;
	}

	/**
	 * Author name@mail.com 设置 付款单流水号 的属性值
	 * 
	 * @param payFlowId
	 *            : 付款单流水号
	 */
	public void setPayFlowId(String payFlowId) {
		this.payFlowId = payFlowId;
	}

	/**
	 * Author name@mail.com 获取 payment_month 的属性值
	 * 
	 * @return paymentMonth : payment_month
	 */
	@Column(name = "PAYMENT_MONTH", columnDefinition = "VARCHAR")
	public java.lang.String getPaymentMonth() {
		return this.paymentMonth;
	}

	/**
	 * Author name@mail.com 设置 payment_month 的属性值
	 * 
	 * @param paymentMonth
	 *            : payment_month
	 */
	public void setPaymentMonth(java.lang.String paymentMonth) {
		this.paymentMonth = paymentMonth;
	}

	/**
	 * Author name@mail.com 获取 总笔数 的属性值
	 * 
	 * @return batchNo : 总笔数
	 */
	@Column(name = "BATCH_NO", columnDefinition = "INT")
	public java.lang.Integer getBatchNo() {
		return this.batchNo;
	}

	/**
	 * Author name@mail.com 设置 总笔数 的属性值
	 * 
	 * @param batchNo
	 *            : 总笔数
	 */
	public void setBatchNo(java.lang.Integer batchNo) {
		this.batchNo = batchNo;
	}

	/**
	 * Author name@mail.com 获取 备注 的属性值
	 * 
	 * @return memo : 备注
	 */
	@Column(name = "MEMO", columnDefinition = "VARCHAR")
	public java.lang.String getMemo() {
		return this.memo;
	}

	/**
	 * Author name@mail.com 设置 备注 的属性值
	 * 
	 * @param memo
	 *            : 备注
	 */
	public void setMemo(java.lang.String memo) {
		this.memo = memo;
	}

	/**
	 * Author name@mail.com 获取 付款账号 的属性值
	 * 
	 * @return payAccountNo : 付款账号
	 */
	@Column(name = "PAY_ACCOUNT_NO", columnDefinition = "VARCHAR")
	public java.lang.String getPayAccountNo() {
		return this.payAccountNo;
	}

	/**
	 * Author name@mail.com 设置 付款账号 的属性值
	 * 
	 * @param payAccountNo
	 *            : 付款账号
	 */
	public void setPayAccountNo(java.lang.String payAccountNo) {
		this.payAccountNo = payAccountNo;
	}

	/**
	 * Author name@mail.com 获取 付款用户对应的支付宝用户 的属性值
	 * 
	 * @return payUserId : 付款用户对应的支付宝用户
	 */
	@Column(name = "PAY_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getPayUserId() {
		return this.payUserId;
	}

	/**
	 * Author name@mail.com 设置 付款用户对应的支付宝用户 的属性值
	 * 
	 * @param payUserId
	 *            : 付款用户对应的支付宝用户
	 */
	public void setPayUserId(java.lang.Integer payUserId) {
		this.payUserId = payUserId;
	}

	/**
	 * Author name@mail.com 获取 付款账号姓名 的属性值
	 * 
	 * @return payUserName : 付款账号姓名
	 */
	@Column(name = "PAY_USER_NAME", columnDefinition = "VARCHAR")
	public java.lang.String getPayUserName() {
		return this.payUserName;
	}

	/**
	 * Author name@mail.com 设置 付款账号姓名 的属性值
	 * 
	 * @param payUserName
	 *            : 付款账号姓名
	 */
	public void setPayUserName(java.lang.String payUserName) {
		this.payUserName = payUserName;
	}

	/**
	 * Author name@mail.com 获取 总计金额 的属性值
	 * 
	 * @return sumMoney : 总计金额
	 */
	@Column(name = "SUM_MONEY", columnDefinition = "DECIMAL")
	public java.math.BigDecimal getSumMoney() {
		return this.sumMoney;
	}

	/**
	 * Author name@mail.com 设置 总计金额 的属性值
	 * 
	 * @param sumMoney
	 *            : 总计金额
	 */
	public void setSumMoney(java.math.BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}

	/**
	 * Author name@mail.com 获取 audit_flag 的属性值
	 * 
	 * @return auditFlag : audit_flag
	 */
	@Column(name = "AUDIT_FLAG", columnDefinition = "INT")
	public java.lang.Integer getAuditFlag() {
		return this.auditFlag;
	}

	/**
	 * Author name@mail.com 设置 audit_flag 的属性值
	 * 
	 * @param auditFlag
	 *            : audit_flag
	 */
	public void setAuditFlag(java.lang.Integer auditFlag) {
		this.auditFlag = auditFlag;
	}

	/**
	 * Author name@mail.com 获取 audit_user_id 的属性值
	 * 
	 * @return auditUserId : audit_user_id
	 */
	@Column(name = "AUDIT_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getAuditUserId() {
		return this.auditUserId;
	}

	/**
	 * Author name@mail.com 设置 audit_user_id 的属性值
	 * 
	 * @param auditUserId
	 *            : audit_user_id
	 */
	public void setAuditUserId(java.lang.Integer auditUserId) {
		this.auditUserId = auditUserId;
	}

	/**
	 * Author name@mail.com 获取 create_user_id 的属性值
	 * 
	 * @return createUserId : create_user_id
	 */
	@Column(name = "CREATE_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getCreateUserId() {
		return this.createUserId;
	}

	/**
	 * Author name@mail.com 设置 create_user_id 的属性值
	 * 
	 * @param createUserId
	 *            : create_user_id
	 */
	public void setCreateUserId(java.lang.Integer createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Author name@mail.com 获取 timestamp 的属性值
	 * 
	 * @return timestamp : timestamp
	 */
	@Column(name = "TIMESTAMP", columnDefinition = "DATETIME")
	public java.sql.Timestamp getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Author name@mail.com 设置 timestamp 的属性值
	 * 
	 * @param timestamp
	 *            : timestamp
	 */
	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Author name@mail.com 获取 update_date 的属性值
	 * 
	 * @return updateDate : update_date
	 */
	@Column(name = "UPDATE_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * Author name@mail.com 设置 update_date 的属性值
	 * 
	 * @param updateDate
	 *            : update_date
	 */
	public void setUpdateDate(java.sql.Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Author name@mail.com 获取 update_user_id 的属性值
	 * 
	 * @return updateUserId : update_user_id
	 */
	@Column(name = "UPDATE_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUpdateUserId() {
		return this.updateUserId;
	}

	/**
	 * Author name@mail.com 设置 update_user_id 的属性值
	 * 
	 * @param updateUserId
	 *            : update_user_id
	 */
	public void setUpdateUserId(java.lang.Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * Author name@mail.com 获取 upload_date 的属性值
	 * 
	 * @return uploadDate : upload_date
	 */
	@Column(name = "UPLOAD_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUploadDate() {
		return this.uploadDate;
	}

	/**
	 * Author name@mail.com 设置 upload_date 的属性值
	 * 
	 * @param uploadDate
	 *            : upload_date
	 */
	public void setUploadDate(java.sql.Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * Author name@mail.com 获取 upload_flag 的属性值
	 * 
	 * @return uploadFlag : upload_flag
	 */
	@Column(name = "UPLOAD_FLAG", columnDefinition = "INT")
	public java.lang.Integer getUploadFlag() {
		return this.uploadFlag;
	}

	/**
	 * Author name@mail.com 设置 upload_flag 的属性值
	 * 
	 * @param uploadFlag
	 *            : upload_flag
	 */
	public void setUploadFlag(java.lang.Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	/**
	 * Author name@mail.com 获取 upload_user_id 的属性值
	 * 
	 * @return uploadUserId : upload_user_id
	 */
	@Column(name = "UPLOAD_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUploadUserId() {
		return this.uploadUserId;
	}

	/**
	 * Author name@mail.com 设置 upload_user_id 的属性值
	 * 
	 * @param uploadUserId
	 *            : upload_user_id
	 */
	public void setUploadUserId(java.lang.Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	/**
	 * Author name@mail.com 获取 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功 的属性值
	 * 
	 * @return alipayState : 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功
	 */
	@Column(name = "ALIPAY_STATE", columnDefinition = "INT")
	public java.lang.Integer getAlipayState() {
		return this.alipayState;
	}

	/**
	 * Author name@mail.com 设置 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功 的属性值
	 * 
	 * @param alipayState
	 *            : 支付宝申请状态 0.未申请 1.申请中 2.申请失败 3.申请成功
	 */
	public void setAlipayState(java.lang.Integer alipayState) {
		this.alipayState = alipayState;
	}

	/**
	 * Author name@mail.com 获取 支付宝申请信息 的属性值
	 * 
	 * @return alipayMsg : 支付宝申请信息
	 */
	@Column(name = "ALIPAY_MSG", columnDefinition = "VARCHAR")
	public java.lang.String getAlipayMsg() {
		return this.alipayMsg;
	}

	/**
	 * Author name@mail.com 设置 支付宝申请信息 的属性值
	 * 
	 * @param alipayMsg
	 *            : 支付宝申请信息
	 */
	public void setAlipayMsg(java.lang.String alipayMsg) {
		this.alipayMsg = alipayMsg;
	}

	/**
	 * Author name@mail.com 获取 付款当天日期 的属性值
	 * 
	 * @return alipayPayDate : 付款当天日期
	 */
	@Column(name = "ALIPAY_PAY_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getAlipayPayDate() {
		return this.alipayPayDate;
	}

	/**
	 * Author name@mail.com 设置 付款当天日期 的属性值
	 * 
	 * @param alipayPayDate
	 *            : 付款当天日期
	 */
	public void setAlipayPayDate(java.sql.Timestamp alipayPayDate) {
		this.alipayPayDate = alipayPayDate;
	}

	/**
	 * Author name@mail.com 获取 回调日期 的属性值
	 * 
	 * @return alipayNodifyDate : 回调日期
	 */
	@Column(name = "ALIPAY_NODIFY_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getAlipayNodifyDate() {
		return this.alipayNodifyDate;
	}

	/**
	 * Author name@mail.com 设置 回调日期 的属性值
	 * 
	 * @param alipayNodifyDate
	 *            : 回调日期
	 */
	public void setAlipayNodifyDate(java.sql.Timestamp alipayNodifyDate) {
		this.alipayNodifyDate = alipayNodifyDate;
	}

	/**
	 * Author name@mail.com 获取 实际支付总笔数 的属性值
	 * 
	 * @return actualBatchNo : 实际支付总笔数
	 */
	@Column(name = "ACTUAL_BATCH_NO", columnDefinition = "INT")
	public java.lang.Integer getActualBatchNo() {
		return this.actualBatchNo;
	}

	/**
	 * Author name@mail.com 设置 实际支付总笔数 的属性值
	 * 
	 * @param actualBatchNo
	 *            : 实际支付总笔数
	 */
	public void setActualBatchNo(java.lang.Integer actualBatchNo) {
		this.actualBatchNo = actualBatchNo;
	}

	/**
	 * Author name@mail.com 获取 实际付款总金额 的属性值
	 * 
	 * @return actualSumMoney : 实际付款总金额
	 */
	@Column(name = "ACTUAL_SUM_MONEY", columnDefinition = "DECIMAL")
	public java.math.BigDecimal getActualSumMoney() {
		return this.actualSumMoney;
	}

	/**
	 * Author name@mail.com 设置 实际付款总金额 的属性值
	 * 
	 * @param actualSumMoney
	 *            : 实际付款总金额
	 */
	public void setActualSumMoney(java.math.BigDecimal actualSumMoney) {
		this.actualSumMoney = actualSumMoney;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=Payment" + ",id=" + id + ",payFlowId=" + payFlowId + ",paymentMonth=" + paymentMonth
				+ ",batchNo=" + batchNo + ",memo=" + memo + ",payAccountNo=" + payAccountNo + ",payUserId=" + payUserId
				+ ",payUserName=" + payUserName + ",sumMoney=" + sumMoney + ",auditFlag=" + auditFlag + ",auditUserId="
				+ auditUserId + ",createUserId=" + createUserId + ",timestamp=" + timestamp + ",updateDate="
				+ updateDate + ",updateUserId=" + updateUserId + ",uploadDate=" + uploadDate + ",uploadFlag="
				+ uploadFlag + ",uploadUserId=" + uploadUserId + ",alipayState=" + alipayState + ",alipayMsg="
				+ alipayMsg + ",alipayPayDate=" + alipayPayDate + ",alipayNodifyDate=" + alipayNodifyDate
				+ ",actualBatchNo=" + actualBatchNo + ",actualSumMoney=" + actualSumMoney + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'Payment'" + ",id:'" + id + "'" + ",payFlowId:'" + payFlowId + "'" + ",paymentMonth:'"
				+ paymentMonth + "'" + ",batchNo:'" + batchNo + "'" + ",memo:'" + memo + "'" + ",payAccountNo:'"
				+ payAccountNo + "'" + ",payUserId:'" + payUserId + "'" + ",payUserName:'" + payUserName + "'"
				+ ",sumMoney:'" + sumMoney + "'" + ",auditFlag:'" + auditFlag + "'" + ",auditUserId:'" + auditUserId
				+ "'" + ",createUserId:'" + createUserId + "'" + ",timestamp:'" + timestamp + "'" + ",updateDate:'"
				+ updateDate + "'" + ",updateUserId:'" + updateUserId + "'" + ",uploadDate:'" + uploadDate + "'"
				+ ",uploadFlag:'" + uploadFlag + "'" + ",uploadUserId:'" + uploadUserId + "'" + ",alipayState:'"
				+ alipayState + "'" + ",alipayMsg:'" + alipayMsg + "'" + ",alipayPayDate:'" + alipayPayDate + "'"
				+ ",alipayNodifyDate:'" + alipayNodifyDate + "'" + ",actualBatchNo:'" + actualBatchNo + "'"
				+ ",actualSumMoney:'" + actualSumMoney + "'" + "}";
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "payment", cascade = CascadeType.ALL)
	public Set<PaymentItem> getPaymentItemList() {
		return paymentItemList;
	}

	public void setPaymentItemList(Set<PaymentItem> PaymentItemList) {
		paymentItemList = PaymentItemList;
	}
}
