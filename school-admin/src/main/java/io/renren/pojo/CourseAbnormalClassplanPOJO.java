package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;


/**
 * 弃考免考记录单
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2018-03-17 17:58:51
 */
public class CourseAbnormalClassplanPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//pk
	private Long id;
	private String abnormalClassplanNo;
	//学员pk
	private Long studentId;
	private String studentName;
	private String studentMobile;
	//异常类型(0-正常 1-休学 2-失联)
	private Integer abnormalType;
	private String abnormalTypeName;
	//订单pk
	private Long orderId;
	private String orderNo;
	private String orderName;
	//
	private Long courseId;
	private String courseName;
	//
	private String classplanId;
	private String classplanName;
	//审核状态(0-审核中 1-取消  2-通过)
	private Integer auditStatus;
	private String auditStatusName;
	//异常原因
	private String abnormalReason;
	//备注
	private String remark;

	//审核用户
	private Long modifyPerson;
	//修改用户
	private Long updatePerson;
	//申请用户pk
	private Long createPerson;

	@Override
	public String toString() {
		return "CourseAbnormalClassplanPOJO{" +
				"id=" + id +
				", abnormalClassplanNo='" + abnormalClassplanNo + '\'' +
				", studentId=" + studentId +
				", studentName='" + studentName + '\'' +
				", studentMobile='" + studentMobile + '\'' +
				", abnormalType=" + abnormalType +
				", abnormalTypeName='" + abnormalTypeName + '\'' +
				", orderId=" + orderId +
				", orderNo='" + orderNo + '\'' +
				", orderName='" + orderName + '\'' +
				", courseId=" + courseId +
				", courseName='" + courseName + '\'' +
				", classplanId='" + classplanId + '\'' +
				", classplanName='" + classplanName + '\'' +
				", auditStatus=" + auditStatus +
				", auditStatusName='" + auditStatusName + '\'' +
				", abnormalReason='" + abnormalReason + '\'' +
				", remark='" + remark + '\'' +
				", modifyPerson=" + modifyPerson +
				", updatePerson=" + updatePerson +
				", createPerson=" + createPerson +
				'}';
	}

	public Long getModifyPerson() {
		return modifyPerson;
	}

	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}

	public Long getUpdatePerson() {
		return updatePerson;
	}

	public void setUpdatePerson(Long updatePerson) {
		this.updatePerson = updatePerson;
	}

	public Long getCreatePerson() {
		return createPerson;
	}

	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}

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

	public String getStudentName() {
		return studentName;
	}

	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}

	public String getStudentMobile() {
		return studentMobile;
	}

	public void setStudentMobile(String studentMobile) {
		this.studentMobile = studentMobile;
	}

	public String getAbnormalTypeName() {
		return abnormalTypeName;
	}

	public void setAbnormalTypeName(String abnormalTypeName) {
		this.abnormalTypeName = abnormalTypeName;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public String getClassplanName() {
		return classplanName;
	}

	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}

	public String getAuditStatusName() {
		return auditStatusName;
	}

	public void setAuditStatusName(String auditStatusName) {
		this.auditStatusName = auditStatusName;
	}
}
