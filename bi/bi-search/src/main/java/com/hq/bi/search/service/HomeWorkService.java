package com.hq.bi.search.service;


import com.hq.bi.search.entity.HomeWorkStaticEntity;
import com.hq.bi.search.util.PageQuery;

import java.util.List;

/**
 * description:作业服务
 * @author: wl
 * @date 2018/12/15 11:42
 */
public interface HomeWorkService {

    /**
     * 查询预习作业统计报表
     * @param pageQuery
     * @return 作业统计报表实体集合
     */
    List<HomeWorkStaticEntity> getPreHomeWorkStaticList(PageQuery pageQuery);

    /**
     * 查询课后作业统计报表
     * @param pageQuery
     * @return 作业统计报表实体集合
     */
    List<HomeWorkStaticEntity> getAfterClassStaticList(PageQuery pageQuery);

}
