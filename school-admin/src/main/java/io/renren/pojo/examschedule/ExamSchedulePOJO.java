package io.renren.pojo.examschedule;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.MallExamScheduleEntity;

public class ExamSchedulePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//表格自增ID
		private Long id;
		//考试时段名称
		private String scheduleName;
		//考试年月
		private String scheduleDate;
		//创建人
		private Long createPerson;
		//修改人
		private Long modifyPerson;
		//创建时间
		private Date createTime;
		//修改时间
		private Date modifyTime;
		//备注
		private String comments;
		//是否启用
		private Integer status;
		//机构id
		private String schoolId;
		//是否删除0：否  1：是
		private Integer dr;
		
		//创建人
		private String creationName;
		//修改人
		private String modifiedName;
		
		//产品线PK
		private Long productId;
		private String productName;
		
		
		public Long getProductId() {
			return productId;
		}
		public void setProductId(Long productId) {
			this.productId = productId;
		}
		public String getProductName() {
			return productName;
		}
		public void setProductName(String productName) {
			this.productName = productName;
		}
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
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
		public String getComments() {
			return comments;
		}
		public void setComments(String comments) {
			this.comments = comments;
		}
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
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
		@Override
		public String toString() {
			return "ExamSchedulePOJO [id=" + id + ", scheduleName=" + scheduleName + ", scheduleDate=" + scheduleDate + ", createPerson="
					+ createPerson + ", modifyPerson=" + modifyPerson + ", createTime=" + createTime + ", modifyTime=" + modifyTime + ", comments="
					+ comments + ", status=" + status + ", schoolId=" + schoolId + ", dr=" + dr + ", creationName=" + creationName + ", modifiedName="
					+ modifiedName + ", productId=" + productId + ", productName=" + productName + "]";
		}
		
		public static MallExamScheduleEntity getEntity(ExamSchedulePOJO pojo){
			MallExamScheduleEntity en = new MallExamScheduleEntity();
			if(null != pojo){
				//表格自增ID
				en.setId(pojo.getId());
				//考试时段名称
				en.setScheduleName(pojo.getScheduleName());
				//考试年月
				en.setScheduleDate(pojo.getScheduleDate());
				//创建人
				en.setCreatePerson(pojo.getCreatePerson());
				//修改人
				en.setModifyPerson(pojo.getModifyPerson());
				//创建时间
				en.setCreateTime(pojo.getCreateTime());
				//修改时间
				en.setModifyTime(pojo.getModifyTime());
				//备注
				en.setComments(pojo.getComments());
				//是否启用
				en.setStatus(pojo.getStatus());
				//机构id
				en.setSchoolId(pojo.getSchoolId());
				//是否删除0：否  1：是
				en.setDr(pojo.getDr());
			}
			return en;
		}

}
