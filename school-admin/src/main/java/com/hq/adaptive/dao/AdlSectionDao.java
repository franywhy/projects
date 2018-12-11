package com.hq.adaptive.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

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
public interface AdlSectionDao {
	/**
	 * 根据ID查询查询节档案
	 * @param sectionId	ID
	 * @return	节档案
	 */
	public AdlSectionPOJO queryObject(@Param(value="sectionId")Long sectionId);
	public AdlSectionEntity queryObjectByName(@Param(value="courseId")Long courseId , @Param(value="sectionName")String sectionName);
	
	/**
	 * 查询节档案列表
	 * @param adlSectionQuery	查询条件
	 * @return	节档案列表
	 */
	public List<AdlSectionEntity> queryList(AdlSectionQuery adlSectionQuery);
	
	/**
	 * 查询节档案数量
	 * @return	节档案数量
	 */
	public int queryTotal(AdlSectionQuery adlSectionQuery);
//	public int queryTotal(@Param(value="courseId")Long courseId);

	/**
	 * 查询节书数量
	 * @param chapterId	章ID
	 * @return			节数量
	 */
	public int queryCountByParentId(Long chapterId);
}
