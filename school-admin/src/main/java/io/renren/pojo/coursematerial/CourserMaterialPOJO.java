package io.renren.pojo.coursematerial;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import io.renren.entity.CourseMaterialEntity;
import io.renren.pojo.timetable.TimeTableDetailPOJO;

public class CourserMaterialPOJO implements Serializable {
	private static final long serialVersionUID = -1698175868815645577L;
	//主键
	private Long materialId;
	//名称
	private String materialName;
	//课程PK
	private Long courseId;
	
	private String  courseName;
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
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
	//产品线pk
	private Long productId;
	//产品线名称
	private String productName;
	
	//班型列表
	private String classTypeIds;
	
    //子表集合
	private List<CourserMaterialTypePOJO> typeList;
	/**
	 * 设置：主键
	 */
	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}
	public List<CourserMaterialTypePOJO> getTypeList() {
		return typeList;
	}
	public void setTypeList(List<CourserMaterialTypePOJO> typeList) {
		this.typeList = typeList;
	}
	/**
	 * 获取：主键
	 */
	public Long getMaterialId() {
		return materialId;
	}
	/**
	 * 设置：名称
	 */
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	/**
	 * 获取：名称
	 */
	public String getMaterialName() {
		return materialName;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
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
	
	public String getClassTypeIds() {
		return classTypeIds;
	}
	public void setClassTypeIds(String classTypeIds) {
		this.classTypeIds = classTypeIds;
	}
	public static CourseMaterialEntity getInstance(CourserMaterialPOJO cmpojo){
	   CourseMaterialEntity cme=new CourseMaterialEntity();
	   cme.setCourseId(cmpojo.getCourseId());
	   cme.setMaterialId(cmpojo.getMaterialId());
	   cme.setCreatePerson(cmpojo.getCreatePerson());
	   cme.setCreationTime(cmpojo.getCreationTime());
	   cme.setMaterialName(cmpojo.getMaterialName());
	   cme.setModifiedTime(cmpojo.getModifiedTime());
	   cme.setModifyPerson(cmpojo.getModifyPerson());
	   cme.setSchoolId(cmpojo.getSchoolId());
	   cme.setProductId(cmpojo.getProductId());
	   cme.setClassTypeIds(cmpojo.getClassTypeIds());
	   return cme;
	} 
}
