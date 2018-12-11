package com.hq.courses.service;

import java.util.List;

import com.hq.courses.entity.CsSectionEntity;
import io.renren.utils.R;
import org.springframework.web.multipart.MultipartFile;

import com.hq.courses.pojo.CsKnowledgePOJO;
import com.hq.courses.pojo.query.CsKnowledgeQuery;

/**
 * 知识点档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-29 15:30:36
 */
public interface CsKnowledgeService {
	
	/**
	 * 根据ID查询查询知识点档案
	 * @param knowledgeId	主键
	 * @return	知识点档案
	 */
	CsKnowledgePOJO queryObject(Long knowledgeId);
	
	/**
	 * 查询知识点档案列表
	 * @param csKnowledgeQuery
	 * @return	知识点档案列表
	 */
	List<CsKnowledgePOJO> queryList(CsKnowledgeQuery csKnowledgeQuery);
	
	/**
	 * 查询知识点档案数量
	 * @return	知识点档案数量
	 */
	int queryTotal(CsKnowledgeQuery csKnowledgeQuery);
	
	/**
	 * 新增知识点档案
	 */
	int save(CsKnowledgePOJO csChapterPOJO);
	
	/**
	 * 更新知识点档案
	 */
	int update(CsKnowledgePOJO csChapterPOJO);
	
	/**
	 * 按照ID删除
	 * @param knowledgeId	主键
	 */
	int updateDr(Long knowledgeId);
	
	/**
	 * 知识点是否被引用
	 * @param knowledgeId	知识点ID
	 * @return				被引用说明从;未被引用返回NULL
	 */
	String checkQuote(Long knowledgeId);
	
	/**
	 * 获取编码
	 * @param sectionId	节ID
	 * @return			知识点编号
	 */
	String getKnowledgeNo(Long sectionId);
	/**
	 * 启用知识点
	 * @param knowledgeId	知识点ID
	 * @return
	 */
	int enable(Long knowledgeId);
	/**
	 * 禁用知识点
	 * @param knowledgeId	知识点ID
	 * @return
	 */
	int unenable(Long knowledgeId);
	
	/**
	 * excel模板
	 * @return
	 */
	String downExcel();
	/**
	 * 批量导入
	 * @param file
	 * @return
	 */
	String importExcel(Long courseId , MultipartFile file);
	/**
	 * 查询知识点编号是否合法
	 * @param courseId
	 * @param knowledgeId
	 * @param knowledgeNo
	 */
	void checkKnowledgeNo(Long courseId , Long knowledgeId,String knowledgeNo);

	/**
	 * 校验知识点点空间是否出现闭环
	 *
	 * @param courseId
	 * @return
	 */
	public R checkKnowledge(Long courseId);
	

	/**
	 * 初始化批量导入
	 * @param file
	 * @return
	 */
	String importExcelInitialization( MultipartFile file);

	/**
	 * 更新code，根据上一级code编码
	 * @param csSectionEntity
	 */
	void updateCodeByParentId(CsSectionEntity csSectionEntity);

}
