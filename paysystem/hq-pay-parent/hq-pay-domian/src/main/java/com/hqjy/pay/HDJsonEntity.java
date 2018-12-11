package com.hqjy.pay;

import java.util.List;

public class HDJsonEntity {
	/*
	 * 状态:-100 系统繁忙，请稍候再试 0 请求成功 40001 参数必填参数 40002 签名验证失败 40003 业务处理失败
	 */
	private String code;
	/**
	 * 返回信息
	 */
	private String message;
	/**
	 * 签名
	 */
	//private String sign;
	/**
	 * 返回数据
	 */
	private List<HDDataEntity> data;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<HDDataEntity> getData() {
		return data;
	}

	public void setData(List<HDDataEntity> data) {
		this.data = data;
	}

	/*public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
    */
}
