package com.school.dao;


import com.school.entity.CourseClassplanGenseeEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 排课计划和展示互动站点关联表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-11-15 17:12:30
 */
@Repository
public interface CourseClassplanGenseeDao  {

    CourseClassplanGenseeEntity getClassplanGenseeByClassplanId(String classplanId);
}
