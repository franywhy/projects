package com.hq.bi.offline.task.mapper;

import com.hq.bi.offline.task.entity.ErrorHistoryReportEntity;
import com.hq.bi.offline.task.entity.PKIndexEvaluationEntity;

import java.util.List;

/**
 * Description:
 * @author: hutao
 * Date: 2018-12-11-9:14
 */
public interface PKIndexMapper {
    /**
     * 获取学习笔记列表数据
     * @return
     */
    List<PKIndexEvaluationEntity> getTeachNoteList();

    /**
     * 全量删除旧数据
     * @return
     */
    int deleteTeachNoteList();

    /**
     * 保存学习笔记列表数据
     * @param pkIndexEvaluationEntity
     * @return
     */
    int saveTeachNoteList(List<PKIndexEvaluationEntity> pkIndexEvaluationEntity);

    /**
     * 获取班级学籍评比列表数据
     * @return
     */
    List<PKIndexEvaluationEntity> getClassSchoolRollList();

    /**
     * 全量删除旧数据
     * @return
     */
    int deleteClassSchoolRollList();

    /**
     * 保存班级学习评比列表数据
     * @param pkIndexEvaluationEntity
     * @return
     */
    int saveClassSchoolRollList(List<PKIndexEvaluationEntity> pkIndexEvaluationEntity);

    /**
     * 获取教学评比明细列表数据
     * @return
     */
    List<PKIndexEvaluationEntity> getTeachingQualityList();

    /**
     * 全量删除旧数据
     * @return
     */
    int deleteTeachingQualityList();

    /**
     * 保存教学评比明细列表数据
     * @param pkIndexEvaluationEntity
     * @return
     */
    int saveTeachingQualityList(List<PKIndexEvaluationEntity> pkIndexEvaluationEntity);

    /**
     * 获取教学评比明细列表数据
     * @return
     */
    List<ErrorHistoryReportEntity> getErrorHistoryList();

    /**
     * 全量删除旧数据
     * @return
     */
    int deleteErrorHistoryList();

    /**
     * 保存教学评比明细列表数据
     * @param pkIndexEvaluationEntity
     * @return
     */
    int saveErrorHistoryList(List<ErrorHistoryReportEntity> pkIndexEvaluationEntity);

}
