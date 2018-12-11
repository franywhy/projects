package com.hq.courses.service;

import java.util.List;

import com.hq.courses.entity.CsChapterEntity;
import com.hq.courses.entity.CsCourseEntity;
import com.hq.courses.pojo.query.CsChapterQuery;

/**
 * 章
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 14:47:57
 */
public interface CsChapterService {
	/**
	 * 根据ID查询查询章
	 * @param chapterId	自增id
	 * @return	章
	 */
	CsChapterEntity queryObject(Long chapterId);
	
	/**
	 * 查询章列表
	 * @param csChapterQuery
	 * @return	章列表
	 */
	List<CsChapterEntity> queryList(CsChapterQuery csChapterQuery);
	
	/**
	 * 查询章数量
	 * @return	章数量
	 */
	int queryTotal(Long courseId);
	/**
	 * 检查章编码是否违法
	 * @param chapterId 自增id 
	 * @param chapterNo 章编码
	 */
	void checkChapterNo( Long chapterId,String chapterNo);
	
	/**
	 * 新增章
	 */
	int save(CsChapterEntity csChapter);
	
	/**
	 * 更新章
	 */
	int update(CsChapterEntity csChapter);
	
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
	 * @param csChapter	章对象
	 * @return	csChapter
	 */
	CsChapterEntity saveOrQueryByChapterName(CsChapterEntity csChapter);
	/**
	 * 根据章编码获取对象
	 * @param chapterNo
	 * @return
	 */
	CsChapterEntity queryObjectByChapterNo(String chapterNo);

	/**
	 * 更新章、节、知识点code，根据上一级code编码
	 * @param csCourse	课程
	 */
	void updateCodeByParentId(CsCourseEntity csCourse);
}
