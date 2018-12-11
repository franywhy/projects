package com.mysqldb.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 学习建议表
 * @author yanzhicheng 
 * @Time 2017年2月23日22:32:16
 */
@Entity
@Table(name = "credit_learning_tip")
@org.hibernate.annotations.Table(appliesTo = "credit_learning_tip", comment = "credit_learning_tip")
public class CreditLearningTip {

	/**
	 * id
	 */
	private Integer id;
	
	/**
	 * nc班级id
	 */
	private String nc_class_id;
	
	/**
	 * 班级名称 
	 */
	private String class_name;
	
	
	/**
	 * nc班级编码
	 */
	private String class_code;
	
	/**
	 * 学习建议
	 */
	private String learning_tip;
	
	/**
	 * 时间戳
	 */
	private Date ts;
	
	/**
	 * 获取 id 的属性值
     *
	 * @return id :  id 
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID",columnDefinition = "INT")
	public Integer getId(){
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 *		
	 * @param id :  id 
	 */
	public void setId(Integer id){
		this.id	= id;
	}
	/**
	 * 获取班级NCid
	 */
	@Column(name = "nc_class_id",columnDefinition = "VARCHAR")
	public String getNc_class_id() {
		return nc_class_id;
	}
	/**
	 * 设置班级NCid
	 */
	public void setNc_class_id(String nc_class_id) {
		this.nc_class_id = nc_class_id;
	}
	/**
	 * 获取班级名称
	 */
	@Column(name = "class_name",columnDefinition = "VARCHAR")
	public String getClass_name() {
		return class_name;
	}
	/**
	 * 设置班级名称
	 */
	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}
	/**
	 * 获班级编码
	 */
	@Column(name = "class_code",columnDefinition = "VARCHAR")
	public String getClass_code() {
		return class_code;
	}
	/**
	 * 设置取班级编码
	 */
	public void setClass_code(String class_code) {
		this.class_code = class_code;
	}
	/**
	 * 获取学习建议
	 */
	@Column(name = "learning_tip",columnDefinition = "VARCHAR")
	public String getLearning_tip() {
		return learning_tip;
	}
	/**
	 * 设置学习建议
	 */
	public void setLearning_tip(String learning_tip) {
		this.learning_tip = learning_tip;
	}

	

	/**
	 * 获取 时间戳
	 */
	@Column(name = "TS",insertable = false,updatable=false,columnDefinition = "DATETIME")
	public Date getTs(){
		return this.ts;
	}

	/**
	 * 设置 时间戳
	 */
	public void setTs(Date ts){
		this.ts	= ts;
	}
	
	
	public String returnTable(){
		return "credit_learning_tip";
	}
	
}
