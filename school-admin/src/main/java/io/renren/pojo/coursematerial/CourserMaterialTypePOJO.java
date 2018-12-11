package io.renren.pojo.coursematerial;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.CourseMaterialEntity;
import io.renren.entity.CourseMaterialTypeEntity;

public class CourserMaterialTypePOJO implements Serializable {
	private static final long serialVersionUID = -1437655416089860937L;
	//主键
		private Long materialTypeId;
		//资料库PK
		private Long materialId;
		//名称
		private String materialTypeName;
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
		//排序
		private Integer orderNum;

	public Long getMaterialTypeId() {
			return materialTypeId;
		}
		public void setMaterialTypeId(Long materialTypeId) {
			this.materialTypeId = materialTypeId;
		}
		public String getMaterialTypeName() {
			return materialTypeName;
		}
		public void setMaterialTypeName(String materialTypeName) {
			this.materialTypeName = materialTypeName;
		}
		public Integer getOrderNum() {
			return orderNum;
		}
		public void setOrderNum(Integer orderNum) {
			this.orderNum = orderNum;
		}
	/**
	 * 设置：主键
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	/**
	 * 获取：主键
	 */
	public Long getMaterialId() {
		return materialId;
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
	public static CourseMaterialTypeEntity getInstance(CourserMaterialTypePOJO cmtpojo){
		   CourseMaterialTypeEntity cmte=new CourseMaterialTypeEntity();
		   cmte.setCreatePerson(cmtpojo.getCreatePerson());
		   cmte.setCreationTime(cmtpojo.getCreationTime());
		   cmte.setMaterialId(cmtpojo.getMaterialId());
		   cmte.setMaterialTypeName(cmtpojo.getMaterialTypeName());
		   cmte.setMaterialTypeId(cmtpojo.getMaterialTypeId());
		   cmte.setModifiedTime(cmtpojo.getModifiedTime());
		   cmte.setModifyPerson(cmtpojo.getModifyPerson());
		   cmte.setOrderNum(cmtpojo.getOrderNum());
		   cmte.setSchoolId(cmtpojo.getSchoolId());
		   return cmte;
	   } 
}
