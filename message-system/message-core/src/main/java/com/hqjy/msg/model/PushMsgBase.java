package com.hqjy.msg.model;

import java.util.HashMap;
import java.util.Map;

public class PushMsgBase {
 
	private String title;
	private String content;
	/*public String msg_url;
	public String push_action_name;
	public String define_info;*/
	private int msgType = 0;
	private int moduleType;
	//private int moduleType = 0;
	/*public String video_id;
	public String video_name;
	public String video_record_id;*/
	private String msgId;
	private Map data;
	private Map custom;

	public Map getCustom() {
		if(null!=data){
			data.put("msg_id",msgId);
		}
		if (null!=custom ) {
			custom.put("module_type",moduleType);
			custom.put("data",data);
		}
		return custom;
	}

	public void setCustom(Map custom) {
		this.custom = custom;
	}

	public PushMsgBase() {
		if(null==data){
			data = new HashMap();
		}
		if(null==custom){
			custom = new HashMap();
		}

	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
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

	public int getMsgType() {
		return msgType;
	}

	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}

	public int getModuleType() {
		return moduleType;
	}

	public void setModuleType(int moduleType) {
		this.moduleType = moduleType;
	}

	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("{");
		sb.append("\"title\":\"")
				.append(title).append('\"');
		sb.append(",\"body\":\"")
				.append(content).append('\"');
		sb.append('}');
		return sb.toString();
	}
}
