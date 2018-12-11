package com.hq.adaptive.dao;

import java.util.List;

import com.hq.adaptive.pojo.AdlKnowledgePOJO;
import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlKnowledgeEntity;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface AdlKnowledgeDao {
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	public AdlKnowledgeEntity queryObject(@Param(value="knowledgeId")Long knowledgeId);
	public AdlKnowledgeEntity queryObjectByName(@Param(value="courseId")Long courseId , @Param(value="knowledgeName")String knowledgeName);
	
	/**
	 * 查询知识点档案列表
	 * @param adlKnowledgeQuery	查询条件
	 * @return	知识点档案列表
	 */
	public List<AdlKnowledgeEntity> queryList(AdlKnowledgeQuery adlKnowledgeQuery);
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	public int queryTotal(AdlKnowledgeQuery adlKnowledgeQuery);
	
	/**
	 * 查询同一课程下知识点名称相同数量
	 * @param courseId		课程ID
	 * @param knowledgeName	知识点名称
	 * @param knowledgeId	不需要校验的知识点ID
	 * @return
	 */
	public int queryCountByName(@Param(value="courseId")Long courseId , @Param(value="knowledgeName")String knowledgeName , @Param(value="knowledgeId")Long knowledgeId);
	
//	/**
//	 * 查询知识点id
//	 * @param courseId	课程id
//	 * @param knowledgeName	知识点名称
//	 * @return
//	 */
//	public Long queryKnowledgeIdByName(@Param(value="courseId")Long courseId , @Param(value="knowledgeName")String knowledgeName);

	/**
	 * 查询知识点id
	 * @param courseId	课程id
	 * @param knowledgeNo	知识点编号
	 * @return
	 */
	public Long queryKnowledgeIdByNo(@Param(value="courseId")Long courseId , @Param(value="knowledgeNo")String knowledgeNo);

	public AdlKnowledgePOJO queryObjectbyPoJO(AdlKnowledgePOJO adlKnowledgePOJO);
}
