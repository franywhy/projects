package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-01-22 15:01:04
 */
@Entity
@Table(name = "user_demo")
@org.hibernate.annotations.Table(appliesTo = "user_demo", comment = "demo表")
public class UserDemo implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * demo表
	 */
	public static final String REF="UserDemo";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 昵称 的属性名
	 */
	public static final String PROP_USERNAME="userName";
	
	/**
	 * 日期 的属性名
	 */
	public static final String PROP_UTIMESTAMP="utimestamp";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 昵称
	 */
	private java.lang.String userName;
	
	/**
	 * 日期
	 */
	private java.sql.Timestamp utimestamp;
	

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
	 * 获取 昵称 的属性值
     *
	 * @return userName :  昵称 
	 */
	@Column(name = "USER_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getUserName(){
		return this.userName;
	}

	/**
	 * Author name@mail.com
	 * 设置 昵称 的属性值
	 *		
	 * @param userName :  昵称 
	 */
	public void setUserName(java.lang.String userName){
		this.userName	= userName;
	}
	/**
	 * Author name@mail.com
	 * 获取 日期 的属性值
     *
	 * @return utimestamp :  日期 
	 */
	@Column(name = "UTIMESTAMP",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUtimestamp(){
		return this.utimestamp;
	}

	/**
	 * Author name@mail.com
	 * 设置 日期 的属性值
	 *		
	 * @param utimestamp :  日期 
	 */
	public void setUtimestamp(java.sql.Timestamp utimestamp){
		this.utimestamp	= utimestamp;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=UserDemo" + ",id=" + id +",userName=" + userName +",utimestamp=" + utimestamp + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'UserDemo'" + ",id:'" + id + "'" +",userName:'" + userName + "'" +",utimestamp:'" + utimestamp + "'" +  "}";
	}
}
