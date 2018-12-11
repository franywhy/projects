package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.AppBannerEntity;

public class AppBannerPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
		private Long id;
		//名称
		private String name;
		//图片
		private String pic;
		//学历pk
		private Long levelId;
		//专业pk
		private Long professionId;
		//状态
		private Integer status;
		//排序
		private Integer orderNum;
		//平台ID
		private String schoolId;
		//创建时间
		private Date createTime;
		//修改人
		private Long modifyPerson;
		//修改时间
		private Date modifyTime;
		//创建人
		private Long createPerson;
		
		//学历
		private String levelName;
		//专业
		private String professionName;
		//创建人名字
		private String creationName;
		//修改人名字
		private String modifiedName;
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getPic() {
			return pic;
		}
		public void setPic(String pic) {
			this.pic = pic;
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
		public Integer getStatus() {
			return status;
		}
		public void setStatus(Integer status) {
			this.status = status;
		}
		public Integer getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(Integer orderNum) {
			this.orderNum = orderNum;
		}
		public String getSchoolId() {
			return schoolId;
		}
		public void setSchoolId(String schoolId) {
			this.schoolId = schoolId;
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
		public Long getCreatePerson() {
			return createPerson;
		}
		public void setCreatePerson(Long createPerson) {
			this.createPerson = createPerson;
		}
		
		public String getLevelName() {
			return levelName;
		}
		public void setLevelName(String levelName) {
			this.levelName = levelName;
		}
		public String getProfessionName() {
			return professionName;
		}
		public void setProfessionName(String professionName) {
			this.professionName = professionName;
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
			return "AppBannerPOJO [id=" + id + ", name=" + name + ", pic=" + pic + ", levelId=" + levelId
					+ ", professionId=" + professionId + ", status=" + status + ", orderNum=" + orderNum + ", schoolId="
					+ schoolId + ", createTime=" + createTime + ", modifyPerson=" + modifyPerson + ", modifyTime="
					+ modifyTime + ", createPerson=" + createPerson + ", levelName=" + levelName + ", professionName="
					+ professionName + ", creationName=" + creationName + ", modifiedName=" + modifiedName + "]";
		}
	public static AppBannerEntity getEntity(AppBannerPOJO pojo){
		AppBannerEntity en = new AppBannerEntity();
		if(null != pojo){
			//主键
			en.setId(pojo.getId());
			//名称
			en.setName(pojo.getName());
			//图片
			en.setPic(pojo.getPic());
			//学历pk
			en.setLevelId(pojo.getLevelId());
			//专业pk
			en.setProfessionId(pojo.getProfessionId());
			//状态
			en.setStatus(pojo.getStatus());
			//排序
			en.setOrderNum(pojo.getOrderNum());
			//平台ID
			en.setSchoolId(pojo.getSchoolId());
			//创建时间
			en.setCreateTime(pojo.getCreateTime());
			//创建人
			en.setCreatePerson(pojo.getCreatePerson());
			//修改时间
			en.setModifyTime(pojo.getModifyTime());
			//修改人
			en.setModifyPerson(pojo.getModifyPerson());
		}
		return en;
	}
}
