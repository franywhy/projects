package io.renren.pojo.classType;

import java.io.Serializable;
import java.util.Date;

import io.renren.entity.MallClassTypeEntity;
import io.renren.utils.Constant;

public class ClassTypePOJO implements Serializable {
	private static final long serialVersionUID = 1L;

	// 班型id
	private Long classtypeId;
	// 是否删除 0.未删除 1.删除 用于软删除
	private Integer dr = Constant.DR.NORMAL.getValue();
	// 机构id
	private String schoolId;
	// 班型名称
	private String classtypeName;
	// 创建用户
	private Long creator;
	// 创建时间
	private Date creationTime;
	// 修改用户
	private Long modifier;
	// 修改时间
	private Date modifiedTime;
	// 状态 0：禁用 1：正常
	private Integer status = (int) Constant.Status.RESUME.getValue();
	// 备注
	private String remake;
	// 产品线pk
	private Long productId;

	// 创建用户名称
	private String creationName;
	// 修改用户名称
	private String modifiedName;
	// 产品线名称
	private String productName;
	public Long getClasstypeId() {
		return classtypeId;
	}
	public void setClasstypeId(Long classtypeId) {
		this.classtypeId = classtypeId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	public String getClasstypeName() {
		return classtypeName;
	}
	public void setClasstypeName(String classtypeName) {
		this.classtypeName = classtypeName;
	}
	public Long getCreator() {
		return creator;
	}
	public void setCreator(Long creator) {
		this.creator = creator;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public Long getModifier() {
		return modifier;
	}
	public void setModifier(Long modifier) {
		this.modifier = modifier;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getRemake() {
		return remake;
	}
	public void setRemake(String remake) {
		this.remake = remake;
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
	
	@Override
	public String toString() {
		return "ClassTypePOJO [classtypeId=" + classtypeId + ", dr=" + dr + ", schoolId=" + schoolId
				+ ", classtypeName=" + classtypeName + ", creator=" + creator + ", creationTime=" + creationTime
				+ ", modifier=" + modifier + ", modifiedTime=" + modifiedTime + ", status=" + status + ", remake="
				+ remake + ", productId=" + productId + ", creationName=" + creationName + ", modifiedName="
				+ modifiedName + ", productName=" + productName + "]";
	}
	public static MallClassTypeEntity getEntity(ClassTypePOJO pojo){
		MallClassTypeEntity en = new MallClassTypeEntity();
		if(null != pojo){
			//班型id
			en.setClasstypeId(pojo.getClasstypeId());
			//是否删除   0.未删除  1.删除   用于软删除
			en.setDr(pojo.getDr());
			//机构id
			en.setSchoolId(pojo.getSchoolId());
			//班型名称
			en.setClasstypeName(pojo.getClasstypeName());
			//创建用户
			en.setCreator(pojo.getCreator());
			//创建时间
			en.setCreationTime(pojo.getCreationTime());
			//修改用户
			en.setModifier(pojo.getModifier());
			//修改时间
			en.setModifiedTime(pojo.getModifiedTime());
			//状态  0：禁用   1：正常
			en.setStatus(pojo.getStatus());
			//备注
			en.setRemake(pojo.getRemake());
			//产品线pk
			en.setProductId(pojo.getProductId());
		}
		return en;
	}
}
