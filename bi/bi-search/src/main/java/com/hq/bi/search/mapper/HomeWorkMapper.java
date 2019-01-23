package com.hq.bi.search.mapper;

import com.hq.bi.search.entity.HomeWorkStaticEntity;
import com.hq.bi.search.util.PageQuery;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Description:
 * Autor: wl
 * Date: 2018-12-15
 */
@Mapper
public interface HomeWorkMapper {


    /**
     * 预习作业统计
     * @param pageQuery
     * @return
     */
    List<HomeWorkStaticEntity> getPreHomeWorkStaticList(PageQuery pageQuery);

    /**
     * 课后作业统计
     * @param pageQuery
     * @return
     */
    List<HomeWorkStaticEntity> getAfterClassStaticList(PageQuery pageQuery);
    ;

}
