package io.renren.pojo;

import java.util.Date;

/**
 * 异常事件范围对象
 */
public class CourseAbormalDatePOJO {

    private Date startDate;

    private Date endDate;

    public CourseAbormalDatePOJO() {
    }

    public CourseAbormalDatePOJO(Date startDate, Date endDate) {

        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "CourseAbormalDatePOJO{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
