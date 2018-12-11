package io.renren.pojo;

import java.util.Date;

public class PushClassplanRemindPojo {

	//主键
	private Integer id;
	//排课计划ID
	private String courseClassplanId;
	//推送模板ID
	private Integer pushTimeTemplateId;
	//创建人
	private Long creater;
	//创建时间
	private Date createTime;
	//最近更新人
	private Long updater;
	//
	private Date updateTime;
	//是否删除 0：正常 1：删除
	private Integer dr;
	
	private String pushTimeTemplateName;
	private String pushTimeTemplateContent;
	private String pushTimeTemplateTime;
	private Integer pushTimeTemplateDay;
	private Integer type;
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getPushTimeTemplateTime() {
		return pushTimeTemplateTime;
	}

	public void setPushTimeTemplateTime(String pushTimeTemplateTime) {
		this.pushTimeTemplateTime = pushTimeTemplateTime;
	}

	public Integer getPushTimeTemplateDay() {
		return pushTimeTemplateDay;
	}

	public void setPushTimeTemplateDay(Integer pushTimeTemplateDay) {
		this.pushTimeTemplateDay = pushTimeTemplateDay;
	}

	public String getPushTimeTemplateContent() {
		return pushTimeTemplateContent;
	}

	public void setPushTimeTemplateContent(String pushTimeTemplateContent) {
		this.pushTimeTemplateContent = pushTimeTemplateContent;
	}

	private String courseClassplanName;
	
	private String courseName;
	
	private Date startTime;
	
	private String teacherName;
	
	private String productName;
	
	private String updaterName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCourseClassplanId() {
		return courseClassplanId;
	}

	public void setCourseClassplanId(String courseClassplanId) {
		this.courseClassplanId = courseClassplanId;
	}

	public Integer getPushTimeTemplateId() {
		return pushTimeTemplateId;
	}

	public void setPushTimeTemplateId(Integer pushTimeTemplateId) {
		this.pushTimeTemplateId = pushTimeTemplateId;
	}

	public Long getCreater() {
		return creater;
	}

	public void setCreater(Long creater) {
		this.creater = creater;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdater() {
		return updater;
	}

	public void setUpdater(Long updater) {
		this.updater = updater;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public String getPushTimeTemplateName() {
		return pushTimeTemplateName;
	}

	public void setPushTimeTemplateName(String pushTimeTemplateName) {
		this.pushTimeTemplateName = pushTimeTemplateName;
	}

	public String getCourseClassplanName() {
		return courseClassplanName;
	}

	public void setCourseClassplanName(String courseClassplanName) {
		this.courseClassplanName = courseClassplanName;
	}

	public String getCourseName() {
		return courseName;
	}

	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getUpdaterName() {
		return updaterName;
	}

	public void setUpdaterName(String updaterName) {
		this.updaterName = updaterName;
	}

	@Override
	public String toString() {
		return "PushClassplanRemindPojo [id=" + id + ", courseClassplanId=" + courseClassplanId
				+ ", pushTimeTemplateId=" + pushTimeTemplateId + ", creater=" + creater + ", createTime=" + createTime
				+ ", updater=" + updater + ", updateTime=" + updateTime + ", dr=" + dr + ", pushTimeTemplateName="
				+ pushTimeTemplateName + ", pushTimeTemplateContent=" + pushTimeTemplateContent
				+ ", pushTimeTemplateTime=" + pushTimeTemplateTime + ", pushTimeTemplateDay=" + pushTimeTemplateDay
				+ ", type=" + type + ", courseClassplanName=" + courseClassplanName + ", courseName=" + courseName
				+ ", startTime=" + startTime + ", teacherName=" + teacherName + ", productName=" + productName
				+ ", updaterName=" + updaterName + "]";
	}

}
