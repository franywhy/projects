package com.hq.learningcenter.school.dao;

import java.util.List;

import com.hq.learningcenter.school.entity.CourseMaterialDetailEntity;
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
public interface CourseMaterialDetailDao{
	
	List<CourseMaterialDetailEntity> queryByMaterialTypeId(@Param("materialTypeId") Long materialTypeId,
                                                           @Param("businessId") String productId);
	
	String queryMaterialContent(@Param("materialDetailId") Long materialDetailId);
	
}
