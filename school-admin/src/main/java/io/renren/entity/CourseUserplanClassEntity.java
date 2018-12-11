package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 学习计划
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-27 09:55:36
 */
public class CourseUserplanClassEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long userplanClassId;
	// 学员规划PK
	private Long userplanId;
	// 计划单号
	private String userplanClassNo;
	// 考试时段PK
	private Long examScheduleId;
	// 备注
	private String remark;
	// 平台ID
	private String schoolId;
	// 创建时间
	private Date createTime;
	// 修改时间
	private Date modifyTime;
	// 创建人
	private Long createPerson;
	// 修改人
	private Long modifyPerson;
	// dr
	private Integer dr;
	// 审核0：未通过 1：通过 2：待审核
	private Integer status;
	private String mId;

	private Long deptId;

	@Override
	public String toString() {
		return "CourseUserplanClassEntity [userplanClassId=" + userplanClassId + ", userplanId=" + userplanId + ", userplanClassNo=" + userplanClassNo
				+ ", examScheduleId=" + examScheduleId + ", remark=" + remark + ", schoolId=" + schoolId + ", createTime=" + createTime
				+ ", modifyTime=" + modifyTime + ", createPerson=" + createPerson + ", modifyPerson=" + modifyPerson + ", dr=" + dr + ", status="
				+ status + ", mId=" + mId + ", deptId=" + deptId + "]";
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 设置：主键
	 */
	public void setUserplanClassId(Long userplanClassId) {
		this.userplanClassId = userplanClassId;
	}

	/**
	 * 获取：主键
	 */
	public Long getUserplanClassId() {
		return userplanClassId;
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
	 * 设置：计划单号
	 */
	public void setUserplanClassNo(String userplanClassNo) {
		this.userplanClassNo = userplanClassNo;
	}

	/**
	 * 获取：计划单号
	 */
	public String getUserplanClassNo() {
		return userplanClassNo;
	}

	/**
	 * 设置：考试时段PK
	 */
	public void setExamScheduleId(Long examScheduleId) {
		this.examScheduleId = examScheduleId;
	}

	/**
	 * 获取：考试时段PK
	 */
	public Long getExamScheduleId() {
		return examScheduleId;
	}

	/**
	 * 设置：
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取：
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
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
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}

	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}

	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}

	/**
	 * 设置：dr
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}

	/**
	 * 获取：dr
	 */
	public Integer getDr() {
		return dr;
	}
}
