package com.hq.bi.search.service;


import com.hq.bi.search.entity.ErrorHistoryReportEntity;
import com.hq.bi.search.entity.PKIndexEvaluationEntity;
import com.hq.bi.search.entity.SchoolRollEvaluationEntity;
import com.hq.bi.search.entity.TeachNoteEvaluationEntity;
import com.hq.bi.search.util.PageQuery;

import java.util.List;

/**
 * description:
 * @author: hutao
 * @date 2018/12/14 11:42
 */
public interface PKIndexManagerService {
    List<TeachNoteEvaluationEntity> getTeachNoteList(PageQuery pageQuery);

    List<ErrorHistoryReportEntity> getErrorHistoryList(PageQuery pageQuery);

    List<SchoolRollEvaluationEntity> getSchoolRollList(PageQuery pageQuery);

    List<PKIndexEvaluationEntity> getTeachingEvaluationList(PageQuery pageQuery);

    List<PKIndexEvaluationEntity> getClassStudyList(PageQuery pageQuery);
}
