package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-04-19 17:55:33
 */
@Entity
@Table(name = "user_wallet_detail")
@org.hibernate.annotations.Table(appliesTo = "user_wallet_detail", comment = "user_wallet_detail")
public class UserWalletDetail implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_wallet_detail
	 */
	public static final String REF="UserWalletDetail";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 金额 的属性名
	 */
	public static final String PROP_MMONEY="mmoney";
	
	/**
	 * 获取金额的原因 的属性名
	 */
	public static final String PROP_MMONEYDETAIL="mmoneyDetail";
	
	/**
	 * 获取金额的类型，参考java枚举 的属性名
	 */
	public static final String PROP_MMONEYTYPE="mmoneyType";
	
	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * user_nickname 的属性名
	 */
	public static final String PROP_USERNICKNAME="userNickname";
	
	/**
	 * create_time 的属性名
	 */
	public static final String PROP_CREATETIME="createTime";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 金额
	 */
	private java.math.BigDecimal mmoney;
	
	/**
	 * 获取金额的原因
	 */
	private java.lang.String mmoneyDetail;
	
	/**
	 * 获取金额的类型，参考java枚举
	 */
	private java.lang.Integer mmoneyType;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * user_nickname
	 */
	private java.lang.String userNickname;
	
	/**
	 * create_time
	 */
	private java.sql.Timestamp createTime;
	

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
	 * 获取 金额 的属性值
     *
	 * @return mmoney :  金额 
	 */
	@Column(name = "MMONEY",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getMmoney(){
		return this.mmoney;
	}

	/**
	 * Author name@mail.com
	 * 设置 金额 的属性值
	 *		
	 * @param mmoney :  金额 
	 */
	public void setMmoney(java.math.BigDecimal mmoney){
		this.mmoney	= mmoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 获取金额的原因 的属性值
     *
	 * @return mmoneyDetail :  获取金额的原因 
	 */
	@Column(name = "MMONEY_DETAIL",columnDefinition = "VARCHAR")
	public java.lang.String getMmoneyDetail(){
		return this.mmoneyDetail;
	}

	/**
	 * Author name@mail.com
	 * 设置 获取金额的原因 的属性值
	 *		
	 * @param mmoneyDetail :  获取金额的原因 
	 */
	public void setMmoneyDetail(java.lang.String mmoneyDetail){
		this.mmoneyDetail	= mmoneyDetail;
	}
	/**
	 * Author name@mail.com
	 * 获取 获取金额的类型，参考java枚举 的属性值
     *
	 * @return mmoneyType :  获取金额的类型，参考java枚举 
	 */
	@Column(name = "MMONEY_TYPE",columnDefinition = "INT")
	public java.lang.Integer getMmoneyType(){
		return this.mmoneyType;
	}

	/**
	 * Author name@mail.com
	 * 设置 获取金额的类型，参考java枚举 的属性值
	 *		
	 * @param mmoneyType :  获取金额的类型，参考java枚举 
	 */
	public void setMmoneyType(java.lang.Integer mmoneyType){
		this.mmoneyType	= mmoneyType;
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
	 * 获取 user_nickname 的属性值
     *
	 * @return userNickname :  user_nickname 
	 */
	@Column(name = "USER_NICKNAME",columnDefinition = "VARCHAR")
	public java.lang.String getUserNickname(){
		return this.userNickname;
	}

	/**
	 * Author name@mail.com
	 * 设置 user_nickname 的属性值
	 *		
	 * @param userNickname :  user_nickname 
	 */
	public void setUserNickname(java.lang.String userNickname){
		this.userNickname	= userNickname;
	}
	/**
	 * Author name@mail.com
	 * 获取 create_time 的属性值
     *
	 * @return createTime :  create_time 
	 */
	@Column(name = "CREATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 create_time 的属性值
	 *		
	 * @param createTime :  create_time 
	 */
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime	= createTime;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=UserWalletDetail" + ",id=" + id +",mmoney=" + mmoney +",mmoneyDetail=" + mmoneyDetail +",mmoneyType=" + mmoneyType +",userId=" + userId +",userNickname=" + userNickname +",createTime=" + createTime + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'UserWalletDetail'" + ",id:'" + id + "'" +",mmoney:'" + mmoney + "'" +",mmoneyDetail:'" + mmoneyDetail + "'" +",mmoneyType:'" + mmoneyType + "'" +",userId:'" + userId + "'" +",userNickname:'" + userNickname + "'" +",createTime:'" + createTime + "'" +  "}";
	}
}
