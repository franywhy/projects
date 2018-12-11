package io.renren.enums;

public enum KjLabelExcelEnum {
	professionName(1,"专业名称"),
	labelName(2,"标签名称"),
	smallPicUrl(3,"小图标地址"),
	bigPicUrl(4,"大图标地址"),
	courseCode(5,"课程代码");
	
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
	
	private KjLabelExcelEnum(Integer index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getExcelIndex() {
        return this.index - 1;
    }
}
