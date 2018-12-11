package com.hq.learningapi.pojo;

import java.io.Serializable;


/**
 * app常量记录表
 * 
 * @author zhaownwei
 * @date 2018-02-26 14:58:44
 */
public class AppConfigPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long ckey;
	//链接
	private String cvalue;
	//名字
	private String name;
	
	
	public Long getCkey() {
		return ckey;
	}
	public void setCkey(Long ckey) {
		this.ckey = ckey;
	}
	public String getCvalue() {
		return cvalue;
	}
	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	
}
