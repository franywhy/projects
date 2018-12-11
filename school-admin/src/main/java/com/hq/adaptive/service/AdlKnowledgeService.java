package com.hq.adaptive.service;

import java.util.List;

import com.hq.adaptive.entity.AdlKnowledgeEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import com.hq.adaptive.pojo.AdlKnowledgePOJO;
import com.hq.adaptive.pojo.query.AdlKnowledgeQuery;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface AdlKnowledgeService {
	
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	AdlKnowledgePOJO queryObject(Long knowledgeId);

    /**
     * 根据ID查询查询知识点档案
     * @param adlKnowledgePOJO	主键
     * @return	知识点档案
     */
    AdlKnowledgePOJO queryObject(AdlKnowledgePOJO adlKnowledgePOJO);

	/**
	 * 查询知识点档案列表
	 * @param adlKnowledgeQuery	查询条件
	 * @return	知识点档案列表
	 */
	List<AdlKnowledgePOJO> queryList(AdlKnowledgeQuery adlKnowledgeQuery);

	List<AdlKnowledgeEntity>queryEntityList(AdlKnowledgeQuery adlKnowledgeQuery);
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	int queryTotal(AdlKnowledgeQuery adlKnowledgeQuery);

	/**
	 * 查询数量-通过章ID
	 * @param chapterId	章ID
	 * @return	知识点数量
	 */
	int queryTotalByChapterId(Long chapterId);
	/**
	 * 更新知识点档案
	 */
	int updateZL(AdlKnowledgePOJO adlChapterPOJO);
	
	
//	/**
//	 * 知识点是否被引用
//	 * @param knowledgeId	知识点ID
//	 * @return				被引用说明从;未被引用返回NULL
//	 */
//	String checkQuote(Long knowledgeId);
	

	/**
	 * 批量导入
	 * @param courseId 课程id
	 * @param productId 产品线pk
	 * @param file
	 * @return
	 */
	String importExcel(Long courseId ,Long productId, MultipartFile file);
	
	/**
	 * 查询知识点id
	 * @param courseId	课程id
	 * @param knowledgeName	知识点名称
	 * @return
	 */
	Long queryKnowledgeIdByNo(Long courseId , String knowledgeName);
	
}
