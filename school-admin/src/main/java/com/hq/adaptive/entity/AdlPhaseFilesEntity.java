package com.hq.adaptive.entity;

import java.io.Serializable;

/**
 * 评测阶段对应资料
 */
public class AdlPhaseFilesEntity implements Serializable {
    /** 资料pk*/
    private Long id;
    /** 阶段PK */
    private Long phaseId;
    /** 文件名称 */
    private String fileName;
    /** 文件路径 */
    private String fileUrl;
    /** 阶段类型 1.课前;2.课后 */
    private Long phaseType;

	public AdlPhaseFilesEntity(Long id, Long phaseId, String fileName, String fileUrl, Long phaseType) {
		this.id = id;
		this.phaseId = phaseId;
		this.fileName = fileName;
		this.fileUrl = fileUrl;
		this.phaseType = phaseType;
	}

	public AdlPhaseFilesEntity() {
	}

	@Override
	public String toString() {
		return "AdlPhaseFilesEntity{" +
				"id=" + id +
				", phaseId=" + phaseId +
				", fileName='" + fileName + '\'' +
				", fileUrl='" + fileUrl + '\'' +
				", phaseType=" + phaseType +
				'}';
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPhaseId() {
		return phaseId;
	}

	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Long getPhaseType() {
		return phaseType;
	}

	public void setPhaseType(Long phaseType) {
		this.phaseType = phaseType;
	}
}
