package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.ClassToTkLogDao;
import com.hq.learningapi.entity.ClassToTkLogEntity;
import com.hq.learningapi.service.ClassToTkLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by DL on 2018/9/12.
 */
@Service("classToTkLogService")
public class ClassToTkLogServiceImpl implements ClassToTkLogService {

    @Autowired
    private ClassToTkLogDao classToTkLogDao;

    @Override
    public void save(ClassToTkLogEntity classToTkLog) {
        classToTkLogDao.save(classToTkLog);
    }
}
