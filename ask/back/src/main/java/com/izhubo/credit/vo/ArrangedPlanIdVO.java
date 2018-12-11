package com.izhubo.credit.vo;

import java.util.List;

/**
 * 排课计划表头id
 * @author 严志城
 * @time 2017年3月22日17:54:08
 */
public class ArrangedPlanIdVO {
	private List<String> arrPlanIdList;
	private String secretKey;
	public List<String> getArrPlanIdList() {
		return arrPlanIdList;
	}
	public void setArrPlanIdList(List<String> arrPlanIdList) {
		this.arrPlanIdList = arrPlanIdList;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
