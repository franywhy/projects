package com.hq.courses.service;

import java.util.List;

import com.hq.courses.pojo.CsKnowledgeContainPOJO;

/**
 * 知识点包含知识点关系
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface CsKnowledgeContainService {
	/**
	 * 查询知识点包含知识点关系列表
	 */
	public List<CsKnowledgeContainPOJO> queryListBySelfId(Long selfId);
	/**
	 * 查询知识点被那些知识点包含
	 */
	public List<CsKnowledgeContainPOJO> queryListByChildId(Long childId);
	
	/**
	 * 批量保存知识点包含知识点信息
	 * @param knowledgeId	知识点ID
	 * @param childList		包含关系
	 * @return
	 */
	public void saveList(Long knowledgeId, List<CsKnowledgeContainPOJO> childList);
}
