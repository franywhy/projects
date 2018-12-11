package io.renren.dao;

import io.renren.entity.NcCommodityOpenCourseInfoEntity;
import io.renren.pojo.NcCommodityOpenCourseInfoPOJO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 报读班型与公开课权限关系表 报读班型与公开课权限关系表1
 * 
 * @date 2018-10-30 15:54:24
 */
@Mapper
public interface NcCommodityOpenCourseInfoDao extends BaseMDao<NcCommodityOpenCourseInfoEntity> {

    List<NcCommodityOpenCourseInfoEntity> queryList(Map<String, Object> params);

    List<NcCommodityOpenCourseInfoPOJO> queryPOJOList(Map<String, Object> params);

    int queryTotal(Map<String, Object> params);

    void insert(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity);

    int update(NcCommodityOpenCourseInfoEntity ncCommodityOpenCourseInfoEntity);

    void deleteBatch(Long[] ids);

    NcCommodityOpenCourseInfoEntity queryObject(Integer id);

    NcCommodityOpenCourseInfoPOJO queryPOJOObject(Integer id);
}
