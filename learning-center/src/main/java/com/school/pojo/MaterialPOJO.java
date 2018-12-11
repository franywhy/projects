package com.school.pojo;

import java.io.Serializable;
import java.util.List;

public class MaterialPOJO implements Serializable {
	
	private static final long serialVersionUID = 8679212456917449L;

	private String name;
	
	private List<MaterialTypePOJO> type;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<MaterialTypePOJO> getType() {
		return type;
	}

	public void setType(List<MaterialTypePOJO> type) {
		this.type = type;
	}
	
}
