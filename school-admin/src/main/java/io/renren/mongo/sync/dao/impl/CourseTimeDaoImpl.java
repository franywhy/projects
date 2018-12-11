package io.renren.mongo.sync.dao.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renren.mongo.sync.dao.ICourseTimesDao;
import io.renren.mongo.sync.entity.CommodityEntity;
import io.renren.mongo.sync.entity.CourseTimeEntity;
import io.renren.mongo.sync.entity.PeriodEntity;
//@Repository("zkCourseTimeDao")
public class CourseTimeDaoImpl extends MongoBaseDao implements ICourseTimesDao{

	@Override
	public List<CourseTimeEntity> findCourseTime() {
		Query query = new Query();
//		query.addCriteria(Criteria.where("dr").is(0));
		List<CourseTimeEntity> list = super.find(query, CourseTimeEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public PeriodEntity findPeriodByPeriodId(String periodId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(periodId)).addCriteria(Criteria.where("dr").is(0));
		return (PeriodEntity) findOne(query, PeriodEntity.class, zikaoTemplate);
	}

	@Override
	public List<CommodityEntity> findCommodity() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0).and("type").ne(0).and("course_time_list").ne(null));
		List<CommodityEntity> list = super.find(query, CommodityEntity.class, zikaoTemplate);
		return list;
	}
	
}
