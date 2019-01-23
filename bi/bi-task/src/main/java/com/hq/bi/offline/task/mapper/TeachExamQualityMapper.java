package com.hq.bi.offline.task.mapper;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-13
 */
public interface TeachExamQualityMapper {
    
    



    /**
     * 获取教学考试明细数据
     * @return
     *
     */
    List<Map> getTeachExamQualityList();


    /**
     * 保存教学考试评比明细列表数据
     * @param list
     * @return
     */
    int saveTeachExamQuality(List<Map> list);

    /**
     * 获取教学考试质量评比明细列表数据
     *
     * @return
     */
    List<Map> getTeachExamQualityEntity();

    /**
     * 删除全部教学考试质量评比明细列表数据
     *
     * @return
     */
    int deleteAllTeachExamQualityEntity();

    /**
     * 获取题库考试成绩用户关联表数据
     * @return
     *
     */
    List<Map> getTkExamRecordUserList();

    /**
     * 保存题库考试成绩用户关联表数据
     * @param list
     * @return
     */
    int saveTkExamRecordUserList(List<Map> list);

    /**
     * 删除全部题库考试成绩用户关联表数据
     *
     * @return
     */
    int deleteAllTkExamRecordUserEntity();

    /**
     * 获取全部蓝鲸课程模考考试成绩关联表数据
     * @return
     *
     */
    List<Map> getBwCourseTkExamRecordUserList();

    /**
     * 保存全部蓝鲸课程模考考试成绩关联表数据
     * @param list
     * @return
     */
    int saveBwCourseTkExamRecordUserList(List<Map> list);

    /**
     * 删除全部蓝鲸课程模考考试成绩关联表数据
     *
     * @return
     */
    int deleteBwCourseAllTkExamRecordUserEntity();



    /**
     * 获取蓝鲸课程国考成绩情况表
     * @return
     *
     */
    List<Map> getBwCourseExaminationRecordList();

    /**
     * 保存蓝鲸课程国考成绩情况表
     * @param list
     * @return
     */
    int saveBwCourseExaminationRecordList(List<Map> list);

    /**
     * 删除蓝鲸课程国考成绩情况表
     *
     * @return
     */
    int deleteBwCourseExaminationRecordEntity();


}
