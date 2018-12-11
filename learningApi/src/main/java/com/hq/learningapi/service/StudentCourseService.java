package com.hq.learningapi.service;

import java.util.Set;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
public interface StudentCourseService {

    Set<String> queryCourseNoList(Long userId, String businessId);

}
