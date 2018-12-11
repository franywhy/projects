package io.renren.dao;

import io.renren.entity.CourseClassplanLivesChangeRecordEntity;
import io.renren.entity.CourseClassplanLivesEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 排课计划直播明细子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-08 09:55:51
 */
public interface CourseClassplanLivesChangeRecordDao extends BaseDao<CourseClassplanLivesChangeRecordEntity> {
    List<CourseClassplanLivesEntity> queryEntityList(@Param(value="classplanId")String classplanId , @Param(value="versionNo")int versionNo);
    List<CourseClassplanLivesChangeRecordEntity> queryPojoList(Map<String, Object> map2);
}
