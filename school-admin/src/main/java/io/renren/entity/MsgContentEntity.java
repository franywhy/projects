package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 消息表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-15 17:00:41
 */
public class MsgContentEntity implements Serializable {
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

	/**
	 * 设置：主键
	 */
	public void setContentId(Long contentId) {
		this.contentId = contentId;
	}
	/**
	 * 获取：主键
	 */
	public Long getContentId() {
		return contentId;
	}
	/**
	 * 设置：标题
	 */
	public void setContentTitle(String contentTitle) {
		this.contentTitle = contentTitle;
	}
	/**
	 * 获取：标题
	 */
	public String getContentTitle() {
		return contentTitle;
	}
	/**
	 * 设置：封面
	 */
	public void setContentPic(String contentPic) {
		this.contentPic = contentPic;
	}
	/**
	 * 获取：封面
	 */
	public String getContentPic() {
		return contentPic;
	}
	/**
	 * 设置：文章
	 */
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	/**
	 * 获取：文章
	 */
	public String getContentHtml() {
		return contentHtml;
	}
	/**
	 * 设置：跳转地址
	 */
	public void setContentUrl(String contentUrl) {
		this.contentUrl = contentUrl;
	}
	/**
	 * 获取：跳转地址
	 */
	public String getContentUrl() {
		return contentUrl;
	}
	/**
	 * 设置：站内消息;0.不推送1.推送
	 */
	public void setCommonSend(Integer commonSend) {
		this.commonSend = commonSend;
	}
	/**
	 * 获取：站内消息;0.不推送1.推送
	 */
	public Integer getCommonSend() {
		return commonSend;
	}
	/**
	 * 设置：弹窗消息0.不发送1.发送
	 */
	public void setTopSend(Integer topSend) {
		this.topSend = topSend;
	}
	/**
	 * 获取：弹窗消息0.不发送1.发送
	 */
	public Integer getTopSend() {
		return topSend;
	}
	/**
	 * 设置：推送0.不推送 1.推送
	 */
	public void setPushSend(Integer pushSend) {
		this.pushSend = pushSend;
	}
	/**
	 * 获取：推送0.不推送 1.推送
	 */
	public Integer getPushSend() {
		return pushSend;
	}
	/**
	 * 设置：通知对象0.学员;1.排课
	 */
	public void setType(Integer type) {
		this.type = type;
	}
	/**
	 * 获取：通知对象0.学员;1.排课
	 */
	public Integer getType() {
		return type;
	}
	/**
	 * 设置：排课PK,type=1时必填
	 */
	public void setClassplanId(String classplanId) {
		this.classplanId = classplanId;
	}
	/**
	 * 获取：排课PK,type=1时必填
	 */
	public String getClassplanId() {
		return classplanId;
	}
	/**
	 * 设置：班型PK,type=1时必填
	 */
	public void setClasstypeIds(String classtypeIds) {
		this.classtypeIds = classtypeIds;
	}
	/**
	 * 获取：班型PK,type=1时必填
	 */
	public String getClasstypeIds() {
		return classtypeIds;
	}
	/**
	 * 设置：发送方式:0.暂不发送;1.立即发送;2.定时发送
	 */
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	
	/**
	 * 获取：发送方式:0.暂不发送;1.立即发送;2.定时发送
	 */
	public Integer getSendType() {
		return sendType;
	}
	/**
	 * 设置：发送时间send_type=1默认当前时间,send_type=2用户输入时间
	 */
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	/**
	 * 获取：发送时间send_type=1默认当前时间,send_type=2用户输入时间
	 */
	public Date getTimestamp() {
		return timestamp;
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
	 * 设置：平台ID
	 */
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	/**
	 * 获取：平台ID
	 */
	public String getSchoolId() {
		return schoolId;
	}
}
