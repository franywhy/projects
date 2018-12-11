package io.renren.pojo.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.MallOrderEntity;

public class OrderPOJO implements Serializable {

	private static final long serialVersionUID = 1L;
	    //主键
		private Long orderId;
		//单号
		private String orderNo;
		//用户ID
		private Long userId;
		//商品ID
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
		//优惠券ID
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
		//微信开放ID
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
		//来源 0.线上;1.NC
		private Integer sourceType;
		//省份ID
		private Long areaId;
		//层次ID
		private Long levelId;
		//班级ID
		private Long classId;
		//NCID
		private String ncId;
		//状态
		private Integer status;
		//班型ID
		private Long classTypeId;
		//nc同步code值
		private String ncCode;
		//省份
		private String areaName;
		//层次
		private String levelName;
		//班级
		private String className;
		//班型
		private String classtypeName;
		//用户昵称
		private String nickName;
		//手机号码
		private String mobile;
		//专业
		private Long professionId;
		private String professionName;
		//学员规划数量
		private Integer userplanCount;
		//nc同步时间
		private Date synTime;
		//合同id
		private String contractId ;
		private Integer signStatus ;
		private Date contractTs ;
		private int companyId;//在线协议的公司id
		
		
		//产品线PK
	 	private Long productId;
	 	private String productName;
	 	//部门PK
	 	private Long deptId;
	 	private String deptName;
	 	//有效期
	 	private Date dateValidity;
	 	//修改之前有效期
	 	private Date oldDateValidity;
		//nc_ts
	 	private Date ncTs;

     //批量转移班级的订单id
        private List<Long> orderList;

        //是否开通题库权限
        private Integer onlyOne;
	    //教师角色
	    private Integer isTeacher;
	    //教师角色显示
	    private String isTeacherStr;
	     
	public String getContractId() {
		return contractId;
	}

	public void setContractId(String contractId) {
		this.contractId = contractId;
	}  
	
    public Integer getSignStatus() {
		return signStatus;

	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;

	}

	public Date getContractTs() {
		return contractTs;
	}

	public void setContractTs(Date contractTs) {
		this.contractTs = contractTs;
	}

public Integer getOnlyOne() {
        return onlyOne;
    }

    public void setOnlyOne(Integer onlyOne) {
        this.onlyOne = onlyOne;
    }

