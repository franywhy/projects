package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlKnowledgeContainEntity;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeContainDao {
	
	/**
	 * 查询知识点包含知识点关系列表-包含知识点的详细信息
	 * @param selfId	知识点ID
	 */
	public List<com.hq.adaptive.pojo.AdlKnowledgeContainPOJO> queryListBySelfId(@Param("selfId")Long selfId);
	/**
	 * 查询知识点包含关系-那些知识点包含该知识点
	 * @param childId	被包含知识点ID
	 * @return
	 */
	public List<com.hq.adaptive.pojo.AdlKnowledgeContainPOJO> queryListByChildId(@Param("childId")Long childId);
	
	/**
	 * 保存
	 * @param adlKnowledgeContainEntity
	 * @return
	 */
	public int save(AdlKnowledgeContainEntity adlKnowledgeContainEntity);
	
	/**
	 * 按照知识点ID删除知识点包含关系
	 * @param knowledgeId	知识点ID
	 * @return
	 */
	public int delete(@Param("knowledgeId")Long knowledgeId);

	/**
	 * 本级知识点下包含子集知识点数量
	 * @param selfId	本级知识点PK
	 * @return
	 */
	public int queryTotalBySelfId(@Param("selfId")Long selfId);

	/**
	 *	知识点被包含的数量
	 * @param childId	知识点PK
	 * @return
	 */
	public int queryTotalByChildId(@Param("childId")Long childId);
}
