package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 资料库类型
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 17:42:53
 */
public class CourseMaterialTypeEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long materialTypeId;
	// 资料库PK
	private Long materialId;
	// 名称
	private String materialTypeName;
	// 创建用户
	private Long createPerson;
	// 创建时间
	private Date creationTime;
	// 最近修改用户
	private Long modifyPerson;
	// 最近修改日期
	private Date modifiedTime;
	// 平台PK
	private String schoolId;
	// 排序
	private Integer orderNum;

	// 产品线PK
	private Long productId;

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "CourseMaterialTypeEntity [materialTypeId=" + materialTypeId + ", materialId=" + materialId + ", materialTypeName=" + materialTypeName
				+ ", createPerson=" + createPerson + ", creationTime=" + creationTime + ", modifyPerson=" + modifyPerson + ", modifiedTime="
				+ modifiedTime + ", schoolId=" + schoolId + ", orderNum=" + orderNum + ", productId=" + productId + "]";
	}

	/**
	 * 设置：主键
	 */
	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	/**
	 * 获取：主键
	 */
	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	/**
	 * 设置：资料库PK
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	/**
	 * 获取：资料库PK
	 */
	public Long getMaterialId() {
		return materialId;
	}

	/**
	 * 设置：名称
	 */
	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}

	/**
	 * 获取：名称
	 */
	public String getMaterialTypeName() {
		return materialTypeName;
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
	 * 设置：排序
	 */
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取：排序
	 */
	public Integer getOrderNum() {
		return orderNum;
	}
}
