package io.renren.enums;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/20 16:42
 */
public enum CourseAbnormalFreeAssessmentExcelEnum {
    nickName(1,"学员姓名"),
    mobile(2,"学员号码"),
    mallOrderNo(3,"订单编号"),
    courseName(4,"课程名称"),
    startTime(5,"开始时间"),
    endTime(6,"结束时间"),
    abnormalReason(7,"原因"),
    remark(8,"备注");
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
    private CourseAbnormalFreeAssessmentExcelEnum(int index, String columnName) {
        this.index = index;
        this.columnName = columnName;
    }

    public int getExcelIndex() {
        return this.index - 1;
    }
}
