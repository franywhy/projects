package com.hqjy.msg.model;

import javax.persistence.Table;

/**
 * 群组表
 * @author Administrator
 *
 */
@Table(name = "msg_group")
public class MsgGroup {

	private Integer id;
	
	private String name;//群组名称
	
	private String channel;//群组频道
	
	private String remark;//群组备注
	
	private Integer type;//类型(0:系统 1:其他
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
	
}
