package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 休学档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-12 10:38:36
 */
public class CourseUserstopEntity implements Serializable {
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
	
	//商品名称
	private String commodityName;
	//学员名称
	private String userName;
	//学员手机号
	private String mobile;
	//审核用户名称
	private String modifyName;

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
	 * 设置：学员规划PK
	 */
	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}
	/**
	 * 获取：学员规划PK
	 */
	public Long getUserplanId() {
		return userplanId;
	}
	/**
	 * 设置：休学时间
	 */
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	/**
	 * 获取：休学时间
	 */
	public Date getStartTime() {
		return startTime;
	}
	/**
	 * 设置：复课时间
	 */
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	/**
	 * 获取：复课时间
	 */
	public Date getEndTime() {
		return endTime;
	}
	/**
	 * 设置：休学原因
	 */
	public void setStopCause(String stopCause) {
		this.stopCause = stopCause;
	}
	/**
	 * 获取：休学原因
	 */
	public String getStopCause() {
		return stopCause;
	}
	/**
	 * 设置：申请用户PK
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：申请用户PK
	 */
	public Long getUserId() {
		return userId;
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
	 * 设置：状态(0-审核中 1-取消 2-申请失败 3-通过)
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态(0-审核中 1-取消 2-申请失败 3-通过)
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：后台创建用户-学员申请为空
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：后台创建用户-学员申请为空
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreationTime() {
		return creationTime;
	}
	/**
	 * 设置：最近审核用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：最近审核用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：最近审核日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近审核日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
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
	public String getModifyName() {
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	
}
