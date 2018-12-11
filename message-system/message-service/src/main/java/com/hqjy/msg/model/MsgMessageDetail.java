package com.hqjy.msg.model;

import java.util.Date;

import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 消息详情表
 * @author Administrator
 *
 */
@Table(name = "msg_message_detail")
public class MsgMessageDetail {

	private Integer id;
	
	private String code;        //消息唯一代码
	
	private String sendBy;      //发送方
	
	private String recBy;       //接收方
	
	private Date sendTime;      //发送时间
			
	private Date createTime;	//创建时间
	
	private String message;     //消息json
	
	private Integer isRead;     //是否已读（1：已读

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

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
	}

	private String rowId;

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Transient
	private String tableName;
	
}
