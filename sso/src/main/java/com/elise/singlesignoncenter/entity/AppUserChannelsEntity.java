package com.elise.singlesignoncenter.entity;

import java.io.Serializable;

/**
 * 
 * @author Created by LiuHai 2018/02/05
 *
 */
public class AppUserChannelsEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long userId;
	
	private String channelId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	@Override
	public String toString() {
		return "AppUserChannelsEntity [id=" + id + ", userId=" + userId + ", channelId=" + channelId + "]";
	}
	
	
}
