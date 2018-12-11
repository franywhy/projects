package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 学员规划
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:16
 */
public class CourseUserplanEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long userPlanId;
	//学员PK
	private Long userId;
	//商品PK
	private Long commodityId;
	//省份ID
	private Long areaId;
	//
	private Long classId;
	//是有学习代替课程 0.否1.正常
	private Integer isRep;
	//专业对口0.对口;1不对口
	private Integer isMatch;
	//班型PK
	private Long classTypeId;
	//状态
	private Integer status;
	//创建用户
	private Long createPerson;
	//创建时间
	private Date creationTime;
	//最近修改用户
	private Long modifyPerson;
	//最近修改日期
	private Date modifiedTime;
	//平台PK
	private String schoolId;
	//删除标志0正常1删除
	private Integer dr;
	//订单PK
	private Long orderId;
	//订单编号
	private String orderNo;
	//学员规划状态:0.正常;1.毕业;2.休学
	private Integer userplanStatus;
	//毕业时间
	private Date graduateTime;
	//最近考试时段PK
	private Long examScheduleId;
	//层次PK
	private Long levelId;
	//专业
	private Long professionId;
	//部门PK
	private Long deptId;
	//产品线
	private Long productId;
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getProfessionId() {
		return professionId;
	}
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	/**
	 * 设置：主键
	 */
	public void setUserPlanId(Long userPlanId) {
		this.userPlanId = userPlanId;
	}
	/**
	 * 获取：主键
	 */
	public Long getUserPlanId() {
		return userPlanId;
	}
	/**
	 * 设置：学员PK
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	/**
	 * 获取：学员PK
	 */
	public Long getUserId() {
		return userId;
	}
	/**
	 * 设置：商品PK
	 */
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	/**
	 * 获取：商品PK
	 */
	public Long getCommodityId() {
		return commodityId;
	}
	/**
	 * 设置：
	 */
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	/**
	 * 获取：
	 */
	public Long getAreaId() {
		return areaId;
	}
	/**
	 * 设置：
	 */
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	/**
	 * 获取：
	 */
	public Long getClassId() {
		return classId;
	}
	/**
	 * 设置：是有学习代替课程 0.否1.正常
	 */
	public void setIsRep(Integer isRep) {
		this.isRep = isRep;
	}
	/**
	 * 获取：是有学习代替课程 0.否1.正常
	 */
	public Integer getIsRep() {
		return isRep;
	}
	/**
	 * 设置：专业对口0.对口;1不对口
	 */
	public void setIsMatch(Integer isMatch) {
		this.isMatch = isMatch;
	}
	/**
	 * 获取：专业对口0.对口;1不对口
	 */
	public Integer getIsMatch() {
		return isMatch;
	}
	/**
	 * 设置：班型PK
	 */
	public void setClassTypeId(Long classTypeId) {
		this.classTypeId = classTypeId;
	}
	/**
	 * 获取：班型PK
	 */
	public Long getClassTypeId() {
		return classTypeId;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 设置：创建用户
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建用户
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
	 * 设置：最近修改用户
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：最近修改用户
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：最近修改日期
	 */
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	/**
	 * 获取：最近修改日期
	 */
	public Date getModifiedTime() {
		return modifiedTime;
	}
	/**
	 * 设置：平台PK
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台PK
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：删除标志0正常1删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标志0正常1删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：订单PK
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	/**
	 * 获取：订单PK
	 */
	public Long getOrderId() {
		return orderId;
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
	 * 设置：学员规划状态:0.正常;1.毕业;2.休学
	 */
	public void setUserplanStatus(Integer userplanStatus) {
		this.userplanStatus = userplanStatus;
	}
	/**
	 * 获取：学员规划状态:0.正常;1.毕业;2.休学
	 */
	public Integer getUserplanStatus() {
		return userplanStatus;
	}
	/**
	 * 设置：毕业时间
	 */
	public void setGraduateTime(Date graduateTime) {
		this.graduateTime = graduateTime;
	}
	/**
	 * 获取：毕业时间
	 */
	public Date getGraduateTime() {
		return graduateTime;
	}
	/**
	 * 设置：最近考试时段PK
	 */
	public void setExamScheduleId(Long examScheduleId) {
		this.examScheduleId = examScheduleId;
	}
	/**
	 * 获取：最近考试时段PK
	 */
	public Long getExamScheduleId() {
		return examScheduleId;
	}
	public Long getLevelId() {
		return levelId;
	}
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	@Override
	public String toString() {
		return "CourseUserplanEntity [userPlanId=" + userPlanId + ", userId=" + userId + ", commodityId=" + commodityId
				+ ", areaId=" + areaId + ", classId=" + classId + ", isRep=" + isRep + ", isMatch=" + isMatch
				+ ", classTypeId=" + classTypeId + ", status=" + status + ", createPerson=" + createPerson
				+ ", creationTime=" + creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime=" + modifiedTime
				+ ", schoolId=" + schoolId + ", dr=" + dr + ", orderId=" + orderId + ", orderNo=" + orderNo
				+ ", userplanStatus=" + userplanStatus + ", graduateTime=" + graduateTime + ", examScheduleId="
				+ examScheduleId + ", levelId=" + levelId + ", professionId=" + professionId + ", deptId=" + deptId
				+ ", productId=" + productId + "]";
	}
	
}
