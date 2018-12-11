package com.bestpay.model;

public class CommInfoModel{
	private String MerchantID;			//SP身份标识，type=String 
	private String SubMerchantID;       //子商户ID
	private String OrderSeq;            //订单号
  	private String OrderReqTranSeq;		//订单请求流水号
  	private String OrderDate;			//订单日期
  	private String OrderAmount;			//总金额，单位为分
  	private String ProductAmount;		//产品金额，单位为分
  	private String AttachAmount;		//附加金额，单位为分
	private String CurType;				//币种
  	private String EncodeType;			//加密方式
  	private String MerchantUrl;         //前台地址
  	private String BackMerchantUrl;     //后台地址
  	private String Attach;              //附加信息
  	private String BusiCode;            //业务类型代码
  	private String ProductID;            //业务标识
  	private String TmNum;               //终端号码
  	private String CustomerID;          //客户标识
  	private String ProductDesc;         //产品描述
  	private String mac;                 //MAC校验域
  	private String Divdetails;        //分账明细
  	private String Pedcnt;     //分期数
  	private String ClientIP;//客户端IP
  	private String BankID;   //银行编码
  	
  	private String REQ_PC_URL;
	public String getBankID() {
		return BankID;
	}
	public void setBankID(String bankID) {
		BankID = bankID;
	}
	public String getMerchantID() {
		return MerchantID;
	}
	public void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}
	public String getSubMerchantID() {
		return SubMerchantID;
	}
	public void setSubMerchantID(String subMerchantID) {
		SubMerchantID = subMerchantID;
	}
	public String getOrderSeq() {
		return OrderSeq;
	}
	public void setOrderSeq(String orderSeq) {
		OrderSeq = orderSeq;
	}
	public String getOrderReqTranSeq() {
		return OrderReqTranSeq;
	}
	public void setOrderReqTranSeq(String orderReqTranSeq) {
		OrderReqTranSeq = orderReqTranSeq;
	}
	public String getOrderDate() {
		return OrderDate;
	}
	public void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}
	public String getOrderAmount() {
		return OrderAmount;
	}
	public void setOrderAmount(String orderAmount) {
		OrderAmount = orderAmount;
	}
	public String getProductAmount() {
		return ProductAmount;
	}
	public void setProductAmount(String productAmount) {
		ProductAmount = productAmount;
	}
	public String getAttachAmount() {
		return AttachAmount;
	}
	public void setAttachAmount(String attachAmount) {
		AttachAmount = attachAmount;
	}
	public String getCurType() {
		return CurType;
	}
	public void setCurType(String curType) {
		CurType = curType;
	}
	public String getEncodeType() {
		return EncodeType;
	}
	public void setEncodeType(String encodeType) {
		EncodeType = encodeType;
	}
	public String getMerchantUrl() {
		return MerchantUrl;
	}
	public void setMerchantUrl(String merchantUrl) {
		MerchantUrl = merchantUrl;
	}
	public String getBackMerchantUrl() {
		return BackMerchantUrl;
	}
	public void setBackMerchantUrl(String backMerchantUrl) {
		BackMerchantUrl = backMerchantUrl;
	}
	public String getAttach() {
		return Attach;
	}
	public void setAttach(String attach) {
		Attach = attach;
	}
	public String getBusiCode() {
		return BusiCode;
	}
	public void setBusiCode(String busiCode) {
		BusiCode = busiCode;
	}
	public String getProductID() {
		return ProductID;
	}
	public void setProductID(String productID) {
		ProductID = productID;
	}
	public String getTmNum() {
		return TmNum;
	}
	public void setTmNum(String tmNum) {
		TmNum = tmNum;
	}
	public String getCustomerID() {
		return CustomerID;
	}
	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}
	public String getProductDesc() {
		return ProductDesc;
	}
	public void setProductDesc(String productDesc) {
		ProductDesc = productDesc;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getDivdetails() {
		return Divdetails;
	}
	public void setDivdetails(String divdetails) {
		Divdetails = divdetails;
	}
	public String getPedcnt() {
		return Pedcnt;
	}
	public void setPedcnt(String pedcnt) {
		Pedcnt = pedcnt;
	}
	public String getClientIP() {
		return ClientIP;
	}
	public void setClientIP(String clientIP) {
		ClientIP = clientIP;
	}
	public String getREQ_PC_URL() {
		return REQ_PC_URL;
	}
	public void setREQ_PC_URL(String rEQ_PC_URL) {
		REQ_PC_URL = rEQ_PC_URL;
	}
}