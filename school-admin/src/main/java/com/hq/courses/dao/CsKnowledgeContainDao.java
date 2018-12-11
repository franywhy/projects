package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsKnowledgeContainEntity;
import com.hq.courses.pojo.CsKnowledgeContainPOJO;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface CsKnowledgeContainDao {
	
	/**
	 * 查询知识点包含知识点关系列表-包含知识点的详细信息
	 * @param selfId	知识点ID
	 */
	public List<CsKnowledgeContainPOJO> queryListBySelfId(@Param("selfId")Long selfId);
	/**
	 * 查询知识点包含关系-那些知识点包含该知识点
	 * @param childId	被包含知识点ID
	 * @return
	 */
	public List<CsKnowledgeContainPOJO> queryListByChildId(@Param("childId")Long childId);
	
	/**
	 * 保存
	 * @param csKnowledgeContainEntity
	 * @return
	 */
	public int save(CsKnowledgeContainEntity csKnowledgeContainEntity);
	
	/**
	 * 按照知识点ID删除知识点包含关系
	 * @param knowledgeId	知识点ID
	 * @return
	 */
	public int delete(@Param("knowledgeId")Long knowledgeId);
}
