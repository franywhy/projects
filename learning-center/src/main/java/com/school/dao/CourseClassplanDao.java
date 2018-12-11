package com.school.dao;

import com.school.entity.CourseClassplanEntity;

import java.util.List;
import java.util.Map;

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
public interface CourseClassplanDao{
	
	List<CourseClassplanEntity> queryClassplanByUserplanId(@Param("userplanId") Long userplanId,
														@Param("dr") Integer dr,
														@Param("status") Integer status);
	
	CourseClassplanEntity queryClassplanByClassplanId(@Param("classplanId") String userplanId,
															@Param("isAudited") Integer isAudited,
															@Param("isOpen") Integer isOpen,
															@Param("dr") Integer dr,
															@Param("status") Integer status);
}
