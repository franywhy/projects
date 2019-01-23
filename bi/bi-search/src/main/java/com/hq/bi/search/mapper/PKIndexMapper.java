package com.hq.bi.search.mapper;

import com.hq.bi.search.entity.ErrorHistoryReportEntity;
import com.hq.bi.search.entity.PKIndexEvaluationEntity;
import com.hq.bi.search.entity.SchoolRollEvaluationEntity;
import com.hq.bi.search.entity.TeachNoteEvaluationEntity;
import com.hq.bi.search.util.PageQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Autor: hutao
 * Date: 2018-12-11-9:14
 */
@Mapper
public interface PKIndexMapper   {

    List<TeachNoteEvaluationEntity> getTeachNoteList(PageQuery pageQuery);

    List<ErrorHistoryReportEntity> getErrorHistoryList(PageQuery pageQuery);

    List<SchoolRollEvaluationEntity> getSchoolRollList(PageQuery pageQuery);

    List<PKIndexEvaluationEntity> getTeachingEvaluationList(PageQuery pageQuery);

    List<PKIndexEvaluationEntity> getClassStudyList(PageQuery pageQuery);
}
