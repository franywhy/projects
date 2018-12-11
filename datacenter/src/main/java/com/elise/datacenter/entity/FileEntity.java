package com.elise.datacenter.entity;

import java.util.Date;

/**
 * Created by Glenn on 2017/4/26 0026.
 */

public class FileEntity {
	private Long id;
	private String fileId;
	private String groupName;
	private String fileUri;
	private Integer fileSuffix;
	private Integer fileLength;
	private Integer isPermanent;
	private Long source;
	private Date createTime;
	private String fileUrl;

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getFileUri() {
		return fileUri;
	}

	public void setFileUri(String fileUri) {
		this.fileUri = fileUri;
	}

	public Integer getFileSuffix() {
		return fileSuffix;
	}

	public void setFileSuffix(Integer fileSuffix) {
		this.fileSuffix = fileSuffix;
	}

	public Integer getFileLength() {
		return fileLength;
	}

	public void setFileLength(Integer fileLength) {
		this.fileLength = fileLength;
	}

	public Integer getIsPermanent() {
		return isPermanent;
	}

	public void setIsPermanent(Integer isPermanent) {
		this.isPermanent = isPermanent;
	}

	public Long getSource() {
		return source;
	}

	public void setSource(Long source) {
		this.source = source;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id=").append(id);
		sb.append("\nfileId=").append(fileId);
		sb.append("\ngroupName=").append(groupName);
		sb.append("\nfileUri=").append(fileUri);
		sb.append("\nfileSuffix=").append(fileSuffix);
		sb.append("\nfileLength=").append(fileLength);
		sb.append("\nisPermanent=").append(isPermanent);
		sb.append("\nsource=").append(source);
		sb.append("\ncreateTime=").append(createTime);
		sb.append("\nfileUrl=").append(fileUrl);
		return sb.toString();
	}
}
