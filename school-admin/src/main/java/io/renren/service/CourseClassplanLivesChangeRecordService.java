package io.renren.service;

import io.renren.entity.CourseClassplanLivesChangeRecordEntity;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.pojo.classplan.ClassplanLivePOJO;
import io.renren.utils.ClassplanLiveException;
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
public interface CourseClassplanLivesChangeRecordService {
	void save(CourseClassplanLivesChangeRecordEntity entity);
	List<CourseClassplanLivesEntity> queryEntityList(String classplanId ,int versionNo);
    List<CourseClassplanLivesChangeRecordEntity> queryPojoList(Map<String, Object> map2);
}
