package com.hq.learningapi.pojo;

import java.io.Serializable;

public class AppCourseBannerPOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;
	
	private Long id;
	
	private String title;
	
	private String pic;
	
	private String url;
	
	private int orderNum;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	@Override
	public String toString() {
		return "AppCourseBannerPOJO [id=" + id + ", title=" + title + ", pic=" + pic + ", url=" + url + ", orderNum="
				+ orderNum + "]";
	}
	
}
