package com.izhubo.web.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserInfomyDiscountUnUseListDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729314703610256575L;
	private int id;
	/** 活动名称 */
	@ApiModelProperty(value = "活动名称")
	private String name;
	
	/** 活动开始日期 */
	@ApiModelProperty(value = "活动开始日期")
	private java.sql.Timestamp start_time;
	
	/** 活动失效日期 */
	@ApiModelProperty(value = "活动失效日期")
	private java.sql.Timestamp end_time;
	
	/** 优惠券code */
	@ApiModelProperty(value = "优惠券code")
	private Integer discount_code;
	
	/** 优惠金额 */
	@ApiModelProperty(value = "优惠金额")
	private Double money;
	
	/** 消费限制 消费满多少元以上 */
	@ApiModelProperty(value = "消费限制 消费满多少元以上")
	private Double discount_price;
	
	/** 使用品台 0.不限制 1.恒企在线 2.会答APP */
	@ApiModelProperty(value = "使用品台 0.不限制 1.恒企在线 2.会答APP")
	private Integer work_type;

	
	
	

	public Integer getDiscount_code() {
		return discount_code;
	}

	public void setDiscount_code(Integer discount_code) {
		this.discount_code = discount_code;
	}

	public java.sql.Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(java.sql.Timestamp start_time) {
		this.start_time = start_time;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public java.sql.Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(java.sql.Timestamp end_time) {
		this.end_time = end_time;
	}

	public Double getMoney() {
		return money;
	}

	public void setMoney(Double money) {
		this.money = money;
	}

	public Double getDiscount_price() {
		return discount_price;
	}

	public void setDiscount_price(Double discount_price) {
		this.discount_price = discount_price;
	}

	public Integer getWork_type() {
		return work_type;
	}

	public void setWork_type(Integer work_type) {
		this.work_type = work_type;
	}

	@Override
	public String toString() {
		return "UserInfomyDiscountUnUseListDto [id=" + id + ", name=" + name
				+ ", start_time=" + start_time + ", end_time=" + end_time
				+ ", discount_code=" + discount_code + ", money=" + money
				+ ", discount_price=" + discount_price + ", work_type="
				+ work_type + "]";
	}
	
	

}
