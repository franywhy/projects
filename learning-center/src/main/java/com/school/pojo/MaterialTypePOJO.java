package com.school.pojo;

import java.io.Serializable;
import java.util.List;

public class MaterialTypePOJO implements Serializable {
	
	private static final long serialVersionUID = 5561949954437354797L;

	private String name;
	
	private List<MaterialDetailPOJO> detail;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MaterialDetailPOJO> getDetail() {
		return detail;
	}

	public void setDetail(List<MaterialDetailPOJO> detail) {
		this.detail = detail;
	}
	
}
