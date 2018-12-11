package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 课次通知提醒详情表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-10-25 14:21:14
 */
public class PushClassplanDetailRemindEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Integer id;
	//课次通知提醒ID
	private Integer pushClassplanRemindId;
	//排课计划ID
	private String courseClassplanId;
	//排课计划详情ID
	private String courseClassplanLivesId;
	//推送内容 ***替换课次名称
	private String pushContent;
	//推送时间 格式：yyyy-MM-dd HH:mm:ss
	private String pushTime;
	//审核状态 0：未通过 1：通过
	private Integer auditStatus;
	//审核人
	private Long auditor;
	//审核时间
	private Date auditTime;
	//创建人
	private Long creater;
	//创建时间
	private Date createTime;
	//消息发生标志
	private String msgId;
	//是否删除 0：正常 1：删除
	private Integer dr;
	//更新时间
	private Date ts;
	//通知模板ID
	private Integer pushTimeTemplateId;
	
	private String classplanLiveName;
	private Date startTime;
	private Date endTime;
	private String teacherName;
	private String templateName;
	private String auditorName;
	private Integer detailId;
	private String classplanId;

	public String getClassplanId() {
		return classplanId;
	}
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	public Integer getDetailId() {
		return detailId;
	}
	public void setDetailId(Integer detailId) {
		this.detailId = detailId;
	}
	public String getClassplanLiveName() {
		return classplanLiveName;
	}
	public void setClassplanLiveName(String classplanLiveName) {
		this.classplanLiveName = classplanLiveName;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public String getTeacherName() {
		return teacherName;
	}
	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public Integer getPushTimeTemplateId() {
		return pushTimeTemplateId;
	}
	public void setPushTimeTemplateId(Integer pushTimeTemplateId) {
		this.pushTimeTemplateId = pushTimeTemplateId;
	}
	/**
	 * 设置：主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：课次通知提醒ID
	 */
	public void setPushClassplanRemindId(Integer pushClassplanRemindId) {
		this.pushClassplanRemindId = pushClassplanRemindId;
	}
	/**
	 * 获取：课次通知提醒ID
	 */
	public Integer getPushClassplanRemindId() {
		return pushClassplanRemindId;
	}
	/**
	 * 设置：排课计划ID
	 */
	public void setCourseClassplanId(String courseClassplanId) {
		this.courseClassplanId = courseClassplanId;
	}
	/**
	 * 获取：排课计划ID
	 */
	public String getCourseClassplanId() {
		return courseClassplanId;
	}
	/**
	 * 设置：排课计划详情ID
	 */
	public void setCourseClassplanLivesId(String courseClassplanLivesId) {
		this.courseClassplanLivesId = courseClassplanLivesId;
	}
	/**
	 * 获取：排课计划详情ID
	 */
	public String getCourseClassplanLivesId() {
		return courseClassplanLivesId;
	}
	/**
	 * 设置：推送内容 ***替换课次名称
	 */
	public void setPushContent(String pushContent) {
		this.pushContent = pushContent;
	}
	/**
	 * 获取：推送内容 ***替换课次名称
	 */
	public String getPushContent() {
		return pushContent;
	}
	/**
	 * 设置：推送时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	public void setPushTime(String pushTime) {
		this.pushTime = pushTime;
	}
	/**
	 * 获取：推送时间 格式：yyyy-MM-dd HH:mm:ss
	 */
	public String getPushTime() {
		return pushTime;
	}
	/**
	 * 设置：审核状态 0：未通过 1：通过
	 */
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	/**
	 * 获取：审核状态 0：未通过 1：通过
	 */
	public Integer getAuditStatus() {
		return auditStatus;
	}
	/**
	 * 设置：审核人
	 */
	public void setAuditor(Long auditor) {
		this.auditor = auditor;
	}
	/**
	 * 获取：审核人
	 */
	public Long getAuditor() {
		return auditor;
	}
	/**
	 * 设置：审核时间
	 */
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	/**
	 * 获取：审核时间
	 */
	public Date getAuditTime() {
		return auditTime;
	}
	/**
	 * 设置：创建人
	 */
	public void setCreater(Long creater) {
		this.creater = creater;
	}
	/**
	 * 获取：创建人
	 */
	public Long getCreater() {
		return creater;
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
	 * 设置：消息发生标志
	 */
	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}
	/**
	 * 获取：消息发生标志
	 */
	public String getMsgId() {
		return msgId;
	}
	/**
	 * 设置：是否删除 0：正常 1：删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：是否删除 0：正常 1：删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：更新时间
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}
	/**
	 * 获取：更新时间
	 */
	public Date getTs() {
		return ts;
	}
	@Override
	public String toString() {
		return "PushClassplanDetailRemindEntity [id=" + id + ", pushClassplanRemindId=" + pushClassplanRemindId
				+ ", courseClassplanId=" + courseClassplanId + ", courseClassplanLivesId=" + courseClassplanLivesId
				+ ", pushContent=" + pushContent + ", pushTime=" + pushTime + ", auditStatus=" + auditStatus
				+ ", auditor=" + auditor + ", auditTime=" + auditTime + ", creater=" + creater + ", createTime="
				+ createTime + ", msgId=" + msgId + ", dr=" + dr + ", ts=" + ts + ", pushTimeTemplateId="
				+ pushTimeTemplateId + "]";
	}
}
