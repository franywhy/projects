package com.school.dao;

import com.school.pojo.CoursesPOJO;

/**
 * Created by DL on 2018/7/16.
 */
public interface CoursesDao {

    CoursesPOJO queryObject(Long courseId);
}
