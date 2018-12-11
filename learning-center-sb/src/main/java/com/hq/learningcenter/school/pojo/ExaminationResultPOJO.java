package com.hq.learningcenter.school.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @auther linchaokai
 * @description 成绩登记
 * @date 2018/8/8
 */
@ApiModel
public class ExaminationResultPOJO {
    @ApiModelProperty(value="pk",name="pk")
    private Long id;
    @ApiModelProperty(value="报考单号",name="registerPk")
    private String registerPk;
    @ApiModelProperty(value="班型",name="orderName")
    private String orderName;
    @ApiModelProperty(value="课程名称",name="courseName")
    private String courseName;
    @ApiModelProperty(value="报考省份",name="registerProinve")
    private String registerProinve;
    @ApiModelProperty(value="考试日期",name="scheduleDate")
    private String scheduleDate;
    @ApiModelProperty(value="分数",name="score")
    private Integer score;
    @ApiModelProperty(value="图片路径",name="img")
    private String img;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRegisterPk() {
        return registerPk;
    }

    public void setRegisterPk(String registerPk) {
        this.registerPk = registerPk;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getRegisterProinve() {
        return registerProinve;
    }

    public void setRegisterProinve(String registerProinve) {
        this.registerProinve = registerProinve;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    @Override
    public String toString() {
        return "ExaminationResultPOJO{" +
                "id=" + id +
                ", registerPk='" + registerPk + '\'' +
                ", orderName='" + orderName + '\'' +
                ", courseName='" + courseName + '\'' +
                ", registerProinve='" + registerProinve + '\'' +
                ", scheduleDate='" + scheduleDate + '\'' +
                ", score=" + score +
                ", img='" + img + '\'' +
                '}';
    }
}

