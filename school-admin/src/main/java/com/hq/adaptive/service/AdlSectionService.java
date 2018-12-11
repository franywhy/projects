package com.hq.adaptive.service;

import java.util.List;

import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.entity.AdlSectionEntity;
import com.hq.adaptive.pojo.AdlSectionPOJO;
import com.hq.adaptive.pojo.query.AdlSectionQuery;

/**
 * 节档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 16:45:53
 */
public interface AdlSectionService {
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	AdlSectionPOJO queryObject(Long sectionId);
	
	/**
	 * 查询节档案列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	节档案列表
	 */
	List<AdlSectionEntity> queryList(AdlSectionQuery adlSectionQuery);
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	int queryTotal(AdlSectionQuery adlSectionQuery);
	

	/**
	 * 查询节书数量
	 * @param chapterId	章ID
	 * @return			节数量
	 */
	int queryCountByParentId(Long chapterId);
	
	/**
	 * 获取节编码
	 * @param chapterId	章ID
	 * @return			节编码
	 */
	String getSectionCode(Long chapterId);
	

	/**
	 * 按照名称查询,如果没有则保存,如果有则查询出对象
	 * @param adlSection	对象
	 * @return	adlSection
	 */
	AdlSectionEntity saveOrQueryBySectionName(AdlSectionEntity adlSection);
}
