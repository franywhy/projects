package com.school.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.school.entity.CourseMaterialEntity;

/**
 * 排课计划表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-27 09:58:34
 */
@Repository
public interface CourseMaterialDao{
	
	CourseMaterialEntity queryByMaterialId(@Param("materialId") Long materialId,
										   @Param("businessId") String productId);
	
}
