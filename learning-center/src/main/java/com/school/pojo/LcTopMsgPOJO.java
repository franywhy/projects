package com.school.pojo;

import java.io.Serializable;
import java.util.List;

public class LcTopMsgPOJO implements Serializable {

	private static final long serialVersionUID = -3854358581942735776L;
	
	private Integer status;
	
	private String msgContent;
	
	private String url;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMsgContent() {
		return msgContent;
	}

	public void setMsgContent(String msgContent) {
		this.msgContent = msgContent;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
