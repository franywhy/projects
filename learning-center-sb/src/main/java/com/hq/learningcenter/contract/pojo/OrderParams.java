package com.hq.learningcenter.contract.pojo;

public class OrderParams {
  
	/**
	 * 省份
	 */
	private Long areaId;
    
	private Integer page = 1;

	/**
	 * 状态
	 */
	private Integer type;
 
	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	} 
	
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	
	
}
