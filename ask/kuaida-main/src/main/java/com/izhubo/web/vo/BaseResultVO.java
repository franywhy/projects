package com.izhubo.web.vo;

import java.util.List;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class BaseResultVO<T> implements BaseVO {
	
	@ApiModelProperty(value = "错误信息")
	private String msg;

	@ApiModelProperty(value = "编码 code=1时操作正确")
	private int code;

	@ApiModelProperty(value = "返回数据")
	private List<T> data;

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

	public List<T> getData() {
		return data;
	}

	public void setData(List<T> data) {
		this.data = data;
	}

	
}
