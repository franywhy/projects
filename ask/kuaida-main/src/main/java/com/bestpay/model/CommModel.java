package com.bestpay.model;

import com.bestpay.util.CryptTool;

public class CommModel {
	
		
		private  String MerchantID="02110103012057000";   //商户代码
		
		private  String CommKey="A22B0962D5A0906D6D01D7756687BAA45DDF91E54FABDEAD";   //商户key
		
	    private  String CommPwd="144246"; //商户密码	
	    
	    private  String MerchantUrl="http://webpay.bestpay.com.cn/pageTest.do"; //前台通知地址
	    
	    private  String BackMerchantUrl="http://webpay.bestpay.com.cn/test.do"; //后台通知地址
	    
	    /*private  String OrderID="20120620132345";       //订单号(当前时间)(格式如：yyyymmddhhmmss)
	    
	    private  String OrderReqTranSeq="20120620132345000001";  //订单流水号(当前时间+000001)(格式如：yyyymmddhhmmss000001)
	    
	    private  String OrderDate="20120620";   //订单日期
	    */
	    private  String OrderID=CryptTool.getCurrentDate();       //订单号(当前时间)(格式如：yyyymmddhhmmss)
	    
	    private  String OrderReqTranSeq=CryptTool.getCurrentDate()+"000001";  //订单流水号(当前时间+000001)(格式如：yyyymmddhhmmss000001)
	    
	    private  String OrderDate=CryptTool.getTodayDate2();   //订单日期
	    
	    private  String ActionUrlSelectBank="https://webpaywg.bestpay.com.cn/payWeb.do";  //请求网关平台地址(选择银行再进行交易)
	    
	    private  String ActionUrlBank="https://webpaywg.bestpay.com.cn/payWebDirect.do";  //请求网关平台地址(直接进行交易)
	    
	   private  String ReFundUrl="http://telepay.bestpay.com.cn/services";      //退款地址
	    
	  // private  String ReqTime = "20120620143550";	//退款请求时间，格式yyyyMMddHHmmss
	    private String ReqTime=CryptTool.getCurrentDate();     //退款请求时间，格式yyyyMMddHHmmss
	    
	    private  String OrderRefundID = "REFUNDID" + System.currentTimeMillis();	//退款请求流水号
	    
	

    
    public  String getReqTime() {
		return ReqTime;
	}

	public  void setReqTime(String reqTime) {
		ReqTime = reqTime;
	}

	public  String getOrderRefundID() {
		return OrderRefundID;
	}

	public  void setOrderRefundID(String orderRefundID) {
		OrderRefundID = orderRefundID;
	}

	public  String getReFundUrl() {
		return ReFundUrl;
	}

	public  void setReFundUrl(String reFundUrl) {
		ReFundUrl = reFundUrl;
	}

	public  String getActionUrlSelectBank() {
		return ActionUrlSelectBank;
	}

	public  void setActionUrlSelectBank(String actionUrlSelectBank) {
		ActionUrlSelectBank = actionUrlSelectBank;
	}

	public  String getActionUrlBank() {
		return ActionUrlBank;
	}

	public  void setActionUrlBank(String actionUrlBank) {
		ActionUrlBank = actionUrlBank;
	}


	public  String getMerchantID() {
		return MerchantID;
	}

	public  void setMerchantID(String merchantID) {
		MerchantID = merchantID;
	}

	public  String getCommKey() {
		return CommKey;
	}

	public  void setCommKey(String commKey) {
		CommKey = commKey;
	}

	public  String getCommPwd() {
		return CommPwd;
	}

	public  void setCommPwd(String commPwd) {
		CommPwd = commPwd;
	}

	public  String getMerchantUrl() {
		return MerchantUrl;
	}

	public  void setMerchantUrl(String merchantUrl) {
		MerchantUrl = merchantUrl;
	}

	public  String getBackMerchantUrl() {
		return BackMerchantUrl;
	}

	public  void setBackMerchantUrl(String backMerchantUrl) {
		BackMerchantUrl = backMerchantUrl;
	}

	public  String getOrderID() {
		return OrderID;
	}

	public  void setOrderID(String orderID) {
		OrderID = orderID;
	}

	public  String getOrderReqTranSeq() {
		return OrderReqTranSeq;
	}

	public  void setOrderReqTranSeq(String orderReqTranSeq) {
		OrderReqTranSeq = orderReqTranSeq;
	}

	public  String getOrderDate() {
		return OrderDate;
	}

	public  void setOrderDate(String orderDate) {
		OrderDate = orderDate;
	}

	
}
