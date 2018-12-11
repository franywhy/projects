package com.school.dao;

import com.school.entity.TeachEvaluate;

import java.util.Map;

/**
 * Created by longduyuan on 2018/12/10 0010.
 */
public interface TeachEvaluateDao {

    Boolean check(Map<String,Object> map);

    int insert(TeachEvaluate teachEvaluate);

}
