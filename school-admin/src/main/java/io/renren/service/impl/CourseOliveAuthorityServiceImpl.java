package io.renren.service.impl;

import io.renren.dao.AppCityDao;
import io.renren.dao.CourseOliveAuthorityDao;
import io.renren.entity.AppCity;
import io.renren.entity.CourseOliveAuthority;
import io.renren.service.AppCityService;
import io.renren.service.CourseOliveAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/1/6 0006.
 */
@Service
public class CourseOliveAuthorityServiceImpl implements CourseOliveAuthorityService {

    @Autowired
    private CourseOliveAuthorityDao courseOliveAuthorityDao;

    @Override
    public List<CourseOliveAuthority> queryList(Map<String, Object> map) {
        return courseOliveAuthorityDao.queryList(map);
    }

    @Override
    public int queryTotal(Map<String, Object> map) {
        return courseOliveAuthorityDao.queryTotal(map);
    }
}
