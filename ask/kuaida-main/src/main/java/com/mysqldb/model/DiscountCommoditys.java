package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
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
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * �Ż���id ��������
	 */
	public static final String PROP_DISCOUNTID="discountId";
	
	/**
	 * ��Ʒid ��������
	 */
	public static final String PROP_COMMODITYID="commodityId";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * �Ż���id
	 */
	private java.lang.Integer discountId;
	
	/**
	 * ��Ʒid
	 */
	private java.lang.String commodityId;
	

	/**
	 * Author name@mail.com
	 * ��ȡ id ������ֵ
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
	 * ���� id ������ֵ
	 *		
	 * @param id :  id 
	 */
	public void setId(java.lang.Integer id){
		this.id	= id;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ �Ż���id ������ֵ
     *
	 * @return discountId :  �Ż���id 
	 */
	@Column(name = "DISCOUNT_ID",columnDefinition = "INT")
	public java.lang.Integer getDiscountId(){
		return this.discountId;
	}

	/**
	 * Author name@mail.com
	 * ���� �Ż���id ������ֵ
	 *		
	 * @param discountId :  �Ż���id 
	 */
	public void setDiscountId(java.lang.Integer discountId){
		this.discountId	= discountId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ ��Ʒid ������ֵ
     *
	 * @return commodityId :  ��Ʒid 
	 */
	@Column(name = "COMMODITY_ID",columnDefinition = "VARCHAR")
	public java.lang.String getCommodityId(){
		return this.commodityId;
	}

	/**
	 * Author name@mail.com
	 * ���� ��Ʒid ������ֵ
	 *		
	 * @param commodityId :  ��Ʒid 
	 */
	public void setCommodityId(java.lang.String commodityId){
		this.commodityId	= commodityId;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=DiscountCommoditys" + ",id=" + id +",discountId=" + discountId +",commodityId=" + commodityId + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'DiscountCommoditys'" + ",id:'" + id + "'" +",discountId:'" + discountId + "'" +",commodityId:'" + commodityId + "'" +  "}";
	}
}
