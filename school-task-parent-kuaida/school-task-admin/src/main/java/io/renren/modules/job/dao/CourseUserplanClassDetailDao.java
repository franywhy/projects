package io.renren.modules.job.dao;

import io.renren.modules.job.entity.CourseUserplanClassDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 学习计划排课详情
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-28 15:41:14
 */
public interface CourseUserplanClassDetailDao extends BaseMDao<CourseUserplanClassDetailEntity> {
	List<Map<String , Object>> queryUserplanClassDetailForQueue(Map<String, Object> map);}
