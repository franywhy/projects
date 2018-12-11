package com.kuaiji.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-10-12 16:02:30
 */
public class LiveLogZegoDetailEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键ID
	private Long id;
	//用户id
	private String user_account;
	//用户昵称
	private String user_nickname;
	//⽤户⻆⾊: 1 anchor(⽼师), 2 audience（学⽣）
	private Integer user_role;
	//⽤户会话id; 新登录填"0", 断线重连登录填上次的session_id
	private String session_id;
	//房间ID
	private String room_id;
	//房间 唯⼀seq; ⽤来判断房间登录登出次序
	private String room_seq;
	//房间名
	private String room_name;
	//用户登录房间时间戳, 单位：毫秒
	private Long login_time;
	//用户退出房间时间戳, 单位：毫秒
	private Long logout_time;
	//服务器当前时间 Uinx时间戳
	private Long timestamp;
	//随机数
	private String nonce;
	//检验串
	private String sigaure;
	//进出标识 1:进入，2：退出
	private Integer intoOut;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	public String getUser_nickname() {
		return user_nickname;
	}
	public void setUser_nickname(String user_nickname) {
		this.user_nickname = user_nickname;
	}
	public Integer getUser_role() {
		return user_role;
	}
	public void setUser_role(Integer user_role) {
		this.user_role = user_role;
	}
	public String getSession_id() {
		return session_id;
	}
	public void setSession_id(String session_id) {
		this.session_id = session_id;
	}
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public String getRoom_seq() {
		return room_seq;
	}
	public void setRoom_seq(String room_seq) {
		this.room_seq = room_seq;
	}
	public String getRoom_name() {
		return room_name;
	}
	public void setRoom_name(String room_name) {
		this.room_name = room_name;
	}
	public Long getLogin_time() {
		return login_time;
	}
	public void setLogin_time(Long login_time) {
		this.login_time = login_time;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getNonce() {
		return nonce;
	}
	public void setNonce(String nonce) {
		this.nonce = nonce;
	}
	public String getSigaure() {
		return sigaure;
	}
	public void setSigaure(String sigaure) {
		this.sigaure = sigaure;
	}
	public Integer getIntoOut() {
		return intoOut;
	}
	public void setIntoOut(Integer intoOut) {
		this.intoOut = intoOut;
	}
	public Long getLogout_time() {
		return logout_time;
	}
	public void setLogout_time(Long logout_time) {
		this.logout_time = logout_time;
	}
	
	
}
