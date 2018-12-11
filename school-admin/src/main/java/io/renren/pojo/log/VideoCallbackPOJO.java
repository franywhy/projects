package io.renren.pojo.log;

import java.io.Serializable;
import java.util.Date;

public class VideoCallbackPOJO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String videoId;
	
	private Long userId;
	
	private Date createTime;
	
	private int type;

	public String getVideoId() {
		return videoId;
	}

	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
