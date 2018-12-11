package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.adaptive.entity.AdlChapterEntity;
import com.hq.adaptive.pojo.query.AdlChapterQuery;

/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
public interface AdlChapterDao {
	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	public AdlChapterEntity queryObject(@Param(value="chapterId")Long chapterId);
	public AdlChapterEntity queryObjectByName(@Param(value="chapterName")String chapterName ,@Param(value="courseId")Long courseId);
	
	/**
	 * 查询章列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	章列表
	 */
	public List<AdlChapterEntity> queryList(AdlChapterQuery adlChapterQuery);
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	public int queryTotal(@Param(value="courseId")Long courseId);
	
	/**
	 * 新增章
	 */
	public int save(AdlChapterEntity adlChapterEntity);
	
	/**
	 * 更新章
	 */
	public int update(AdlChapterEntity adlChapterEntity);
	
	/**
	 * 按照ID删除
	 * @param chapterId	自增id
	 */
	public int delete(@Param(value="chapterId")Long chapterId);
}
