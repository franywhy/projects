package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 报考档案表
 * 
 * @author vince
 * @date 2018-03-28 09:21:52
 */
public class CourseAbnormalRegistrationEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//创建用户
	private Long creater;
	//创建时间
//	private Date createTime;
	//修改用户
	private Long updatePerson;
	//修改时间
//	private Date updateTime;
	//单据号
	private String registrationNo;
	//订单编号
	private String orderNo;
	//订单ID
	private String orderId;
	//学员规划Id
	private String userPlanId;
	//课程ID
	private Long courseId;
	//报考省份
	private Long bkAreaId;
	//报考时间段ID
	private String examScheduleId;
	//报考登记号
	private String registrationNumber;
	//提交时间
	private Date registrationTime;
	//产品线
	private Long productId;
	//状态(0-审核中 1-取消 2-通过)
	private Integer status;
	//删除标志(0未删除,1删除)
	private Integer dr;
	//备注
	private String remark;

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
//	public void setCreateTime(Date createTime) {
//		this.createTime = createTime;
//	}
	/**
	 * 获取：创建时间
	 */
//	public Date getCreateTime() {
//		return createTime;
//	}
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
//	public void setUpdateTime(Date updateTime) {
//		this.updateTime = updateTime;
//	}
	/**
	 * 获取：修改时间
	 */
//	public Date getUpdateTime() {
//		return updateTime;
//	}
	/**
	 * 设置：单据号
	 */
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	/**
	 * 获取：单据号
	 */
	public String getRegistrationNo() {
		return registrationNo;
	}
	/**
	 * 设置：订单编号
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	/**
	 * 获取：订单编号
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * 设置：学员规划Id
	 */
	public void setUserPlanId(String userPlanId) {
		this.userPlanId = userPlanId;
	}
	/**
	 * 获取：学员规划Id
	 */
	public String getUserPlanId() {
		return userPlanId;
	}
	/**
	 * 设置：课程ID
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程ID
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：报考省份
	 */
	public void setBkAreaId(Long bkAreaId) {
		this.bkAreaId = bkAreaId;
	}
	/**
	 * 获取：报考省份
	 */
	public Long getBkAreaId() {
		return bkAreaId;
	}
	/**
	 * 设置：报考时间段ID
	 */
	public void setExamScheduleId(String examScheduleId) {
		this.examScheduleId = examScheduleId;
	}
	/**
	 * 获取：报考时间段ID
	 */
	public String getExamScheduleId() {
		return examScheduleId;
	}
	/**
	 * 设置：报考登记号
	 */
	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}
	/**
	 * 获取：报考登记号
	 */
	public String getRegistrationNumber() {
		return registrationNumber;
	}
	/**
	 * 设置：提交时间
	 */
	public void setRegistrationTime(Date registrationTime) {
		this.registrationTime = registrationTime;
	}
	/**
	 * 获取：提交时间
	 */
	public Date getRegistrationTime() {
		return registrationTime;
	}
	/**
	 * 设置：产品线
	 */
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	/**
	 * 获取：产品线
	 */
	public Long getProductId() {
		return productId;
	}
	/**
	 * 设置：状态(0-审核中 1-取消 2-通过)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0-审核中 1-取消 2-通过)
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：删除标志(0未删除,1删除)
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标志(0未删除,1删除)
	 */
	public Integer getDr() {
		return dr;
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
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	@Override
	public String toString() {
		return "CourseAbnormalRegistrationEntity [id=" + id + ", creater=" + creater + ", updatePerson=" + updatePerson
				+ ", registrationNo=" + registrationNo + ", orderNo=" + orderNo + ", orderId=" + orderId
				+ ", userPlanId=" + userPlanId + ", courseId=" + courseId + ", bkAreaId=" + bkAreaId
				+ ", examScheduleId=" + examScheduleId + ", registrationNumber=" + registrationNumber
				+ ", registrationTime=" + registrationTime + ", productId=" + productId + ", status=" + status + ", dr="
				+ dr + ", remark=" + remark + "]";
	}
	
	
	

}
