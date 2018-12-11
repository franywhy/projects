package com.hq.learningapi.pojo;

import java.io.Serializable;

public class AppMarketCoursePOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;
	
	private Long id;
	
	private String courseName;
	
	private String pic;
	
	private String appUrl;
	
	private int orderNum;
	
	private Integer isShare;
	
	private String classWay;
	
	private String suitableObject;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	public Integer getIsShare() {
		return isShare;
	}

	public void setIsShare(Integer isShare) {
		this.isShare = isShare;
	}

	public String getClassWay() {
		return classWay;
	}

	public void setClassWay(String classWay) {
		this.classWay = classWay;
	}

	public String getSuitableObject() {
		return suitableObject;
	}

	public void setSuitableObject(String suitableObject) {
		this.suitableObject = suitableObject;
	}

	@Override
	public String toString() {
		return "AppMarketCoursePOJO [id=" + id + ", courseName=" + courseName + ", pic=" + pic + ", appUrl=" + appUrl
				+ ", orderNum=" + orderNum + ", isShare=" + isShare + ", classWay=" + classWay + ", suitableObject="
				+ suitableObject + "]";
	}

}
