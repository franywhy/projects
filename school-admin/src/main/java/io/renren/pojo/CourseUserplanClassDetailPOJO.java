package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.CourseUserplanClassDetailEntity;

public class CourseUserplanClassDetailPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
		private Long userplanClassDetailId;
		//学习计划PK
		private Long userplanClassId;
		//学员规划课程子表PK
		private Long userplanDetailId;
		//排课计划PK
		private String classplanId;
		//入课时间
		private Date timestamp;
		//备注
		private String remark;
		//dr
		private Integer dr;
		//平台id
		private String schoolId;
		//排序
		private Integer orderNum;
		
		//课程名称
		private String courseName;
		//排课计划名称
		private String classplanName;
		
		public String getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(String schoolId) {
			this.schoolId = schoolId;
		}
		public Integer getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(Integer orderNum) {
			this.orderNum = orderNum;
		}
		
		public String getCourseName() {
			return courseName;
		}
		public void setCourseName(String courseName) {
			this.courseName = courseName;
		}
		public Long getUserplanClassDetailId() {
			return userplanClassDetailId;
		}
		public void setUserplanClassDetailId(Long userplanClassDetailId) {
			this.userplanClassDetailId = userplanClassDetailId;
		}
		public Long getUserplanClassId() {
			return userplanClassId;
		}
		public void setUserplanClassId(Long userplanClassId) {
			this.userplanClassId = userplanClassId;
		}
		public Long getUserplanDetailId() {
			return userplanDetailId;
		}
		public void setUserplanDetailId(Long userplanDetailId) {
			this.userplanDetailId = userplanDetailId;
		}
		public String getClassplanId() {
			return classplanId;
		}
		public void setClassplanId(String classplanId) {
			this.classplanId = classplanId;
		}
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		public Integer getDr() {
			return dr;
		}
		public void setDr(Integer dr) {
			this.dr = dr;
		}
		public String getClassplanName() {
			return classplanName;
		}
		public void setClassplanName(String classplanName) {
			this.classplanName = classplanName;
		}
		
		
		@Override
		public String toString() {
			return "CourseUserplanClassDetailPOJO [userplanClassDetailId=" + userplanClassDetailId
					+ ", userplanClassId=" + userplanClassId + ", userplanDetailId=" + userplanDetailId
					+ ", classplanId=" + classplanId + ", timestamp=" + timestamp + ", remark=" + remark + ", dr=" + dr
					+ ", schoolId=" + schoolId + ", orderNum=" + orderNum + ", courseName=" + courseName
					+ ", classplanName=" + classplanName + "]";
		}
		public static CourseUserplanClassDetailEntity getEntity(CourseUserplanClassDetailPOJO pojo){
			CourseUserplanClassDetailEntity en = new CourseUserplanClassDetailEntity();
			if(null != pojo){
				//主键
				en.setUserplanClassDetailId(pojo.getUserplanClassDetailId());
				//学习计划PK
				en.setUserplanClassId(pojo.getUserplanClassId());
				//学员规划课程子表PK
				en.setUserplanDetailId(pojo.getUserplanDetailId());
				//排课计划PK
				en.setClassplanId(pojo.getClassplanId());
				//入课时间
				en.setTimestamp(pojo.getTimestamp());
				//备注
				en.setRemark(pojo.getRemark());
				//dr
				en.setDr(pojo.getDr());
				//schoolId
				en.setSchoolId(pojo.getSchoolId());
				//orderNum
				en.setOrderNum(pojo.getOrderNum());
			}
			return en;
		}
}
