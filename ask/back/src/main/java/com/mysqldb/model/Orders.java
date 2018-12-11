package com.mysqldb.model;


import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;

/**
 * Author: 汤垲峰-MagicalTools
 * Date: 2016-03-31 19:16:53
 */
@Entity
@Table(name = "orders")
@org.hibernate.annotations.Table(appliesTo = "orders", comment = "orders")
public class Orders implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * orders
	 */
	public static final String REF="Orders";

	/**
	 * id 的属性名
	 */
	public static final String PROP_ID="id";
	
	/**
	 * 订单编号 的属性名
	 */
	public static final String PROP_ORDERNO="orderno";
	
	/**
	 * 购买用户id 的属性名
	 */
	public static final String PROP_USERID="userId";
	
	/**
	 * 商品ID 的属性名
	 */
	public static final String PROP_PRODUCTID="productId";
	
	/**
	 * NC商品id 的属性名
	 */
	public static final String PROP_NCPRODUCTID="ncProductId";
	
	/**
	 * pic 的属性名
	 */
	public static final String PROP_PIC="pic";
	
	/**
	 * 订单描述 的属性名
	 */
	public static final String PROP_ORDERDESCRIBE="orderDescribe";
	
	/**
	 * 校区ncId 的属性名
	 */
	public static final String PROP_NCSCHOOLID="ncSchoolId";
	
	/**
	 * 订单金额 的属性名
	 */
	public static final String PROP_TOTALMONEY="totalMoney";
	
	/**
	 * 支付状态 0.未支付 1.待支付 ,2.支付成功 的属性名
	 */
	public static final String PROP_PAYSTATUS="payStatus";
	
	/**
	 * 支付方式 0.在线支付 1.分期付款 的属性名
	 */
	public static final String PROP_PAYTYPE="payType";
	
	/**
	 * 订单状态 0.有效 1.取消 的属性名
	 */
	public static final String PROP_STATUS="status";
	
	/**
	 * 订单有效期 的属性名
	 */
	public static final String PROP_VALIDITY="validity";
	
	/**
	 * 备注 的属性名
	 */
	public static final String PROP_REMARK="remark";
	
	/**
	 * 创建时间 的属性名
	 */
	public static final String PROP_CREATETIME="createTime";
	
	/**
	 * 最后修改人 的属性名
	 */
	public static final String PROP_MODIFIER="modifier";
	
	/**
	 * 最后修改时间 的属性名
	 */
	public static final String PROP_MODIFIEDTIME="modifiedtime";
	
	/**
	 * 是否删除 的属性名
	 */
//	public static final String PROP_DR="dr";
	
	/**
	 * 支付时间 的属性名
	 */
	public static final String PROP_PAYTIME="payTime";
	
	/**
	 * 商品类型 0.VIP 1.课程商品 的属性名
	 */
	public static final String PROP_PRODUCTTYPE="productType";
	
	/**
	 * 实际支付金额 的属性名
	 */
	public static final String PROP_PAYMONEY="payMoney";
	
	/**
	 * 优惠金额 的属性名
	 */
	public static final String PROP_FAVORABLEMONEY="favorableMoney";
	
	/**
	 * 优惠方案 0.不使用优惠 1.积分 2.优惠券 的属性名
	 */
	public static final String PROP_PREFERENCESCHEME="preferenceScheme";
	
	/**
	 * 使用积分 的属性名
	 */
	public static final String PROP_USEINTEGRAL="useIntegral";
	
	/**
	 * 优惠券id 的属性名
	 */
	public static final String PROP_COUPONID="couponId";
	
	/**
	 * 线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱 的属性名
	 */
	public static final String PROP_PAYONLINETYPE="payOnLineType";
	
	/**
	 * 支付回调时间 的属性名
	 */
	public static final String PROP_PAYCALLBLACKTIME="payCallblackTime";
	
	/**
	 * 支付回调备注 的属性名
	 */
	public static final String PROP_PAYCALLBLACKREMARK="payCallblackRemark";
	
	/**
	 * 是否取消 用户操作 的属性名
	 */
