package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 资料库资料
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 10:10:01
 */
public class CourseMaterialDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	// 主键
	private Long detailId;
	// 名称
	private String detailName;
	// 资料库类型PK
	private Long materialTypeId;
	// html内容
	private String contentHtml;
	// 地址
	private String url;
	// 阅读量
	private Long readNum;
	// 排序
	private Integer orderNum;
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

	private String materialTypeName;

	private String materialName;

	private String materialId;

	// 产品线PK
	private Long productId;
	//产品线名称
	private String productName;

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	@Override
	public String toString() {
		return "CourseMaterialDetailEntity [detailId=" + detailId + ", detailName=" + detailName + ", materialTypeId="
				+ materialTypeId + ", contentHtml=" + contentHtml + ", url=" + url + ", readNum=" + readNum
				+ ", orderNum=" + orderNum + ", createPerson=" + createPerson + ", creationTime=" + creationTime
				+ ", modifyPerson=" + modifyPerson + ", modifiedTime=" + modifiedTime + ", schoolId=" + schoolId
				+ ", materialTypeName=" + materialTypeName + ", materialName=" + materialName + ", materialId="
				+ materialId + ", productId=" + productId + ", productName=" + productName + "]";
	}

	public String getMaterialId() {
		return materialId;
	}

	public void setMaterialId(String materialId) {
		this.materialId = materialId;
	}

	public String getMaterialTypeName() {
		return materialTypeName;
	}

	public void setMaterialTypeName(String materialTypeName) {
		this.materialTypeName = materialTypeName;
	}

	public String getMaterialName() {
		return materialName;
	}

	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}

	/**
	 * 设置：主键
	 */
	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	/**
	 * 获取：主键
	 */
	public Long getDetailId() {
		return detailId;
	}

	/**
	 * 设置：名称
	 */
	public void setDetailName(String detailName) {
		this.detailName = detailName;
	}

	/**
	 * 获取：名称
	 */
	public String getDetailName() {
		return detailName;
	}

	/**
	 * 设置：资料库类型PK
	 */
	public void setMaterialTypeId(Long materialTypeId) {
		this.materialTypeId = materialTypeId;
	}

	/**
	 * 获取：资料库类型PK
	 */
	public Long getMaterialTypeId() {
		return materialTypeId;
	}

	/**
	 * 设置：html内容
	 */
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}

	/**
	 * 获取：html内容
	 */
	public String getContentHtml() {
		return contentHtml;
	}

	/**
	 * 设置：地址
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 获取：地址
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 设置：阅读量
	 */
	public void setReadNum(Long readNum) {
		this.readNum = readNum;
	}

	/**
	 * 获取：阅读量
	 */
	public Long getReadNum() {
		return readNum;
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
}
