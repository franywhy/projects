package com.hq.learningapi.dao;

import com.hq.learningapi.entity.ClassToTkLogEntity;
import org.springframework.stereotype.Repository;

/**
 * Created by DL on 2018/9/12.
 */
@Repository
public interface ClassToTkLogDao {

    void save(ClassToTkLogEntity classToTkLog);
}
