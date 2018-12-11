package com.mysqldb.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 学分制自定义档案
 * 
 * @author lintf
 * @Time 2017年11月13日14:23:32
 */
@Entity
@Table(name = "credit_def")
@org.hibernate.annotations.Table(appliesTo = "credit_def", comment = "credit_def")
public class CreditDef implements Serializable {

	private static final long serialVersionUID = 1L;

	public CreditDef() {

	}

	/**
	 * id
	 */
	private Integer id;
	/**
	 * 同类ID
	 */
	private String defList;
	/**
	 * 名称
	 */
	private String defName;
	/**
	 * 每一个的唯一id
	 */
	private String defCode;
	/**
	 * 定义类的pk内容
	 */
	private String defKey;
	/**
	 * 同类的名称
	 */
	private String defListName;

	/**
	 * 1 是删除 0是不删除
	 */
	private Integer dr = 1;
	/**
	 * NC中的编号
	 */
	private String ncCode;

	/**
	 * 获取 id 的属性值
	 * 
	 * @return id : id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID", columnDefinition = "INT")
	public Integer getId() {
		return this.id;
	}

	/**
	 * 设置 id 的属性值
	 * 
	 * @param id
	 *            : id
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	@Column(name = "def_list",columnDefinition = "VARCHAR")
	public String getDefList() {
		return defList;
	}

	/**
	 * 设置同一项的ID
	 * 
	 * @param defList
	 */
	public void setDefList(String defList) {
		this.defList = defList;
	}
	@Column(name = "def_name",columnDefinition = "VARCHAR")
	public String getDefName() {
		return defName;
	}

	public void setDefName(String defName) {
		this.defName = defName;
	}
	@Column(name = "def_key",columnDefinition = "VARCHAR")
	public String getDefKey() {
		return defKey;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}
	@Column(name = "def_list_name",columnDefinition = "VARCHAR")
	public String getDefListName() {
		return defListName;
	}

	public void setDefListName(String defListName) {
		this.defListName = defListName;
	}

	 
	@Column(name = "def_code",columnDefinition = "VARCHAR")
	public String getDefCode() {
		return defCode;
	}

	public void setDefCode(String defCode) {
		this.defCode = defCode;
	}
	
	
	@Column(name = "nc_code",columnDefinition = "VARCHAR")
	public String getNcCode() {
		return ncCode;
	}

	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}
	
	
	

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	
	/**
	 * 获取dr
	 */
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
	}
}
