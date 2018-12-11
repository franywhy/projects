package com.hq.courses.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.pojo.query.CsChapterQuery;

/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
public interface CsChapterDao {
	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	public CsChapterEntity queryObject(@Param(value="chapterId")Long chapterId);
	public CsChapterEntity queryObjectByName(@Param(value="chapterName")String chapterName ,@Param(value="courseId")Long courseId);
	
	/**
	 * 查询章列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	章列表
	 */
	public List<CsChapterEntity> queryList(CsChapterQuery csChapterQuery);
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	public int queryTotal(@Param(value="courseId")Long courseId);
	
	/**
	 * 新增章
	 */
	public int save(CsChapterEntity csChapterQuery);
	
	/**
	 * 更新章
	 */
	public int update(CsChapterEntity csChapterQuery);
	
	/**
	 * 按照ID删除
	 * @param chapterId	自增id
	 */
	public int delete(@Param(value="chapterId")Long chapterId);
	
	/**
	 * 按照章编码查询 
	 * @param chapterId 章ID,不为空时查询不等该课程ID的章信息
	 * @param chapterNo 章编码
	 * @return
	 */
	public CsChapterEntity queryObjectByChapterNo(@Param(value="chapterId")Long chapterId,@Param(value="chapterNo")String chapterNo);
}
