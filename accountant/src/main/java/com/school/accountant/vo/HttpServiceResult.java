package com.school.accountant.vo;

/**
 * 实训项目http接口的返回值
 * 
 * @author WuZhiFeng
 *
 */
public class HttpServiceResult {

	/**
	 * 等于1时代表操作成功
	 */
	public static final String STATUS_SUCCESSFUL = "1";
	/**
	 * code为2时代表参数校验失败
	 */
	public static final String STATUS_FAILURE = "2";
	/**
	 * code为3时验签失败
	 */
	public static final String STATUS_FAILURE_VERIFY = "3";

	/**
	 * code 等于1时代表操作成功 code为2时代表参数校验失败 code为3时验签失败 code为其他时请指明错误
	 */
	private String code;

	/**
	 * 返回值
	 */
	private Object data;

	/**
	 * 为错误原因,code为1时可为空
	 */
	private String msg;

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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
