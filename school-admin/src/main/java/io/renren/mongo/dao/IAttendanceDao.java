package io.renren.mongo.dao;

import java.util.List;

import io.renren.mongo.entity.AttendanceEntity;

public interface IAttendanceDao {

	public List<AttendanceEntity> findAllAttendance();
	
	public List<AttendanceEntity> findAttendanceToday();
}
