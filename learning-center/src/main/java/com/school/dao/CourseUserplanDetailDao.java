package com.school.dao;

import com.school.entity.CourseUserplanDetailEntity;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by Glenn on 2017/5/15 0015.
 */

public interface CourseUserplanDetailDao {
    List<CourseUserplanDetailEntity> queryList(Map<String,Object> map);
}
