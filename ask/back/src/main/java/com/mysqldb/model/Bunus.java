package com.mysqldb.model;


import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2015-08-06 11:40:29
 */
@Entity
@Table(name = "bunus")
@org.hibernate.annotations.Table(appliesTo = "bunus", comment = "bunus")
public class Bunus implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * bunus
	 */
	public static final String REF="Bunus";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 级别 的属性名
	 */
	public static final String PROP_MLEVEL="mlevel";
	
	/**
	 * 金额 的属性名
	 */
	public static final String PROP_MMONEY="mmoney";
	
	/**
	 * 模板类型 0-红色模板 1-蓝色模板 的属性名
	 */
	public static final String PROP_MTEMPLATE="mtemplate";
	
	/**
	 * 提问id 的属性名
	 */
	public static final String PROP_TOPICID="topicId";
	
	/**
	 * 奖品的唯一标记 的属性名
	 */
	public static final String PROP_UNIQUEMARK="uniqueMark";
	
	/**
	 * 获奖人编号 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * 是否被打开过 true:红包被打开过 false:红包未被打开过 的属性名
	 */
	public static final String PROP_ISOPEN="isOpen";
	
	/**
	 * 红包打开时间 的属性名
	 */
	public static final String PROP_OPENTIME="openTime";
	
	/**
	 * 权重 的属性名
	 */
	public static final String PROP_MWEIGHT="mweight";
	
	/**
	 * yyyyMM 的属性名
	 */
	public static final String PROP_YEARMON="yearMon";
	
	/**
	 * 红包分发时间(红包分给用户的时间) 的属性名
	 */
	public static final String PROP_LOTTERYTIME="lotteryTime";
	
	/**
	 * 记录生成时间 的属性名
	 */
	public static final String PROP_MTIMESTAMP="mtimestamp";
	
	/**
	 * main_id 的属性名
	 */
	public static final String PROP_MAINID="mainId";
	
	/**
	 * vip权重 的属性名
	 */
	public static final String PROP_VWEIGHT="vweight";

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 级别
	 */
	private java.lang.String mlevel;
	
	/**
	 * 金额
	 */
	private java.math.BigDecimal mmoney;
	
	/**
	 * 模板类型 0-红色模板 1-蓝色模板
	 */
	private java.lang.Integer mtemplate;
	
	/**
	 * 提问id
	 */
	private java.lang.String topicId;
	
	/**
	 * 奖品的唯一标记
	 */
	private java.lang.String uniqueMark;
	
	/**
	 * 获奖人编号
	 */
	private java.lang.Integer userId;
	
	/**
	 * 是否被打开过 true:红包被打开过 false:红包未被打开过
	 */
	private java.lang.Integer isOpen;
	
	/**
	 * 红包打开时间
	 */
	private java.sql.Timestamp openTime;
	
	/**
	 * 权重
	 */
	private java.lang.Integer mweight;
	
	/**
	 * yyyyMM
	 */
	private java.lang.String yearMon;
	
	/**
	 * 红包分发时间(红包分给用户的时间)
	 */
	private java.sql.Timestamp lotteryTime;
	
	/**
	 * 记录生成时间
	 */
	private java.sql.Timestamp mtimestamp;

	

	/**
	 * main_id
	 */
	 private BonusPools bonusPools;
	 
		/**
		 * main_id
		 */
		private java.lang.Integer mainId;
	

  
  private java.lang.Integer _id;
  
	/**
	 * vip权重
	 */
	private java.lang.Integer vweight;
  
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
	 * 获取 级别 的属性值
     *
	 * @return mlevel :  级别 
	 */
	@Column(name = "MLEVEL",columnDefinition = "VARCHAR")
	public java.lang.String getMlevel(){
		return this.mlevel;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 级别 的属性值
	 *		
	 * @param mlevel :  级别 
	 */
	public void setMlevel(java.lang.String mlevel){
		this.mlevel	= mlevel;
	}
	/**
	 * Author name@mail.com
	 * 获取 金额 的属性值
     *
	 * @return mmoney :  金额 
	 */
	@Column(name = "MMONEY",columnDefinition = "DECIMAL")
	public java.math.BigDecimal getMmoney(){
		return this.mmoney;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 金额 的属性值
	 *		
	 * @param mmoney :  金额 
	 */
	public void setMmoney(java.math.BigDecimal mmoney){
		this.mmoney	= mmoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 模板类型 0-红色模板 1-蓝色模板 的属性值
     *
	 * @return mtemplate :  模板类型 0-红色模板 1-蓝色模板 
	 */
	@Column(name = "MTEMPLATE",columnDefinition = "INT")
	public java.lang.Integer getMtemplate(){
		return this.mtemplate;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 模板类型 0-红色模板 1-蓝色模板 的属性值
	 *		
	 * @param mtemplate :  模板类型 0-红色模板 1-蓝色模板 
	 */
	public void setMtemplate(java.lang.Integer mtemplate){
		this.mtemplate	= mtemplate;
	}
	/**
	 * Author name@mail.com
	 * 获取 提问id 的属性值
     *
	 * @return topicId :  提问id 
	 */
	@Column(name = "TOPIC_ID",columnDefinition = "VARCHAR")
	public java.lang.String getTopicId(){
		return this.topicId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 提问id 的属性值
	 *		
	 * @param topicId :  提问id 
	 */
	public void setTopicId(java.lang.String topicId){
		this.topicId	= topicId;
	}
	/**
	 * Author name@mail.com
	 * 获取 奖品的唯一标记 的属性值
     *
	 * @return uniqueMark :  奖品的唯一标记 
	 */
	@Column(name = "UNIQUE_MARK",columnDefinition = "VARCHAR")
	public java.lang.String getUniqueMark(){
		return this.uniqueMark;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 奖品的唯一标记 的属性值
	 *		
	 * @param uniqueMark :  奖品的唯一标记 
	 */
	public void setUniqueMark(java.lang.String uniqueMark){
		this.uniqueMark	= uniqueMark;
	}
	/**
	 * Author name@mail.com
	 * 获取 获奖人编号 的属性值
     *
	 * @return userId :  获奖人编号 
	 */
	@Column(name = "USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUserId(){
		return this.userId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 获奖人编号 的属性值
	 *		
	 * @param userId :  获奖人编号 
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * 获取 是否被打开过 true:红包被打开过 false:红包未被打开过 的属性值
     *
	 * @return isOpen :  是否被打开过 true:红包被打开过 false:红包未被打开过 
	 */
	@Column(name = "IS_OPEN",columnDefinition = "INT")
	public java.lang.Integer getIsOpen(){
		return this.isOpen;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 是否被打开过 true:红包被打开过 false:红包未被打开过 的属性值
	 *		
	 * @param isOpen :  是否被打开过 true:红包被打开过 false:红包未被打开过 
	 */
	public void setIsOpen(java.lang.Integer isOpen){
		this.isOpen	= isOpen;
	}
	/**
	 * Author name@mail.com
	 * 获取 红包打开时间 的属性值
     *
	 * @return openTime :  红包打开时间 
	 */
	@Column(name = "OPEN_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getOpenTime(){
		return this.openTime;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 红包打开时间 的属性值
	 *		
	 * @param openTime :  红包打开时间 
	 */
	public void setOpenTime(java.sql.Timestamp openTime){
		this.openTime	= openTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 权重 的属性值
     *
	 * @return mweight :  权重 
	 */
	@Column(name = "MWEIGHT",columnDefinition = "INT")
	public java.lang.Integer getMweight(){
		return this.mweight;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 权重 的属性值
	 *		
	 * @param mweight :  权重 
	 */
	public void setMweight(java.lang.Integer mweight){
		this.mweight	= mweight;
	}
	/**
	 * Author name@mail.com
	 * 获取 yyyyMM 的属性值
     *
	 * @return yearMon :  yyyyMM 
	 */
	@Column(name = "YEAR_MON",columnDefinition = "VARCHAR")
	public java.lang.String getYearMon(){
		return this.yearMon;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 yyyyMM 的属性值
	 *		
	 * @param yearMon :  yyyyMM 
	 */
	public void setYearMon(java.lang.String yearMon){
		this.yearMon	= yearMon;
	}
	/**
	 * Author name@mail.com
	 * 获取 红包分发时间(红包分给用户的时间) 的属性值
     *
	 * @return lotteryTime :  红包分发时间(红包分给用户的时间) 
	 */
	@Column(name = "LOTTERY_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getLotteryTime(){
		return this.lotteryTime;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 红包分发时间(红包分给用户的时间) 的属性值
	 *		
	 * @param lotteryTime :  红包分发时间(红包分给用户的时间) 
	 */
	public void setLotteryTime(java.sql.Timestamp lotteryTime){
		this.lotteryTime	= lotteryTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 记录生成时间 的属性值
     *
	 * @return mtimestamp :  记录生成时间 
	 */
	@Column(name = "MTIMESTAMP",columnDefinition = "DATETIME")
	public java.sql.Timestamp getMtimestamp(){
		return this.mtimestamp;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 记录生成时间 的属性值
	 *		
	 * @param mtimestamp :  记录生成时间 
	 */
	public void setMtimestamp(java.sql.Timestamp mtimestamp){
		this.mtimestamp	= mtimestamp;
	}

	/**
	 * Author name@mail.com
	 * 获取 main_id 的属性值
     *
	 * @return mainId :  main_id 
	 */
	@Column(name = "MAIN_ID",columnDefinition = "INT", insertable=false, updatable=false)
	public java.lang.Integer getMainId(){
		return this.mainId;
	}
	
	
	

	/**
	 * Author name@mail.com
	 * 设置 main_id 的属性值
	 *		
	 * @param mainId :  main_id 
	 */
	public void setMainId(java.lang.Integer mainId){
		this.mainId	= mainId;
	}
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=Bunus" + ",id=" + id +",mlevel=" + mlevel +",mmoney=" + mmoney +",mtemplate=" + mtemplate +",topicId=" + topicId +",uniqueMark=" + uniqueMark +",userId=" + userId +",isOpen=" + isOpen +",openTime=" + openTime +",mweight=" + mweight +",yearMon=" + yearMon +",lotteryTime=" + lotteryTime +",mtimestamp=" + mtimestamp +",mainId=" + bonusPools.get_id()  +",vweight=" + vweight + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'Bunus'" + ",id:'" + id + "'" +",mlevel:'" + mlevel + "'" +",mmoney:'" + mmoney + "'" +",mtemplate:'" + mtemplate + "'" +",topicId:'" + topicId + "'" +",uniqueMark:'" + uniqueMark + "'" +",userId:'" + userId + "'" +",isOpen:'" + isOpen + "'" +",openTime:'" + openTime + "'" +",mweight:'" + mweight + "'" +",yearMon:'" + yearMon + "'" +",lotteryTime:'" + lotteryTime + "'" +",mtimestamp:'" + mtimestamp + "'" +",mainId:'" + bonusPools.get_id() + "'" +",vweight:'" + vweight + "'" +  "}";
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MAIN_ID")
	@JsonIgnore
	public BonusPools getBonusPools() {
		return bonusPools;
	}

	public void setBonusPools(BonusPools _bonusPools) {
		this.bonusPools = _bonusPools;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 vip权重 的属性值
     *
	 * @return vweight :  vip权重 
	 */
	@Column(name = "VWEIGHT",columnDefinition = "INT")
	public java.lang.Integer getVweight(){
		return this.vweight;
	}

	/**
	 * Author name@mail.com
	 * 设置 vip权重 的属性值
	 *		
	 * @param vweight :  vip权重 
	 */
	public void setVweight(java.lang.Integer vweight){
		this.vweight	= vweight;
	}
}
