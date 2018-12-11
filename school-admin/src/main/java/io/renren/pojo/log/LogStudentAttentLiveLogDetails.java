package io.renren.pojo.log;

import java.io.Serializable;
import java.util.Date;

/**
 * 考勤统计-按学员统计-查看日志详情
 * Created by DL on 2017/11/22.
 */
public class LogStudentAttentLiveLogDetails  implements Serializable{
    private String userName; //学员名称

    private String mobile; //学员手机(没有学员名称的显示学员手机号码)

    private String courseName; //课程名称

    private String teacherName; //讲师名称

    private String devices; //观看设备(PC/app)

    private Integer type; //课程类型

    private Date startTime;//开课时间

    private Date endTime;//结束时间

    private Date inTime;//进入课程时间

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getDevices() {
        return devices;
    }

    public void setDevices(String devices) {
        this.devices = devices;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    @Override
    public String toString() {
        return "LogStudentAttentLiveLogDetails{" +
                "userName='" + userName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", courseName='" + courseName + '\'' +
                ", teacherName='" + teacherName + '\'' +
                ", devices='" + devices + '\'' +
                ", type=" + type +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", inTime=" + inTime +
                '}';
    }
}
