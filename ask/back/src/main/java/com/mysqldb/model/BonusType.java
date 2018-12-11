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
 * Author: 汤垲峰-MagicalTools
 * Date: 2015-08-05 10:18:16
 */
@Entity
@Table(name = "bonus_type")
@org.hibernate.annotations.Table(appliesTo = "bonus_type", comment = "bonus_type")
public class BonusType implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * bonus_type
	 */
	public static final String REF = "BonusType";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * main_id 的属性名
	 */
	public static final String PROP_MAINID = "mainId";

	/**
	 * mlevel 的属性名
	 */
	public static final String PROP_MLEVEL = "mlevel";

	/**
	 * mmoney 的属性名
	 */
	public static final String PROP_MMONEY = "mmoney";

	/**
	 * mtemplate 的属性名
	 */
	public static final String PROP_MTEMPLATE = "mtemplate";

	/**
	 * mweight 的属性名
	 */
	public static final String PROP_MWEIGHT = "mweight";

	/**
	 * quantity 的属性名
	 */
	public static final String PROP_QUANTITY = "quantity";

	/**
	 * vip权重 的属性名
	 */
	public static final String PROP_VWEIGHT = "vweight";

	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * main_id
	 */
	private BonusTypeMain bonusTypeMain;

	/**
	 * mlevel
	 */
	private java.lang.String mlevel;

	/**
	 * mmoney
	 */
	private java.math.BigDecimal mmoney;

	/**
	 * mtemplate
	 */
	private java.lang.Integer mtemplate;

	/**
	 * mweight
	 */
	private java.lang.Integer mweight;

	/**
	 * quantity
	 */
	private java.lang.Integer quantity;

	/**
	 * vip权重
	 */
	private java.lang.Integer vweight;

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
	 * Author name@mail.com 获取 mlevel 的属性值
	 * 
	 * @return mlevel : mlevel
	 */
	@Column(name = "MLEVEL", columnDefinition = "VARCHAR")
	public java.lang.String getMlevel() {
		return this.mlevel;
	}

	/**
	 * Author name@mail.com 设置 mlevel 的属性值
	 * 
	 * @param mlevel
	 *            : mlevel
	 */
	public void setMlevel(java.lang.String mlevel) {
		this.mlevel = mlevel;
	}

	/**
	 * Author name@mail.com 获取 mmoney 的属性值
	 * 
	 * @return mmoney : mmoney
	 */
	@Column(name = "MMONEY", columnDefinition = "DECIMAL")
	public java.math.BigDecimal getMmoney() {
		return this.mmoney;
	}

	/**
	 * Author name@mail.com 设置 mmoney 的属性值
	 * 
	 * @param mmoney
	 *            : mmoney
	 */
	public void setMmoney(java.math.BigDecimal mmoney) {
		this.mmoney = mmoney;
	}

	/**
	 * Author name@mail.com 获取 mtemplate 的属性值
	 * 
	 * @return mtemplate : mtemplate
	 */
	@Column(name = "MTEMPLATE", columnDefinition = "INT")
	public java.lang.Integer getMtemplate() {
		return this.mtemplate;
	}

	/**
	 * Author name@mail.com 设置 mtemplate 的属性值
	 * 
	 * @param mtemplate
	 *            : mtemplate
	 */
	public void setMtemplate(java.lang.Integer mtemplate) {
		this.mtemplate = mtemplate;
	}

	/**
	 * Author name@mail.com 获取 mweight 的属性值
	 * 
	 * @return mweight : mweight
	 */
	@Column(name = "MWEIGHT", columnDefinition = "INT")
	public java.lang.Integer getMweight() {
		return this.mweight;
	}

	/**
	 * Author name@mail.com 设置 mweight 的属性值
	 * 
	 * @param mweight
	 *            : mweight
	 */
	public void setMweight(java.lang.Integer mweight) {
		this.mweight = mweight;
	}

	/**
	 * Author name@mail.com 获取 quantity 的属性值
	 * 
	 * @return quantity : quantity
	 */
	@Column(name = "QUANTITY", columnDefinition = "INT")
	public java.lang.Integer getQuantity() {
		return this.quantity;
	}

	/**
	 * Author name@mail.com 设置 quantity 的属性值
	 * 
	 * @param quantity
	 *            : quantity
	 */
	public void setQuantity(java.lang.Integer quantity) {
		this.quantity = quantity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_ID")
	@JsonIgnore
	public BonusTypeMain getBonusTypeMain() {
		return bonusTypeMain;
	}

	public void setBonusTypeMain(BonusTypeMain bonusTypeMain) {
		this.bonusTypeMain = bonusTypeMain;
	}

	/**
	 * Author name@mail.com 获取 vip权重 的属性值
	 * 
	 * @return vweight : vip权重
	 */
	@Column(name = "VWEIGHT", columnDefinition = "INT")
	public java.lang.Integer getVweight() {
		return this.vweight;
	}

	/**
	 * Author name@mail.com 设置 vip权重 的属性值
	 * 
	 * @param vweight
	 *            : vip权重
	 */
	public void setVweight(java.lang.Integer vweight) {
		this.vweight = vweight;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=BonusType" + ",id=" + id + ",mainId="
				+ this.bonusTypeMain.getId() + ",mlevel=" + mlevel + ",mmoney="
				+ mmoney + ",mtemplate=" + mtemplate + ",mweight=" + mweight
				+ ",quantity=" + quantity + ",vweight=" + vweight + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'BonusType'" + ",id:'" + id + "'" + ",mainId:'"
				+ this.bonusTypeMain.getId() + "'" + ",mlevel:'" + mlevel + "'"
				+ ",mmoney:'" + mmoney + "'" + ",mtemplate:'" + mtemplate + "'"
				+ ",mweight:'" + mweight + "'" + ",quantity:'" + quantity + "'"
				+ ",vweight:'" + vweight + "'" + "}";
	}

}
