package com.izhubo.model;

import java.io.Serializable;
import java.util.List;

public class MessageBase implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String from_user;/* ������ */
	private List<String> to_user;;/* ������ */
	private Long create_time;/* ����ʱ�� */
	private String text;/* ��Ϣ���� */
	private String type;/* ��Ϣ����*/
	private String module;/* ģ��*/
	private String action;/* ��Ϣ��Ϊ*/
	private boolean is_confirm;/* �Ƿ���Ҫȷ�ϻ���*/
	private String msg_id;
	
	private static final String D_STRING = ".";
	
	public String getFrom_user() {
		return from_user;
	}
	public void setFrom_user(String from_user) {
		this.from_user = from_user;
	}
	
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<String> getTo_user() {
		return to_user;
	}
	public void setTo_user(List<String> to_user) {
		this.to_user = to_user;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public boolean isIs_confirm() {
		return is_confirm;
	}
	public void setIs_confirm(boolean is_confirm) {
		this.is_confirm = is_confirm;
	}
	public void initMA(String module , String action){
		this.module = module;
		this.action = action;
		this.type = module + D_STRING + action;
	}
}
