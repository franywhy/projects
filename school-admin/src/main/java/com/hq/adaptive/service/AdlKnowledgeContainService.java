package com.hq.adaptive.service;

import java.util.List;

import com.hq.adaptive.pojo.AdlKnowledgeContainPOJO;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeContainService {
	/**
	 * 查询知识点包含知识点关系列表
	 */
	public List<com.hq.adaptive.pojo.AdlKnowledgeContainPOJO> queryListBySelfId(Long selfId);
	/**
	 * 查询知识点被那些知识点包含
	 */
	public List<com.hq.adaptive.pojo.AdlKnowledgeContainPOJO> queryListByChildId(Long childId);
	
	/**
	 * 批量保存知识点包含知识点信息
	 * @param knowledgeId	知识点ID
	 * @param childList		包含关系
	 * @return
	 */
	public void saveList(Long knowledgeId, List<AdlKnowledgeContainPOJO> childList);

	/**
	 * 本级知识点下包含子集知识点数量
	 * @param selfId	本级知识点PK
	 * @return
	 */
	public int queryTotalBySelfId(Long selfId);

	/**
	 *	知识点被包含的数量
	 * @param childId	知识点PK
	 * @return
	 */
	public int queryTotalByChildId(Long childId);
}
