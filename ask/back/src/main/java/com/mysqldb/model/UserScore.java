package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-03-31 11:39:35
 */
@Entity
@Table(name = "user_score")
@org.hibernate.annotations.Table(appliesTo = "user_score", comment = "user_score")
public class UserScore implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * user_score
	 */
	public static final String REF="UserScore";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * user_id 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * user_score_remain 的属性名
	 */
	public static final String PROP_USERSCOREREMAIN="userScoreRemain";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * user_id
	 */
	private java.lang.Integer userId;
	
	/**
	 * user_score_remain
	 */
	private java.math.BigDecimal userScoreRemain;
	

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
	 * 获取 user_score_remain 的属性值
     *
	 * @return userScoreRemain :  user_score_remain 
	 */
	@Column(name = "USER_SCORE_REMAIN",columnDefinition = "FLOAT")
	public java.math.BigDecimal getUserScoreRemain(){
		return this.userScoreRemain;
	}

	/**
	 * Author name@mail.com
	 * 设置 user_score_remain 的属性值
	 *		
	 * @param userScoreRemain :  user_score_remain 
	 */
	public void setUserScoreRemain(java.math.BigDecimal userScoreRemain){
		this.userScoreRemain	= userScoreRemain;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=UserScore" + ",id=" + id +",userId=" + userId +",userScoreRemain=" + userScoreRemain + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'UserScore'" + ",id:'" + id + "'" +",userId:'" + userId + "'" +",userScoreRemain:'" + userScoreRemain + "'" +  "}";
	}
}
