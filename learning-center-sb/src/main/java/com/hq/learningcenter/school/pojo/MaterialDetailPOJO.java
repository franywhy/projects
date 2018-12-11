package com.hq.learningcenter.school.pojo;

import java.io.Serializable;

public class MaterialDetailPOJO implements Serializable {

	private static final long serialVersionUID = 3581259714192139434L;
	
	private Long detailId;
	
	private String name;
	
	private String url;

	public Long getDetailId() {
		return detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
}
