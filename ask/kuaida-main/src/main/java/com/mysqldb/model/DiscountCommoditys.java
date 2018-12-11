package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-04-01 12:03:29
 */
@Entity
@Table(name = "discount_commoditys")
@org.hibernate.annotations.Table(appliesTo = "discount_commoditys", comment = "discount_commoditys")
public class DiscountCommoditys implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * discount_commoditys
	 */
	public static final String REF="DiscountCommoditys";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 优惠码id 的属性名
	 */
	public static final String PROP_DISCOUNTID="discountId";
	
	/**
	 * 商品id 的属性名
	 */
	public static final String PROP_COMMODITYID="commodityId";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 优惠码id
	 */
	private java.lang.Integer discountId;
	
	/**
	 * 商品id
	 */
	private java.lang.String commodityId;
	

	/**
	 * Author name@mail.com
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public java.lang.Integer getId(){
		return this.id;
	}

	/**
	 * Author name@mail.com
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(java.lang.Integer id){
		this.id	= id;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠码id 的属性值
     *
	 * @return discountId :  优惠码id 
	 */
	@Column(name = "DISCOUNT_ID",columnDefinition = "INT")
	public java.lang.Integer getDiscountId(){
		return this.discountId;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠码id 的属性值
	 *		
	 * @param discountId :  优惠码id 
	 */
	public void setDiscountId(java.lang.Integer discountId){
		this.discountId	= discountId;
	}
	/**
	 * Author name@mail.com
	 * 获取 商品id 的属性值
     *
	 * @return commodityId :  商品id 
	 */
	@Column(name = "COMMODITY_ID",columnDefinition = "VARCHAR")
	public java.lang.String getCommodityId(){
		return this.commodityId;
	}

	/**
	 * Author name@mail.com
	 * 设置 商品id 的属性值
	 *		
	 * @param commodityId :  商品id 
	 */
	public void setCommodityId(java.lang.String commodityId){
		this.commodityId	= commodityId;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=DiscountCommoditys" + ",id=" + id +",discountId=" + discountId +",commodityId=" + commodityId + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'DiscountCommoditys'" + ",id:'" + id + "'" +",discountId:'" + discountId + "'" +",commodityId:'" + commodityId + "'" +  "}";
	}
}
