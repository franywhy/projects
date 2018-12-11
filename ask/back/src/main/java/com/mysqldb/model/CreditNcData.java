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
@Table(name = "hq_allstudent")
@org.hibernate.annotations.Table(appliesTo = "hq_allstudent", comment = "hq_allstudent")
public class CreditNcData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public CreditNcData(){
		
	}
	 





	/**
	 * id
	 */
	private Integer id;
	 
	private Date ts;
 

     /**
      * 报名月份
      */
	private String studentid;	
 /**
  * 当前月份
  */
	private String subjecttype;	 
 
	/**
	 * 1 是删除 0是不删除
	 */
	private Integer dr=1;	
	private Integer type=1;	
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
	 
 
	@Column(name = "studentid",columnDefinition = "VARCHAR") 
	public String getstudentid() {
		return studentid;
	}

	public void setstudentid(String studentid) {
		this.studentid = studentid;
	}
	 /**
	  * 
	  * @return
	  */
	@Column(name = "subject_type",columnDefinition = "VARCHAR") 
	public String getsubjecttype() {
		return subjecttype;
	}

	public void setsubjecttype(String subjecttype) {
		this.subjecttype = subjecttype;
	}
	
	
	 
 
	@Column(name = "dr",columnDefinition = "INT")
	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}
	 
	@Column(name = "type",columnDefinition = "INT")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
}
