package io.renren.pojo.order;

import java.io.Serializable;
import java.util.Date;

public class OrderGridPOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long orderId;
	// 单号
	private String orderNo;
	// 用户ID
	private Long userId;
	// 商品ID
	private Long commodityId;
	// 订单名称
	private String orderName;
	// 订单总额
	private Double totalMoney;
	// 支付金额
	private Double payMoney;
	// 支付时间
	private Date payTime;
	// 来源 0.线上;1.NC
	private Integer sourceType;
	// 省份ID
	private Long areaId;
	// 层次ID
	private Long levelId;
	// 班级ID
	private Long classId;
	// NCID
	private String ncId;
	// 电话号码
	private String phoneNumber;
	// 学员昵称
	private String nickname;
	// 班型ID
	private Long classTypeId;

	// 省份
	private String areaName;
	// 层次
	private String levelName;
	// 班级
	private String className;
	// 班型
	private String classtypeName;
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public String getOrderName() {
		return orderName;
	}
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	public Double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	public Double getPayMoney() {
		return payMoney;
	}
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Integer getSourceType() {
		return sourceType;
	}
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Long getLevelId() {
		return levelId;
	}
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	public String getNcId() {
		return ncId;
	}
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public Long getClassTypeId() {
		return classTypeId;
	}
	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClasstypeName() {
		return classtypeName;
	}
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	@Override
	public String toString() {
		return "OrderGridPOJO [orderId=" + orderId + ", orderNo=" + orderNo + ", userId=" + userId + ", commodityId=" + commodityId + ", orderName="
				+ orderName + ", totalMoney=" + totalMoney + ", payMoney=" + payMoney + ", payTime=" + payTime + ", sourceType=" + sourceType
				+ ", areaId=" + areaId + ", levelId=" + levelId + ", classId=" + classId + ", ncId=" + ncId + ", phoneNumber=" + phoneNumber
				+ ", nickname=" + nickname + ", classTypeId=" + classTypeId + ", areaName=" + areaName + ", levelName=" + levelName + ", className="
				+ className + ", classtypeName=" + classtypeName + "]";
	}
	
	

}
