package com.hq.bi.search.service.impl;


import com.github.pagehelper.PageHelper;
import com.hq.bi.search.entity.ClassExamQualityEntity;
import com.hq.bi.search.mapper.ClassExamQualityMapper;
import com.hq.bi.search.service.ClassExamQualityService;
import com.hq.bi.search.util.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * description:班级考试质量评比服务实现
 * @author: wl
 * @date 2018/12/15 11:59
 */
@Service
public class ClassExamQualityServiceImpl implements ClassExamQualityService {

    @Autowired
    private ClassExamQualityMapper classExamQualityMapper;

    @Override
    public List<ClassExamQualityEntity> getClassExamQualityList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<ClassExamQualityEntity> list = classExamQualityMapper.getClassExamQualityList(pageQuery);
        return list;
    }


}
