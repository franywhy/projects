package com.izhubo.web.vo;

/**
 * SSO接口的返回值
 * 
 *
 */
public class SSOResult {

	/**
	 * code 200:成功
	 */
	private String code;

	/**
	 * 返回值（json格式的字符串）
	 */
	private Object data;

	/**
	 * 提示信息
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
