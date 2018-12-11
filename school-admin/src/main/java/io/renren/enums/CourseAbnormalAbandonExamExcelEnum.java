package io.renren.enums;

/**
 * @author zhaowenwei
 * @Description
 * @date 2018/4/8 09:42
 */
public enum CourseAbnormalAbandonExamExcelEnum {
	orderNo(1,"订单编号"),
	courseName(2,"课程名称"),
	registerProvince(3,"报考省份"),
	scheduleName(4,"考试时段"),
	reason(5,"原因"),
	remark(6,"备注");
	
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
    private CourseAbnormalAbandonExamExcelEnum(int index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getExcelIndex() {
        return this.index - 1;
    }
}
