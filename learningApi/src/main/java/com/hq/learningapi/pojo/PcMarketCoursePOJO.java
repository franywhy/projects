package com.hq.learningapi.pojo;

import java.io.Serializable;

public class PcMarketCoursePOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;
	
	private Long id;
	
	private Long parentId;
	
	private Integer level;
	
	private String name;
	
	private String pic;
	
	private String pcUrl;
	
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

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getPcUrl() {
		return pcUrl;
	}

	public void setPcUrl(String pcUrl) {
		this.pcUrl = pcUrl;
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
		return "PcMarketCoursePOJO [id=" + id + ", parentId=" + parentId + ", level=" + level + ", name=" + name
				+ ", pic=" + pic + ", pcUrl=" + pcUrl + ", orderNum=" + orderNum + ", isShare=" + isShare
				+ ", classWay=" + classWay + ", suitableObject=" + suitableObject + "]";
	}
	
}
