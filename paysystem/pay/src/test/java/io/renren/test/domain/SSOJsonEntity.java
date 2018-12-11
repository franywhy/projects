package io.renren.test.domain;

import java.util.List;

import io.renren.entity.HDDataEntity;

public class SSOJsonEntity {
	private String code;
	/**
	 * 返回信息
	 */
	private String message;
	
	/**
	 * 返回数据
	 */
	private SSOEntity data;

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

	public SSOEntity getData() {
		return data;
	}

	public void setData(SSOEntity data) {
		this.data = data;
	}
}
