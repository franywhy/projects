package io.renren.enums;

/**
 * @author zhaowenwei
 * @Description
 * @date 2018/4/8 09:42
 */
public enum CourseAbnormalRegistrationExcelEnum {
	mallOrderNo(1,"订单编号"),
	courseName(2,"课程"),
	bkArea(3,"报考省份"),
	scheduleName(4,"报考时间"),
	registrationNo(5,"报考单号");
	
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
    private CourseAbnormalRegistrationExcelEnum(int index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getExcelIndex() {
        return this.index - 1;
    }
}
