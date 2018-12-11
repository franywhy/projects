package io.renren.pojo.log;

import java.io.Serializable;
import java.util.Date;

public class LiveCallbackPOJO implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String liveId;
	
	private Long userId;
	
	private Long createTime;
	
	private int type;

	public String getLiveId() {
		return liveId;
	}

	public void setLiveId(String liveId) {
		this.liveId = liveId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
