package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;



/**
 * 弃考档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-03-27 14:37:18
 */
public class CourseAbnormalAbandonExamPOJO implements Serializable {
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
	//订单编号
	private String orderNo;
	//班型名称
	private String classTypeName;
	//课程名称
	private String courseName;
	//报读省份
	private String applyProvince;
	//报考省份
	private String registerProvince;	
	//专业
	private String professionName;	
	//层次
	private String levelName;	
	//班级名称
	private String className;	
	//班主任
	private String teacherName;	
	//学员姓名
	private String studentName;	
	//手机号
	private String mobile;	
	//考试时段
	private String scheduleName;
	//考试时间
	private String scheduleDate;
	//产品线id
	private Long productId;
	//产品线名称
	private String productName;
	//状态 (0 : 审批中,1: 作废，2：通过）
	private Integer status;
	//删除标志
	private Integer dr;
	//原因
	private String reason;
	//备注
	private String remark;
	//报考id
	private Long registrationId;
	//报考单号
	private String registrationNo;
					
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
		
	
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getClassTypeName() {
		return classTypeName;
	}
	public void setClassTypeName(String classTypeName) {
		this.classTypeName = classTypeName;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	
	public String getApplyProvince() {
		return applyProvince;
	}
	public void setApplyProvince(String applyProvince) {
		this.applyProvince = applyProvince;
	}
	public String getRegisterProvince() {
		return registerProvince;
	}
	public void setRegisterProvince(String registerProvince) {
		this.registerProvince = registerProvince;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getStudentName() {
		return studentName;
	}
	public void setStudentName(String studentName) {
		this.studentName = studentName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	
	public String getScheduleName() {
		return scheduleName;
	}
	public void setScheduleName(String scheduleName) {
		this.scheduleName = scheduleName;
	}
	
	public String getScheduleDate() {
		return scheduleDate;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
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
	
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
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
	public String getRegistrationNo() {
		return registrationNo;
	}
	public void setRegistrationNo(String registrationNo) {
		this.registrationNo = registrationNo;
	}
	
}
