package com.hq.learningcenter.school.dao;

import com.hq.learningcenter.school.entity.CourseUserplanDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/15 0015.
 */

public interface CourseUserplanDetailDao {
    List<CourseUserplanDetailEntity> queryList(Map<String,Object> map);
}
