package com.izhubo.credit.vo;

import java.util.List;

/**
 * 班级表头id
 * @author 严志城
 * @time 2017年3月22日17:54:08
 */
public class ClassIdVO {
	private List<String> classIdList;
	private String secretKey;
	
	public List<String> getClassIdList() {
		return classIdList;
	}
	public void setClassIdList(List<String> classIdList) {
		this.classIdList = classIdList;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
