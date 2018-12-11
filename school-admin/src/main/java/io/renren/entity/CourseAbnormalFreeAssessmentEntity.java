package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 免考记录单
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-19 09:26:43
 */
public class CourseAbnormalFreeAssessmentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long id;
	//订单pk
	private Long orderId;
	//课程pk
	private Long courseId;

	//预计开始时间
	private Date startTime;
	//实际结束时间
	private Date endTime;
	//状态(0-审核中 1-取消 2-通过)
	private Integer auditStatus;
	//异常原因
	private String abnormalReason;
	//备注
	private String remark;
	//创建时间
	private Date createTime;
	//申请用户pk
	private Long createPerson;
	//审核用户
	private Long modifyPerson;
	//审核时间
	private Date modifiedTime;
	//修改用户
	private Long updatePerson;
	//修改时间
	private Date updateTime;
	//
	private String orderno;
	//产品
	private Long productId;

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
	/**
	 * 设置：订单pk
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单pk
	 */
	public Long getOrderId() {
		return orderId;
	}

	public Long getCourseId() {
		return courseId;
	}

	public void setCourseId(Long courseId) {
		this.courseId = courseId;
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
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：申请用户pk
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：申请用户pk
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：审核用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：审核用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：审核时间
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：修改用户
	 */
	public void setUpdatePerson(Long updatePerson) {
		this.updatePerson = updatePerson;
	}
	/**
	 * 获取：修改用户
	 */
	public Long getUpdatePerson() {
		return updatePerson;
	}
	/**
	 * 设置：修改时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：
	 */
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	/**
	 * 获取：
	 */
	public String getOrderno() {
		return orderno;
	}

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
