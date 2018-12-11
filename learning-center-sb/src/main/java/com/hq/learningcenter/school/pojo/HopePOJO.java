package com.hq.learningcenter.school.pojo;

import java.util.List;
import java.util.Map;

public class HopePOJO {

	private Integer type;
	
	private String name;

	private String icon;
	
	private String typeName;

	private List<Map<String,Object>> webData;

	private String data;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    public List<Map<String, Object>> getWebData() {
        return webData;
    }

    public void setWebData(List<Map<String, Object>> webData) {
        this.webData = webData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
