package com.hq.adaptive.pojo;

import java.io.Serializable;


/**
 * 知识点资料档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public class AdlKnowledgeFilePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//自增id
	private Long fileId;
	//知识点PK
	private Long knowledgeId;
	//名称
	private String fileName;
	//资料文件下载地址
	private String fileUrl;

	/**
	 * 设置：自增id
	 */
	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取：自增id
	 */
	public Long getFileId() {
		return fileId;
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
	 * 设置：名称
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * 获取：名称
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * 设置：资料文件下载地址
	 */
	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}
	/**
	 * 获取：资料文件下载地址
	 */
	public String getFileUrl() {
		return fileUrl;
	}

	@Override
	public String toString() {
		return "AdlKnowledgeFilePOJO{" +
				"fileId=" + fileId +
				", knowledgeId=" + knowledgeId +
				", fileName='" + fileName + '\'' +
				", fileUrl='" + fileUrl + '\'' +
				'}';
	}
}
