package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsKnowledgeQuestiontypeEntity;
import com.hq.courses.pojo.CsKnowledgeQuestiontypePOJO;

/**
 * 知识点题型
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-12-01 14:46:48
 */
public interface CsKnowledgeQuestiontypeDao {
	/**
	 * 查询知识点题型列表
	 * @param knowledgeId	知识点ID
	 */
	public List<CsKnowledgeQuestiontypePOJO> queryList(@Param(value="knowledgeId")Long knowledgeId ,@Param(value="ckey")String ckey);
	
	/**
	 * 新增知识点题型
	 */
	public int save(CsKnowledgeQuestiontypeEntity csKnowledgeQuestiontypeEntity);
	
	
	/**
	 * 按照ID删除
	 * @param knowledgeId	知识点ID
	 */
	public int delete(@Param(value="knowledgeId")Long knowledgeId);
}
