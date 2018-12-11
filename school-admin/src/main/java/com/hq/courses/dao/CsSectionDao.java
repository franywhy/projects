package com.hq.courses.dao;

import java.util.List;

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
public interface CsSectionDao {
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	public CsSectionPOJO queryObject(@Param(value="sectionId")Long sectionId);
	public CsSectionEntity queryObjectByName(@Param(value="courseId")Long courseId , @Param(value="sectionName")String sectionName);
	
	/**
	 * 查询节档案列表
	 * @param offset	返回记录行的偏移量
	 * @param limit		返回记录行的最大数目
	 * @return	节档案列表
	 */
	public List<CsSectionEntity> queryList(CsSectionQuery csSectionQuery);
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	public int queryTotal(CsSectionQuery csSectionQuery);
	
	/**
	 * 新增节档案
	 */
	public int save(CsSectionEntity csSectionEntity);
	
	/**
	 * 更新节档案
	 */
	public int update(CsSectionEntity csSectionEntity);
	
	/**
	 * 按照ID删除
	 * @param sectionId	ID
	 */
	public int delete(@Param(value="sectionId")Long sectionId);
	
	/**
	 * 查询节书数量
	 * @param chapterId	章ID
	 * @return			节数量
	 */
	public int queryCountByParentId(Long chapterId);
	
	/**
	 * 按照节编码查询 
	 * @param sectionId 节ID,不为空时查询不等该节信息
	 * @param sectionNo
	 * @return
	 */
	public CsSectionEntity queryObjectBySectionNo(@Param(value="sectionId")Long sectionId,@Param(value="sectionNo")String sectionNo);
}
