package io.renren.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 弃考免考记录单
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
public class CourseAbnormalClassplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long id;
	private String abnormalClassplanNo;
	//学员pk
	private Long studentId;
	//异常类型(0-正常 1-休学 2-失联)
	private Integer abnormalType;
	//
	private Long courseId;
	//
	private String classplanId;
	//审核状态(0-审核中 1-取消  2-通过)
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
	 * 设置：学员pk
	 */
	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}
	/**
	 * 获取：学员pk
	 */
	public Long getStudentId() {
		return studentId;
	}
	/**
	 * 设置：异常类型(0-正常 1-休学 2-失联)
	 */
	public void setAbnormalType(Integer abnormalType) {
		this.abnormalType = abnormalType;
	}
	/**
	 * 获取：异常类型(0-正常 1-休学 2-失联)
	 */
	public Integer getAbnormalType() {
		return abnormalType;
	}
	/**
	 * 设置：
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：
	 */
	public Long getCourseId() {
		return courseId;
	}

	public String getAbnormalClassplanNo() {
		return abnormalClassplanNo;
	}

	public void setAbnormalClassplanNo(String abnormalClassplanNo) {
		this.abnormalClassplanNo = abnormalClassplanNo;
	}

	@Override
	public String toString() {
		return "CourseAbnormalClassplanEntity{" +
				"id=" + id +
				", abnormalClassplanNo='" + abnormalClassplanNo + '\'' +
				", studentId=" + studentId +
				", abnormalType=" + abnormalType +
				", courseId=" + courseId +
				", classplanId='" + classplanId + '\'' +
				", auditStatus=" + auditStatus +
				", abnormalReason='" + abnormalReason + '\'' +
				", remark='" + remark + '\'' +
				", createTime=" + createTime +
				", createPerson=" + createPerson +
				", modifyPerson=" + modifyPerson +
				", modifiedTime=" + modifiedTime +
				", updatePerson=" + updatePerson +
				", updateTime=" + updateTime +
				'}';
	}

	/**
	 * 设置：
	 */
	public void setClassplanId(String classplanId) {

		this.classplanId = classplanId;
	}
	/**
	 * 获取：
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：审核状态(0-审核中 1-取消  2-通过)
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * 获取：审核状态(0-审核中 1-取消  2-通过)
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
}