    public List<Long> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Long> orderList) {
        this.orderList = orderList;
    }

    public Date getNcTs() {
			return ncTs;
		}
		public void setNcTs(Date ncTs) {
			this.ncTs = ncTs;
		}
		public Date getOldDateValidity() {
			return oldDateValidity;
		}
		public void setOldDateValidity(Date oldDateValidity) {
			this.oldDateValidity = oldDateValidity;
		}
		public Long getDeptId() {
			return deptId;
		}
		public void setDeptId(Long deptId) {
			this.deptId = deptId;
		}
		public String getDeptName() {
			return deptName;
		}
		public void setDeptName(String deptName) {
			this.deptName = deptName;
		}
		public Long getProductId() {
			return productId;
		}
		public void setProductId(Long productId) {
			this.productId = productId;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public Date getDateValidity() {
			return dateValidity;
		}
		public void setDateValidity(Date dateValidity) {
			this.dateValidity = dateValidity;
		}
		public Date getSynTime() {
			return synTime;
		}
		public void setSynTime(Date synTime) {
			this.synTime = synTime;
		}
		public String getNcCode() {
			return ncCode;
		}
		public void setNcCode(String ncCode) {
			this.ncCode = ncCode;
		}
		public Integer getUserplanCount() {
			return userplanCount;
		}
		public void setUserplanCount(Integer userplanCount) {
			this.userplanCount = userplanCount;
		}
		public String getNickName() {
			return nickName;
		}
		public void setNickName(String nickName) {
			this.nickName = nickName;
		}
		public String getMobile() {
			return mobile;
		}
		public void setMobile(String mobile) {
			this.mobile = mobile;
		}
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
		public String getCommodityName() {
			return commodityName;
		}
		public void setCommodityName(String commodityName) {
			this.commodityName = commodityName;
		}
		public String getOrderName() {
			return orderName;
		}
		public void setOrderName(String orderName) {
			this.orderName = orderName;
		}
		public String getOrderDescribe() {
			return orderDescribe;
		}
		public void setOrderDescribe(String orderDescribe) {
			this.orderDescribe = orderDescribe;
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
		public Integer getPayStatus() {
			return payStatus;
		}
		public void setPayStatus(Integer payStatus) {
			this.payStatus = payStatus;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Date getPayTime() {
			return payTime;
		}
		public void setPayTime(Date payTime) {
			this.payTime = payTime;
		}
		public Double getFavorableMoney() {
			return favorableMoney;
		}
		public void setFavorableMoney(Double favorableMoney) {
			this.favorableMoney = favorableMoney;
		}
		public Long getDiscountId() {
			return discountId;
		}
		public void setDiscountId(Long discountId) {
			this.discountId = discountId;
		}
		public Date getPayCallblackTime() {
			return payCallblackTime;
		}
		public void setPayCallblackTime(Date payCallblackTime) {
			this.payCallblackTime = payCallblackTime;
		}
		public String getPayCallblackMsg() {
			return payCallblackMsg;
		}
		public void setPayCallblackMsg(String payCallblackMsg) {
			this.payCallblackMsg = payCallblackMsg;
		}
		public String getAlipayTradeNo() {
			return alipayTradeNo;
		}
		public void setAlipayTradeNo(String alipayTradeNo) {
			this.alipayTradeNo = alipayTradeNo;
		}
		public String getPayip() {
			return payip;
		}
		public void setPayip(String payip) {
			this.payip = payip;
		}
		public String getPayUrl() {
			return payUrl;
		}
		public void setPayUrl(String payUrl) {
			this.payUrl = payUrl;
		}
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
		public String getSpic() {
			return spic;
		}
		public void setSpic(String spic) {
			this.spic = spic;
		}
		public Integer getDr() {
			return dr;
		}
		public void setDr(Integer dr) {
			this.dr = dr;
		}
		public Integer getUstatus() {
			return ustatus;
		}
		public void setUstatus(Integer ustatus) {
			this.ustatus = ustatus;
		}
		public String getWxOpenId() {
			return wxOpenId;
		}
		public void setWxOpenId(String wxOpenId) {
			this.wxOpenId = wxOpenId;
		}
		public Integer getPayType() {
			return payType;
		}
		public void setPayType(Integer payType) {
			this.payType = payType;
		}
		public String getBankCode() {
			return bankCode;
		}
		public void setBankCode(String bankCode) {
			this.bankCode = bankCode;
		}
		public String getBankName() {
			return bankName;
		}
		public void setBankName(String bankName) {
			this.bankName = bankName;
		}
		public Long getCreator() {
			return creator;
		}
		public void setCreator(Long creator) {
			this.creator = creator;
		}
 

		public Date getCreationTime() {
			return creationTime;
		}
		public void setCreationTime(Date creationTime) {
			this.creationTime = creationTime;
		}
		public Long getModifier() {
			return modifier;
		}
		public void setModifier(Long modifier) {
			this.modifier = modifier;
		}
		public Date getModifiedTime() {
			return modifiedTime;
		}
		public void setModifiedTime(Date modifiedTime) {
			this.modifiedTime = modifiedTime;
		}
		public String getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(String schoolId) {
			this.schoolId = schoolId;
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
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
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
		
		
		public Long getProfessionId() {
			return professionId;
		}
		public void setProfessionId(Long professionId) {
			this.professionId = professionId;
		}
		public String getProfessionName() {
			return professionName;
		}
		public void setProfessionName(String professionName) {
			this.professionName = professionName;
		}

    @Override
    public String toString() {
        return "OrderPOJO{" +
                "orderId=" + orderId +
                ", orderNo='" + orderNo + '\'' +
                ", userId=" + userId +
                ", commodityId=" + commodityId +
                ", commodityName='" + commodityName + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderDescribe='" + orderDescribe + '\'' +
                ", totalMoney=" + totalMoney +
                ", payMoney=" + payMoney +
                ", payStatus=" + payStatus +
                ", remark='" + remark + '\'' +
                ", payTime=" + payTime +
                ", favorableMoney=" + favorableMoney +
                ", discountId=" + discountId +
                ", payCallblackTime=" + payCallblackTime +
                ", payCallblackMsg='" + payCallblackMsg + '\'' +
                ", alipayTradeNo='" + alipayTradeNo + '\'' +
                ", payip='" + payip + '\'' +
                ", payUrl='" + payUrl + '\'' +
                ", pic='" + pic + '\'' +
                ", spic='" + spic + '\'' +
                ", dr=" + dr +
                ", ustatus=" + ustatus +
                ", wxOpenId='" + wxOpenId + '\'' +
                ", payType=" + payType +
                ", bankCode='" + bankCode + '\'' +
                ", bankName='" + bankName + '\'' +
                ", creator=" + creator +
                ", creationTime=" + creationTime +
                ", modifier=" + modifier +
                ", modifiedTime=" + modifiedTime +
                ", schoolId='" + schoolId + '\'' +
                ", sourceType=" + sourceType +
                ", areaId=" + areaId +
                ", levelId=" + levelId +
                ", classId=" + classId +
                ", ncId='" + ncId + '\'' +
                ", status=" + status +
                ", classTypeId=" + classTypeId +
                ", ncCode='" + ncCode + '\'' +
                ", areaName='" + areaName + '\'' +
                ", levelName='" + levelName + '\'' +
                ", className='" + className + '\'' +
                ", classtypeName='" + classtypeName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", professionId=" + professionId +
                ", professionName='" + professionName + '\'' +
                ", userplanCount=" + userplanCount +
                ", synTime=" + synTime +
                ", productId=" + productId +
                ", productName='" + productName + '\'' +
                ", deptId=" + deptId +
                ", deptName='" + deptName + '\'' +
                ", dateValidity=" + dateValidity +
                ", oldDateValidity=" + oldDateValidity +
                ", ncTs=" + ncTs +
                ", orderList=" + orderList +
                ", onlyOne=" + onlyOne +
                '}';
    }

    public static MallOrderEntity getEntity(OrderPOJO pojo){
			MallOrderEntity en = new MallOrderEntity();
			if(null != pojo){
				//主键
				en.setOrderId(pojo.getOrderId());
				//单号
				en.setOrderNo(pojo.getOrderNo());
				//用户ID
				en.setUserId(pojo.getUserId());
				//商品ID
				en.setCommodityId(pojo.getCommodityId());
				//商品名称
				en.setCommodityName(pojo.getCommodityName());
				//订单名称
				en.setOrderName(pojo.getOrderName());
				//订单描述
				en.setOrderDescribe(pojo.getOrderDescribe());
				//订单总额
				en.setTotalMoney(pojo.getTotalMoney());
				//支付金额
				en.setPayMoney(pojo.getPayMoney());
				//支付状态 0.未支付 1.发起支付 ,2.支付成功
				en.setPayStatus(pojo.getPayStatus());
				//备注
				en.setRemark(pojo.getRemark());
				//支付时间
				en.setPayTime(pojo.getPayTime());
				//优惠金额
				en.setFavorableMoney(pojo.getFavorableMoney());
				//优惠券ID
				en.setDiscountId(pojo.getDiscountId());
				//第三方支付回调时间
				en.setPayCallblackTime(pojo.getPayCallblackTime());
				//第三方支付回调信息
				en.setPayCallblackMsg(pojo.getPayCallblackMsg());
				//支付宝交易号
				en.setAlipayTradeNo(pojo.getAlipayTradeNo());
				//支付IP
				en.setPayip(pojo.getPayip());
				//生成支付地址
				en.setPayUrl(pojo.getPayUrl());
				//商品图片
				en.setPic(pojo.getPic());
				//商品小图
				en.setSpic(pojo.getSpic());
				//dr 0.正常 1.删除
				en.setDr(pojo.getDr());
				//用户操作状态 0.正常 1.取消 2.删除
				en.setUstatus(pojo.getUstatus());
				//微信开放ID
				en.setWxOpenId(pojo.getWxOpenId());
				//0.未选择 1.支付宝 2.微信 
				en.setPayType(pojo.getPayType());
				//银行编码
				en.setBankCode(pojo.getBankCode());
				//银行名称
				en.setBankName(pojo.getBankName());
				//创建用户
				en.setCreator(pojo.getCreator());
				//创建时间
				en.setCreationTime(pojo.getCreationTime());
				//最近修改用户
				en.setModifier(pojo.getModifier());
				//最近修改时间
				en.setModifiedTime(pojo.getModifiedTime());
				//平台ID
				en.setSchoolId(pojo.getSchoolId());
				//来源 0.线上;1.NC
				en.setSourceType(pojo.getSourceType());
				//省份ID
				en.setAreaId(pojo.getAreaId());
				//层次ID
				en.setLevelId(pojo.getLevelId());
				//班级ID
				en.setClassId(pojo.getClassId());
				//nc同步时间
				en.setSynTime(pojo.getSynTime());
				//NCID
				en.setNcId(pojo.getNcId());
				//状态
				en.setStatus(pojo.getStatus());
				//班型ID
				en.setClassTypeId(pojo.getClassTypeId());
				en.setProfessionId(pojo.getProfessionId());
				en.setNcCode(pojo.getNcCode());
				en.setDeptId(pojo.getDeptId());
				en.setDateValidity(pojo.getDateValidity());
				en.setOldDateValidity(pojo.getOldDateValidity());
				en.setNcTs(pojo.getNcTs());
                en.setOnlyOne(pojo.getOnlyOne());
			}
			return en;
		}

	public Integer getIsTeacher() {
		return isTeacher;
	}

	public void setIsTeacher(Integer isTeacher) {
		this.isTeacher = isTeacher;
	}

	public String getIsTeacherStr() {
   	 	if(isTeacher.intValue()==0){
			isTeacherStr = "正常";
		}else{
			isTeacherStr = "其他";
		}
		return isTeacherStr;
	}

	public void setIsTeacherStr(String isTeacherStr) {
		this.isTeacherStr = isTeacherStr;
	}

	public int getCompanyId() {
		return companyId;
	}

	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	
	
	
}
