package com.school.service.impl;

import com.school.dao.TeachEvaluateDao;
import com.school.entity.TeachEvaluate;
import com.school.service.TeachEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * Created by longduyuan on 2018/12/10 0010.
 */
@Service
public class TeachEvaluateServiceImpl implements TeachEvaluateService {

    @Autowired
    private TeachEvaluateDao teachEvaluateDao;

    @Override
    public boolean check(Map<String, Object> map) {
        Boolean has = teachEvaluateDao.check(map);
        return has == null ? false : has;
    }

    @Override
    public void save(TeachEvaluate teachEvaluate) {
        teachEvaluate.setCreateTime(new Date());
        teachEvaluate.setStatus(1);
        teachEvaluateDao.insert(teachEvaluate);
    }
}
