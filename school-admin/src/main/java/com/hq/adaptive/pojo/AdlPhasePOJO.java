package com.hq.adaptive.pojo;

import com.hq.adaptive.entity.AdlPhaseFilesEntity;

import java.io.Serializable;
import java.util.List;


/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhasePOJO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//主键
	private Long phaseId;
	//课程PK
	private Long courseId;
	//阶段名称
	private String phaseName;
	//阶段编号
	private String phaseNo;
	//考点:1.正常;2.考点
	private Integer keyPoint;
	private String keyPointName;
//	//阶段类型:0.预习阶段;1.正常
//	private Integer phaseType;
	//知识空间版本号;0:知识空间未生成
	private Long versionNo;
	//知识空间哈希版本号
	private String versionHash;
	//状态 0：禁用 1：启用
	private Integer status;
	//节数量
	private Integer sectionCount;
	//知识点数量
	private Integer knowledgeCount;

	private List<AdlPhaseSectionPOJO> sectionList;

	private AdlPhaseFilesEntity phaseBeforeFile;

	private AdlPhaseFilesEntity phaseAfterFile;

	private List<Long> knowledgeIdList;

	@Override
	public String toString() {
		return "AdlPhasePOJO{" +
				"phaseId=" + phaseId +
				", courseId=" + courseId +
				", phaseName='" + phaseName + '\'' +
				", phaseNo='" + phaseNo + '\'' +
				", keyPoint=" + keyPoint +
				", keyPointName='" + keyPointName + '\'' +
				", versionNo=" + versionNo +
				", versionHash='" + versionHash + '\'' +
				", status=" + status +
				", sectionCount=" + sectionCount +
				", knowledgeCount=" + knowledgeCount +
				", sectionList=" + sectionList +
				", phaseBeforeFile=" + phaseBeforeFile +
				", phaseAfterFile=" + phaseAfterFile +
				'}';
	}

	public List<AdlPhaseSectionPOJO> getSectionList() {
		return sectionList;
	}

	public void setSectionList(List<AdlPhaseSectionPOJO> sectionList) {
		this.sectionList = sectionList;
	}

	public Integer getSectionCount() {
		return sectionCount;
	}
	public void setSectionCount(Integer sectionCount) {
		this.sectionCount = sectionCount;
	}
	public Integer getKnowledgeCount() {
		return knowledgeCount;
	}
	public void setKnowledgeCount(Integer knowledgeCount) {
		this.knowledgeCount = knowledgeCount;
	}
	public String getKeyPointName() {
		return keyPointName;
	}
	public void setKeyPointName(String keyPointName) {
		this.keyPointName = keyPointName;
	}
	/**
	 * 设置：主键
	 */
	public void setPhaseId(Long phaseId) {
		this.phaseId = phaseId;
	}
	/**
	 * 获取：主键
	 */
	public Long getPhaseId() {
		return phaseId;
	}
	/**
	 * 设置：课程PK
	 */
	public void setCourseId(Long courseId) {
		this.courseId = courseId;
	}
	/**
	 * 获取：课程PK
	 */
	public Long getCourseId() {
		return courseId;
	}
	/**
	 * 设置：阶段名称
	 */
	public void setPhaseName(String phaseName) {
		this.phaseName = phaseName;
	}
	/**
	 * 获取：阶段名称
	 */
	public String getPhaseName() {
		return phaseName;
	}
	/**
	 * 设置：阶段编号
	 */
	public void setPhaseNo(String phaseNo) {
		this.phaseNo = phaseNo;
	}
	/**
	 * 获取：阶段编号
	 */
	public String getPhaseNo() {
		return phaseNo;
	}
	/**
	 * 设置：考点:1.正常;2.考点
	 */
	public void setKeyPoint(Integer keyPoint) {
		this.keyPoint = keyPoint;
	}
	/**
	 * 获取：考点:1.正常;2.考点
	 */
	public Integer getKeyPoint() {
		return keyPoint;
	}
//	/**
//	 * 设置：阶段类型:0.预习阶段;1.正常
//	 */
//	public void setPhaseType(Integer phaseType) {
//		this.phaseType = phaseType;
//	}
//	/**
//	 * 获取：阶段类型:0.预习阶段;1.正常
//	 */
//	public Integer getPhaseType() {
//		return phaseType;
//	}
	/**
	 * 设置：知识空间版本号;0:知识空间未生成
	 */
	public void setVersionNo(Long versionNo) {
		this.versionNo = versionNo;
	}
	/**
	 * 获取：知识空间版本号;0:知识空间未生成
	 */
	public Long getVersionNo() {
		return versionNo;
	}
	/**
	 * 设置：知识空间哈希版本号
	 */
	public void setVersionHash(String versionHash) {
		this.versionHash = versionHash;
	}
	/**
	 * 获取：知识空间哈希版本号
	 */
	public String getVersionHash() {
		return versionHash;
	}
	/**
	 * 设置：状态 0：禁用 1：启用
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}
	/**
	 * 获取：状态 0：禁用 1：启用
	 */
	public Integer getStatus() {
		return status;
	}

	public AdlPhaseFilesEntity getPhaseBeforeFile() {
		return phaseBeforeFile;
	}

	public void setPhaseBeforeFile(AdlPhaseFilesEntity phaseBeforeFile) {
		this.phaseBeforeFile = phaseBeforeFile;
	}

	public AdlPhaseFilesEntity getPhaseAfterFile() {
		return phaseAfterFile;
	}

	public void setPhaseAfterFile(AdlPhaseFilesEntity phaseAfterFile) {
		this.phaseAfterFile = phaseAfterFile;
	}

	public List<Long> getKnowledgeIdList() {
		return knowledgeIdList;
	}

	public void setKnowledgeIdList(List<Long> knowledgeIdList) {
		this.knowledgeIdList = knowledgeIdList;
	}
}
