package com.hqjy.pay;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class BorrowMoneyEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// id
	private Integer id;
	/*
	 * 状态:-100 系统繁忙，请稍候再试 0 请求成功 40001 参数必填参数 40002 签名验证失败 40003 业务处理失败
	 */
	private String code;
	
	// 订单号
	private String tradeNo;
	/*
	 * 订单状态:0 待审批 -3000 订单取消 -1000 订单拒绝 2000 订单打回 5000 审批通过 7000 已放款 8000 提前结清
	 * 9000 正常结清
	 */
	private String orderStatus;
	// 订单状态中文描述
	private String orderStatusDesc;
	// 备注
	private String remark;
	// 姓名
	private String idName;
	// 身份证号
	private String idNo;
	// 手机号码
	private String phoneNo;
	// 贷款机构名称
	private String capitalName;
	// 申请贷款金额
	private BigDecimal applyAmount;
	// 申请时间
	private String applyTime;
	// 商品内容
	private String commodity;
	// 所属校区
	private String schoolZone;
	// 放款订单号
	private String lendingTradeNo;
	// 放款时间
	private String lendingTime;
	// 放款金额
	private BigDecimal lendingAmount;
	// 借款接口json
	private String bmJson;
	//报错信息
	private String error;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;
	
	//申请通过时间
    private String approveTime;

	/**
	 * 设置：id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	public String getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}

	/**
	 * 获取：id
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：状态:-100 系统繁忙，请稍候再试 0 请求成功 40001 参数必填参数 40002 签名验证失败 40003 业务处理失败
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取：状态:-100 系统繁忙，请稍候再试 0 请求成功 40001 参数必填参数 40002 签名验证失败 40003 业务处理失败
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置：订单号
	 */
	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	/**
	 * 获取：订单号
	 */
	public String getTradeNo() {
		return tradeNo;
	}

	/**
	 * 设置：订单状态:0 待审批 -3000 订单取消 -1000 订单拒绝 2000 订单打回 5000 审批通过 7000 已放款 8000
	 * 提前结清 9000 正常结清
	 */
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * 获取：订单状态:0 待审批 -3000 订单取消 -1000 订单拒绝 2000 订单打回 5000 审批通过 7000 已放款 8000
	 * 提前结清 9000 正常结清
	 */
	public String getOrderStatus() {
		return orderStatus;
	}

	/**
	 * 设置：订单状态中文描述
	 */
	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	/**
	 * 获取：订单状态中文描述
	 */
	public String getOrderStatusDesc() {
		return orderStatusDesc;
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

	/**
	 * 设置：姓名
	 */
	public void setIdName(String idName) {
		this.idName = idName;
	}

	/**
	 * 获取：姓名
	 */
	public String getIdName() {
		return idName;
	}

	/**
	 * 设置：身份证号
	 */
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	/**
	 * 获取：身份证号
	 */
	public String getIdNo() {
		return idNo;
	}

	/**
	 * 设置：手机号码
	 */
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	/**
	 * 获取：手机号码
	 */
	public String getPhoneNo() {
		return phoneNo;
	}

	/**
	 * 设置：贷款机构名称
	 */
	public void setCapitalName(String capitalName) {
		this.capitalName = capitalName;
	}

	/**
	 * 获取：贷款机构名称
	 */
	public String getCapitalName() {
		return capitalName;
	}

	/**
	 * 设置：申请贷款金额
	 */
	public void setApplyAmount(BigDecimal applyAmount) {
		this.applyAmount = applyAmount;
	}

	/**
	 * 获取：申请贷款金额
	 */
	public BigDecimal getApplyAmount() {
		return applyAmount;
	}

	/**
	 * 设置：申请时间
	 */
	

	/**
	 * 设置：商品内容
	 */
	public void setCommodity(String commodity) {
		this.commodity = commodity;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}

	/**
	 * 获取：商品内容
	 */
	public String getCommodity() {
		return commodity;
	}

	/**
	 * 设置：所属校区
	 */
	public void setSchoolZone(String schoolZone) {
		this.schoolZone = schoolZone;
	}

	/**
	 * 获取：所属校区
	 */
	public String getSchoolZone() {
		return schoolZone;
	}

	/**
	 * 设置：放款订单号
	 */
	public void setLendingTradeNo(String lendingTradeNo) {
		this.lendingTradeNo = lendingTradeNo;
	}

	/**
	 * 获取：放款订单号
	 */
	public String getLendingTradeNo() {
		return lendingTradeNo;
	}

	/**
	 * 设置：放款时间
	 */
	

	/**
	 * 设置：放款金额
	 */
	public void setLendingAmount(BigDecimal lendingAmount) {
		this.lendingAmount = lendingAmount;
	}

	public String getLendingTime() {
		return lendingTime;
	}

	public void setLendingTime(String lendingTime) {
		this.lendingTime = lendingTime;
	}

	/**
	 * 获取：放款金额
	 */
	public BigDecimal getLendingAmount() {
		return lendingAmount;
	}

	/**
	 * 设置：借款接口json
	 */
	public void setBmJson(String bmJson) {
		this.bmJson = bmJson;
	}

	/**
	 * 获取：借款接口json
	 */
	public String getBmJson() {
		return bmJson;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
}
