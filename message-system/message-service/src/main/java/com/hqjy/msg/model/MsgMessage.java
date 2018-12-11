package com.hqjy.msg.model;


import com.hqjy.msg.util.Constant;

import java.util.Date;

import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 消息表
 * @author Administrator
 *
 */
@Table(name = "msg_message")
public class MsgMessage {

	private Integer id;
	
	private String code;       //消息唯一代码
	
	private String sendBy;     //发送方
	
	private String recBy;       //接收方
	
	private Date sendTime;      //发送时间
	
	private Integer sendType;   //发送类型（0:立即 1:按时
	
	private Integer sendStatus; //推送状态:1:已推送，0:未推送
	
	private Date createTime;     //创建时间
	
	private String message;      //消息json

	private Integer dr;

	private Double version;

	public MsgMessage(){
		msgSort = Constant.MSG_SORT_NOT;
		msgType = Constant.MSG_TYPE_GENERALLY;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public Integer getDr() {
		return dr;
	}

	public void setDr(Integer dr) {
		this.dr = dr;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getSendBy() {
		return sendBy;
	}
	public void setSendBy(String sendBy) {
		this.sendBy = sendBy;
	}
	public String getRecBy() {
		return recBy;
	}
	public void setRecBy(String recBy) {
		this.recBy = recBy;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public Integer getSendType() {
		return sendType;
	}
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}
	public Integer getSendStatus() {
		return sendStatus;
	}
	public void setSendStatus(Integer sendStatus) {
		this.sendStatus = sendStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Transient
	private String sendTimeStr;

	public Integer getIsReaded() {
		return isReaded;
	}

	public void setIsReaded(Integer isReaded) {
		this.isReaded = isReaded;
	}

	@Transient
	private Integer isReaded;

	private String title ;

	private String content ;

	private Integer msgType;

	private Integer msgSort;

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getMsgSort() {
		return msgSort;
	}

	public void setMsgSort(Integer msgSort) {
		this.msgSort = msgSort;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendTimeStr() {
		return sendTimeStr;
	}

	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}
}
