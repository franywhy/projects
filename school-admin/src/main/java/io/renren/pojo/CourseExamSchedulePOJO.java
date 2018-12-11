package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.CourseExamScheduleEntity;

public class CourseExamSchedulePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键id
		private Long courseExamId;
		//平台id
		private String schoolId;
		//是否删除0：否  1：是
		private Integer dr;
		//创建人
		private Long createPerson;
		//创建时间
		private Date createTime;
		//修改人
		private Long modifyPerson;
		//修改时间
		private Date modifyTime;
		//状态0：禁用  1：启用
		private Integer status;
		//备注
		private String remark;
		//课程pk
		private Long courseId;
		//省份pk
		private Long areaId;
		//考试时段pk
		private Long examScheduleId;
		//考试日期
		private Date examDate;
		//考试时段
		private Integer examBucket;
		
		//创建人名称
		private String creationName;
		//修改人名称
		private String modifiedName;
		//课程名称
		private String courseName;
		//省份名称
		private String areaName;
		//考试时段名称
		private String examScheduleName;
		public Long getCourseExamId() {
			return courseExamId;
		}
		public void setCourseExamId(Long courseExamId) {
			this.courseExamId = courseExamId;
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
		public Long getCreatePerson() {
			return createPerson;
		}
		public void setCreatePerson(Long createPerson) {
			this.createPerson = createPerson;
		}
		public Date getCreateTime() {
			return createTime;
		}
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		public Long getModifyPerson() {
			return modifyPerson;
		}
		public void setModifyPerson(Long modifyPerson) {
			this.modifyPerson = modifyPerson;
		}
		public Date getModifyTime() {
			return modifyTime;
		}
		public void setModifyTime(Date modifyTime) {
			this.modifyTime = modifyTime;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Long getCourseId() {
			return courseId;
		}
		public void setCourseId(Long courseId) {
			this.courseId = courseId;
		}
		public Long getAreaId() {
			return areaId;
		}
		public void setAreaId(Long areaId) {
			this.areaId = areaId;
		}
		public Long getExamScheduleId() {
			return examScheduleId;
		}
		public void setExamScheduleId(Long examScheduleId) {
			this.examScheduleId = examScheduleId;
		}
		public Date getExamDate() {
			return examDate;
		}
		public void setExamDate(Date examDate) {
			this.examDate = examDate;
		}
		public Integer getExamBucket() {
			return examBucket;
		}
		public void setExamBucket(Integer examBucket) {
			this.examBucket = examBucket;
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
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public String getAreaName() {
			return areaName;
		}
		public void setAreaName(String areaName) {
			this.areaName = areaName;
		}
		public String getExamScheduleName() {
			return examScheduleName;
		}
		public void setExamScheduleName(String examScheduleName) {
			this.examScheduleName = examScheduleName;
		}
		@Override
		public String toString() {
			return "CourseExamSchedulePOJO [courseExamId=" + courseExamId + ", schoolId=" + schoolId + ", dr=" + dr
					+ ", createPerson=" + createPerson + ", createTime=" + createTime + ", modifyPerson=" + modifyPerson
					+ ", modifyTime=" + modifyTime + ", status=" + status + ", remark=" + remark + ", courseId="
					+ courseId + ", areaId=" + areaId + ", examScheduleId=" + examScheduleId + ", examDate=" + examDate
					+ ", examBucket=" + examBucket + ", creationName=" + creationName + ", modifiedName=" + modifiedName
					+ ", courseName=" + courseName + ", areaName=" + areaName + ", examScheduleName=" + examScheduleName
					+ "]";
		}
		
		public static CourseExamScheduleEntity getEntity(CourseExamSchedulePOJO pojo){
			CourseExamScheduleEntity en = new CourseExamScheduleEntity();
			if(null != pojo){
				//主键id
				en.setCourseExamId(pojo.getCourseExamId());
				//平台id
				en.setSchoolId(pojo.getSchoolId());
				//是否删除0：否  1：是
				en.setDr(pojo.getDr());
				//创建人
				en.setCreatePerson(pojo.getCreatePerson());
				//创建时间
				en.setCreateTime(pojo.getCreateTime());
				//修改人
				en.setModifyPerson(pojo.getModifyPerson());
				//修改时间
				en.setModifyTime(pojo.getModifyTime());
				//状态0：禁用  1：启用
				en.setStatus(pojo.getStatus());
				//备注
				en.setRemark(pojo.getRemark());
				//课程pk
				en.setCourseId(pojo.getCourseId());
				//省份pk
				en.setAreaId(pojo.getAreaId());
				//考试时段pk
				en.setExamScheduleId(pojo.getExamScheduleId());
				//考试日期
				en.setExamDate(pojo.getExamDate());
				//考试时段
				en.setExamBucket(pojo.getExamBucket());
			}
			return en;
		}
}
