package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 标签对应红包限额关系的修改记录表
 * 
 * @author vince
 * @date 2018-08-29 11:08:11
 */
public class LogLabelBonusRelationRecordPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Long id;
	//修改者
	private Long modifier;
	//修改者昵称
	private String nickName;

	//更新时间
	private Date modifyTime;
	//标签分类名称
	private String tipName;
	//满意金额
	private String satisyMoney;
	//非常满意金额
	private String verySatisyMoney;
	//修改原因
	private String modifyReason;
	//备注
	private String remark;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getModifier() {
		return modifier;
	}

	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getTipName() {
		return tipName;
	}

	public void setTipName(String tipName) {
		this.tipName = tipName;
	}

	public String getSatisyMoney() {
		return satisyMoney;
	}

	public void setSatisyMoney(String satisyMoney) {
		this.satisyMoney = satisyMoney;
	}

	public String getVerySatisyMoney() {
		return verySatisyMoney;
	}

	public void setVerySatisyMoney(String verySatisyMoney) {
		this.verySatisyMoney = verySatisyMoney;
	}

	public String getModifyReason() {
		return modifyReason;
	}

	public void setModifyReason(String modifyReason) {
		this.modifyReason = modifyReason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
}
