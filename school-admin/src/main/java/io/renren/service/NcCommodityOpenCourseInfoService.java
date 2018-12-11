package io.renren.service;


import io.renren.entity.NcCommodityOpenCourseInfoEntity;
import io.renren.pojo.NcCommodityOpenCourseInfoPOJO;
import io.renren.utils.PageUtils;

import java.util.List;
import java.util.Map;

/**
 * 报读班型与公开课权限关系表
 *
 * @date 2018-10-30 15:54:24
 */
public interface NcCommodityOpenCourseInfoService{

    List<NcCommodityOpenCourseInfoPOJO> queryList(Map<String, Object> params);

    int queryTotal(Map<String, Object> params);

    void insert(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity);

    int update(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity);

    void deleteBatch(Map<String, Object> map);

    NcCommodityOpenCourseInfoEntity queryObject(Integer id);

    NcCommodityOpenCourseInfoPOJO queryPOJOObject(Integer id);


}

