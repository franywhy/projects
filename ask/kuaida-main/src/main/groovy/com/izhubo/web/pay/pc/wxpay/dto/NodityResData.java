package com.izhubo.web.pay.pc.wxpay.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.izhubo.rest.common.util.Snippet;

/**
 * 微信异步通知 返回微信结构
 * 
 * @author shihongjie
 * @see https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement (name = "xml")  
public class NodityResData {

	/** SUCCESS/FAIL SUCCESS表示商户接收通知成功并校验成功 */
	@XmlElement
	private String return_code;

	/** 返回信息，如非空，为错误原因：签名失败参数格式校验错误 */
	@XmlElement
	private String return_msg;

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
		return "NodityResData [return_code=" + return_code + ", return_msg=" + return_msg + "]";
	}

	public NodityResData(String return_code, String return_msg) {
		super();
		this.return_code = return_code;
		this.return_msg = return_msg;
	}

	private static NodityResData success = new NodityResData("SUCCESS", "SUCCESS");
	private static NodityResData fail = new NodityResData("FAIL", "FAIL");
	private static String successStr = null;
	private static String failStr = null;
	static {
		successStr = Snippet.convertToXml(success);
		failStr = Snippet.convertToXml(fail);
	}

	public static String getSuccessXml() {
		return successStr;
	}

	public static String getFailXml() {
		return failStr;
	}

	public static void main(String[] args) {
		System.out.println(getSuccessXml());
		System.out.println(getFailXml());
	}
	
	public NodityResData() {
		super();
	}

}
