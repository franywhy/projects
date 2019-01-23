package com.hq.bi.search.service.impl;


import com.github.pagehelper.PageHelper;
import com.hq.bi.search.entity.TeachExamQualityEntity;
import com.hq.bi.search.mapper.TeachExamQualityMapper;
import com.hq.bi.search.service.TeachExamQualityService;
import com.hq.bi.search.util.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * description:教学考试质量评比服务实现
 * @author: wl
 * @date 2018/12/15 11:59
 */
@Service
public class TeachExamQualityServiceImpl implements TeachExamQualityService {

    @Autowired
    private TeachExamQualityMapper teachExamQualityMapper;

    @Override
    public List<TeachExamQualityEntity> getTeachExamQualityList(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<TeachExamQualityEntity> list = teachExamQualityMapper.getTeachExamQualityList(pageQuery);
        return list;
    }


}
