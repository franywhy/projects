package com.hq.bi.offline.task.mapper;

import com.hq.bi.offline.task.entity.ClassExamQualityEntity;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-13
 */
public interface ClassExamQualityMapper {
    
    



    /**
     * 获取班级考试明细数据
     * @return
     */
    List<Map> getClassExamQualityList();


    /**
     * 保存教学评比明细列表数据
     * @param list
     * @return
     */
    int saveClassExamQuality(List<Map> list);

    /**
     * 获取班级考试质量评比明细列表数据
     *
     * @return
     */
    List<Map> getClassExamQualityEntity();

    /**
     * 删除全部班级考试质量评比明细列表数据
     *
     * @return
     */
    int deleteAllClassExamQualityEntity();
}
