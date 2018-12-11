package com.hq.learningcenter.school.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/8 0008.
 */

public interface CourseUserplanClassDetailDao {

    List<String> queryList(Map<String,Object> map);

    String queryClassPlanId(@Param("userPlanDetailId") Long userPlanDetailId,
                            @Param("status") Integer status,
                            @Param("dr")Integer dr);

    String queryClassPlanId4Push(Map<String,Object> map);
}
