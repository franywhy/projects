package io.renren.utils.bmutils.model;



import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;


/**
 * @author pan
 *
 */
public class HdBizLoanInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@JSONField(name="apply_amount")  
	private String applyAmount;

	@JSONField(name="apply_time")  
	private String applyTime;
	
	@JSONField(name="capital_name")  
	private String capitalName;

	@JSONField(name="commodity")  
	private String commodity;

	@JSONField(name="id_name")  
	private String idName;

	@JSONField(name="id_no")  
	private String idNo;
	
	@JSONField(name="lending_trade_no")  
	private String lendingTradeNo;
	
	@JSONField(name="lending_time")  
	private String lendingTime;
	
	@JSONField(name="lending_amount")  
	private String lendingAmount;
	@JSONField(name="trade_no")  
	private String tradeNo;
	
	@JSONField(name="order_status")  
	private String orderStatus;
	
	@JSONField(name="order_status_desc")  
	private String orderStatusDesc;
	
	@JSONField(name="approve_time")  
	private String approveTime;

	@JSONField(name="phone_no")  
	private String phoneNo;
	
	@JSONField(name="remark")  
	private String remark;
	
	@JSONField(name="school_zone")  
	private String schoolZone;

	public String getApplyAmount() {
		return applyAmount;
	}

	public void setApplyAmount(String applyAmount) {
		this.applyAmount = applyAmount;
	}

	public String getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}



	public String getApproveTime() {
		return approveTime;
	}

	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}

	public String getCommodity() {
		return commodity;
	}

	public void setCommodity(String commodity) {
		this.commodity = commodity;
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

	public String getLendingTradeNo() {
		return lendingTradeNo;
	}

	public void setLendingTradeNo(String lendingTradeNo) {
		this.lendingTradeNo = lendingTradeNo;
	}

	public String getLendingTime() {
		return lendingTime;
	}

	public void setLendingTime(String lendingTime) {
		this.lendingTime = lendingTime;
	}

	public String getLendingAmount() {
		return lendingAmount;
	}

	public void setLendingAmount(String lendingAmount) {
		this.lendingAmount = lendingAmount;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusDesc() {
		return orderStatusDesc;
	}

	public void setOrderStatusDesc(String orderStatusDesc) {
		this.orderStatusDesc = orderStatusDesc;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSchoolZone() {
		return schoolZone;
	}

	public void setSchoolZone(String schoolZone) {
		this.schoolZone = schoolZone;
	}

	public String getCapitalName() {
		return capitalName;
	}

	public void setCapitalName(String capitalName) {
		this.capitalName = capitalName;
	}

	
}
