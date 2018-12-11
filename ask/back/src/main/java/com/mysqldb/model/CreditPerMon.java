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
 * 学分完成率月目标完成率单
 * @author lintf 
 * @Time 2017年11月13日14:23:32
 */
@Entity
@Table(name = "credit_percent_mon")
@org.hibernate.annotations.Table(appliesTo = "credit_percent_mon", comment = "credit_percent_mon")
public class CreditPerMon implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CreditPerMon(){
		
	}
	 





	/**
	 * id
	 */
	private Integer id;
	 
	private Date ts;
 

     /**
      * 报名月份
      */
	private String months;	
 /**
  * 当前月份
  */
	private String dbilldate;	 
	/**
	 * 学分目标完成率
	 */
	private java.math.BigDecimal  mbpercent;
	 /**
	  * 排课目标完成率
	  */
	private java.math.BigDecimal  pkmbpercent;
	
	
	/**
	 * 1 是删除 0是不删除
	 */
	private Integer dr=1;	
	 
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
	 
 
	@Column(name = "months",columnDefinition = "VARCHAR") 
	public String getMonths() {
		return months;
	}

	public void setMonths(String months) {
		this.months = months;
	}
	 /**
	  * 
	  * @return
	  */
	@Column(name = "dbilldate",columnDefinition = "VARCHAR") 
	public String getDbilldate() {
		return dbilldate;
	}

	public void setDbilldate(String dbilldate) {
		this.dbilldate = dbilldate;
	}
	
	
	@Column(name = "mbpercent",columnDefinition = "DECIMAL") 
	public java.math.BigDecimal  getMbpercent() {
		return mbpercent;
	}

	public void setMbpercent(java.math.BigDecimal  mbpercent) {
		this.mbpercent = mbpercent;
	}
	
	@Column(name = "pkmbpercent",columnDefinition = "DECIMAL") 
	public java.math.BigDecimal  getPkpercent() {
		return pkmbpercent;
	}

	public void setPkpercent(java.math.BigDecimal  pkmbpercent) {
		this.pkmbpercent = pkmbpercent;
	}
	 
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	 
	
}
