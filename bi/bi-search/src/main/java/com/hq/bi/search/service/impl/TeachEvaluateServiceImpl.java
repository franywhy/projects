package com.hq.bi.search.service.impl;


import com.github.pagehelper.PageHelper;
import com.hq.bi.search.mapper.TeachEvaluateMapper;
import com.hq.bi.search.service.TeachEvaluateService;
import com.hq.bi.search.util.PageQuery;
import com.hq.bi.search.vo.LiveEvaluateVo;
import com.hq.bi.search.vo.RecordEvaluateVo;
import com.hq.bi.search.vo.TeachEvaluateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * <p>
 * 评价 服务实现类
 * </p>
 *
 * @author hzr
 * @since 2018-12-05
 */
@Service
public class TeachEvaluateServiceImpl implements TeachEvaluateService {

    @Autowired
    private TeachEvaluateMapper teachEvaluateMapper;

    @Override
    public List<TeachEvaluateVo> listEvaluate(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<TeachEvaluateVo> list = teachEvaluateMapper.queryEvaluate(pageQuery);
        return list;
    }

    @Override
    public List listLiveEvaluate(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<LiveEvaluateVo> list = teachEvaluateMapper.queryLiveEvaluate(pageQuery);
        return list;
    }

    @Override
    public List listRecordEvaluate(PageQuery pageQuery) {
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<RecordEvaluateVo> list = teachEvaluateMapper.queryRecordEvaluate(pageQuery);
        return list;
    }
}
