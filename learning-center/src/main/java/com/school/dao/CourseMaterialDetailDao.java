package com.school.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.school.entity.CourseMaterialDetailEntity;
import com.school.entity.CourseMaterialEntity;
import com.school.entity.CourseMaterialTypeEntity;

/**
 * 排课计划表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
@Repository
public interface CourseMaterialDetailDao{
	
	List<CourseMaterialDetailEntity> queryByMaterialTypeId(@Param("materialTypeId") Long materialTypeId,
														   @Param("businessId") String productId);
	
	String queryMaterialContent(@Param("materialDetailId") Long materialDetailId);
	
}
