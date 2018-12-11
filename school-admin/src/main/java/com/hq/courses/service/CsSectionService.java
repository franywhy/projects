package com.hq.courses.service;

import java.util.List;

import com.hq.courses.entity.CsChapterEntity;
import org.apache.ibatis.annotations.Param;

import com.hq.courses.entity.CsSectionEntity;
import com.hq.courses.pojo.CsSectionPOJO;
import com.hq.courses.pojo.query.CsSectionQuery;

/**
 * 节档案
 * 
 * @author shihongjie
 * @email shihongjie@hengqijy.com
 * @date 2017-11-22 16:45:53
 */
public interface CsSectionService {
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	CsSectionPOJO queryObject(Long sectionId);
	
	/**
	 * 查询节档案列表
	 * @return	节档案列表
	 */
	List<CsSectionEntity> queryList(CsSectionQuery csSectionQuery);
	/**
	 * 根据节编码获取节对象
	 * @param sectionNo
	 * @return
	 */
	CsSectionEntity queryObjectBySectionNo(String sectionNo);
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	int queryTotal(CsSectionQuery csSectionQuery);
	
	/**
	 * 新增节档案
	 */
	int save(CsSectionEntity csSection);
	
	/**
	 * 更新节档案
	 */
	int update(CsSectionEntity csSection);
	
	/**
	 * 按照ID删除
	 * @param sectionId	ID
	 */
	int delete(Long sectionId);

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
	 * 修改节父节点
	 * @param csSection	节信息
	 * @return				成功标志
	 */
	int updateSectionParent(CsSectionEntity csSection);
	
	/**
	 * 按照名称查询,如果没有则保存,如果有则查询出对象
	 * @param csSection	对象
	 * @return	csSection
	 */
	CsSectionEntity saveOrQueryBySectionName(CsSectionEntity csSection);
	
	/**
	 * 查询节编号是否合法
	 * @param sectionId 当sectionId不为空时，查询不等于该sectionId的数据
	 * @param sectionNo
	 * @return
	 */
	void checkSectionNo( Long sectionId,String sectionNo);

	/**
	 * 更新code，根据上一级code编码
	 * @param csChapterEntity
	 */
	void updateCodeByParentId(CsChapterEntity csChapterEntity);
}
