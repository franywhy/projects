package com.school.pojo;

/**
 * @author linchaokai
 * @Description 考试时间表
 * @date 2018/4/3 10:19
 */
public class MallExamSchedulePOJO {
    private Integer id;

    private String scheduleDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScheduleDate() {
        return scheduleDate.replace("/","-");
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }
}
