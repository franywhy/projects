package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class MallFeedbackRecordPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//序号
	private Long id;
	//反馈类型
	private String feedbackType;
	//反馈内容
	private String feedbackContent;
	//创建人
	private Long createPerson;
	//修改人
	private Long modifyPerson;
	//创建时间
	private Date createTime;
	//修改时间
	private Date modifyTime;
	//状态
	private Integer status;
	//平台id
	private String schoolId;
	
	//创建人
	private String creationName;
	//修改人
	private String modifiedName;
	
	/**
	 * 设置：序号
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：序号
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：反馈类型
	 */
	public void setFeedbackType(String feedbackType) {
		this.feedbackType = feedbackType;
	}
	/**
	 * 获取：反馈类型
	 */
	public String getFeedbackType() {
		return feedbackType;
	}
	/**
	 * 设置：反馈内容
	 */
	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}
	/**
	 * 获取：反馈内容
	 */
	public String getFeedbackContent() {
		return feedbackContent;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreatePerson() {
		return createPerson;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	/**
	 * 获取：修改人
	 */
	public Long getModifyPerson() {
		return modifyPerson;
	}
	/**
	 * 设置：创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 设置：修改时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	/**
	 * 获取：修改时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}
	/**
	 * 设置：状态
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态
	 */
	public Integer getStatus() {
		return status;
	}
	/**
	 * 获取：平台id
	 */
	public String getSchoolId() {
		return schoolId;
	}
	/**
	 * 设置：平台id
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：创建人
	 */
	public String getCreationName() {
		return creationName;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreationName(String creationName) {
		this.creationName = creationName;
	}
	/**
	 * 获取：修改人
	 */
	public String getModifiedName() {
		return modifiedName;
	}
	/**
	 * 设置：修改人
	 */
	public void setModifiedName(String modifiedName) {
		this.modifiedName = modifiedName;
	}
	
}
