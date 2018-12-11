package com.hqjy.msg.model;

import javax.persistence.Table;

/**
 * 群组成频道表
 * @author Administrator
 *
 */
@Table(name = "msg_channels")
public class MsgChannels {

	private Integer id;  //主键id
	
	private String groupChannel;//群组频道
	
	private String leaguerChannel;//成员频道
	
	private String remark;//描述
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	
	public String getGroupChannel() {
		return groupChannel;
	}
	public void setGroupChannel(String groupChannel) {
		this.groupChannel = groupChannel;
	}
	public String getLeaguerChannel() {
		return leaguerChannel;
	}
	public void setLeaguerChannel(String leaguerChannel) {
		this.leaguerChannel = leaguerChannel;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
