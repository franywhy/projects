package com.hq.learningcenter.school.pojo;

import java.util.List;

public class ClassplanPOJO {

	private String classplanId;
	
	private String classplanName;
	
	private List<ClassplanLivesPOJO> list;

	private Long classTypeId;

    public Long getClassTypeId() {
        return classTypeId;
    }

    public void setClassTypeId(Long classTypeId) {
        this.classTypeId = classTypeId;
    }

    public String getClassplanId() {
		return classplanId;
	}

	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}

	public String getClassplanName() {
		return classplanName;
	}

	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}

	public List<ClassplanLivesPOJO> getList() {
		return list;
	}

	public void setList(List<ClassplanLivesPOJO> list) {
		this.list = list;
	}
}
