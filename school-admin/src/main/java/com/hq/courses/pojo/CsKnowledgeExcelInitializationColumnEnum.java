package com.hq.courses.pojo;

public enum CsKnowledgeExcelInitializationColumnEnum {
	kcbm(1,"课程编码"),
	zbm(2,"章编码"),
	zmc(3,"章名称"),
	jbm(4,"节编码"),
	jmc(5,"节名称"),
	zsdbm(6,"知识点编码"),
	zsdmc(7,"知识点名称");
	
	private int index;
	//列名称
	private String columnName;
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	private CsKnowledgeExcelInitializationColumnEnum(int index, String columnName) {
		this.index = index;
		this.columnName = columnName;
	}
	
	public int getExcelIndex() {
		return this.index - 1;
	}
}
