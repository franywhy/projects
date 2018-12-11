package com.hq.adaptive.service;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.pojo.AdlKnowledgeVideoPOJO;

/**
 * 知识点视频档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeVideoService {
	
	/**
	 * 根据ID查询查询知识点视频档案
	 * @param videoId	主键
	 * @return	知识点视频档案
	 */
	AdlKnowledgeVideoPOJO queryObject(Long KnowledgeId);
	
	/**
	 * 查询知识点视频表是否存在该记录
	 * @param knowledgeId	知识点id
	 * @return	true 不存在 false存在
	 */
	boolean queryKnowledgeIdIsNotExist(Long knowledgeId);
	
	/**
	 * 新增知识点视频档案
	 */
	int save(AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO);
	
	/**
	 * 更新知识点视频档案
	 */
	int saveOrUpdate(Long KnowledgeId , AdlKnowledgeVideoPOJO adlKnowledgeVideoPOJO);
	
}
