package com.hq.adaptive.dao;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlKnowledgeVideoEntity;
import com.hq.adaptive.pojo.AdlKnowledgeVideoPOJO;

/**
 * 知识点视频档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 16:42:46
 */
public interface AdlKnowledgeVideoDao {
	/**
	 * 根据ID查询查询知识点视频档案
	 * @param videoId	主键
	 * @return	知识点视频档案
	 */
	public AdlKnowledgeVideoPOJO queryObject(@Param(value="knowledgeId")Long knowledgeId);
	
	/**
	 * 根据ID查询知识点id
	 * @param knowledgeId	知识点id
	 * @return	知识点id
	 */
	public Long queryKnowledgeId(@Param(value="knowledgeId")Long knowledgeId);
	
	/**
	 * 新增知识点视频档案
	 */
	public int save(AdlKnowledgeVideoEntity adlKnowledgeVideoEntity);
	
	/**
	 * 更新知识点视频档案
	 */
	public int update(AdlKnowledgeVideoEntity adlKnowledgeVideoEntity);
	
	/**
	 * 按照ID删除
	 * @param videoId	主键
	 */
	public int delete(@Param(value="knowledgeId")Long knowledgeId);
}
