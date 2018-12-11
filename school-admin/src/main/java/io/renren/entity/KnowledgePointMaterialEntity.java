package io.renren.entity;

import java.io.Serializable;
import java.util.Date;



/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 14:06:17
 */
public class KnowledgePointMaterialEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//
	private Integer id;
	//知识点ID
	private Integer knowledgePointId;
	//资料ID
	private Integer materialId;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 设置：知识点ID
	 */
	public void setKnowledgePointId(Integer knowledgePointId) {
		this.knowledgePointId = knowledgePointId;
	}
	/**
	 * 获取：知识点ID
	 */
	public Integer getKnowledgePointId() {
		return knowledgePointId;
	}
	/**
	 * 设置：资料ID
	 */
	public void setMaterialId(Integer materialId) {
		this.materialId = materialId;
	}
	/**
	 * 获取：资料ID
	 */
	public Integer getMaterialId() {
		return materialId;
	}
}
