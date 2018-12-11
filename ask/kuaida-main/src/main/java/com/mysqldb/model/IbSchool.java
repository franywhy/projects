package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-04-20 14:52:03
 */
@Entity
@Table(name = "ib_school")
@org.hibernate.annotations.Table(appliesTo = "ib_school", comment = "ib_school")
public class IbSchool implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ib_school
	 */
	public static final String REF="IbSchool";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 名称 的属性名
	 */
	public static final String PROP_NAME="name";
	
	/**
	 * address 的属性名
	 */
	public static final String PROP_ADDRESS="address";
	
	/**
	 * 经度 的属性名
	 */
	public static final String PROP_LONGITUDE="longitude";
	
	/**
	 * 纬度 的属性名
	 */
	public static final String PROP_LATITUDE="latitude";
	
	/**
	 * 偏差值 的属性名
	 */
	public static final String PROP_DEVIATION="deviation";
	
	/**
	 * 开启调试 的属性名
	 */
	public static final String PROP_ISDEBUG="isDebug";
	
	/**
	 * 添加时间 的属性名
	 */
	public static final String PROP_ADDTIME="addtime";
	
	/**
	 * 编辑时间 的属性名
	 */
	public static final String PROP_EDITTIME="edittime";
	
	/**
	 * 校区PK值 的属性名
	 */
	public static final String PROP_PKORG="pkOrg";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 名称
	 */
	private java.lang.String name;
	
	/**
	 * address
	 */
	private java.lang.String address;
	
	/**
	 * 经度
	 */
	private java.lang.String longitude;
	
	/**
	 * 纬度
	 */
	private java.lang.String latitude;
	
	/**
	 * 偏差值
	 */
	private java.lang.Integer deviation;
	
	/**
	 * 开启调试
	 */
	private java.lang.Integer isDebug;
	
	/**
	 * 添加时间
	 */
	private java.lang.Integer addtime;
	
	/**
	 * 编辑时间
	 */
	private java.lang.Integer edittime;
	
	/**
	 * 校区PK值
	 */
	private java.lang.String pkOrg;
	

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
	 * 获取 名称 的属性值
     *
	 * @return name :  名称 
	 */
	@Column(name = "NAME",columnDefinition = "VARCHAR")
	public java.lang.String getName(){
		return this.name;
	}

	/**
	 * Author name@mail.com
	 * 设置 名称 的属性值
	 *		
	 * @param name :  名称 
	 */
	public void setName(java.lang.String name){
		this.name	= name;
	}
	/**
	 * Author name@mail.com
	 * 获取 address 的属性值
     *
	 * @return address :  address 
	 */
	@Column(name = "ADDRESS",columnDefinition = "VARCHAR")
	public java.lang.String getAddress(){
		return this.address;
	}

	/**
	 * Author name@mail.com
	 * 设置 address 的属性值
	 *		
	 * @param address :  address 
	 */
	public void setAddress(java.lang.String address){
		this.address	= address;
	}
	/**
	 * Author name@mail.com
	 * 获取 经度 的属性值
     *
	 * @return longitude :  经度 
	 */
	@Column(name = "LONGITUDE",columnDefinition = "VARCHAR")
	public java.lang.String getLongitude(){
		return this.longitude;
	}

	/**
	 * Author name@mail.com
	 * 设置 经度 的属性值
	 *		
	 * @param longitude :  经度 
	 */
	public void setLongitude(java.lang.String longitude){
		this.longitude	= longitude;
	}
	/**
	 * Author name@mail.com
	 * 获取 纬度 的属性值
     *
	 * @return latitude :  纬度 
	 */
	@Column(name = "LATITUDE",columnDefinition = "VARCHAR")
	public java.lang.String getLatitude(){
		return this.latitude;
	}

	/**
	 * Author name@mail.com
	 * 设置 纬度 的属性值
	 *		
	 * @param latitude :  纬度 
	 */
	public void setLatitude(java.lang.String latitude){
		this.latitude	= latitude;
	}
	/**
	 * Author name@mail.com
	 * 获取 偏差值 的属性值
     *
	 * @return deviation :  偏差值 
	 */
	@Column(name = "DEVIATION",columnDefinition = "INT")
	public java.lang.Integer getDeviation(){
		return this.deviation;
	}

	/**
	 * Author name@mail.com
	 * 设置 偏差值 的属性值
	 *		
	 * @param deviation :  偏差值 
	 */
	public void setDeviation(java.lang.Integer deviation){
		this.deviation	= deviation;
	}
	/**
	 * Author name@mail.com
	 * 获取 开启调试 的属性值
     *
	 * @return isDebug :  开启调试 
	 */
	@Column(name = "IS_DEBUG",columnDefinition = "TINYINT")
	public java.lang.Integer getIsDebug(){
		return this.isDebug;
	}

	/**
	 * Author name@mail.com
	 * 设置 开启调试 的属性值
	 *		
	 * @param isDebug :  开启调试 
	 */
	public void setIsDebug(java.lang.Integer isDebug){
		this.isDebug	= isDebug;
	}
	/**
	 * Author name@mail.com
	 * 获取 添加时间 的属性值
     *
	 * @return addtime :  添加时间 
	 */
	@Column(name = "ADDTIME",columnDefinition = "INT")
	public java.lang.Integer getAddtime(){
		return this.addtime;
	}

	/**
	 * Author name@mail.com
	 * 设置 添加时间 的属性值
	 *		
	 * @param addtime :  添加时间 
	 */
	public void setAddtime(java.lang.Integer addtime){
		this.addtime	= addtime;
	}
	/**
	 * Author name@mail.com
	 * 获取 编辑时间 的属性值
     *
	 * @return edittime :  编辑时间 
	 */
	@Column(name = "EDITTIME",columnDefinition = "INT")
	public java.lang.Integer getEdittime(){
		return this.edittime;
	}

	/**
	 * Author name@mail.com
	 * 设置 编辑时间 的属性值
	 *		
	 * @param edittime :  编辑时间 
	 */
	public void setEdittime(java.lang.Integer edittime){
		this.edittime	= edittime;
	}
	/**
	 * Author name@mail.com
	 * 获取 校区PK值 的属性值
     *
	 * @return pkOrg :  校区PK值 
	 */
	@Column(name = "PK_ORG",columnDefinition = "VARCHAR")
	public java.lang.String getPkOrg(){
		return this.pkOrg;
	}

	/**
	 * Author name@mail.com
	 * 设置 校区PK值 的属性值
	 *		
	 * @param pkOrg :  校区PK值 
	 */
	public void setPkOrg(java.lang.String pkOrg){
		this.pkOrg	= pkOrg;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=IbSchool" + ",id=" + id +",name=" + name +",address=" + address +",longitude=" + longitude +",latitude=" + latitude +",deviation=" + deviation +",isDebug=" + isDebug +",addtime=" + addtime +",edittime=" + edittime +",pkOrg=" + pkOrg + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'IbSchool'" + ",id:'" + id + "'" +",name:'" + name + "'" +",address:'" + address + "'" +",longitude:'" + longitude + "'" +",latitude:'" + latitude + "'" +",deviation:'" + deviation + "'" +",isDebug:'" + isDebug + "'" +",addtime:'" + addtime + "'" +",edittime:'" + edittime + "'" +",pkOrg:'" + pkOrg + "'" +  "}";
	}
}
