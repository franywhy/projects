package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class ClassGoodsDeptsPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//班级id
	private Long classId;
	//商品id
	private Long goodId;
	//部门id
	private Long deptId;
	//软删除0：不删除 1：删除
	private Integer dr;
	//创建者
	private Long creator;
	//创建时间
	private Date creationTime;
	//修改者
	private Long modifier;
	//修改时间
	private Date modifyTime;
	
	//商品名称
	private String goodName;
	//校区名称
	private String deptName;
	//班级名称
	private String className;
	//班主任
	private String teacherName;
	//默认班级
	private Integer defaultClass;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getClassId() {
		return classId;
	}
	public void setClassId(Long classId) {
		this.classId = classId;
	}
	public Long getGoodId() {
		return goodId;
	}
	public void setGoodId(Long goodId) {
		this.goodId = goodId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Integer getDr() {
		return dr;
	}
	public void setDr(Integer dr) {
		this.dr = dr;
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
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public Integer getDefaultClass() {
		return defaultClass;
	}
	public void setDefaultClass(Integer defaultClass) {
		this.defaultClass = defaultClass;
	}
	@Override
	public String toString() {
		return "ClassGoodsDeptsPOJO [id=" + id + ", classId=" + classId + ", goodId=" + goodId + ", deptId=" + deptId
				+ ", dr=" + dr + ", creator=" + creator + ", creationTime=" + creationTime + ", modifier=" + modifier
				+ ", modifyTime=" + modifyTime + ", goodName=" + goodName + ", deptName=" + deptName + ", className="
				+ className + ", teacherName=" + teacherName + ", defaultClass=" + defaultClass + "]";
	}
	
}
