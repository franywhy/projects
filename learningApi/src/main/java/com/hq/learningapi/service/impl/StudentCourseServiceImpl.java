package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.StudentCourseDao;
import com.hq.learningapi.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Administrator on 2018/1/9 0009.
 */
@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private StudentCourseDao studentCourseDao;

    @Override
    public Set<String> queryCourseNoList(Long userId, String businessId) {
        return studentCourseDao.queryCourseNoList(userId,businessId);
    }
}
