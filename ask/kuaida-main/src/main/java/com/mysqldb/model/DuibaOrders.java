package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: �����-MagicalTools
 * Date: 2016-04-19 11:43:25
 */
@Entity
@Table(name = "duiba_orders")
@org.hibernate.annotations.Table(appliesTo = "duiba_orders", comment = "duiba_orders")
public class DuibaOrders implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * duiba_orders
	 */
	public static final String REF="DuibaOrders";

	/**
	 * id ��������
	 */
	public static final String PROP_ID="id";
	
	/**
	 * app_id ��������
	 */
	public static final String PROP_APPID="appId";
	
	/**
	 * user_id ��������
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * credits ��������
	 */
	public static final String PROP_CREDITS="credits";
	
	/**
	 * actual_price ��������
	 */
	public static final String PROP_ACTUALPRICE="actualPrice";
	
	/**
	 * order_num ��������
	 */
	public static final String PROP_ORDERNUM="orderNum";
	
	/**
	 * duiba_order_num ��������
	 */
	public static final String PROP_DUIBAORDERNUM="duibaOrderNum";
	
	/**
	 * order_status ��������
	 */
	public static final String PROP_ORDERSTATUS="orderStatus";
	
	/**
	 * credits_status ��������
	 */
	public static final String PROP_CREDITSSTATUS="creditsStatus";
	
	/**
	 * type ��������
	 */
	public static final String PROP_TYPE="type";
	
	/**
	 * description ��������
	 */
	public static final String PROP_DESCRIPTION="description";
	
	/**
	 * gmt_create ��������
	 */
	public static final String PROP_GMTCREATE="gmtCreate";
	
	/**
	 * gmt_modified ��������
	 */
	public static final String PROP_GMTMODIFIED="gmtModified";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * app_id
	 */
	private java.lang.Long appId;
	
	/**
	 * user_id
	 */
	private java.lang.Long userId;
	
	/**
	 * credits
	 */
	private java.lang.Long credits;
	
	/**
	 * actual_price
	 */
	private java.lang.Integer actualPrice;
	
	/**
	 * order_num
	 */
	private java.lang.String orderNum;
	
	/**
	 * duiba_order_num
	 */
	private java.lang.String duibaOrderNum;
	
	/**
	 * order_status
	 */
	private java.lang.Integer orderStatus;
	
	/**
	 * credits_status
	 */
	private java.lang.Integer creditsStatus;
	
	/**
	 * type
	 */
	private java.lang.String type;
	
	/**
	 * description
	 */
	private java.lang.String description;
	
	/**
	 * gmt_create
	 */
	private java.sql.Timestamp gmtCreate;
	
	/**
	 * gmt_modified
	 */
	private java.sql.Timestamp gmtModified;
	

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
	 * ��ȡ app_id ������ֵ
     *
	 * @return appId :  app_id 
	 */
	@Column(name = "APP_ID",columnDefinition = "BIGINT")
	public java.lang.Long getAppId(){
		return this.appId;
	}

	/**
	 * Author name@mail.com
	 * ���� app_id ������ֵ
	 *		
	 * @param appId :  app_id 
	 */
	public void setAppId(java.lang.Long appId){
		this.appId	= appId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ user_id ������ֵ
     *
	 * @return userId :  user_id 
	 */
	@Column(name = "USER_ID",columnDefinition = "BIGINT")
	public java.lang.Long getUserId(){
		return this.userId;
	}

	/**
	 * Author name@mail.com
	 * ���� user_id ������ֵ
	 *		
	 * @param userId :  user_id 
	 */
	public void setUserId(java.lang.Long userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ credits ������ֵ
     *
	 * @return credits :  credits 
	 */
	@Column(name = "CREDITS",columnDefinition = "BIGINT")
	public java.lang.Long getCredits(){
		return this.credits;
	}

	/**
	 * Author name@mail.com
	 * ���� credits ������ֵ
	 *		
	 * @param credits :  credits 
	 */
	public void setCredits(java.lang.Long credits){
		this.credits	= credits;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ actual_price ������ֵ
     *
	 * @return actualPrice :  actual_price 
	 */
	@Column(name = "ACTUAL_PRICE",columnDefinition = "INT")
	public java.lang.Integer getActualPrice(){
		return this.actualPrice;
	}

	/**
	 * Author name@mail.com
	 * ���� actual_price ������ֵ
	 *		
	 * @param actualPrice :  actual_price 
	 */
	public void setActualPrice(java.lang.Integer actualPrice){
		this.actualPrice	= actualPrice;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ order_num ������ֵ
     *
	 * @return orderNum :  order_num 
	 */
	@Column(name = "ORDER_NUM",columnDefinition = "VARCHAR")
	public java.lang.String getOrderNum(){
		return this.orderNum;
	}

	/**
	 * Author name@mail.com
	 * ���� order_num ������ֵ
	 *		
	 * @param orderNum :  order_num 
	 */
	public void setOrderNum(java.lang.String orderNum){
		this.orderNum	= orderNum;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ duiba_order_num ������ֵ
     *
	 * @return duibaOrderNum :  duiba_order_num 
	 */
	@Column(name = "DUIBA_ORDER_NUM",columnDefinition = "VARCHAR")
	public java.lang.String getDuibaOrderNum(){
		return this.duibaOrderNum;
	}

	/**
	 * Author name@mail.com
	 * ���� duiba_order_num ������ֵ
	 *		
	 * @param duibaOrderNum :  duiba_order_num 
	 */
	public void setDuibaOrderNum(java.lang.String duibaOrderNum){
		this.duibaOrderNum	= duibaOrderNum;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ order_status ������ֵ
     *
	 * @return orderStatus :  order_status 
	 */
	@Column(name = "ORDER_STATUS",columnDefinition = "INT")
	public java.lang.Integer getOrderStatus(){
		return this.orderStatus;
	}

	/**
	 * Author name@mail.com
	 * ���� order_status ������ֵ
	 *		
	 * @param orderStatus :  order_status 
	 */
	public void setOrderStatus(java.lang.Integer orderStatus){
		this.orderStatus	= orderStatus;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ credits_status ������ֵ
     *
	 * @return creditsStatus :  credits_status 
	 */
	@Column(name = "CREDITS_STATUS",columnDefinition = "INT")
	public java.lang.Integer getCreditsStatus(){
		return this.creditsStatus;
	}

	/**
	 * Author name@mail.com
	 * ���� credits_status ������ֵ
	 *		
	 * @param creditsStatus :  credits_status 
	 */
	public void setCreditsStatus(java.lang.Integer creditsStatus){
		this.creditsStatus	= creditsStatus;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ type ������ֵ
     *
	 * @return type :  type 
	 */
	@Column(name = "TYPE",columnDefinition = "VARCHAR")
	public java.lang.String getType(){
		return this.type;
	}

	/**
	 * Author name@mail.com
	 * ���� type ������ֵ
	 *		
	 * @param type :  type 
	 */
	public void setType(java.lang.String type){
		this.type	= type;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ description ������ֵ
     *
	 * @return description :  description 
	 */
	@Column(name = "DESCRIPTION",columnDefinition = "VARCHAR")
	public java.lang.String getDescription(){
		return this.description;
	}

	/**
	 * Author name@mail.com
	 * ���� description ������ֵ
	 *		
	 * @param description :  description 
	 */
	public void setDescription(java.lang.String description){
		this.description	= description;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ gmt_create ������ֵ
     *
	 * @return gmtCreate :  gmt_create 
	 */
	@Column(name = "GMT_CREATE",columnDefinition = "DATETIME")
	public java.sql.Timestamp getGmtCreate(){
		return this.gmtCreate;
	}

	/**
	 * Author name@mail.com
	 * ���� gmt_create ������ֵ
	 *		
	 * @param gmtCreate :  gmt_create 
	 */
	public void setGmtCreate(java.sql.Timestamp gmtCreate){
		this.gmtCreate	= gmtCreate;
	}
	/**
	 * Author name@mail.com
	 * ��ȡ gmt_modified ������ֵ
     *
	 * @return gmtModified :  gmt_modified 
	 */
	@Column(name = "GMT_MODIFIED",columnDefinition = "DATETIME")
	public java.sql.Timestamp getGmtModified(){
		return this.gmtModified;
	}

	/**
	 * Author name@mail.com
	 * ���� gmt_modified ������ֵ
	 *		
	 * @param gmtModified :  gmt_modified 
	 */
	public void setGmtModified(java.sql.Timestamp gmtModified){
		this.gmtModified	= gmtModified;
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ�ַ���
	 */
	public String toString(){		
		return "{ _name=DuibaOrders" + ",id=" + id +",appId=" + appId +",userId=" + userId +",credits=" + credits +",actualPrice=" + actualPrice +",orderNum=" + orderNum +",duibaOrderNum=" + duibaOrderNum +",orderStatus=" + orderStatus +",creditsStatus=" + creditsStatus +",type=" + type +",description=" + description +",gmtCreate=" + gmtCreate +",gmtModified=" + gmtModified + "}";
	}
	
	/**
	 * Author name@mail.com
	 * ת��Ϊ JSON �ַ���
	 */
	public String toJson(){
		return "{ _name:'DuibaOrders'" + ",id:'" + id + "'" +",appId:'" + appId + "'" +",userId:'" + userId + "'" +",credits:'" + credits + "'" +",actualPrice:'" + actualPrice + "'" +",orderNum:'" + orderNum + "'" +",duibaOrderNum:'" + duibaOrderNum + "'" +",orderStatus:'" + orderStatus + "'" +",creditsStatus:'" + creditsStatus + "'" +",type:'" + type + "'" +",description:'" + description + "'" +",gmtCreate:'" + gmtCreate + "'" +",gmtModified:'" + gmtModified + "'" +  "}";
	}
}
