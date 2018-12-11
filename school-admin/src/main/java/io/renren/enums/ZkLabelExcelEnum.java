package io.renren.enums;

public enum ZkLabelExcelEnum {
	professionName(1,"专业名称"),
	labelName(2,"标签名称"),
	courseCode(3,"课程代码");
	
	//下标
	private Integer index;
	//列名称
	private String columnName;
	public Integer getIndex() {
		return index;
	}
	public void setIndex(Integer index) {
		this.index = index;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	
	private ZkLabelExcelEnum(Integer index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getExcelIndex() {
        return this.index - 1;
    }
}
