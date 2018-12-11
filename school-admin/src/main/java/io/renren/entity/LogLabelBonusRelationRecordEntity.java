package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签对应红包限额关系的修改记录表
 * 
 * @author vince
 * @date 2018-08-29 11:08:11
 */
public class LogLabelBonusRelationRecordEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//创建者
	private Long creator;
	//创建时间
	private Date createTime;
	//修改者
	private Long modifier;
	//更新时间
	private Date modifyTime;
	//标签分类名称
	private Long labelId;
	//标签分类名称
	private String tipName;
	//满意金额
	private String satisyMoney;
	//非常满意金额
	private String verySatisyMoney;
	//修改原因
	private String modifyReason;
	//软删除0：正常 1：删除
	private Integer dr;
	//备注
	private String remark;

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：创建者
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建者
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改者
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：修改者
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：更新时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：标签分类名称
	 */
	public void setTipName(String tipName) {
		this.tipName = tipName;
	}
	/**
	 * 获取：标签分类名称
	 */
	public String getTipName() {
		return tipName;
	}
	/**
	 * 设置：满意金额
	 */
	public void setSatisyMoney(String satisyMoney) {
		this.satisyMoney = satisyMoney;
	}
	/**
	 * 获取：满意金额
	 */
	public String getSatisyMoney() {
		return satisyMoney;
	}
	/**
	 * 设置：非常满意金额
	 */
	public void setVerySatisyMoney(String verySatisyMoney) {
		this.verySatisyMoney = verySatisyMoney;
	}
	/**
	 * 获取：非常满意金额
	 */
	public String getVerySatisyMoney() {
		return verySatisyMoney;
	}
	/**
	 * 设置：修改原因
	 */
	public void setModifyReason(String modifyReason) {
		this.modifyReason = modifyReason;
	}
	/**
	 * 获取：修改原因
	 */
	public String getModifyReason() {
		return modifyReason;
	}
	/**
	 * 设置：软删除0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：软删除0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：备注
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * 获取：备注
	 */
	public String getRemark() {
		return remark;
	}

	public Long getLabelId() {
		return labelId;
	}

	public void setLabelId(Long labelId) {
		this.labelId = labelId;
	}
}
