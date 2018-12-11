package com.hq.learningapi.pojo;

import java.io.Serializable;
import java.util.List;

public class PcMarketParentCoursePOJO implements Serializable  {

	private static final long serialVersionUID = 1436814577593284974L;
	
	private Long id;
	
	private Integer level;
	
	private String classifyName;
	
//	private int orderNum;
	
	List<PcMarketCoursePOJO> list;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}
	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

//	public int getOrderNum() {
//		return orderNum;
//	}
//
//	public void setOrderNum(int orderNum) {
//		this.orderNum = orderNum;
//	}

	public List<PcMarketCoursePOJO> getList() {
		return list;
	}

	public void setList(List<PcMarketCoursePOJO> list) {
		this.list = list;
	}

	@Override
	public String toString() {
		return "PcMarketParentCoursePOJO [id=" + id + ", level=" + level + ", classifyName=" + classifyName + ", list="
				+ list + "]";
	}

	

}
