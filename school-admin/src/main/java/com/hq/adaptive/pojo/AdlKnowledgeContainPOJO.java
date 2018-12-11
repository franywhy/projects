package com.hq.adaptive.pojo;

import java.io.Serializable;


/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public class AdlKnowledgeContainPOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//id
	private Long id;
	//本级知识点pk
	private Long selfId;
	private String selfName;
	private String selfNo;
	//后续知识点pk
	private Long childId;
	private String childName;
	private String childNo;
	
	@Override
	public String toString() {
		return "AdlKnowledgeContainPOJO [id=" + id + ", selfId=" + selfId + ", selfName=" + selfName + ", selfNo="
				+ selfNo + ", childId=" + childId + ", childName=" + childName + ", childNo=" + childNo + "]";
	}
	
	
	public String getSelfName() {
		return selfName;
	}


	public void setSelfName(String selfName) {
		this.selfName = selfName;
	}


	public String getSelfNo() {
		return selfNo;
	}


	public void setSelfNo(String selfNo) {
		this.selfNo = selfNo;
	}


	public String getChildNo() {
		return childNo;
	}


	public void setChildNo(String childNo) {
		this.childNo = childNo;
	}


	public String getChildName() {
		return childName;
	}

	public void setChildName(String childName) {
		this.childName = childName;
	}

	/**
	 * 设置：id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：本级知识点pk
	 */
	public void setSelfId(Long selfId) {
		this.selfId = selfId;
	}
	/**
	 * 获取：本级知识点pk
	 */
	public Long getSelfId() {
		return selfId;
	}
	/**
	 * 设置：后续知识点pk
	 */
	public void setChildId(Long childId) {
		this.childId = childId;
	}
	/**
	 * 获取：后续知识点pk
	 */
	public Long getChildId() {
		return childId;
	}
}
