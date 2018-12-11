package io.renren.pojo.timetable;

import java.util.Date;

public class TimeTableDisplayPOJO {
	    //编号
		private Long number;
		//名称
		private String name;
		//建档时间
		private Date createTime;
		//最后修改日期
		private Date updateTime;
		//建档人
		private String createPerson;
		//最后修改人
		private String updatePerson;
		//备注
		private String comments;
		//是否停用
		private Integer status;
		/**
		 * 设置：编号
		 */
		public void setNumber(Long number) {
			this.number = number;
		}
		/**
		 * 获取：编号
		 */
		public Long getNumber() {
			return number;
		}
		/**
		 * 设置：名称
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * 获取：名称
		 */
		public String getName() {
			return name;
		}
		/**
		 * 设置：建档时间
		 */
		public void setCreateTime(Date createTime) {
			this.createTime = createTime;
		}
		/**
		 * 获取：建档时间
		 */
		public Date getCreateTime() {
			return createTime;
		}
		/**
		 * 设置：最后修改日期
		 */
		public void setUpdateTime(Date updateTime) {
			this.updateTime = updateTime;
		}
		/**
		 * 获取：最后修改日期
		 */
		public Date getUpdateTime() {
			return updateTime;
		}
		/**
		 * 设置：建档人
		 */
		public void setCreatePerson(String createPerson) {
			this.createPerson = createPerson;
		}
		/**
		 * 获取：建档人
		 */
		public String getCreatePerson() {
			return createPerson;
		}
		/**
		 * 设置：最后修改人
		 */
		public void setUpdatePerson(String updatePerson) {
			this.updatePerson = updatePerson;
		}
		/**
		 * 获取：最后修改人
		 */
		public String getUpdatePerson() {
			return updatePerson;
		}
		/**
		 * 设置：备注
		 */
		public void setComments(String comments) {
			this.comments = comments;
		}
		/**
		 * 获取：备注
		 */
		public String getComments() {
			return comments;
		}
		/**
		 * 设置：是否停用
		 */
		public void setStatus(Integer status) {
			this.status = status;
		}
		/**
		 * 获取：是否停用
		 */
		public Integer getStatus() {
			return status;
		}
}
