package com.hq.bi.search.service;


import com.hq.bi.search.entity.ClassExamQualityEntity;
import com.hq.bi.search.entity.TeachNoteEvaluationEntity;
import com.hq.bi.search.util.PageQuery;

import java.util.List;

/**
 * description:班级考试质量评比服务
 * @author: wl
 * @date 2018/12/15 11:42
 */
public interface ClassExamQualityService {
    /**
     * 查询班级考试质量报表
     * @param pageQuery
     * @return 班级考试质量报表结果实体集合
     */
    List<ClassExamQualityEntity> getClassExamQualityList(PageQuery pageQuery);

}
