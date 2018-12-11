package io.renren.entity;

import java.io.Serializable;
import java.util.Date;

import io.renren.utils.Constant;



/**
 * 班级档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 10:24:52
 */
public class MallClassEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//班级id
	private Long classId;
	//机构id
	private String schoolId;
	//是否删除   0.未删除  1.删除   用于软删除
	private Integer dr = Constant.DR.NORMAL.getValue();
	//省份
	private Long areaId;
	//专业
	private Long professionId;
	//学历
	private Long levelId;
	//班主任
	private Long userId;
	//班级名称
	private String className;
	//审核状态  1.已审核  0.未审核
	private Integer status = (int) Constant.Status.RESUME.getValue();
	//默认班级
	private Integer defaultClass;
	//备注
	private String remake;
	//创建用户
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改用户
	private Long modifier;
	//修改时间
	private Date modifiedTime;
	
	//省份名称
	private String areaName;
	//专业名称
	private String professionName;
	//班主任名称
	private String classTeacherName;
	//学历层次名称
	private String levelName;
	
	//产品线
	private Long productId;
	//部门
	private Long deptId;
	
	
	
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
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
	public Long getAreaId() {
		return areaId;
	}
	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}
	public Long getProfessionId() {
		return professionId;
	}
	public void setProfessionId(Long professionId) {
		this.professionId = professionId;
	}
	public Long getLevelId() {
		return levelId;
	}
	public void setLevelId(Long levelId) {
		this.levelId = levelId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getDefaultClass() {
		return defaultClass;
	}
	public void setDefaultClass(Integer defaultClass) {
		this.defaultClass = defaultClass;
	}
	public String getRemake() {
		return remake;
	}
	public void setRemake(String remake) {
		this.remake = remake;
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
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getProfessionName() {
		return professionName;
	}
	public void setProfessionName(String professionName) {
		this.professionName = professionName;
	}
	public String getClassTeacherName() {
		return classTeacherName;
	}
	public void setClassTeacherName(String classTeacherName) {
		this.classTeacherName = classTeacherName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	@Override
	public String toString() {
		return "MallClassEntity [classId=" + classId + ", schoolId=" + schoolId + ", dr=" + dr + ", areaId=" + areaId + ", professionId="
				+ professionId + ", levelId=" + levelId + ", userId=" + userId + ", className=" + className + ", status=" + status + ", defaultClass="
				+ defaultClass + ", remake=" + remake + ", creator=" + creator + ", creationTime=" + creationTime + ", modifier=" + modifier
				+ ", modifiedTime=" + modifiedTime + ", areaName=" + areaName + ", professionName=" + professionName + ", classTeacherName="
				+ classTeacherName + ", levelName=" + levelName + ", productId=" + productId + ", deptId=" + deptId + "]";
	}

	
}
