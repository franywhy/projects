package com.hq.courses.service;

import java.util.List;

import com.hq.courses.pojo.CsKnowledgeQuestiontypePOJO;

/**
 * 知识点题型
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 14:46:48
 */
public interface CsKnowledgeQuestiontypeService {
	/**
	 * 查询知识点题型列表
	 * @param knowledgeId	知识点ID
	 */
	public List<CsKnowledgeQuestiontypePOJO> queryList(Long knowledgeId);
	
	/**
	 * 批量保存题型
	 * @param knowledgeId		知识点ID
	 * @param questiontypeList	题型
	 */
	public void saveList(Long knowledgeId, List<CsKnowledgeQuestiontypePOJO> questiontypeList) ;
}
