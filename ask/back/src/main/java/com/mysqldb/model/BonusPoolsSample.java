package com.mysqldb.model;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2015-08-06 11:33:55
 */
@Entity
@Table(name = "bonus_pools")
@org.hibernate.annotations.Table(appliesTo = "bonus_pools", comment = "bonus_pools")
public class BonusPoolsSample implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * bonus_pools
	 */
	public static final String REF="BonusPools";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * bonus 的属性名
	 */
	public static final String PROP_BONUS="bonus";
	
	/**
	 * year_mon 的属性名
	 */
	public static final String PROP_YEARMON="yearMon";
	
	/**
	 * audit_flag 的属性名
	 */
	public static final String PROP_AUDITFLAG="auditFlag";
	
	
	/**
	 * 引用的模板id 的属性名
	 */
	public static final String PROP_TEMPLATEID="templateId";
	
	/**
	 * audit_user_id 的属性名
	 */
	public static final String PROP_AUDITUSERID="auditUserId";
	
	/**
	 * create_user_id 的属性名
	 */
	public static final String PROP_CREATEUSERID="createUserId";
	
	/**
	 * timestamp 的属性名
	 */
	public static final String PROP_TIMESTAMP="timestamp";
	
	/**
	 * update_date 的属性名
	 */
	public static final String PROP_UPDATEDATE="updateDate";
	
	/**
	 * update_user_id 的属性名
	 */
	public static final String PROP_UPDATEUSERID="updateUserId";
	
	/**
	 * upload_date 的属性名
	 */
	public static final String PROP_UPLOADDATE="uploadDate";
	
	/**
	 * upload_flag 的属性名
	 */
	public static final String PROP_UPLOADFLAG="uploadFlag";
	
	/**
	 * upload_user_id 的属性名
	 */
	public static final String PROP_UPLOADUSERID="uploadUserId";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * bonus
	 */
	private java.math.BigDecimal bonus;
	
	/**
	 * year_mon
	 */
	private java.lang.String yearMon;
	
	
	/**
	 * 引用的模板id
	 */
	private java.lang.Integer templateId;
	
	/**
	 * audit_flag
	 */
	private java.lang.Integer auditFlag;
	
	/**
	 * audit_user_id
	 */
	private java.lang.Integer auditUserId;
	
	/**
	 * create_user_id
	 */
	private java.lang.Integer createUserId;
	
	/**
	 * timestamp
	 */
	private java.sql.Timestamp timestamp;
	
	/**
	 * update_date
	 */
	private java.sql.Timestamp updateDate;
	
	/**
	 * update_user_id
	 */
	private java.lang.Integer updateUserId;
	
	/**
	 * upload_date
	 */
	private java.sql.Timestamp uploadDate;
	
	/**
	 * upload_flag
	 */
	private java.lang.Integer uploadFlag;
	
	/**
	 * upload_user_id
	 */
	private java.lang.Integer uploadUserId;
	
	
	private Set<Bunus> BonusList;

  
  private java.lang.Integer _id;
  @Transient
  public java.lang.Integer get_id(){
		return this.id;
	}
	
	public void set_id(java.lang.Integer _id){
		this._id	= _id;
	}

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
	 * 获取 bonus 的属性值
     *
	 * @return bonus :  bonus 
	 */
	@Column(name = "BONUS",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getBonus(){
		return this.bonus;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 bonus 的属性值
	 *		
	 * @param bonus :  bonus 
	 */
	public void setBonus(java.math.BigDecimal bonus){
		this.bonus	= bonus;
	}
	/**
	 * Author name@mail.com
	 * 获取 year_mon 的属性值
     *
	 * @return yearMon :  year_mon 
	 */
	@Column(name = "YEAR_MON",columnDefinition = "VARCHAR")
	public java.lang.String getYearMon(){
		return this.yearMon;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 year_mon 的属性值
	 *		
	 * @param yearMon :  year_mon 
	 */
	public void setYearMon(java.lang.String yearMon){
		this.yearMon	= yearMon;
	}
	/**
	 * Author name@mail.com
	 * 获取 audit_flag 的属性值
     *
	 * @return auditFlag :  audit_flag 
	 */
	@Column(name = "AUDIT_FLAG",columnDefinition = "INT")
	public java.lang.Integer getAuditFlag(){
		return this.auditFlag;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 audit_flag 的属性值
	 *		
	 * @param auditFlag :  audit_flag 
	 */
	public void setAuditFlag(java.lang.Integer auditFlag){
		this.auditFlag	= auditFlag;
	}
	/**
	 * Author name@mail.com
	 * 获取 audit_user_id 的属性值
     *
	 * @return auditUserId :  audit_user_id 
	 */
	@Column(name = "AUDIT_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getAuditUserId(){
		return this.auditUserId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 audit_user_id 的属性值
	 *		
	 * @param auditUserId :  audit_user_id 
	 */
	public void setAuditUserId(java.lang.Integer auditUserId){
		this.auditUserId	= auditUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 create_user_id 的属性值
     *
	 * @return createUserId :  create_user_id 
	 */
	@Column(name = "CREATE_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getCreateUserId(){
		return this.createUserId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 create_user_id 的属性值
	 *		
	 * @param createUserId :  create_user_id 
	 */
	public void setCreateUserId(java.lang.Integer createUserId){
		this.createUserId	= createUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 timestamp 的属性值
     *
	 * @return timestamp :  timestamp 
	 */
	@Column(name = "TIMESTAMP",columnDefinition = "DATETIME")
	public java.sql.Timestamp getTimestamp(){
		return this.timestamp;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 timestamp 的属性值
	 *		
	 * @param timestamp :  timestamp 
	 */
	public void setTimestamp(java.sql.Timestamp timestamp){
		this.timestamp	= timestamp;
	}
	/**
	 * Author name@mail.com
	 * 获取 update_date 的属性值
     *
	 * @return updateDate :  update_date 
	 */
	@Column(name = "UPDATE_DATE",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateDate(){
		return this.updateDate;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 update_date 的属性值
	 *		
	 * @param updateDate :  update_date 
	 */
	public void setUpdateDate(java.sql.Timestamp updateDate){
		this.updateDate	= updateDate;
	}
	/**
	 * Author name@mail.com
	 * 获取 update_user_id 的属性值
     *
	 * @return updateUserId :  update_user_id 
	 */
	@Column(name = "UPDATE_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUpdateUserId(){
		return this.updateUserId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 update_user_id 的属性值
	 *		
	 * @param updateUserId :  update_user_id 
	 */
	public void setUpdateUserId(java.lang.Integer updateUserId){
		this.updateUserId	= updateUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 upload_date 的属性值
     *
	 * @return uploadDate :  upload_date 
	 */
	@Column(name = "UPLOAD_DATE",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUploadDate(){
		return this.uploadDate;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 upload_date 的属性值
	 *		
	 * @param uploadDate :  upload_date 
	 */
	public void setUploadDate(java.sql.Timestamp uploadDate){
		this.uploadDate	= uploadDate;
	}
	/**
	 * Author name@mail.com
	 * 获取 upload_flag 的属性值
     *
	 * @return uploadFlag :  upload_flag 
	 */
	@Column(name = "UPLOAD_FLAG",columnDefinition = "INT")
	public java.lang.Integer getUploadFlag(){
		return this.uploadFlag;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 引用的模板id 的属性值
     *
	 * @return templateId :  引用的模板id 
	 */
	@Column(name = "TEMPLATE_ID",columnDefinition = "INT")
	public java.lang.Integer getTemplateId(){
		return this.templateId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 引用的模板id 的属性值
	 *		
	 * @param templateId :  引用的模板id 
	 */
	public void setTemplateId(java.lang.Integer templateId){
		this.templateId	= templateId;
	}
	

	/**
	 * Author name@mail.com
	 * 设置 upload_flag 的属性值
	 *		
	 * @param uploadFlag :  upload_flag 
	 */
	public void setUploadFlag(java.lang.Integer uploadFlag){
		this.uploadFlag	= uploadFlag;
	}
	/**
	 * Author name@mail.com
	 * 获取 upload_user_id 的属性值
     *
	 * @return uploadUserId :  upload_user_id 
	 */
	@Column(name = "UPLOAD_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUploadUserId(){
		return this.uploadUserId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 upload_user_id 的属性值
	 *		
	 * @param uploadUserId :  upload_user_id 
	 */
	public void setUploadUserId(java.lang.Integer uploadUserId){
		this.uploadUserId	= uploadUserId;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=BonusPools" + ",id=" + id +",bonus=" + bonus +",yearMon=" + yearMon +",auditFlag=" + auditFlag +",auditUserId=" + auditUserId +",createUserId=" + createUserId +",timestamp=" + timestamp +",updateDate=" + updateDate +",updateUserId=" + updateUserId +",uploadDate=" + uploadDate +",uploadFlag=" + uploadFlag +",uploadUserId=" + uploadUserId + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'BonusPools'" + ",id:'" + id + "'" +",bonus:'" + bonus + "'" +",yearMon:'" + yearMon + "'" +",auditFlag:'" + auditFlag + "'" +",auditUserId:'" + auditUserId + "'" +",createUserId:'" + createUserId + "'" +",timestamp:'" + timestamp + "'" +",updateDate:'" + updateDate + "'" +",updateUserId:'" + updateUserId + "'" +",uploadDate:'" + uploadDate + "'" +",uploadFlag:'" + uploadFlag + "'" +",uploadUserId:'" + uploadUserId + "'" +  "}";
	}
	
	
}
