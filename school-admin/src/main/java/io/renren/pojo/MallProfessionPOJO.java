package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class MallProfessionPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
		private Long professionId;
		//APP图片
		private String appPic;
		//专业名称
		private String professionName;
		//是否删除
		private Integer dr;
		//创建用户
		private Long createPerson;
		//创建时间
		private Date creationTime;
		//修改用户
		private Long modifyPerson;
		//修改时间
		private Date modifiedTime;
		//机构ID
		private String schoolId;
		//是否使用
		private Integer status;
		//排序
		private Integer orderNum;
		//专业封面
		private String pic;
		//品牌封面
		private String brandPic;
		//专业简介
		private String remark;
		
		//创建人
		private String creationName;
		//修改人
		private String modifiedName;
		public Long getProfessionId() {
			return professionId;
		}
		public void setProfessionId(Long professionId) {
			this.professionId = professionId;
		}
		public String getAppPic() {
			return appPic;
		}
		public void setAppPic(String appPic) {
			this.appPic = appPic;
		}
		public String getProfessionName() {
			return professionName;
		}
		public void setProfessionName(String professionName) {
			this.professionName = professionName;
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
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
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
		
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
		}
		public String getBrandPic() {
			return brandPic;
		}
		public void setBrandPic(String brandPic) {
			this.brandPic = brandPic;
		}
		public String getRemark() {
			return remark;
		}
		public void setRemark(String remark) {
			this.remark = remark;
		}
		
		
		public Integer getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(Integer orderNum) {
			this.orderNum = orderNum;
		}
		@Override
		public String toString() {
			return "MallProfessionPOJO [professionId=" + professionId + ", appPic=" + appPic + ", professionName=" + professionName + ", dr=" + dr
					+ ", createPerson=" + createPerson + ", creationTime=" + creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime="
					+ modifiedTime + ", schoolId=" + schoolId + ", status=" + status + ", orderNum=" + orderNum + ", pic=" + pic + ", brandPic="
					+ brandPic + ", remark=" + remark + ", creationName=" + creationName + ", modifiedName=" + modifiedName + "]";
		}
		
}