//	public static final String PROP_ISCANCEL="isCancel";
	
	/**
	 * 学生期望上课时间段 
	 */
	public static final String PROP_HOPECLASSTIME="hopeClassTime";
	
	/**
	 * 订单名称 的属性名
	 */
	public static final String PROP_ORDERNAME="orderName";
	
	/**
	 * 支付宝交易号 的属性名
	 */
	public static final String PROP_ALIPAYTRADENO="alipayTradeNo";
	
	/**
	 * 支付IP 的属性名
	 */
	public static final String PROP_PAYIP="payip";
	
	/**
	 * 支付地址 的属性名
	 */
	public static final String PROP_PAYURL="payUrl";
	
	/**
	 * 赠送积分 的属性名
	 */
	public static final String PROP_ADDSCORE="addScore";
	
	
	
	/**
	 * id
	 */
	private java.lang.Integer id;
	
	/**
	 * 订单编号
	 */
	private java.lang.String orderno;
	
	/**
	 * 购买用户id
	 */
	private java.lang.Integer userId;
	
	/**
	 * 商品ID
	 */
	private java.lang.String productId;
	
	/**
	 * NC商品id
	 */
	private java.lang.String ncProductId;
	
	/**
	 * pic
	 */
	private java.lang.String pic;
	
	/**
	 * 订单名称
	 */
	private java.lang.String orderName;
	
	/**
	 * 订单描述
	 */
	private java.lang.String orderDescribe;
	
	/**
	 * 校区ncId
	 */
	private java.lang.String ncSchoolId;
	
	/**
	 * 订单金额
	 */
	private java.lang.Double totalMoney;
	
	/**
	 * 支付状态 0.未支付 1.待支付 ,2.支付成功
	 */
	private java.lang.Integer payStatus;
	
	/**
	 * 支付方式 0.在线支付 1.分期付款
	 */
	private java.lang.Integer payType;
	
	/**
	 * 订单状态 0.有效 1.取消
	 */
	private java.lang.Integer status;
	
	/**
	 * 订单有效期
	 */
	private java.sql.Timestamp validity;
	
	/**
	 * 备注
	 */
	private java.lang.String remark;
	
	/**
	 * 创建时间
	 */
	private java.sql.Timestamp createTime;
	
	/**
	 * 最后修改人
	 */
	private java.lang.Integer modifier;
	
	/**
	 * 最后修改时间
	 */
	private java.sql.Timestamp modifiedtime;
	
	/**
	 * 是否删除
	 */
//	private java.lang.Integer dr;
	
	/**
	 * 支付时间
	 */
	private java.sql.Timestamp payTime;
	
	/**
	 * 商品类型 0.VIP 1.课程商品
	 */
	private java.lang.Integer productType;
	
	/**
	 * 实际支付金额
	 */
	private java.lang.Double payMoney;
	
	/**
	 * 优惠金额
	 */
	private java.lang.Double favorableMoney;
	
	/**
	 * 优惠方案 0.不使用优惠 1.积分 2.优惠券
	 */
	private java.lang.Integer preferenceScheme;
	
	/**
	 * 使用积分
	 */
	private java.lang.Integer useIntegral;
	
	/**
	 * 优惠券id
	 */
	private Integer couponId;
	
	/**
	 * 线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱
	 */
	private java.lang.Integer payOnLineType;
	
	/**
	 * 支付回调时间
	 */
	private java.sql.Timestamp payCallblackTime;
	
	/**
	 * 支付回调备注
	 */
	private java.lang.String payCallblackRemark;
	
	/**
	 * 是否取消 用户操作
	 */
