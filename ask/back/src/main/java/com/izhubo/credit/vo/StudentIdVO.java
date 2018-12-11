package com.izhubo.credit.vo;

import java.util.List;

/**
 * 国考的需要传的学员主键实体类
 * @author 严志城
 *
 */
public class StudentIdVO {
	private List<String> stuIdList;
	private String secretKey;
	public List<String> getStuIdList() {
		return stuIdList;
	}
	public void setStuIdList(List<String> stuIdList) {
		this.stuIdList = stuIdList;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
}
