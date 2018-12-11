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

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Author: 汤垲峰-MagicalTools Date: 2016-05-11 11:07:05
 */
@Entity
@Table(name = "discount_user")
@org.hibernate.annotations.Table(appliesTo = "discount_user", comment = "discount_user")
public class DiscountUser implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * discount_user
	 */
	public static final String REF = "DiscountUser";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * 优惠码id 的属性名
	 */
	public static final String PROP_DISCOUNTID = "discountId";

	/**
	 * discount_code 的属性名
	 */
	public static final String PROP_DISCOUNTCODE = "discountCode";

	/**
	 * 手机号码 的属性名
	 */
	public static final String PROP_PHONE = "phone";

	/**
	 * 优惠码领取时间 的属性名
	 */
	public static final String PROP_GETTIME = "getTime";

	/**
	 * 是否已使用 0.否 1.是 的属性名
	 */
	public static final String PROP_ISUSE = "isUse";

	/**
	 * 优惠券使用日期 的属性名
	 */
	public static final String PROP_USETIME = "useTime";

	/**
	 * 优惠券失效日期 的属性名
	 */
	public static final String PROP_DISCOUNTENDTIME = "discountEndTime";

	/**
	 * 优惠券有效起始日期 的属性名
	 */
	public static final String PROP_DISCOUNTSTARTTIME = "discountStartTime";

	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * 优惠码id
	 */
	private java.lang.Integer discountId;

	/**
	 * discount_code
	 */
	private java.lang.Integer discountCode;

	/**
	 * 手机号码
	 */
	private java.lang.String phone;

	/**
	 * 优惠码领取时间
	 */
	private java.sql.Timestamp getTime;

	/**
	 * 是否已使用 0.否 1.是
	 */
	private java.lang.Integer isUse;

	/**
	 * 优惠券使用日期
	 */
	private java.sql.Timestamp useTime;

	/**
	 * 优惠券失效日期
	 */
	private java.sql.Timestamp discountEndTime;

	/**
	 * 优惠券有效起始日期
	 */
	private java.sql.Timestamp discountStartTime;

	private Discount discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DISCOUNT_ID")
	@JsonIgnore
	public Discount getDiscount() {
		return discount;
	}

	public void setDiscount(Discount discount) {
		this.discount = discount;
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
	 * Author name@mail.com 获取 优惠码id 的属性值
	 * 
	 * @return discountId : 优惠码id
	 */
	 @Column(name = "DISCOUNT_ID",columnDefinition = "INT", insertable=false, updatable=false)
	public java.lang.Integer getDiscountId() {
		return this.discountId;
	}

	/**
	 * Author name@mail.com 设置 优惠码id 的属性值
	 * 
	 * @param discountId
	 *            : 优惠码id
	 */
	public void setDiscountId(java.lang.Integer discountId) {
		this.discountId = discountId;
	}

	/**
	 * Author name@mail.com 获取 discount_code 的属性值
	 * 
	 * @return discountCode : discount_code
	 */
	@Column(name = "DISCOUNT_CODE", columnDefinition = "INT")
	public java.lang.Integer getDiscountCode() {
		return this.discountCode;
	}

	/**
	 * Author name@mail.com 设置 discount_code 的属性值
	 * 
	 * @param discountCode
	 *            : discount_code
	 */
	public void setDiscountCode(java.lang.Integer discountCode) {
		this.discountCode = discountCode;
	}

	/**
	 * Author name@mail.com 获取 手机号码 的属性值
	 * 
	 * @return phone : 手机号码
	 */
	@Column(name = "PHONE", columnDefinition = "VARCHAR")
	public java.lang.String getPhone() {
		return this.phone;
	}

	/**
	 * Author name@mail.com 设置 手机号码 的属性值
	 * 
	 * @param phone
	 *            : 手机号码
	 */
	public void setPhone(java.lang.String phone) {
		this.phone = phone;
	}

	/**
	 * Author name@mail.com 获取 优惠码领取时间 的属性值
	 * 
	 * @return getTime : 优惠码领取时间
	 */
	@Column(name = "GET_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getGetTime() {
		return this.getTime;
	}

	/**
	 * Author name@mail.com 设置 优惠码领取时间 的属性值
	 * 
	 * @param getTime
	 *            : 优惠码领取时间
	 */
	public void setGetTime(java.sql.Timestamp getTime) {
		this.getTime = getTime;
	}

	/**
	 * Author name@mail.com 获取 是否已使用 0.否 1.是 的属性值
	 * 
	 * @return isUse : 是否已使用 0.否 1.是
	 */
	@Column(name = "IS_USE", columnDefinition = "INT")
	public java.lang.Integer getIsUse() {
		return this.isUse;
	}

	/**
	 * Author name@mail.com 设置 是否已使用 0.否 1.是 的属性值
	 * 
	 * @param isUse
	 *            : 是否已使用 0.否 1.是
	 */
	public void setIsUse(java.lang.Integer isUse) {
		this.isUse = isUse;
	}

	/**
	 * Author name@mail.com 获取 优惠券使用日期 的属性值
	 * 
	 * @return useTime : 优惠券使用日期
	 */
	@Column(name = "USE_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUseTime() {
		return this.useTime;
	}

	/**
	 * Author name@mail.com 设置 优惠券使用日期 的属性值
	 * 
	 * @param useTime
	 *            : 优惠券使用日期
	 */
	public void setUseTime(java.sql.Timestamp useTime) {
		this.useTime = useTime;
	}

	/**
	 * Author name@mail.com 获取 优惠券失效日期 的属性值
	 * 
	 * @return discountEndTime : 优惠券失效日期
	 */
	@Column(name = "DISCOUNT_END_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getDiscountEndTime() {
		return this.discountEndTime;
	}

	/**
	 * Author name@mail.com 设置 优惠券失效日期 的属性值
	 * 
	 * @param discountEndTime
	 *            : 优惠券失效日期
	 */
	public void setDiscountEndTime(java.sql.Timestamp discountEndTime) {
		this.discountEndTime = discountEndTime;
	}

	/**
	 * Author name@mail.com 获取 优惠券有效起始日期 的属性值
	 * 
	 * @return discountStartTime : 优惠券有效起始日期
	 */
	@Column(name = "DISCOUNT_START_TIME", columnDefinition = "DATETIME")
	public java.sql.Timestamp getDiscountStartTime() {
		return this.discountStartTime;
	}

	/**
	 * Author name@mail.com 设置 优惠券有效起始日期 的属性值
	 * 
	 * @param discountStartTime
	 *            : 优惠券有效起始日期
	 */
	public void setDiscountStartTime(java.sql.Timestamp discountStartTime) {
		this.discountStartTime = discountStartTime;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=DiscountUser" + ",id=" + id + ",discountId="
				+ discountId + ",discountCode=" + discountCode + ",phone="
				+ phone + ",getTime=" + getTime + ",isUse=" + isUse
				+ ",useTime=" + useTime + ",discountEndTime=" + discountEndTime
				+ ",discountStartTime=" + discountStartTime + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'DiscountUser'" + ",id:'" + id + "'" + ",discountId:'"
				+ discountId + "'" + ",discountCode:'" + discountCode + "'"
				+ ",phone:'" + phone + "'" + ",getTime:'" + getTime + "'"
				+ ",isUse:'" + isUse + "'" + ",useTime:'" + useTime + "'"
				+ ",discountEndTime:'" + discountEndTime + "'"
				+ ",discountStartTime:'" + discountStartTime + "'" + "}";
	}
}
