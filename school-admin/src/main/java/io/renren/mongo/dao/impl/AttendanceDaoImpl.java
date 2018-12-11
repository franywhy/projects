package io.renren.mongo.dao.impl;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renren.mongo.dao.IAttendanceDao;
import io.renren.mongo.entity.AttendanceEntity;
import io.renren.mongo.entity.CourseEntity;
import io.renren.utils.DateUtils;

@Repository("attendanceDao")
public class AttendanceDaoImpl extends MongodbBaseDao<AttendanceEntity> implements IAttendanceDao {

	

	@Override
	protected Class<AttendanceEntity> getEntityClass() {
		// TODO Auto-generated method stub
		return AttendanceEntity.class;
	}

	@Override
	public List<AttendanceEntity> findAllAttendance() {
		// TODO Auto-generated method stub
		Query query = new Query();
		
		List list = this.mongoTemplate.find(query, AttendanceEntity.class);
		
		return list;
	}

	@Override
	public List<AttendanceEntity> findAttendanceToday() {
		// TODO Auto-generated method stub
		Date date = new Date();
		date = DateUtils.getDateAfter(date, 1);
		String str = DateUtils.format(date, "yyyy-MM-dd");
		Query query = new Query();
		query.addCriteria(Criteria.where("attendance_time").gte(str+" 00:00:00").lte(str+" 23:59:59"));
		List list = this.mongoTemplate.find(query, AttendanceEntity.class);
		
		return list;
	}

	

	

}
