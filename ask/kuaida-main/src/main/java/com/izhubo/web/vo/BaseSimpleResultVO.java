package com.izhubo.web.vo;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class BaseSimpleResultVO<T> implements BaseVO {
	
	@ApiModelProperty(value = "错误信息")
	private String msg;

	@ApiModelProperty(value = "编码 code=1时操作正确")
	private int code;

	@ApiModelProperty(value = "错误信息")
	private String data;

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	
	
}
