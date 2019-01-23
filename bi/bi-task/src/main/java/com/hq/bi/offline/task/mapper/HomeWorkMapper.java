package com.hq.bi.offline.task.mapper;

import java.util.List;
import java.util.Map;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-13
 */
public interface HomeWorkMapper {
    
    



    /**
     * 获取作业明细数据
     * @return
     */
    List<Map> getHomeWorkStaticList();


    /**
     * 保存作业明细列表数据
     * @param list
     * @return
     */
    int saveHomeWorkStaticEntity(List<Map> list);

    /**
     * 获取作业明细列表数据
     *
     * @return
     */
    List<Map> getHomeWorkStaticEntity();

    /**
     * 删除全部作业明细列表数据
     *
     * @return
     */
    int deleteAllHomeWorkStaticEntity();


}
