package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-05-11 11:07:05
 */
@Entity
@Table(name = "discount")
@org.hibernate.annotations.Table(appliesTo = "discount", comment = "discount")
public class Discount implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * discount
	 */
	public static final String REF="Discount";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 编码 的属性名
	 */
	public static final String PROP_CODE="code";
	
	/**
	 * 名称 的属性名
	 */
	public static final String PROP_NAME="name";
	
	/**
	 * 优惠金额 的属性名
	 */
	public static final String PROP_MONEY="money";
	
	/**
	 * 是否购买商品赠送 0.标准 1.活动赠送 的属性名
	 */
	public static final String PROP_TYPE="type";
	
	/**
	 * 时间类型 0.固定日期 1.领取后生效的天数  的属性名
	 */
	public static final String PROP_TIMETYPE="timeType";
	
	/**
	 * 备注信息 的属性名
	 */
	public static final String PROP_CONTENT="content";
	
	/**
	 * 优惠券数量 的属性名
	 */
	public static final String PROP_LIMITNUMBER="limitNumber";
	
	/**
	 * 领取数量 的属性名
	 */
	public static final String PROP_GETNUM="getNum";
	
	/**
	 * 优惠图片地址 的属性名
	 */
	public static final String PROP_PICURL="picUrl";
	
	/**
	 * 是否停用 0.否 1.是 的属性名
	 */
	public static final String PROP_ISSTOP="isStop";
	
	/**
	 * 使用品台 0.不限制 1.恒企在线 2.会答APP 的属性名
	 */
	public static final String PROP_WORKTYPE="workType";
	
	/**
	 * 消费最低限制 的属性名
	 */
	public static final String PROP_ENOUGHMONEY="enoughMoney";
	
	/**
	 * 时间段-起始时间 的属性名
	 */
	public static final String PROP_STARTTIME="startTime";
	
	/**
	 * 时间段-结束时间 的属性名
	 */
	public static final String PROP_ENDTIME="endTime";
	
	/**
	 * 有效天数 的属性名
	 */
	public static final String PROP_VALIDDAYS="validDays";
	
	/**
	 * 每日领取最大限制 的属性名
	 */
	public static final String PROP_DAYMAXNUM="dayMaxNum";
	
	/**
	 * 创建用户id 的属性名
	 */
	public static final String PROP_CREATEUSERID="createUserId";
	
	/**
	 * 创建时间 的属性名
	 */
	public static final String PROP_CREATETIME="createTime";
	
	/**
	 * 最后修改用户id 的属性名
	 */
	public static final String PROP_UPDATEUSERID="updateUserId";
	
	/**
	 * 最后修改时间 的属性名
	 */
	public static final String PROP_UPDATETIME="updateTime";
	
	/**
	 * 提交用户id 的属性名
	 */
	public static final String PROP_UPLOADUSERID="uploadUserId";
	
	/**
	 * 提交时间 的属性名
	 */
	public static final String PROP_UPLOADTIME="uploadTime";
	
	/**
	 * 提交标志 0.未提交 1.已提交 的属性名
	 */
	public static final String PROP_UPLOADFLAG="uploadFlag";
	
	/**
	 * 审核用户id 的属性名
	 */
	public static final String PROP_AUDITUSERID="auditUserId";
	
	/**
	 * 审核时间 的属性名
	 */
	public static final String PROP_AUDITTIME="auditTime";
	
	/**
	 * 审核标记 0.未通过 1.通过 的属性名
	 */
	public static final String PROP_AUDITFLAG="auditFlag";
	

	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 编码
	 */
	private java.lang.Integer code;
	
	/**
	 * 名称
	 */
	private java.lang.String name;
	
	/**
	 * 优惠金额
	 */
	private java.lang.Double money;
	
	/**
	 * 是否购买商品赠送 0.标准 1.活动赠送
	 */
	private java.lang.Integer type;
	
	/**
	 * 时间类型 0.固定日期 1.领取后生效的天数 
	 */
	private int timeType;
	
	/**
	 * 备注信息
	 */
	private java.lang.String content;
	
	/**
	 * 优惠券数量
	 */
	private java.lang.Integer limitNumber;
	
	/**
	 * 领取数量
	 */
	private java.lang.Integer getNum;
	
	/**
	 * 优惠图片地址
	 */
	private java.lang.String picUrl;
	
	/**
	 * 是否停用 0.否 1.是
	 */
	private java.lang.Integer isStop;
	
	/**
	 * 使用品台 0.不限制 1.恒企在线 2.会答APP
	 */
	private java.lang.Integer workType;
	
	/**
	 * 消费最低限制
	 */
	private java.lang.Double enoughMoney;
	
	/**
	 * 时间段-起始时间
	 */
	private java.sql.Timestamp startTime;
	
	/**
	 * 时间段-结束时间
	 */
	private java.sql.Timestamp endTime;
	
	/**
	 * 有效天数
	 */
	private java.lang.Integer validDays;
	
	/**
	 * 每日领取最大限制
	 */
	private java.lang.Integer dayMaxNum;
	
	/**
	 * 创建用户id
	 */
	private java.lang.Integer createUserId;
	
	/**
	 * 创建时间
	 */
	private java.sql.Timestamp createTime;
	
	/**
	 * 最后修改用户id
	 */
	private java.lang.Integer updateUserId;
	
	/**
	 * 最后修改时间
	 */
	private java.sql.Timestamp updateTime;
	
	/**
	 * 提交用户id
	 */
	private java.lang.Integer uploadUserId;
	
	/**
	 * 提交时间
	 */
	private java.sql.Timestamp uploadTime;
	
	/**
	 * 提交标志 0.未提交 1.已提交
	 */
	private java.lang.Integer uploadFlag;
	
	/**
	 * 审核用户id
	 */
	private java.lang.Integer auditUserId;
	
	/**
	 * 审核时间
	 */
	private java.sql.Timestamp auditTime;
	
	/**
	 * 审核标记 0.未通过 1.通过
	 */
	private java.lang.Integer auditFlag;
	

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
	 * 获取 编码 的属性值
     *
	 * @return code :  编码 
	 */
	@Column(name = "CODE",columnDefinition = "INT")
	public java.lang.Integer getCode(){
		return this.code;
	}

	/**
	 * Author name@mail.com
	 * 设置 编码 的属性值
	 *		
	 * @param code :  编码 
	 */
	public void setCode(java.lang.Integer code){
		this.code	= code;
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
	 * 获取 优惠金额 的属性值
     *
	 * @return money :  优惠金额 
	 */
	@Column(name = "MONEY",columnDefinition = "DOUBLE")
	public java.lang.Double getMoney(){
		return this.money;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠金额 的属性值
	 *		
	 * @param money :  优惠金额 
	 */
	public void setMoney(java.lang.Double money){
		this.money	= money;
	}
	/**
	 * Author name@mail.com
	 * 获取 是否购买商品赠送 0.标准 1.活动赠送 的属性值
     *
	 * @return type :  是否购买商品赠送 0.标准 1.活动赠送 
	 */
	@Column(name = "TYPE",columnDefinition = "INT")
	public java.lang.Integer getType(){
		return this.type;
	}

	/**
	 * Author name@mail.com
	 * 设置 是否购买商品赠送 0.标准 1.活动赠送 的属性值
	 *		
	 * @param type :  是否购买商品赠送 0.标准 1.活动赠送 
	 */
	public void setType(java.lang.Integer type){
		this.type	= type;
	}
	/**
	 * Author name@mail.com
	 * 获取 时间类型 0.固定日期 1.领取后生效的天数  的属性值
     *
	 * @return timeType :  时间类型 0.固定日期 1.领取后生效的天数  
	 */
	@Column(name = "TIME_TYPE",columnDefinition = "INT")
	public int getTimeType(){
		return this.timeType;
	}

	/**
	 * Author name@mail.com
	 * 设置 时间类型 0.固定日期 1.领取后生效的天数  的属性值
	 *		
	 * @param timeType :  时间类型 0.固定日期 1.领取后生效的天数  
	 */
	public void setTimeType(int timeType){
		this.timeType	= timeType;
	}
	/**
	 * Author name@mail.com
	 * 获取 备注信息 的属性值
     *
	 * @return content :  备注信息 
	 */
	@Column(name = "CONTENT",columnDefinition = "VARCHAR")
	public java.lang.String getContent(){
		return this.content;
	}

	/**
	 * Author name@mail.com
	 * 设置 备注信息 的属性值
	 *		
	 * @param content :  备注信息 
	 */
	public void setContent(java.lang.String content){
		this.content	= content;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠券数量 的属性值
     *
	 * @return limitNumber :  优惠券数量 
	 */
	@Column(name = "LIMIT_NUMBER",columnDefinition = "INT")
	public java.lang.Integer getLimitNumber(){
		return this.limitNumber;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠券数量 的属性值
	 *		
	 * @param limitNumber :  优惠券数量 
	 */
	public void setLimitNumber(java.lang.Integer limitNumber){
		this.limitNumber	= limitNumber;
	}
	/**
	 * Author name@mail.com
	 * 获取 领取数量 的属性值
     *
	 * @return getNum :  领取数量 
	 */
	@Column(name = "GET_NUM",columnDefinition = "INT")
	public java.lang.Integer getGetNum(){
		return this.getNum;
	}

	/**
	 * Author name@mail.com
	 * 设置 领取数量 的属性值
	 *		
	 * @param getNum :  领取数量 
	 */
	public void setGetNum(java.lang.Integer getNum){
		this.getNum	= getNum;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠图片地址 的属性值
     *
	 * @return picUrl :  优惠图片地址 
	 */
	@Column(name = "PIC_URL",columnDefinition = "VARCHAR")
	public java.lang.String getPicUrl(){
		return this.picUrl;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠图片地址 的属性值
	 *		
	 * @param picUrl :  优惠图片地址 
	 */
	public void setPicUrl(java.lang.String picUrl){
		this.picUrl	= picUrl;
	}
	/**
	 * Author name@mail.com
	 * 获取 是否停用 0.否 1.是 的属性值
     *
	 * @return isStop :  是否停用 0.否 1.是 
	 */
	@Column(name = "IS_STOP",columnDefinition = "INT")
	public java.lang.Integer getIsStop(){
		return this.isStop;
	}

	/**
	 * Author name@mail.com
	 * 设置 是否停用 0.否 1.是 的属性值
	 *		
	 * @param isStop :  是否停用 0.否 1.是 
	 */
	public void setIsStop(java.lang.Integer isStop){
		this.isStop	= isStop;
	}
	/**
	 * Author name@mail.com
	 * 获取 使用品台 0.不限制 1.恒企在线 2.会答APP 的属性值
     *
	 * @return workType :  使用品台 0.不限制 1.恒企在线 2.会答APP 
	 */
	@Column(name = "WORK_TYPE",columnDefinition = "INT")
	public java.lang.Integer getWorkType(){
		return this.workType;
	}

	/**
	 * Author name@mail.com
	 * 设置 使用品台 0.不限制 1.恒企在线 2.会答APP 的属性值
	 *		
	 * @param workType :  使用品台 0.不限制 1.恒企在线 2.会答APP 
	 */
	public void setWorkType(java.lang.Integer workType){
		this.workType	= workType;
	}
	/**
	 * Author name@mail.com
	 * 获取 消费最低限制 的属性值
     *
	 * @return enoughMoney :  消费最低限制 
	 */
	@Column(name = "ENOUGH_MONEY",columnDefinition = "DOUBLE")
	public java.lang.Double getEnoughMoney(){
		return this.enoughMoney;
	}

	/**
	 * Author name@mail.com
	 * 设置 消费最低限制 的属性值
	 *		
	 * @param enoughMoney :  消费最低限制 
	 */
	public void setEnoughMoney(java.lang.Double enoughMoney){
		this.enoughMoney	= enoughMoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 时间段-起始时间 的属性值
     *
	 * @return startTime :  时间段-起始时间 
	 */
	@Column(name = "START_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getStartTime(){
		return this.startTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 时间段-起始时间 的属性值
	 *		
	 * @param startTime :  时间段-起始时间 
	 */
	public void setStartTime(java.sql.Timestamp startTime){
		this.startTime	= startTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 时间段-结束时间 的属性值
     *
	 * @return endTime :  时间段-结束时间 
	 */
	@Column(name = "END_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getEndTime(){
		return this.endTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 时间段-结束时间 的属性值
	 *		
	 * @param endTime :  时间段-结束时间 
	 */
	public void setEndTime(java.sql.Timestamp endTime){
		this.endTime	= endTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 有效天数 的属性值
     *
	 * @return validDays :  有效天数 
	 */
	@Column(name = "VALID_DAYS",columnDefinition = "INT")
	public java.lang.Integer getValidDays(){
		return this.validDays;
	}

	/**
	 * Author name@mail.com
	 * 设置 有效天数 的属性值
	 *		
	 * @param validDays :  有效天数 
	 */
	public void setValidDays(java.lang.Integer validDays){
		this.validDays	= validDays;
	}
	/**
	 * Author name@mail.com
	 * 获取 每日领取最大限制 的属性值
     *
	 * @return dayMaxNum :  每日领取最大限制 
	 */
	@Column(name = "DAY_MAX_NUM",columnDefinition = "INT")
	public java.lang.Integer getDayMaxNum(){
		return this.dayMaxNum;
	}

	/**
	 * Author name@mail.com
	 * 设置 每日领取最大限制 的属性值
	 *		
	 * @param dayMaxNum :  每日领取最大限制 
	 */
	public void setDayMaxNum(java.lang.Integer dayMaxNum){
		this.dayMaxNum	= dayMaxNum;
	}
	/**
	 * Author name@mail.com
	 * 获取 创建用户id 的属性值
     *
	 * @return createUserId :  创建用户id 
	 */
	@Column(name = "CREATE_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getCreateUserId(){
		return this.createUserId;
	}

	/**
	 * Author name@mail.com
	 * 设置 创建用户id 的属性值
	 *		
	 * @param createUserId :  创建用户id 
	 */
	public void setCreateUserId(java.lang.Integer createUserId){
		this.createUserId	= createUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 创建时间 的属性值
     *
	 * @return createTime :  创建时间 
	 */
	@Column(name = "CREATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getCreateTime(){
		return this.createTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 创建时间 的属性值
	 *		
	 * @param createTime :  创建时间 
	 */
	public void setCreateTime(java.sql.Timestamp createTime){
		this.createTime	= createTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 最后修改用户id 的属性值
     *
	 * @return updateUserId :  最后修改用户id 
	 */
	@Column(name = "UPDATE_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUpdateUserId(){
		return this.updateUserId;
	}

	/**
	 * Author name@mail.com
	 * 设置 最后修改用户id 的属性值
	 *		
	 * @param updateUserId :  最后修改用户id 
	 */
	public void setUpdateUserId(java.lang.Integer updateUserId){
		this.updateUserId	= updateUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 最后修改时间 的属性值
     *
	 * @return updateTime :  最后修改时间 
	 */
	@Column(name = "UPDATE_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUpdateTime(){
		return this.updateTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 最后修改时间 的属性值
	 *		
	 * @param updateTime :  最后修改时间 
	 */
	public void setUpdateTime(java.sql.Timestamp updateTime){
		this.updateTime	= updateTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 提交用户id 的属性值
     *
	 * @return uploadUserId :  提交用户id 
	 */
	@Column(name = "UPLOAD_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUploadUserId(){
		return this.uploadUserId;
	}

	/**
	 * Author name@mail.com
	 * 设置 提交用户id 的属性值
	 *		
	 * @param uploadUserId :  提交用户id 
	 */
	public void setUploadUserId(java.lang.Integer uploadUserId){
		this.uploadUserId	= uploadUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 提交时间 的属性值
     *
	 * @return uploadTime :  提交时间 
	 */
	@Column(name = "UPLOAD_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getUploadTime(){
		return this.uploadTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 提交时间 的属性值
	 *		
	 * @param uploadTime :  提交时间 
	 */
	public void setUploadTime(java.sql.Timestamp uploadTime){
		this.uploadTime	= uploadTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 提交标志 0.未提交 1.已提交 的属性值
     *
	 * @return uploadFlag :  提交标志 0.未提交 1.已提交 
	 */
	@Column(name = "UPLOAD_FLAG",columnDefinition = "INT")
	public java.lang.Integer getUploadFlag(){
		return this.uploadFlag;
	}

	/**
	 * Author name@mail.com
	 * 设置 提交标志 0.未提交 1.已提交 的属性值
	 *		
	 * @param uploadFlag :  提交标志 0.未提交 1.已提交 
	 */
	public void setUploadFlag(java.lang.Integer uploadFlag){
		this.uploadFlag	= uploadFlag;
	}
	/**
	 * Author name@mail.com
	 * 获取 审核用户id 的属性值
     *
	 * @return auditUserId :  审核用户id 
	 */
	@Column(name = "AUDIT_USER_ID",columnDefinition = "INT")
	public java.lang.Integer getAuditUserId(){
		return this.auditUserId;
	}

	/**
	 * Author name@mail.com
	 * 设置 审核用户id 的属性值
	 *		
	 * @param auditUserId :  审核用户id 
	 */
	public void setAuditUserId(java.lang.Integer auditUserId){
		this.auditUserId	= auditUserId;
	}
	/**
	 * Author name@mail.com
	 * 获取 审核时间 的属性值
     *
	 * @return auditTime :  审核时间 
	 */
	@Column(name = "AUDIT_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getAuditTime(){
		return this.auditTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 审核时间 的属性值
	 *		
	 * @param auditTime :  审核时间 
	 */
	public void setAuditTime(java.sql.Timestamp auditTime){
		this.auditTime	= auditTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 审核标记 0.未通过 1.通过 的属性值
     *
	 * @return auditFlag :  审核标记 0.未通过 1.通过 
	 */
	@Column(name = "AUDIT_FLAG",columnDefinition = "INT")
	public java.lang.Integer getAuditFlag(){
		return this.auditFlag;
	}

	/**
	 * Author name@mail.com
	 * 设置 审核标记 0.未通过 1.通过 的属性值
	 *		
	 * @param auditFlag :  审核标记 0.未通过 1.通过 
	 */
	public void setAuditFlag(java.lang.Integer auditFlag){
		this.auditFlag	= auditFlag;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=Discount" + ",id=" + id +",code=" + code +",name=" + name +",money=" + money +",type=" + type +",timeType=" + timeType +",content=" + content +",limitNumber=" + limitNumber +",getNum=" + getNum +",picUrl=" + picUrl +",isStop=" + isStop +",workType=" + workType +",enoughMoney=" + enoughMoney +",startTime=" + startTime +",endTime=" + endTime +",validDays=" + validDays +",dayMaxNum=" + dayMaxNum +",createUserId=" + createUserId +",createTime=" + createTime +",updateUserId=" + updateUserId +",updateTime=" + updateTime +",uploadUserId=" + uploadUserId +",uploadTime=" + uploadTime +",uploadFlag=" + uploadFlag +",auditUserId=" + auditUserId +",auditTime=" + auditTime +",auditFlag=" + auditFlag + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'Discount'" + ",id:'" + id + "'" +",code:'" + code + "'" +",name:'" + name + "'" +",money:'" + money + "'" +",type:'" + type + "'" +",timeType:'" + timeType + "'" +",content:'" + content + "'" +",limitNumber:'" + limitNumber + "'" +",getNum:'" + getNum + "'" +",picUrl:'" + picUrl + "'" +",isStop:'" + isStop + "'" +",workType:'" + workType + "'" +",enoughMoney:'" + enoughMoney + "'" +",startTime:'" + startTime + "'" +",endTime:'" + endTime + "'" +",validDays:'" + validDays + "'" +",dayMaxNum:'" + dayMaxNum + "'" +",createUserId:'" + createUserId + "'" +",createTime:'" + createTime + "'" +",updateUserId:'" + updateUserId + "'" +",updateTime:'" + updateTime + "'" +",uploadUserId:'" + uploadUserId + "'" +",uploadTime:'" + uploadTime + "'" +",uploadFlag:'" + uploadFlag + "'" +",auditUserId:'" + auditUserId + "'" +",auditTime:'" + auditTime + "'" +",auditFlag:'" + auditFlag + "'" +  "}";
	}
}
