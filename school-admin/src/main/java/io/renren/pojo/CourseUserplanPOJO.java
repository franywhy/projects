package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class CourseUserplanPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
		private Long userPlanId;
		//学员PK
		private Long userId;
		//商品PK
		private Long commodityId;
		//省份ID
		private Long areaId;
		//班级ID
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
		
		//学员名称
		private String userName;
		//学员手机
		private String userMobile;
		//商品名称
		private String commodityName;
		//省份名称
		private String areaName;
		//班级名称
		private String className;
		//班型名称
		private String classTypeName;
		//专业名称
		private String professionName;
		//层次名
		private String levelName;
		//班主任
		private String teacherName;
		//部门PK
		private Long deptId;
		//部门
		private Long deptName;
		
		@Override
		public String toString() {
			return "CourseUserplanPOJO [userPlanId=" + userPlanId + ", userId=" + userId + ", commodityId=" + commodityId + ", areaId=" + areaId
					+ ", classId=" + classId + ", isRep=" + isRep + ", isMatch=" + isMatch + ", classTypeId=" + classTypeId + ", status=" + status
					+ ", createPerson=" + createPerson + ", creationTime=" + creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime="
					+ modifiedTime + ", schoolId=" + schoolId + ", dr=" + dr + ", orderId=" + orderId + ", orderNo=" + orderNo + ", userplanStatus="
					+ userplanStatus + ", graduateTime=" + graduateTime + ", examScheduleId=" + examScheduleId + ", levelId=" + levelId
					+ ", professionId=" + professionId + ", userName=" + userName + ", userMobile=" + userMobile + ", commodityName=" + commodityName
					+ ", areaName=" + areaName + ", className=" + className + ", classTypeName=" + classTypeName + ", professionName="
					+ professionName + ", levelName=" + levelName + ", teacherName=" + teacherName + ", deptId=" + deptId + ", deptName=" + deptName
					+ "]";
		}
		public Long getDeptName() {
			return deptName;
		}
		public void setDeptName(Long deptName) {
			this.deptName = deptName;
		}
		public Long getDeptId() {
			return deptId;
		}
		public void setDeptId(Long deptId) {
			this.deptId = deptId;
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

		public String getTeacherName() {
			return teacherName;
		}

		public void setTeacherName(String teacherName) {
			this.teacherName = teacherName;
		}

		public String getUserMobile() {
			return userMobile;
		}

		public void setUserMobile(String userMobile) {
			this.userMobile = userMobile;
		}

		public String getCommodityName() {
			return commodityName;
		}

		public void setCommodityName(String commodityName) {
			this.commodityName = commodityName;
		}

		public String getAreaName() {
			return areaName;
		}

		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}

		public String getClassName() {
			return className;
		}

		public void setClassName(String className) {
			this.className = className;
		}

		public String getClassTypeName() {
			return classTypeName;
		}

		public void setClassTypeName(String classTypeName) {
			this.classTypeName = classTypeName;
		}

		public Long getUserPlanId() {
			return userPlanId;
		}

		public void setUserPlanId(Long userPlanId) {
			this.userPlanId = userPlanId;
		}

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public Long getCommodityId() {
			return commodityId;
		}

		public void setCommodityId(Long commodityId) {
			this.commodityId = commodityId;
		}

		public Long getAreaId() {
			return areaId;
		}

		public void setAreaId(Long areaId) {
			this.areaId = areaId;
		}

		public Long getClassId() {
			return classId;
		}

		public void setClassId(Long classId) {
			this.classId = classId;
		}

		public Integer getIsRep() {
			return isRep;
		}

		public void setIsRep(Integer isRep) {
			this.isRep = isRep;
		}

		public Integer getIsMatch() {
			return isMatch;
		}

		public void setIsMatch(Integer isMatch) {
			this.isMatch = isMatch;
		}

		public Long getClassTypeId() {
			return classTypeId;
		}

		public void setClassTypeId(Long classTypeId) {
			this.classTypeId = classTypeId;
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

		public String getSchoolId() {
			return schoolId;
		}

		public void setSchoolId(String schoolId) {
			this.schoolId = schoolId;
		}

		public Integer getDr() {
			return dr;
		}

		public void setDr(Integer dr) {
			this.dr = dr;
		}

		public Long getOrderId() {
			return orderId;
		}

		public void setOrderId(Long orderId) {
			this.orderId = orderId;
		}

		public String getOrderNo() {
			return orderNo;
		}

		public void setOrderNo(String orderNo) {
			this.orderNo = orderNo;
		}

		public Integer getUserplanStatus() {
			return userplanStatus;
		}

		public void setUserplanStatus(Integer userplanStatus) {
			this.userplanStatus = userplanStatus;
		}

		public Date getGraduateTime() {
			return graduateTime;
		}

		public void setGraduateTime(Date graduateTime) {
			this.graduateTime = graduateTime;
		}

		public Long getExamScheduleId() {
			return examScheduleId;
		}

		public void setExamScheduleId(Long examScheduleId) {
			this.examScheduleId = examScheduleId;
		}

		public Long getLevelId() {
			return levelId;
		}

		public void setLevelId(Long levelId) {
			this.levelId = levelId;
		}

		public Long getProfessionId() {
			return professionId;
		}

		public void setProfessionId(Long professionId) {
			this.professionId = professionId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
		
}
