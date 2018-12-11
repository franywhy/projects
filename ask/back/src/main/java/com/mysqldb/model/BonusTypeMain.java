package com.mysqldb.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools Date: 2015-08-05 10:17:41
 */
@Entity
@Table(name = "bonus_type_main")
@org.hibernate.annotations.Table(appliesTo = "bonus_type_main", comment = "bonus_type_main")
public class BonusTypeMain implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * bonus_type_main
	 */
	public static final String REF = "BonusTypeMain";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID = "id";

	/**
	 * 模板名称 的属性名
	 */
	public static final String PROP_MAINTEMPLATENAME = "mainTemplateName";

	/**
	 * 模板总计金额 的属性名
	 */
	public static final String PROP_ALLMONEY = "allMoney";

	/**
	 * 模板所属月份 的属性名
	 */
	public static final String PROP_YEARMONTH = "yearMonth";

	/**
	 * 备注 的属性名
	 */
	public static final String PROP_REMARK = "remark";

	/**
	 * audit_flag 的属性名
	 */
	public static final String PROP_AUDITFLAG = "auditFlag";

	/**
	 * audit_user_id 的属性名
	 */
	public static final String PROP_AUDITUSERID = "auditUserId";

	/**
	 * create_user_id 的属性名
	 */
	public static final String PROP_CREATEUSERID = "createUserId";

	/**
	 * timestamp 的属性名
	 */
	public static final String PROP_TIMESTAMP = "timestamp";

	/**
	 * update_date 的属性名
	 */
	public static final String PROP_UPDATEDATE = "updateDate";

	/**
	 * update_user_id 的属性名
	 */
	public static final String PROP_UPDATEUSERID = "updateUserId";

	/**
	 * upload_date 的属性名
	 */
	public static final String PROP_UPLOADDATE = "uploadDate";

	/**
	 * upload_flag 的属性名
	 */
	public static final String PROP_UPLOADFLAG = "uploadFlag";

	/**
	 * upload_user_id 的属性名
	 */
	public static final String PROP_UPLOADUSERID = "uploadUserId";

	/**
	 * id
	 */
	private java.lang.Integer id;

	/**
	 * 模板名称
	 */
	private java.lang.String mainTemplateName;

	/**
	 * 模板总计金额
	 */
	private java.math.BigDecimal allMoney;

	/**
	 * 模板所属月份
	 */
	private java.lang.String yearMonth;

	/**
	 * 备注
	 */
	private java.lang.String remark;

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

	
	private Set<BonusType> BonusTypeList;

	private java.lang.Integer _id;

	@Transient
	public java.lang.Integer get_id() {
		return this.id;
	}

	public void set_id(java.lang.Integer _id) {
		this._id = _id;
	}

	/**
	 * Author name@mail.com 获取 id 的属性值
	 * 
	 * @return id : id
	 */
	@Id
	@GeneratedValue
	@Column(name = "ID", columnDefinition = "INT")
	public java.lang.Integer getId() {
		return this.id;
	}

	/**
	 * Author name@mail.com 设置 id 的属性值
	 * 
	 * @param id
	 *            : id
	 */
	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	/**
	 * Author name@mail.com 获取 模板名称 的属性值
	 * 
	 * @return mainTemplateName : 模板名称
	 */
	@Column(name = "MAIN_TEMPLATE_NAME", columnDefinition = "VARCHAR")
	public java.lang.String getMainTemplateName() {
		return this.mainTemplateName;
	}

	/**
	 * Author name@mail.com 设置 模板名称 的属性值
	 * 
	 * @param mainTemplateName
	 *            : 模板名称
	 */
	public void setMainTemplateName(java.lang.String mainTemplateName) {
		this.mainTemplateName = mainTemplateName;
	}

	/**
	 * Author name@mail.com 获取 模板总计金额 的属性值
	 * 
	 * @return allMoney : 模板总计金额
	 */
	@Column(name = "ALL_MONEY", columnDefinition = "DECIMAL")
	public java.math.BigDecimal getAllMoney() {
		return this.allMoney;
	}

	/**
	 * Author name@mail.com 设置 模板总计金额 的属性值
	 * 
	 * @param allMoney
	 *            : 模板总计金额
	 */
	public void setAllMoney(java.math.BigDecimal allMoney) {
		this.allMoney = allMoney;
	}

	/**
	 * Author name@mail.com 获取 模板所属月份 的属性值
	 * 
	 * @return yearMonth : 模板所属月份
	 */
	@Column(name = "YEAR_MON", columnDefinition = "VARCHAR")
	public java.lang.String getYearMonth() {
		return this.yearMonth;
	}

	/**
	 * Author name@mail.com 设置 模板所属月份 的属性值
	 * 
	 * @param yearMonth
	 *            : 模板所属月份
	 */
	public void setYearMonth(java.lang.String yearMonth) {
		this.yearMonth = yearMonth;
	}

	/**
	 * Author name@mail.com 获取 备注 的属性值
	 * 
	 * @return remark : 备注
	 */
	@Column(name = "REMARK", columnDefinition = "VARCHAR")
	public java.lang.String getRemark() {
		return this.remark;
	}

	/**
	 * Author name@mail.com 设置 备注 的属性值
	 * 
	 * @param remark
	 *            : 备注
	 */
	public void setRemark(java.lang.String remark) {
		this.remark = remark;
	}

	/**
	 * Author name@mail.com 获取 audit_flag 的属性值
	 * 
	 * @return auditFlag : audit_flag
	 */
	@Column(name = "AUDIT_FLAG", columnDefinition = "INT")
	public java.lang.Integer getAuditFlag() {
		return this.auditFlag;
	}

	/**
	 * Author name@mail.com 设置 audit_flag 的属性值
	 * 
	 * @param auditFlag
	 *            : audit_flag
	 */
	public void setAuditFlag(java.lang.Integer auditFlag) {
		this.auditFlag = auditFlag;
	}

	/**
	 * Author name@mail.com 获取 audit_user_id 的属性值
	 * 
	 * @return auditUserId : audit_user_id
	 */
	@Column(name = "AUDIT_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getAuditUserId() {
		return this.auditUserId;
	}

	/**
	 * Author name@mail.com 设置 audit_user_id 的属性值
	 * 
	 * @param auditUserId
	 *            : audit_user_id
	 */
	public void setAuditUserId(java.lang.Integer auditUserId) {
		this.auditUserId = auditUserId;
	}

	/**
	 * Author name@mail.com 获取 create_user_id 的属性值
	 * 
	 * @return createUserId : create_user_id
	 */
	@Column(name = "CREATE_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getCreateUserId() {
		return this.createUserId;
	}

	/**
	 * Author name@mail.com 设置 create_user_id 的属性值
	 * 
	 * @param createUserId
	 *            : create_user_id
	 */
	public void setCreateUserId(java.lang.Integer createUserId) {
		this.createUserId = createUserId;
	}

	/**
	 * Author name@mail.com 获取 timestamp 的属性值
	 * 
	 * @return timestamp : timestamp
	 */
	@Column(name = "TIMESTAMP", columnDefinition = "DATETIME")
	public java.sql.Timestamp getTimestamp() {
		return this.timestamp;
	}

	/**
	 * Author name@mail.com 设置 timestamp 的属性值
	 * 
	 * @param timestamp
	 *            : timestamp
	 */
	public void setTimestamp(java.sql.Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Author name@mail.com 获取 update_date 的属性值
	 * 
	 * @return updateDate : update_date
	 */
	@Column(name = "UPDATE_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateDate() {
		return this.updateDate;
	}

	/**
	 * Author name@mail.com 设置 update_date 的属性值
	 * 
	 * @param updateDate
	 *            : update_date
	 */
	public void setUpdateDate(java.sql.Timestamp updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * Author name@mail.com 获取 update_user_id 的属性值
	 * 
	 * @return updateUserId : update_user_id
	 */
	@Column(name = "UPDATE_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUpdateUserId() {
		return this.updateUserId;
	}

	/**
	 * Author name@mail.com 设置 update_user_id 的属性值
	 * 
	 * @param updateUserId
	 *            : update_user_id
	 */
	public void setUpdateUserId(java.lang.Integer updateUserId) {
		this.updateUserId = updateUserId;
	}

	/**
	 * Author name@mail.com 获取 upload_date 的属性值
	 * 
	 * @return uploadDate : upload_date
	 */
	@Column(name = "UPLOAD_DATE", columnDefinition = "DATETIME")
	public java.sql.Timestamp getUploadDate() {
		return this.uploadDate;
	}

	/**
	 * Author name@mail.com 设置 upload_date 的属性值
	 * 
	 * @param uploadDate
	 *            : upload_date
	 */
	public void setUploadDate(java.sql.Timestamp uploadDate) {
		this.uploadDate = uploadDate;
	}

	/**
	 * Author name@mail.com 获取 upload_flag 的属性值
	 * 
	 * @return uploadFlag : upload_flag
	 */
	@Column(name = "UPLOAD_FLAG", columnDefinition = "INT")
	public java.lang.Integer getUploadFlag() {
		return this.uploadFlag;
	}

	/**
	 * Author name@mail.com 设置 upload_flag 的属性值
	 * 
	 * @param uploadFlag
	 *            : upload_flag
	 */
	public void setUploadFlag(java.lang.Integer uploadFlag) {
		this.uploadFlag = uploadFlag;
	}

	/**
	 * Author name@mail.com 获取 upload_user_id 的属性值
	 * 
	 * @return uploadUserId : upload_user_id
	 */
	@Column(name = "UPLOAD_USER_ID", columnDefinition = "INT")
	public java.lang.Integer getUploadUserId() {
		return this.uploadUserId;
	}

	/**
	 * Author name@mail.com 设置 upload_user_id 的属性值
	 * 
	 * @param uploadUserId
	 *            : upload_user_id
	 */
	public void setUploadUserId(java.lang.Integer uploadUserId) {
		this.uploadUserId = uploadUserId;
	}

	/**
	 * Author name@mail.com 转换为字符串
	 */
	public String toString() {
		return "{ _name=BonusTypeMain" + ",id=" + id + ",mainTemplateName="
				+ mainTemplateName + ",allMoney=" + allMoney + ",yearMonth="
				+ yearMonth + ",remark=" + remark + ",auditFlag=" + auditFlag
				+ ",auditUserId=" + auditUserId + ",createUserId="
				+ createUserId + ",timestamp=" + timestamp + ",updateDate="
				+ updateDate + ",updateUserId=" + updateUserId + ",uploadDate="
				+ uploadDate + ",uploadFlag=" + uploadFlag + ",uploadUserId="
				+ uploadUserId + "}";
	}

	/**
	 * Author name@mail.com 转换为 JSON 字符串
	 */
	public String toJson() {
		return "{ _name:'BonusTypeMain'" + ",id:'" + id + "'"
				+ ",mainTemplateName:'" + mainTemplateName + "'"
				+ ",allMoney:'" + allMoney + "'" + ",yearMonth:'" + yearMonth
				+ "'" + ",remark:'" + remark + "'" + ",auditFlag:'" + auditFlag
				+ "'" + ",auditUserId:'" + auditUserId + "'"
				+ ",createUserId:'" + createUserId + "'" + ",timestamp:'"
				+ timestamp + "'" + ",updateDate:'" + updateDate + "'"
				+ ",updateUserId:'" + updateUserId + "'" + ",uploadDate:'"
				+ uploadDate + "'" + ",uploadFlag:'" + uploadFlag + "'"
				+ ",uploadUserId:'" + uploadUserId + "'" + "}";
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "bonusTypeMain", cascade=CascadeType.ALL)
	public Set<BonusType> getBonusTypeList() {
		return BonusTypeList;
	}

	public void setBonusTypeList(Set<BonusType> bonusTypeList) {
		BonusTypeList = bonusTypeList;
	}
}
