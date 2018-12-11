package com.hq.adaptive.pojo;

import java.io.Serializable;


/**
 * 知识点题型
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 14:46:48
 */
public class AdlKnowledgeQuestiontypePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long id;
	//知识点PK
	private Long knowledgeId;
	//题型PK
	private Long questiontypeId;

	private String cname;
	
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	/**
	 * 设置：主键
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * 获取：主键
	 */
	public Long getId() {
		return id;
	}
	/**
	 * 设置：知识点PK
	 */
	public void setKnowledgeId(Long knowledgeId) {
		this.knowledgeId = knowledgeId;
	}
	/**
	 * 获取：知识点PK
	 */
	public Long getKnowledgeId() {
		return knowledgeId;
	}
	/**
	 * 设置：题型PK
	 */
	public void setQuestiontypeId(Long questiontypeId) {
		this.questiontypeId = questiontypeId;
	}
	/**
	 * 获取：题型PK
	 */
	public Long getQuestiontypeId() {
		return questiontypeId;
	}
	@Override
	public String toString() {
		return "AdlKnowledgeQuestiontypePOJO [id=" + id + ", knowledgeId=" + knowledgeId + ", questiontypeId="
				+ questiontypeId + ", cname=" + cname + "]";
	}
}
