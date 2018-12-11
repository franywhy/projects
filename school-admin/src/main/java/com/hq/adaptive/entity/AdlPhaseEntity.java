package com.hq.adaptive.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 评测阶段表
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:37
 */
public class AdlPhaseEntity implements Serializable {
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
//	//阶段类型:0.预习阶段;1.正常
//	private Integer phaseType;
	//知识空间版本号;0:知识空间未生成
	private Long versionNo;
	//知识空间哈希版本号
	private String versionHash;
	//状态 0：禁用 1：启用
	private Integer status;
	//删除标记 0.正常; 1.删除
	private Integer dr;
	//更新时间戳
	private Date updateTime;
	//创建时间戳
	private Date createTime;

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
	/**
	 * 设置：删除标记 0.正常; 1.删除
	 */
	public void setDr(Integer dr) {
		this.dr = dr;
	}
	/**
	 * 获取：删除标记 0.正常; 1.删除
	 */
	public Integer getDr() {
		return dr;
	}
	/**
	 * 设置：更新时间戳
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	/**
	 * 获取：更新时间戳
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	/**
	 * 设置：创建时间戳
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	/**
	 * 获取：创建时间戳
	 */
	public Date getCreateTime() {
		return createTime;
	}
}
