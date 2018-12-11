package com.school.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.school.entity.CourseUserplanEntity;


public interface CourseUserplanDao {

    List<Long> queryList(Map<String,Object> map);
    
    CourseUserplanEntity queryUserPlanEntity(Map<String,Object> map);
    
    List<CourseUserplanEntity> queryUserPlan4Push(Map<String,Object> map);
    
    String queryCourseName(Map<String,Object> map);
    
    CourseUserplanEntity query(@Param("userplanId")Long userplanId, @Param("businessId")String businessId);
}
