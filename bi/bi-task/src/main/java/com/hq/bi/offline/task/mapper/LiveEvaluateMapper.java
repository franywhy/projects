package com.hq.bi.offline.task.mapper;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Autor: hzr
 * Date: 2018-12-13
 */
public interface LiveEvaluateMapper {


    /**
     * 获取直播评价list
     * @return
     */
    List<Map> getLiveEvaluateList();


    /**
     * 保存教学评比明细列表数据
     * @param list
     * @return
     */
    int saveLiveEvaluate(List<Map> list);

    /**
     * 删除全部班级考试质量评比明细列表数据
     *
     * @return
     */
    int deleteLiveEvaluate();
}
