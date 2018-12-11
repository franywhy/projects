package com.hq.adaptive.service;

import com.hq.adaptive.pojo.AdlKnowledgeFilePOJO;

/**
 * 知识点资料档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeFileService {
	/**
	 * 根据ID查询查询知识点资料档案
	 * @param KnowledgeId	知识点ID
	 * @return	知识点资料档案
	 */
	AdlKnowledgeFilePOJO queryObject(Long KnowledgeId);
//	
	/**
	 * 新增知识点资料档案
	 */
	int save(AdlKnowledgeFilePOJO adlKnowledgeFilePOJO);
//	
	/**
	 * 更新知识点资料档案
	 * @param KnowledgeId	知识点ID
	 */
	int saveOrUpdate(Long KnowledgeId ,AdlKnowledgeFilePOJO adlKnowledgeFilePOJO);

	int delete(Long KnowledgeId);
}
