package com.izhubo.web.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OrderDiscountUnUseListDto implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -729314703610256575L;
	private int id;
	/** 活动名称 */
	@ApiModelProperty(value = "活动名称")
	private String name;
	
	/** 优惠金额 */
	@ApiModelProperty(value = "优惠金额")
	private Double money;
	
	/** 消费限制 消费满多少元以上 */
	@ApiModelProperty(value = "消费限制 消费满多少元以上")
	private Double discount_price;
	

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


	@Override
	public String toString() {
		return "OrderDiscountUnUseListDto [id=" + id + ", name=" + name
				+ ", money=" + money + ", discount_price=" + discount_price
				+ "]";
	}
	
	

}
