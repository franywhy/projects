package com.hq.courses.pojo;

public enum CsCourseExcelInitializationColumnEnum {
	kcbm(1,"课程编码"),
	kcmc(2,"课程名称"),
	zt(3,"状态"),
	dr(4,"dr"),
	cpxId(5,"产品线ID"),
	bmId(6,"部门ID"),
	bz(7,"备注"),
	ncId(8,"NCID"),
	ljId(9,"LJID"),
	tkId(10,"TKID");
	
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
	private CsCourseExcelInitializationColumnEnum(int index, String columnName) {
		this.index = index;
		this.columnName = columnName;
	}
	
	public int getExcelIndex() {
		return this.index - 1;
	}
}
