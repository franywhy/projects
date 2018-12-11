package com.school.pojo;

import com.school.enums.AuditStatusEnum;
import com.school.utils.DateUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/19 9:47
 */
@ApiModel
public class CourseAbnormalFreeAssessmentPOJO{
    @ApiModelProperty(value="pk",name="pk")
    private Long id;
    @ApiModelProperty(value="班型",name="orderName")
    private String orderName;

    @ApiModelProperty(value="课程名称",name="courseName")
    private String courseName;

    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;

    @ApiModelProperty(value="状态值(0-审核中 1-取消 2-通过)",name="auditStatus")
    private Integer auditStatus;
    @ApiModelProperty(value="原因",name="abnormalReason")
    private String abnormalReason;
    @ApiModelProperty(value="未通过原因",name="remark")
    private String remark;

    @ApiModelProperty(value="开始时间",name="startTimeStr")
    private String startTimeStr;

    @ApiModelProperty(value="结束时间",name="endTimeStr")
    private String endTimeStr;

    @ApiModelProperty(value="状态",name="auditStatusStr")
    private String auditStatusStr;

    /**
     * 设置：pk
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * 获取：pk
     */
    public Long getId() {
        return id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    /**
     * 设置：预计开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    /**
     * 获取：预计开始时间
     */
    public Date getStartTime() {
        return startTime;
    }
    /**
     * 设置：实际结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    /**
     * 获取：实际结束时间
     */
    public Date getEndTime() {
        return endTime;
    }
    /**
     * 设置：状态(0-审核中 1-取消 2-通过)
     */
    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
    /**
     * 获取：状态(0-审核中 1-取消 2-通过)
     */
    public Integer getAuditStatus() {
        return auditStatus;
    }
    /**
     * 设置：异常原因
     */
    public void setAbnormalReason(String abnormalReason) {
        this.abnormalReason = abnormalReason;
    }
    /**
     * 获取：异常原因
     */
    public String getAbnormalReason() {
        return abnormalReason;
    }
    /**
     * 设置：备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }
    /**
     * 获取：备注
     */
    public String getRemark() {
        return remark;
    }

    public String getStartTimeStr() {
        return DateUtils.format(startTime);
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getEndTimeStr() {
        if(endTime != null){
            endTimeStr = DateUtils.format(endTime);
        }
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }

    public String getAuditStatusStr() {
        return AuditStatusEnum.getText(auditStatus);
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }


    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
}
