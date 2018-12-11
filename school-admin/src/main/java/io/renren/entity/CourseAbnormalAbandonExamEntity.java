package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
public class CourseAbnormalAbandonExamEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//创建用户
	private Long creater;
	//创建时间
	private Date createTime;
	//修改用户
	private Long updatePerson;
	//修改时间
	private Date updateTime;
	//单据号
	private String invoicesNumber;	
	//产品线id
	private Long productId;
	//状态 (0 : 审批中,1: 作废，2：通过）
	private Integer status;
	//删除标志
	private Integer dr;
	//原因
	private String reason;
	//备注
	private String remark;
	//报考单PK
	private Long registrationId;

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
	/**
	 * 设置：创建用户
	 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/**
	 * 获取：创建用户
	 */
	public Long getCreater() {
		return creater;
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
	 * 设置：单据号
	 */
	public void setInvoicesNumber(String invoicesNumber) {
		this.invoicesNumber = invoicesNumber;
	}
	/**
	 * 获取：单据号
	 */
	public String getInvoicesNumber() {
		return invoicesNumber;
	}
	
	/**
	 * 设置：产品线id
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线id
	 */
	public Long getProductId() {
		return productId;
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
	 * 设置：删除标志
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标志
	 */
	public Integer getDr() {
		return dr;
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
	/**
	 * 设置：报考单PK
	 */
	public void setRegistrationId(Long registrationId) {
		this.registrationId = registrationId;
	}
	/**
	 * 获取：报考单PK
	 */
	public Long getRegistrationId() {
		return registrationId;
	}
	@Override
	public String toString() {
		return "CourseAbnormalAbandonExamEntity [id=" + id + ", creater=" + creater + ", createTime=" + createTime
				+ ", updatePerson=" + updatePerson + ", updateTime=" + updateTime + ", invoicesNumber=" + invoicesNumber
				+ ", productId=" + productId + ", status=" + status + ", dr=" + dr + ", reason=" + reason + ", remark="
				+ remark + ", registrationId=" + registrationId + ", getId()=" + getId() + ", getCreater()="
				+ getCreater() + ", getCreateTime()=" + getCreateTime() + ", getUpdatePerson()=" + getUpdatePerson()
				+ ", getUpdateTime()=" + getUpdateTime() + ", getInvoicesNumber()=" + getInvoicesNumber()
				+ ", getProductId()=" + getProductId() + ", getStatus()=" + getStatus() + ", getDr()=" + getDr()
				+ ", getReason()=" + getReason() + ", getRemark()=" + getRemark() + ", getRegistrationId()="
				+ getRegistrationId() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
}
