package com.izhubo.web.pay.pc.wxpay.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "xml")
public class PayResData {
	// 1
	// <xml>
	// <return_code><![CDATA[FAIL]]></return_code>
	// <return_msg><![CDATA[您没有APP支付权限]]></return_msg>
	// </xml>

	// 2
	//
	// <xml>
	// <return_code><![CDATA[SUCCESS]]></return_code>
	// <return_msg><![CDATA[OK]]></return_msg>
	// <appid><![CDATA[wx69a9416509e517c5]]></appid>
	// <mch_id><![CDATA[1344054801]]></mch_id>
	// <nonce_str><![CDATA[bN0KKx0XgF3rPQ3R]]></nonce_str>
	// <sign><![CDATA[84B20D56111552CC4EF0A3DD1FC11B33]]></sign>
	// <result_code><![CDATA[SUCCESS]]></result_code>
	// <prepay_id><![CDATA[wx20160720164512dce11d10d90381605471]]></prepay_id>
	// <trade_type><![CDATA[JSAPI]]></trade_type>
	// </xml>

	@XmlElement
	private String return_code;
	@XmlElement
	private String return_msg;

	@XmlElement
	private String appid;
	@XmlElement
	private String mch_id;
	@XmlElement
	private String nonce_str;
	@XmlElement
	private String sign;
	@XmlElement
	private String result_code;
	@XmlElement
	private String prepay_id;
	@XmlElement
	private String trade_type;
	@XmlElement
	private String timestamp;
	
	

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getReturn_code() {
		return return_code;
	}

	public void setReturn_code(String return_code) {
		this.return_code = return_code;
	}

	public String getReturn_msg() {
		return return_msg;
	}

	public void setReturn_msg(String return_msg) {
		this.return_msg = return_msg;
	}

	@Override
	public String toString() {
		return "PayResData [return_code=" + return_code + ", return_msg=" + return_msg + ", appid=" + appid
				+ ", mch_id=" + mch_id + ", nonce_str=" + nonce_str + ", sign=" + sign + ", result_code=" + result_code
				+ ", prepay_id=" + prepay_id + ", trade_type=" + trade_type + "]";
	}

	public PayResData(String return_code, String return_msg, String appid, String mch_id, String nonce_str,
			String sign, String result_code, String prepay_id, String trade_type) {
		super();
		this.return_code = return_code;
		this.return_msg = return_msg;
		this.appid = appid;
		this.mch_id = mch_id;
		this.nonce_str = nonce_str;
		this.sign = sign;
		this.result_code = result_code;
		this.prepay_id = prepay_id;
		this.trade_type = trade_type;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getResult_code() {
		return result_code;
	}

	public void setResult_code(String result_code) {
		this.result_code = result_code;
	}

	public String getPrepay_id() {
		return prepay_id;
	}

	public void setPrepay_id(String prepay_id) {
		this.prepay_id = prepay_id;
	}

	public String getTrade_type() {
		return trade_type;
	}

	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public PayResData(String return_code, String return_msg) {
		super();
		this.return_code = return_code;
		this.return_msg = return_msg;
	}

	public PayResData() {
		super();
	}

}
