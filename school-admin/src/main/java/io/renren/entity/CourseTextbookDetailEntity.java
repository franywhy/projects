package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 教材档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-16 16:09:29
 */
public class CourseTextbookDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private Integer textbookId;
	//教材名称
	private String textbookName;
	//课程代号
	private String courseNo;
	//课程
	private String courseName;
	//状态0.禁用1.启用
	private Integer status;
	//创建人
	private String createPerson;
	//创建时间
	private Date creationTime;
	//修改人
	private String modifyPerson;
	//修改时间
	private Date modifiedTime;
	//备注
	private String remark;
	
	public Integer getTextbookId() {
		return textbookId;
	}
	public void setTextbookId(Integer textbookId) {
		this.textbookId = textbookId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTextbookName() {
		return textbookName;
	}
	public void setTextbookName(String textbookName) {
		this.textbookName = textbookName;
	}
	public String getCourseNo() {
		return courseNo;
	}
	public void setCourseNo(String courseNo) {
		this.courseNo = courseNo;
	}

	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(String createPerson) {
		this.createPerson = createPerson;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(String modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public Date getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
    
}