//	private java.lang.Integer isCancel;
	
	/**
	 * 学生期望上课时间段 
	 */
	private String hopeClassTime;
	
	/**
	 * 支付宝交易号
	 */
	private java.lang.String alipayTradeNo;
	

	/**
	 * 支付IP
	 */
	private java.lang.String payip;
	
	/**
	 * 支付地址
	 */
	private java.lang.String payUrl;
	
	/**
	 * 赠送积分
	 */
	private java.lang.Double addScore;
	
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
	 * 获取 订单编号 的属性值
     *
	 * @return orderno :  订单编号 
	 */
	@Column(name = "ORDERNO",columnDefinition = "VARCHAR")
	public java.lang.String getOrderno(){
		return this.orderno;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单编号 的属性值
	 *		
	 * @param orderno :  订单编号 
	 */
	public void setOrderno(java.lang.String orderno){
		this.orderno	= orderno;
	}
	/**
	 * Author name@mail.com
	 * 获取 购买用户id 的属性值
     *
	 * @return userId :  购买用户id 
	 */
	@Column(name = "USER_ID",columnDefinition = "INT")
	public java.lang.Integer getUserId(){
		return this.userId;
	}

	/**
	 * Author name@mail.com
	 * 设置 购买用户id 的属性值
	 *		
	 * @param userId :  购买用户id 
	 */
	public void setUserId(java.lang.Integer userId){
		this.userId	= userId;
	}
	/**
	 * Author name@mail.com
	 * 获取 商品ID 的属性值
     *
	 * @return productId :  商品ID 
	 */
	@Column(name = "PRODUCT_ID",columnDefinition = "VARCHAR")
	public java.lang.String getProductId(){
		return this.productId;
	}

	/**
	 * Author name@mail.com
	 * 设置 商品ID 的属性值
	 *		
	 * @param productId :  商品ID 
	 */
	public void setProductId(java.lang.String productId){
		this.productId	= productId;
	}
	/**
	 * Author name@mail.com
	 * 获取 NC商品id 的属性值
     *
	 * @return ncProductId :  NC商品id 
	 */
	@Column(name = "NC_PRODUCT_ID",columnDefinition = "VARCHAR")
	public java.lang.String getNcProductId(){
		return this.ncProductId;
	}

	/**
	 * Author name@mail.com
	 * 设置 NC商品id 的属性值
	 *		
	 * @param ncProductId :  NC商品id 
	 */
	public void setNcProductId(java.lang.String ncProductId){
		this.ncProductId	= ncProductId;
	}
	/**
	 * Author name@mail.com
	 * 获取 pic 的属性值
     *
	 * @return pic :  pic 
	 */
	@Column(name = "PIC",columnDefinition = "VARCHAR")
	public java.lang.String getPic(){
		return this.pic;
	}

	/**
	 * Author name@mail.com
	 * 设置 pic 的属性值
	 *		
	 * @param pic :  pic 
	 */
	public void setPic(java.lang.String pic){
		this.pic	= pic;
	}
	/**
	 * Author name@mail.com
	 * 获取 订单描述 的属性值
     *
	 * @return orderDescribe :  订单描述 
	 */
	@Column(name = "ORDER_DESCRIBE",columnDefinition = "VARCHAR")
	public java.lang.String getOrderDescribe(){
		return this.orderDescribe;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单描述 的属性值
	 *		
	 * @param orderDescribe :  订单描述 
	 */
	public void setOrderDescribe(java.lang.String orderDescribe){
		this.orderDescribe	= orderDescribe;
	}
	/**
	 * Author name@mail.com
	 * 获取 校区ncId 的属性值
     *
	 * @return ncSchoolId :  校区ncId
	 */
	@Column(name = "NC_SCHOOL_ID",columnDefinition = "VARCHAR")
	public java.lang.String getNcSchoolId(){
		return this.ncSchoolId;
	}

	/**
	 * Author name@mail.com
	 * 设置校区ncId 的属性值
	 *		
	 * @param ncSchoolId :  校区ncId
	 */
	public void setNcSchoolId(java.lang.String ncSchoolId){
		this.ncSchoolId	= ncSchoolId;
	}
	/**
	 * Author name@mail.com
	 * 获取 订单金额 的属性值
     *
	 * @return totalMoney :  订单金额 
	 */
	@Column(name = "TOTAL_MONEY",columnDefinition = "DOUBLE")
	public java.lang.Double getTotalMoney(){
		return this.totalMoney;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单金额 的属性值
	 *		
	 * @param totalMoney :  订单金额 
	 */
	public void setTotalMoney(java.lang.Double totalMoney){
		this.totalMoney	= totalMoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 支付状态 0.未支付 1.待支付 ,2.支付成功 的属性值
     *
	 * @return payStatus :  支付状态 0.未支付 1.待支付 ,2.支付成功 
	 */
	@Column(name = "PAY_STATUS",columnDefinition = "INT")
	public java.lang.Integer getPayStatus(){
		return this.payStatus;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付状态 0.未支付 1.待支付 ,2.支付成功 的属性值
	 *		
	 * @param payStatus :  支付状态 0.未支付 1.待支付 ,2.支付成功 
	 */
	public void setPayStatus(java.lang.Integer payStatus){
		this.payStatus	= payStatus;
	}
	/**
	 * Author name@mail.com
	 * 获取 支付方式 0.在线支付 1.分期付款 的属性值
     *
	 * @return payType :  支付方式 0.在线支付 1.分期付款 
	 */
	@Column(name = "PAY_TYPE",columnDefinition = "INT")
	public java.lang.Integer getPayType(){
		return this.payType;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付方式 0.在线支付 1.分期付款 的属性值
	 *		
	 * @param payType :  支付方式 0.在线支付 1.分期付款 
	 */
	public void setPayType(java.lang.Integer payType){
		this.payType	= payType;
	}
	/**
	 * Author name@mail.com
	 * 获取 订单状态 0.有效 1.取消 的属性值
     *
	 * @return status :  订单状态 0.有效 1.取消 
	 */
	@Column(name = "STATUS",columnDefinition = "INT")
	public java.lang.Integer getStatus(){
		return this.status;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单状态 0.有效 1.取消 的属性值
	 *		
	 * @param status :  订单状态 0.有效 1.取消 
	 */
	public void setStatus(java.lang.Integer status){
		this.status	= status;
	}
	/**
	 * Author name@mail.com
	 * 获取 订单有效期 的属性值
     *
	 * @return validity :  订单有效期 
	 */
	@Column(name = "VALIDITY",columnDefinition = "DATETIME")
	public java.sql.Timestamp getValidity(){
		return this.validity;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单有效期 的属性值
	 *		
	 * @param validity :  订单有效期 
	 */
	public void setValidity(java.sql.Timestamp validity){
		this.validity	= validity;
	}
	/**
	 * Author name@mail.com
	 * 获取 备注 的属性值
     *
	 * @return remark :  备注 
	 */
	@Column(name = "REMARK",columnDefinition = "VARCHAR")
	public java.lang.String getRemark(){
		return this.remark;
	}

	/**
	 * Author name@mail.com
	 * 设置 备注 的属性值
	 *		
	 * @param remark :  备注 
	 */
	public void setRemark(java.lang.String remark){
		this.remark	= remark;
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
	 * 获取 最后修改人 的属性值
     *
	 * @return modifier :  最后修改人 
	 */
	@Column(name = "MODIFIER",columnDefinition = "INT")
	public java.lang.Integer getModifier(){
		return this.modifier;
	}

	/**
	 * Author name@mail.com
	 * 设置 最后修改人 的属性值
	 *		
	 * @param modifier :  最后修改人 
	 */
	public void setModifier(java.lang.Integer modifier){
		this.modifier	= modifier;
	}
	/**
	 * Author name@mail.com
	 * 获取 最后修改时间 的属性值
     *
	 * @return modifiedtime :  最后修改时间 
	 */
	@Column(name = "MODIFIEDTIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getModifiedtime(){
		return this.modifiedtime;
	}

	/**
	 * Author name@mail.com
	 * 设置 最后修改时间 的属性值
	 *		
	 * @param modifiedtime :  最后修改时间 
	 */
	public void setModifiedtime(java.sql.Timestamp modifiedtime){
		this.modifiedtime	= modifiedtime;
	}
//	/**
//	 * Author name@mail.com
//	 * 获取 是否删除 的属性值
//     *
//	 * @return dr :  是否删除 
//	 */
//	@Column(name = "DR",columnDefinition = "INT")
//	public java.lang.Integer getDr(){
//		return this.dr;
//	}
//
//	/**
//	 * Author name@mail.com
//	 * 设置 是否删除 的属性值
//	 *		
//	 * @param dr :  是否删除 
//	 */
//	public void setDr(java.lang.Integer dr){
//		this.dr	= dr;
//	}
	/**
	 * Author name@mail.com
	 * 获取 支付时间 的属性值
     *
	 * @return payTime :  支付时间 
	 */
	@Column(name = "PAY_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getPayTime(){
		return this.payTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付时间 的属性值
	 *		
	 * @param payTime :  支付时间 
	 */
	public void setPayTime(java.sql.Timestamp payTime){
		this.payTime	= payTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 商品类型 0.VIP 1.课程商品 的属性值
     *
	 * @return productType :  商品类型 0.VIP 1.课程商品 
	 */
	@Column(name = "PRODUCT_TYPE",columnDefinition = "INT")
	public java.lang.Integer getProductType(){
		return this.productType;
	}

	/**
	 * Author name@mail.com
	 * 设置 商品类型 0.VIP 1.课程商品 的属性值
	 *		
	 * @param productType :  商品类型 0.VIP 1.课程商品 
	 */
	public void setProductType(java.lang.Integer productType){
		this.productType	= productType;
	}
	/**
	 * Author name@mail.com
	 * 获取 实际支付金额 的属性值
     *
	 * @return payMoney :  实际支付金额 
	 */
	@Column(name = "PAY_MONEY",columnDefinition = "DOUBLE")
	public java.lang.Double getPayMoney(){
		return this.payMoney;
	}

	/**
	 * Author name@mail.com
	 * 设置 实际支付金额 的属性值
	 *		
	 * @param payMoney :  实际支付金额 
	 */
	public void setPayMoney(java.lang.Double payMoney){
		this.payMoney	= payMoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠金额 的属性值
     *
	 * @return favorableMoney :  优惠金额 
	 */
	@Column(name = "FAVORABLE_MONEY",columnDefinition = "DOUBLE")
	public java.lang.Double getFavorableMoney(){
		return this.favorableMoney;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠金额 的属性值
	 *		
	 * @param favorableMoney :  优惠金额 
	 */
	public void setFavorableMoney(java.lang.Double favorableMoney){
		this.favorableMoney	= favorableMoney;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠方案 0.不使用优惠 1.积分 2.优惠券 的属性值
     *
	 * @return preferenceScheme :  优惠方案 0.不使用优惠 1.积分 2.优惠券 
	 */
	@Column(name = "PREFERENCE_SCHEME",columnDefinition = "INT")
	public java.lang.Integer getPreferenceScheme(){
		return this.preferenceScheme;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠方案 0.不使用优惠 1.积分 2.优惠券 的属性值
	 *		
	 * @param preferenceScheme :  优惠方案 0.不使用优惠 1.积分 2.优惠券 
	 */
	public void setPreferenceScheme(java.lang.Integer preferenceScheme){
		this.preferenceScheme	= preferenceScheme;
	}
	/**
	 * Author name@mail.com
	 * 获取 使用积分 的属性值
     *
	 * @return useIntegral :  使用积分 
	 */
	@Column(name = "USE_INTEGRAL",columnDefinition = "INT")
	public java.lang.Integer getUseIntegral(){
		return this.useIntegral;
	}

	/**
	 * Author name@mail.com
	 * 设置 使用积分 的属性值
	 *		
	 * @param useIntegral :  使用积分 
	 */
	public void setUseIntegral(java.lang.Integer useIntegral){
		this.useIntegral	= useIntegral;
	}
	/**
	 * Author name@mail.com
	 * 获取 优惠券id 的属性值
     *
	 * @return couponId :  优惠券id 
	 */
	@Column(name = "COUPON_ID",columnDefinition = "INT")
	public Integer getCouponId(){
		return this.couponId;
	}

	/**
	 * Author name@mail.com
	 * 设置 优惠券id 的属性值
	 *		
	 * @param couponId :  优惠券id 
	 */
	public void setCouponId(Integer couponId){
		this.couponId	= couponId;
	}
	/**
	 * Author name@mail.com
	 * 获取 线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱 的属性值
     *
	 * @return payOnLineType :  线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱 
	 */
	@Column(name = "PAY_ON_LINE_TYPE",columnDefinition = "INT")
	public java.lang.Integer getPayOnLineType(){
		return this.payOnLineType;
	}

	/**
	 * Author name@mail.com
	 * 设置 线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱 的属性值
	 *		
	 * @param payOnLineType :  线上付款方式 0.undefined 1.支付宝 2.微信 3.快钱 
	 */
	public void setPayOnLineType(java.lang.Integer payOnLineType){
		this.payOnLineType	= payOnLineType;
	}
	/**
	 * Author name@mail.com
	 * 获取 支付回调时间 的属性值
     *
	 * @return payCallblackTime :  支付回调时间 
	 */
	@Column(name = "PAY_CALLBLACK_TIME",columnDefinition = "DATETIME")
	public java.sql.Timestamp getPayCallblackTime(){
		return this.payCallblackTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付回调时间 的属性值
	 *		
	 * @param payCallblackTime :  支付回调时间 
	 */
	public void setPayCallblackTime(java.sql.Timestamp payCallblackTime){
		this.payCallblackTime	= payCallblackTime;
	}
	/**
	 * Author name@mail.com
	 * 获取 支付回调备注 的属性值
     *
	 * @return payCallblackRemark :  支付回调备注 
	 */
	@Column(name = "PAY_CALLBLACK_REMARK",columnDefinition = "TEXT")
	public java.lang.String getPayCallblackRemark(){
		return this.payCallblackRemark;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付回调备注 的属性值
	 *		
	 * @param payCallblackRemark :  支付回调备注 
	 */
	public void setPayCallblackRemark(java.lang.String payCallblackRemark){
		this.payCallblackRemark	= payCallblackRemark;
	}
	/**
//	 * Author name@mail.com
//	 * 获取 是否取消 用户操作 的属性值
//     *
//	 * @return isCancel :  是否取消 用户操作 
//	 */
//	@Column(name = "IS_CANCEL",columnDefinition = "INT")
//	public java.lang.Integer getIsCancel(){
//		return this.isCancel;
//	}
//
//	/**
//	 * Author name@mail.com
//	 * 设置 是否取消 用户操作 的属性值
//	 *		
//	 * @param isCancel :  是否取消 用户操作 
//	 */
//	public void setIsCancel(java.lang.Integer isCancel){
//		this.isCancel	= isCancel;
//	}
	/**
	 * Author name@mail.com
	 * 获取 学生期望上课时间段
     *
	 * @return hopeClassTimeType :  学生期望上课时间段 
	 */
	@Column(name = "HOPE_CLASS_TIME",columnDefinition = "VARCHAR")
	public String getHopeClassTime(){
		return this.hopeClassTime;
	}

	/**
	 * Author name@mail.com
	 * 设置 学生期望上课时间段 
	 *		
	 * @param hopeClassTimeType :  学生期望上课时间段 
	 */
	public void setHopeClassTime(String hopeClassTime){
		this.hopeClassTime	= hopeClassTime;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 订单名称 的属性值
     *
	 * @return orderName :  订单名称 
	 */
	@Column(name = "ORDER_NAME",columnDefinition = "VARCHAR")
	public java.lang.String getOrderName(){
		return this.orderName;
	}

	/**
	 * Author name@mail.com
	 * 设置 订单名称 的属性值
	 *		
	 * @param orderName :  订单名称 
	 */
	public void setOrderName(java.lang.String orderName){
		this.orderName	= orderName;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 支付宝交易号 的属性值
     *
	 * @return alipayTradeNo :  支付宝交易号 
	 */
	@Column(name = "ALIPAY_TRADE_NO",columnDefinition = "VARCHAR")
	public java.lang.String getAlipayTradeNo(){
		return this.alipayTradeNo;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付宝交易号 的属性值
	 *		
	 * @param alipayTradeNo :  支付宝交易号 
	 */
	public void setAlipayTradeNo(java.lang.String alipayTradeNo){
		this.alipayTradeNo	= alipayTradeNo;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 支付IP 的属性值
     *
	 * @return payip :  支付IP 
	 */
	@Column(name = "PAYIP",columnDefinition = "VARCHAR")
	public java.lang.String getPayip(){
		return this.payip;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付IP 的属性值
	 *		
	 * @param payip :  支付IP 
	 */
	public void setPayip(java.lang.String payip){
		this.payip	= payip;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 支付地址 的属性值
     *
	 * @return payUrl :  支付地址 
	 */
	@Column(name = "PAY_URL",columnDefinition = "TEXT")
	public java.lang.String getPayUrl(){
		return this.payUrl;
	}

	/**
	 * Author name@mail.com
	 * 设置 支付地址 的属性值
	 *		
	 * @param payUrl :  支付地址 
	 */
	public void setPayUrl(java.lang.String payUrl){
		this.payUrl	= payUrl;
	}
	
	/**
	 * Author name@mail.com
	 * 获取 赠送积分 的属性值
     *
	 * @return addScore :  赠送积分 
	 */
	@Column(name = "ADD_SCORE",columnDefinition = "Double")
	public java.lang.Double getAddScore(){
		return this.addScore;
	}

	/**
	 * Author name@mail.com
	 * 设置 赠送积分 的属性值
	 *		
	 * @param addScore :  赠送积分 
	 */
	public void setAddScore(java.lang.Double addScore){
		this.addScore	= addScore;
	}
	
	/**
	 * Author name@mail.com
	 * 转换为字符串
	 */
	public String toString(){		
		return "{ _name=Orders" + ",id=" + id +",orderno=" + orderno +",userId=" + userId +",productId=" + productId +",ncProductId=" + ncProductId +",pic=" + pic +",orderName=" + orderName +",orderDescribe=" + orderDescribe +",ncSchoolId=" + ncSchoolId +",totalMoney=" + totalMoney +",payStatus=" + payStatus +",payType=" + payType +",status=" + status +",validity=" + validity +",remark=" + remark +",createTime=" + createTime +",modifier=" + modifier +",modifiedtime=" + modifiedtime +",payTime=" + payTime +",productType=" + productType +",payMoney=" + payMoney +",favorableMoney=" + favorableMoney +",preferenceScheme=" + preferenceScheme +",useIntegral=" + useIntegral +",couponId=" + couponId +",payOnLineType=" + payOnLineType +",payCallblackTime=" + payCallblackTime +",payCallblackRemark=" + payCallblackRemark  + ",hopeClassTime=" + hopeClassTime +",alipayTradeNo=" + alipayTradeNo +",payip=" + payip +",payUrl=" + payUrl +",addScore=" + addScore + "}";
	}
	
	/**
	 * Author name@mail.com
	 * 转换为 JSON 字符串
	 */
	public String toJson(){
		return "{ _name:'Orders'" + ",id:'" + id + "'" +",orderno:'" + orderno + "'" +",userId:'" + userId + "'" +",productId:'" + productId + "'" +",ncProductId:'" + ncProductId + "'" +",pic:'" + pic + "'" +",orderName:'" + orderName + "'" +",orderDescribe:'" + orderDescribe + "'" +",ncSchoolId:'" + ncSchoolId + "'" +",totalMoney:'" + totalMoney + "'" +",payStatus:'" + payStatus + "'" +",payType:'" + payType + "'" +",status:'" + status + "'" +",validity:'" + validity + "'" +",remark:'" + remark + "'" +",createTime:'" + createTime + "'" +",modifier:'" + modifier + "'" +",modifiedtime:'" + modifiedtime  + "'" +",payTime:'" + payTime + "'" +",productType:'" + productType + "'" +",payMoney:'" + payMoney + "'" +",favorableMoney:'" + favorableMoney + "'" +",preferenceScheme:'" + preferenceScheme + "'" +",useIntegral:'" + useIntegral + "'" +",couponId:'" + couponId + "'" +",payOnLineType:'" + payOnLineType + "'" +",payCallblackTime:'" + payCallblackTime + "'" +",payCallblackRemark:'" + payCallblackRemark + "'" +",hopeClassTime:'" + hopeClassTime + "'" +",alipayTradeNo:'" + alipayTradeNo + "'" +",payip:'" + payip + "'" +",payUrl:'" + payUrl + "'" +",addScore:'" + addScore + "'" +  "}";
	}
}
