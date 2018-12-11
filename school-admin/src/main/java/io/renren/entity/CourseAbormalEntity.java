package io.renren.entity;

import io.renren.enums.CourseAbormalTypeEnum;

import java.util.Date;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/26 10:44
 */
public class CourseAbormalEntity {
    //类型
    private CourseAbormalTypeEnum abnormalType;
    //单号
    private String no;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;

    public CourseAbormalTypeEnum getAbnormalType() {
        return abnormalType;
    }

    public void setAbnormalType(CourseAbormalTypeEnum abnormalType) {
        this.abnormalType = abnormalType;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "CourseAbormalEntity{" +
                "abnormalType=" + abnormalType +
                ", no='" + no + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public CourseAbormalEntity(CourseAbormalTypeEnum abnormalType, String no, Date startTime, Date endTime) {
        this.abnormalType = abnormalType;
        this.no = no;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public CourseAbormalEntity() {
    }
}
