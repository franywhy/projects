package com.izhubo.model;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

public class BonusDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7019753794512100603L;
	/** 时间 */
	private java.sql.Date optime;
	/** 发出红包数 */
	private java.math.BigInteger open_bonus;
	/** 奖金数 */
	private java.math.BigDecimal bonus_count;

	
	
	
	public BonusDto(Date optime) {
		super();
		this.optime = optime;
		this.open_bonus = new BigInteger("0");
		this.bonus_count = new BigDecimal(0);
	}

	public BonusDto() {
		super();
	}

	public BonusDto(Date optime, BigInteger open_bonus, BigDecimal bonus_count) {
		super();
		this.optime = optime;
		this.open_bonus = open_bonus;
		this.bonus_count = bonus_count;
	}

	public java.math.BigInteger getOpen_bonus() {
		return open_bonus;
	}

	public void setOpen_bonus(java.math.BigInteger open_bonus) {
		this.open_bonus = open_bonus;
	}

	public java.math.BigDecimal getBonus_count() {
		return bonus_count;
	}

	public void setBonus_count(java.math.BigDecimal bonus_count) {
		this.bonus_count = bonus_count;
	}

	public java.sql.Date getOptime() {
		return optime;
	}

	public void setOptime(java.sql.Date optime) {
		this.optime = optime;
	}

	@Override
	public String toString() {
		return "BonusVO [optime=" + optime + ", open_bonus=" + open_bonus
				+ ", bonus_count=" + bonus_count + "]";
	}

}
