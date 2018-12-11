package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.CourseMaterialEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
