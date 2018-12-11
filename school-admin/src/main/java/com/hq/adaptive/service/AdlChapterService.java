package com.hq.adaptive.service;

import java.util.List;

import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.pojo.query.AdlChapterQuery;

/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
public interface AdlChapterService {
	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	AdlChapterEntity queryObject(Long chapterId);
	
	/**
	 * 查询章列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	章列表
	 */
	List<AdlChapterEntity> queryList(AdlChapterQuery adlChapterQuery);
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	int queryTotal(Long courseId);
	
	/**
	 * 新增章
	 */
	int save(AdlChapterEntity adlChapter);
	
	/**
	 * 更新章
	 */
	int update(AdlChapterEntity adlChapter);
	
	/**
	 * 按照ID删除
	 * @param chapterId	自增id
	 */
	int delete(Long chapterId);
	
	/**
	 * 获取章节编码
	 * @param courseId	课程ID
	 * @return			章节编码
	 */
	String getChapterNo(Long courseId);
	/**
	 * 按照章名称查询,如果没有则保存,如果有则查询出对象
	 * @param adlChapter	章对象
	 * @return	adlChapter
	 */
	AdlChapterEntity saveOrQueryByChapterName(AdlChapterEntity adlChapter);
}
