package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.CourseUserplanClassEntity;

public class CourseUserplanClassPOJO implements Serializable {
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

	// 创建人名称
	private String creationName;
	// 修改人名称
	private String modifiedName;
	// 学员名称
	private String userName;
	// 学员手机号
	private String mobile;
	// 考试时段名称
	private String examScheduleName;
	// 订单编号
	private String orderNo;

	// 部门
	private Long deptId;
	// 部门
	private String deptName;

	// 子表集合
	private List<CourseUserplanClassDetailPOJO> detailList;

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<CourseUserplanClassDetailPOJO> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<CourseUserplanClassDetailPOJO> detailList) {
		this.detailList = detailList;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Long getUserplanClassId() {
		return userplanClassId;
	}

	public void setUserplanClassId(Long userplanClassId) {
		this.userplanClassId = userplanClassId;
	}

	public Long getUserplanId() {
		return userplanId;
	}

	public void setUserplanId(Long userplanId) {
		this.userplanId = userplanId;
	}

	public String getUserplanClassNo() {
		return userplanClassNo;
	}

	public void setUserplanClassNo(String userplanClassNo) {
		this.userplanClassNo = userplanClassNo;
	}

	public Long getExamScheduleId() {
		return examScheduleId;
	}

	public void setExamScheduleId(Long examScheduleId) {
		this.examScheduleId = examScheduleId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Long getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}

	public Long getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getCreationName() {
		return creationName;
	}

	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}

	public String getModifiedName() {
		return modifiedName;
	}

	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getExamScheduleName() {
		return examScheduleName;
	}

	public void setExamScheduleName(String examScheduleName) {
		this.examScheduleName = examScheduleName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public static CourseUserplanClassEntity getEntity(CourseUserplanClassPOJO pojo) {
		CourseUserplanClassEntity en = new CourseUserplanClassEntity();
		if (null != pojo) {
			// 主键
			en.setUserplanClassId(pojo.getUserplanClassId());
			// 学员规划PK
			en.setUserplanId(pojo.getUserplanId());
			// 计划单号
			en.setUserplanClassNo(pojo.getUserplanClassNo());
			// 考试时段PK
			en.setExamScheduleId(pojo.getExamScheduleId());
			// 备注
			en.setRemark(pojo.getRemark());
			// 平台ID
			en.setSchoolId(pojo.getSchoolId());
			// 创建时间
			en.setCreateTime(pojo.getCreateTime());
			// 修改时间
			en.setModifyTime(pojo.getModifyTime());
			// 创建人
			en.setCreatePerson(pojo.getCreatePerson());
			// 修改人
			en.setModifyPerson(pojo.getModifyPerson());
			// dr
			en.setDr(pojo.getDr());
			// 审核
			en.setStatus(pojo.getStatus());
			// 部门id
			en.setDeptId(pojo.getDeptId());
		}
		return en;
	}

}
