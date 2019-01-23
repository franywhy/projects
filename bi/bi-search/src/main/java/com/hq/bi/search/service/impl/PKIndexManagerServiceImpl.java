package com.hq.bi.search.service.impl;


import com.github.pagehelper.PageHelper;
import com.hq.bi.search.entity.ErrorHistoryReportEntity;
import com.hq.bi.search.entity.PKIndexEvaluationEntity;
import com.hq.bi.search.entity.SchoolRollEvaluationEntity;
import com.hq.bi.search.entity.TeachNoteEvaluationEntity;
import com.hq.bi.search.mapper.PKIndexMapper;
import com.hq.bi.search.service.PKIndexManagerService;
import com.hq.bi.search.util.PageQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * description:
 * @author: hutao
 * @date 2018/12/14 11:59
 */
@Service
public class PKIndexManagerServiceImpl implements PKIndexManagerService {

    @Autowired
    private PKIndexMapper pkIndexMapper;

    @Override
    public  List<TeachNoteEvaluationEntity> getTeachNoteList(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<TeachNoteEvaluationEntity> list = pkIndexMapper.getTeachNoteList(pageQuery);
        return list;
    }

    @Override
    public  List<ErrorHistoryReportEntity> getErrorHistoryList(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<ErrorHistoryReportEntity> list = pkIndexMapper.getErrorHistoryList(pageQuery);
        return list;
    }

    @Override
    public  List<SchoolRollEvaluationEntity> getSchoolRollList(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<SchoolRollEvaluationEntity> list = pkIndexMapper.getSchoolRollList(pageQuery);
        return list;
    }

    @Override
    public  List<PKIndexEvaluationEntity> getTeachingEvaluationList(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<PKIndexEvaluationEntity> list = pkIndexMapper.getTeachingEvaluationList(pageQuery);
        return list;
    }

    @Override
    public  List<PKIndexEvaluationEntity> getClassStudyList(PageQuery pageQuery){
        PageHelper.startPage(pageQuery.getPageNum(), pageQuery.getPageSize(), pageQuery.getPageOrder());
        List<PKIndexEvaluationEntity> list = pkIndexMapper.getClassStudyList(pageQuery);
        return list;
    }

}
