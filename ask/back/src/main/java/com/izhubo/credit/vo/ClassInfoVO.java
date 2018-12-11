package com.izhubo.credit.vo;

/**
 *班级
 * @author 严志城
 * @time 2017年3月21日19:29:02
 */
public class ClassInfoVO {
	private String classId;
	private String subjectilesid;
	
	/**
	 * 班级名称
	 */
	private String className;
	
	/**
	 * 班级编码
	 */
	private String classCode;
	
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getSubjectilesid() {
		return subjectilesid;
	}
	public void setSubjectilesid(String subjectilesid) {
		this.subjectilesid = subjectilesid;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public String getClassCode() {
		return classCode;
	}
	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}
	
}
