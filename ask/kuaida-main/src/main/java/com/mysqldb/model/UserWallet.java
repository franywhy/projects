package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-04-19 17:55:29
 */
@Entity
@Table(name = "user_wallet")
@org.hibernate.annotations.Table(appliesTo = "user_wallet", comment = "user_wallet")
public class UserWallet implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_wallet
	 */
	public static final String REF="UserWallet";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * 收益的总金额-累计提现总金额 的属性名
	 */
	public static final String PROP_USERBALANCE="userBalance";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * 收益的总金额-累计提现总金额
	 */
	private java.math.BigDecimal userBalance;
	

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
	 * 获取 user_id 的属性值
     *
	 * @return userId :  user_id 
	 */
	@Column(name = "USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 * Author name@mail.com
	 * 设置 user_id 的属性值
	 *		
	 * @param userId :  user_id 
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * 获取 收益的总金额-累计提现总金额 的属性值
     *
	 * @return userBalance :  收益的总金额-累计提现总金额 
	 */
	@Column(name = "USER_BALANCE",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getUserBalance(){
		return this.userBalance;
	}

	/**
	 * Author name@mail.com
	 * 设置 收益的总金额-累计提现总金额 的属性值
	 *		
	 * @param userBalance :  收益的总金额-累计提现总金额 
	 */
	public void setUserBalance(java.math.BigDecimal userBalance){
		this.userBalance	= userBalance;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=UserWallet" + ",id=" + id +",userId=" + userId +",userBalance=" + userBalance + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'UserWallet'" + ",id:'" + id + "'" +",userId:'" + userId + "'" +",userBalance:'" + userBalance + "'" +  "}";
	}
}
