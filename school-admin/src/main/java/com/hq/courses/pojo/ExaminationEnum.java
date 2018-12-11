package com.hq.courses.pojo;

public enum ExaminationEnum {
	 
	userId(1,"学员ID"),
	registrationNo(3,"报考单号"),
	score(8,"统考成绩"),
	examType(9,"成绩类型");
	 
	private ExaminationEnum(int index,String name){
		this.index = index;
		this.name = name;
	}
	
	private int index;
	
	private String name;

	public int getIndex() {
		return index - 1;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
