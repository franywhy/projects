package com.school.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 休学档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-25 15:14:34
 */
public class UserstopEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//学员规划PK
	private Long userplanId;
	//休学时间
	private Date startTime;
	//复课时间
	private Date endTime;
	//休学原因
	private String stopCause;
	//申请用户PK
	private Long userId;
	//备注
	private String remark;
	//状态(0-审核中 1-取消 2-申请失败 3-通过)
	private Integer status;
	//后台创建用户-学员申请为空
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近审核用户
	private Long modifyPerson;
	//最近审核日期
	private Date modifiedTime;
	//产品线
	private Long productId;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserplanId() {
		return userplanId;
	}
	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
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
	public String getStopCause() {
		return stopCause;
	}
	public void setStopCause(String stopCause) {
		this.stopCause = stopCause;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Long getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	@Override
	public String toString() {
		return "UserstopEntity [id=" + id + ", userplanId=" + userplanId + ", startTime=" + startTime + ", endTime="
				+ endTime + ", stopCause=" + stopCause + ", userId=" + userId + ", remark=" + remark + ", status="
				+ status + ", createPerson=" + createPerson + ", creationTime=" + creationTime + ", modifyPerson="
				+ modifyPerson + ", modifiedTime=" + modifiedTime + "]";
	}
	
}
