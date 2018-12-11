package io.renren.entity;

import java.io.Serializable;

public class GoodsCoursetkEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long commodityId;
	
	private Long courseTkId;
	
	private String courseTkCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCommodityId() {
		return commodityId;
	}

	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}

	public Long getCourseTkId() {
		return courseTkId;
	}

	public void setCourseTkId(Long courseTkId) {
		this.courseTkId = courseTkId;
	}

	public String getCourseTkCode() {
		return courseTkCode;
	}

	public void setCourseTkCode(String courseTkCode) {
		this.courseTkCode = courseTkCode;
	}

	@Override
	public String toString() {
		return "GoodsCoursetkEntity [id=" + id + ", commodityId=" + commodityId + ", courseTkId=" + courseTkId
				+ ", courseTkCode=" + courseTkCode + "]";
	}
	
	
}
