package com.school.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 订单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-10 14:33:05
 */
public class MallOrderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long orderId;
	//单号
	private String orderNo;
	//用户PK
	private Long userId;
	//商品PK
	private Long commodityId;
	//商品名称
	private String commodityName;
	//订单名称
	private String orderName;
	//订单描述
	private String orderDescribe;
	//订单总额
	private Double totalMoney;
	//支付金额
	private Double payMoney;
	//支付状态 0.未支付 1.发起支付 ,2.支付成功
	private Integer payStatus;
	//备注
	private String remark;
	//支付时间
	private Date payTime;
	//优惠金额
	private Double favorableMoney;
	//优惠券pk
	private Long discountId;
	//第三方支付回调时间
	private Date payCallblackTime;
	//第三方支付回调信息
	private String payCallblackMsg;
	//支付宝交易号
	private String alipayTradeNo;
	//支付IP
	private String payip;
	//生成支付地址
	private String payUrl;
	//商品图片
	private String pic;
	//商品小图
	private String spic;
	//dr 0.正常 1.删除
	private Integer dr;
	//用户操作状态 0.正常 1.取消 2.删除
	private Integer ustatus;
	//微信开放id
	private String wxOpenId;
	//0.未选择 1.支付宝 2.微信 
	private Integer payType;
	//银行编码
	private String bankCode;
	//银行名称
	private String bankName;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifier;
	//最近修改时间
	private Date modifiedTime;
	//平台ID
	private String schoolId;
	//来源 0.线上;1.NC 2測試 3.体验
	private Integer sourceType;
	//省份PK
	private Long areaId;
	//层次PK
	private Long levelId;
	//班级PK
	private Long classId;
	//NCID
	private String ncId;
	//状态
	private Integer status;
	//班型PK
	private Long classTypeId;
	//专业PK
	private Long professionId;
	//nc同步code
	private String ncCode;
	//nc同步时间
	private Date synTime;
	//产品线PK
	private Long productId;
	//有效期(某一天)
	private Date dateValidity;

	/**
	 * 设置：主键
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：主键
	 */
	public Long getOrderId() {
		return orderId;
	}
	/**
	 * 设置：单号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：单号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：用户PK
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：用户PK
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：商品PK
	 */
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	/**
	 * 获取：商品PK
	 */
	public Long getCommodityId() {
		return commodityId;
	}
	/**
	 * 设置：商品名称
	 */
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	/**
	 * 获取：商品名称
	 */
	public String getCommodityName() {
		return commodityName;
	}
	/**
	 * 设置：订单名称
	 */
	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}
	/**
	 * 获取：订单名称
	 */
	public String getOrderName() {
		return orderName;
	}
	/**
	 * 设置：订单描述
	 */
	public void setOrderDescribe(String orderDescribe) {
		this.orderDescribe = orderDescribe;
	}
	/**
	 * 获取：订单描述
	 */
	public String getOrderDescribe() {
		return orderDescribe;
	}
	/**
	 * 设置：订单总额
	 */
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}
	/**
	 * 获取：订单总额
	 */
	public Double getTotalMoney() {
		return totalMoney;
	}
	/**
	 * 设置：支付金额
	 */
	public void setPayMoney(Double payMoney) {
		this.payMoney = payMoney;
	}
	/**
	 * 获取：支付金额
	 */
	public Double getPayMoney() {
		return payMoney;
	}
	/**
	 * 设置：支付状态 0.未支付 1.发起支付 ,2.支付成功
	 */
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}
	/**
	 * 获取：支付状态 0.未支付 1.发起支付 ,2.支付成功
	 */
	public Integer getPayStatus() {
		return payStatus;
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
	/**
	 * 设置：支付时间
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	/**
	 * 获取：支付时间
	 */
	public Date getPayTime() {
		return payTime;
	}
	/**
	 * 设置：优惠金额
	 */
	public void setFavorableMoney(Double favorableMoney) {
		this.favorableMoney = favorableMoney;
	}
	/**
	 * 获取：优惠金额
	 */
	public Double getFavorableMoney() {
		return favorableMoney;
	}
	/**
	 * 设置：优惠券pk
	 */
	public void setDiscountId(Long discountId) {
		this.discountId = discountId;
	}
	/**
	 * 获取：优惠券pk
	 */
	public Long getDiscountId() {
		return discountId;
	}
	/**
	 * 设置：第三方支付回调时间
	 */
	public void setPayCallblackTime(Date payCallblackTime) {
		this.payCallblackTime = payCallblackTime;
	}
	/**
	 * 获取：第三方支付回调时间
	 */
	public Date getPayCallblackTime() {
		return payCallblackTime;
	}
	/**
	 * 设置：第三方支付回调信息
	 */
	public void setPayCallblackMsg(String payCallblackMsg) {
		this.payCallblackMsg = payCallblackMsg;
	}
	/**
	 * 获取：第三方支付回调信息
	 */
	public String getPayCallblackMsg() {
		return payCallblackMsg;
	}
	/**
	 * 设置：支付宝交易号
	 */
	public void setAlipayTradeNo(String alipayTradeNo) {
		this.alipayTradeNo = alipayTradeNo;
	}
	/**
	 * 获取：支付宝交易号
	 */
	public String getAlipayTradeNo() {
		return alipayTradeNo;
	}
	/**
	 * 设置：支付IP
	 */
	public void setPayip(String payip) {
		this.payip = payip;
	}
	/**
	 * 获取：支付IP
	 */
	public String getPayip() {
		return payip;
	}
	/**
	 * 设置：生成支付地址
	 */
	public void setPayUrl(String payUrl) {
		this.payUrl = payUrl;
	}
	/**
	 * 获取：生成支付地址
	 */
	public String getPayUrl() {
		return payUrl;
	}
	/**
	 * 设置：商品图片
	 */
	public void setPic(String pic) {
		this.pic = pic;
	}
	/**
	 * 获取：商品图片
	 */
	public String getPic() {
		return pic;
	}
	/**
	 * 设置：商品小图
	 */
	public void setSpic(String spic) {
		this.spic = spic;
	}
	/**
	 * 获取：商品小图
	 */
	public String getSpic() {
		return spic;
	}
	/**
	 * 设置：dr 0.正常 1.删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：dr 0.正常 1.删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：用户操作状态 0.正常 1.取消 2.删除
	 */
	public void setUstatus(Integer ustatus) {
		this.ustatus = ustatus;
	}
	/**
	 * 获取：用户操作状态 0.正常 1.取消 2.删除
	 */
	public Integer getUstatus() {
		return ustatus;
	}
	/**
	 * 设置：微信开放id
	 */
	public void setWxOpenId(String wxOpenId) {
		this.wxOpenId = wxOpenId;
	}
	/**
	 * 获取：微信开放id
	 */
	public String getWxOpenId() {
		return wxOpenId;
	}
	/**
	 * 设置：0.未选择 1.支付宝 2.微信 
	 */
	public void setPayType(Integer payType) {
		this.payType = payType;
	}
	/**
	 * 获取：0.未选择 1.支付宝 2.微信 
	 */
	public Integer getPayType() {
		return payType;
	}
	/**
	 * 设置：银行编码
	 */
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	/**
	 * 获取：银行编码
	 */
	public String getBankCode() {
		return bankCode;
	}
	/**
	 * 设置：银行名称
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	/**
	 * 获取：银行名称
	 */
	public String getBankName() {
		return bankName;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreator() {
		return creator;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：最近修改用户
	 */
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifier() {
		return modifier;
	}
	/**
	 * 设置：最近修改时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近修改时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：来源 0.线上;1.NC 2測試 3.体验
	 */
	public void setSourceType(Integer sourceType) {
		this.sourceType = sourceType;
	}
	/**
	 * 获取：来源 0.线上;1.NC 2測試 3.体验
	 */
	public Integer getSourceType() {
		return sourceType;
	}
	/**
	 * 设置：省份PK
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：省份PK
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：层次PK
	 */
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	/**
	 * 获取：层次PK
	 */
	public Long getLevelId() {
		return levelId;
	}
	/**
	 * 设置：班级PK
	 */
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	/**
	 * 获取：班级PK
	 */
	public Long getClassId() {
		return classId;
	}
	/**
	 * 设置：NCID
	 */
	public void setNcId(String ncId) {
		this.ncId = ncId;
	}
	/**
	 * 获取：NCID
	 */
	public String getNcId() {
		return ncId;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：班型PK
	 */
	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}
	/**
	 * 获取：班型PK
	 */
	public Long getClassTypeId() {
		return classTypeId;
	}
	/**
	 * 设置：专业PK
	 */
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	/**
	 * 获取：专业PK
	 */
	public Long getProfessionId() {
		return professionId;
	}
	/**
	 * 设置：nc同步code
	 */
	public void setNcCode(String ncCode) {
		this.ncCode = ncCode;
	}
	/**
	 * 获取：nc同步code
	 */
	public String getNcCode() {
		return ncCode;
	}
	/**
	 * 设置：nc同步时间
	 */
	public void setSynTime(Date synTime) {
		this.synTime = synTime;
	}
	/**
	 * 获取：nc同步时间
	 */
	public Date getSynTime() {
		return synTime;
	}
	/**
	 * 设置：产品线PK
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线PK
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：有效期(某一天)
	 */
	public void setDateValidity(Date dateValidity) {
		this.dateValidity = dateValidity;
	}
	/**
	 * 获取：有效期(某一天)
	 */
	public Date getDateValidity() {
		return dateValidity;
	}
}
