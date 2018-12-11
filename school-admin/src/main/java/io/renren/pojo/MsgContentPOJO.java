package io.renren.pojo;

import java.io.Serializable;
import java.util.Date;

public class MsgContentPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long contentId;
	//标题
	private String contentTitle;
	//封面
	private String contentPic;
	//文章
	private String contentHtml;
	//跳转地址
	private String contentUrl;
	//站内消息;0.不推送1.推送
	private Integer commonSend;
	//弹窗消息0.不发送1.发送
	private Integer topSend;
	//推送0.不推送 1.推送
	private Integer pushSend;
	//通知对象0.学员;1.排课
	private Integer type;
	//排课PK,type=1时必填
	private String classplanId;
	//班型PK,type=1时必填
	private String classtypeIds;
	//发送方式:0.暂不发送;1.立即发送;2.定时发送
	private Integer sendType;
	//发送时间send_type=1默认当前时间,send_type=2用户输入时间
	private Date timestamp;
	//创建人
	private Long createPerson;
	//创建时间
	private Date createTime;
	//修改人
	private Long modifyPerson;
	//修改时间
	private Date modifyTime;
	//平台ID
	private String schoolId;
	
	//创建人
	private String creationName;
	//修改人
	private String modifiedName;
	//排课
	private String classplanName;
	public Long getContentId() {
		return contentId;
	}
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	public String getContentTitle() {
		return contentTitle;
	}
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	public String getContentPic() {
		return contentPic;
	}
	public void setContentPic(String contentPic) {
		this.contentPic = contentPic;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public String getContentUrl() {
		return contentUrl;
	}
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	public Integer getCommonSend() {
		return commonSend;
	}
	public void setCommonSend(Integer commonSend) {
		this.commonSend = commonSend;
	}
	public Integer getTopSend() {
		return topSend;
	}
	public void setTopSend(Integer topSend) {
		this.topSend = topSend;
	}
	public Integer getPushSend() {
		return pushSend;
	}
	public void setPushSend(Integer pushSend) {
		this.pushSend = pushSend;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getClassplanId() {
		return classplanId;
	}
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	public String getClasstypeIds() {
		return classtypeIds;
	}
	public void setClasstypeIds(String classtypeIds) {
		this.classtypeIds = classtypeIds;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	public Long getCreatePerson() {
		return createPerson;
	}
	public void setCreatePerson(Long createPerson) {
		this.createPerson = createPerson;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Long getModifyPerson() {
		return modifyPerson;
	}
	public void setModifyPerson(Long modifyPerson) {
		this.modifyPerson = modifyPerson;
	}
	public Date getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getSchoolId() {
		return schoolId;
	}
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
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
	public String getClassplanName() {
		return classplanName;
	}
	public void setClassplanName(String classplanName) {
		this.classplanName = classplanName;
	}
	
	
}
