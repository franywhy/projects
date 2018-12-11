package com.izhubo.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

public class RedApplyDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7019753794512100603L;
	/** 时间 */
	private java.sql.Date optime;

	/** 已提现奖金 */
	private java.math.BigDecimal apply_money;
	
	
	
	
	public RedApplyDto(Date optime) {
		super();
		this.optime = optime;
		this.apply_money = new java.math.BigDecimal(0);
	}

	public RedApplyDto() {
		super();
	}

	public RedApplyDto(Date optime, BigDecimal apply_money) {
		super();
		this.optime = optime;
		this.apply_money = apply_money;
	}

	public java.sql.Date getOptime() {
		return optime;
	}

	public void setOptime(java.sql.Date optime) {
		this.optime = optime;
	}

	public java.math.BigDecimal getApply_money() {
		return apply_money;
	}

	public void setApply_money(java.math.BigDecimal apply_money) {
		this.apply_money = apply_money;
	}

	@Override
	public String toString() {
		return "RedApplyDto [optime=" + optime + ", apply_money=" + apply_money
				+ "]";
	}

}
