package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsKnowledgeEntity;
import com.hq.courses.pojo.query.CsKnowledgeQuery;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface CsKnowledgeDao {
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	public CsKnowledgeEntity queryObject(@Param(value="knowledgeId")Long knowledgeId);
	public CsKnowledgeEntity queryObjectByName(@Param(value="courseId")Long courseId , @Param(value="knowledgeName")String knowledgeName);
	
	/**
	 * 查询知识点档案列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	知识点档案列表
	 */
	public List<CsKnowledgeEntity> queryList(CsKnowledgeQuery csKnowledgeQuery);
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	public int queryTotal(CsKnowledgeQuery csKnowledgeQuery);
	
	/**
	 * 新增知识点档案
	 */
	public int save(CsKnowledgeEntity csKnowledgeEntity);
	
	/**
	 * 更新知识点档案
	 */
	public int update(CsKnowledgeEntity csKnowledgeEntity);
	
	/**
	 * 按照ID删除
	 * @param knowledgeId	主键
	 */
	public int updateDr(@Param(value="knowledgeId")Long knowledgeId);
	
	/**
	 * 修改状态
	 * @param knowledgeId
	 * @param status
	 * @return
	 */
	public int updateStatus(@Param(value="knowledgeId")Long knowledgeId ,@Param(value="status")Integer status);
	
	/**
	 * 查询同一课程下知识点名称相同数量
	 * @param courseId		课程ID
	 * @param knowledgeName	知识点名称
	 * @param knowledgeId	不需要校验的知识点ID
	 * @return
	 */
	public int queryCountByName(@Param(value="courseId")Long courseId , @Param(value="knowledgeName")String knowledgeName , @Param(value="knowledgeId")Long knowledgeId);

	/**
	 * 根据知识点编号查询
	 * @param courseId 课程ID
	 * @param knowledgeId 当knowledgeId不为空时，查询不等于该knowledgeId的数据
	 * @param knowledgeNo 知识点名称
	 * @return
	 */
	public CsKnowledgeEntity queryObjectKnowledgeNo(@Param(value="courseId")Long courseId ,@Param(value="knowledgeId")Long knowledgeId ,@Param(value="knowledgeNo")String knowledgeNo);
}
