package com.school.pojo;

import com.school.enums.AuditStatusEnum;
import com.school.utils.DateUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linchaokai
 * @Description
 * @date 2018/3/19 9:47
 */
public class CourseAbnormalOrderPOJO{

    //pk
    private Long id;
    //班型
    private String orderName;
    //休学时间
    private Date startTime;
    //预计结束时间
    private Date expectEndTime;
    //休学原因
    private String abnormalReason;
    //状态(0-审核中 1-取消 2-通过)
    private Integer auditStatus;
    //备注
    private String remark;
    //复课时间
    private Date endTime;

    //休学时间页面展示
    private String startTimeStr;
    //预计结束时间页面展示
    private String expectEndTimeStr;
    //复课时间页面展示
    private String endTimeStr;
    //状态页面展示(0-审核中 1-取消 2-通过)
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
     * 设置：预计结束时间
     */
    public void setExpectEndTime(Date expectEndTime) {
        this.expectEndTime = expectEndTime;
    }
    /**
     * 获取：预计结束时间
     */
    public Date getExpectEndTime() {
        return expectEndTime;
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

    public String getStartTimeStr() {
        return DateUtils.format(startTime);
    }

    public void setStartTimeStr(String startTimeStr) {
        this.startTimeStr = startTimeStr;
    }

    public String getExpectEndTimeStr() {
        return DateUtils.format(expectEndTime);
    }

    public void setExpectEndTimeStr(String expectEndTimeStr) {
        this.expectEndTimeStr = expectEndTimeStr;
    }

    public String getAuditStatusStr() {
        return AuditStatusEnum.getText(auditStatus);
    }

    public void setAuditStatusStr(String auditStatusStr) {
        this.auditStatusStr = auditStatusStr;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getEndTimeStr() {
        if(endTime != null){
            endTimeStr = DateUtils.format(endTime);
        }else{
            endTimeStr = "-";
        }
        return endTimeStr;
    }

    public void setEndTimeStr(String endTimeStr) {
        this.endTimeStr = endTimeStr;
    }
}
