package com.hq.learningcenter.school.pojo;

import com.hq.learningcenter.school.enums.AuditStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
@ApiModel
public class CourseAbnormalAbandonExamPOJO{

    @ApiModelProperty(value="pk",name="pk")
	private Long id;
    @ApiModelProperty(value="班型",name="orderName")
	private String orderName;
    @ApiModelProperty(value="课程名称",name="courseName")
	private String courseName;
	@ApiModelProperty(value="报考省份",name="registerProinve")
	private String registerProinve;
    @ApiModelProperty(value="考试日期",name="scheduleDate")
	private String scheduleDate;
    @ApiModelProperty(value="状态值(0 : 审批中,1: 作废，2：通过）",name="status")
	private Integer status;
    @ApiModelProperty(value="原因",name="reason")
	private String reason;
    @ApiModelProperty(value="未通过原因",name="remark")
	private String remark;
    @ApiModelProperty(value="状态展示",name="statusStr")
	private String statusStr;
	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}

	public String getScheduleDate() {
		return scheduleDate.replace("/","-");
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	/**
	 * 设置：状态 (0 : 审批中,1: 作废，2：通过）
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 (0 : 审批中,1: 作废，2：通过）
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：原因
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}
	/**
	 * 获取：原因
	 */
	public String getReason() {
		return reason;
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

    public String getStatusStr() {
        return AuditStatusEnum.getText(status);
    }

    public void setStatusStr(String statusStr) {
        this.statusStr = statusStr;
    }
}
