package io.renren.mongo.sync.dao;

import java.util.List;

import io.renren.mongo.sync.entity.CommodityEntity;
import io.renren.mongo.sync.entity.CourseTimeEntity;
import io.renren.mongo.sync.entity.PeriodEntity;

public interface ICourseTimesDao {
	
	/**
	 * 查询需要同步的课程
	 */
	public List<CourseTimeEntity> findCourseTime();
	
	/**
	 * 查询课时 根据课时ID
	 * @param periodId	课时ID
	 * @return
	 */
	public PeriodEntity findPeriodByPeriodId(String periodId);
	
	/**
	 * 查询需要同步的课程
	 */
	public List<CommodityEntity> findCommodity();
	
}
