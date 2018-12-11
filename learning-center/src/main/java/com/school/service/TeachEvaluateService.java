package com.school.service;

import com.school.entity.TeachEvaluate;

import java.util.Map;

/**
 * Created by longduyuan on 2018/12/10 0010.
 */
public interface TeachEvaluateService {

    boolean check(Map<String,Object> map);

    void save(TeachEvaluate teachEvaluate);
}
