package com.hq.adaptive.dao;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlKnowledgeFileEntity;
import com.hq.adaptive.pojo.AdlKnowledgeFilePOJO;

/**
 * 知识点资料档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeFileDao {
	/**
	 * 根据ID查询查询知识点资料档案
	 * @param KnowledgeId	知识点ID
	 * @return	知识点资料档案
	 */
	public AdlKnowledgeFilePOJO queryObject(@Param(value="knowledgeId")Long knowledgeId);
	
	
	/**
	 * 新增知识点资料档案
	 */
	public int save(AdlKnowledgeFileEntity adlKnowledgeFileEntity);
	
	/**
	 * 更新知识点资料档案
	 */
	public int update(AdlKnowledgeFileEntity adlKnowledgeFileEntity);
	
	/**
	 * 按照ID删除
	 * @param KnowledgeId	知识点ID
	 */
	public int delete(@Param(value="knowledgeId")Long knowledgeId);
}
