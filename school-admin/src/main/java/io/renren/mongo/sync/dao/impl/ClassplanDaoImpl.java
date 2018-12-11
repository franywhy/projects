package io.renren.mongo.sync.dao.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import io.renren.mongo.sync.dao.IClassplanDao;
import io.renren.mongo.sync.entity.ClassplanEntity;
import io.renren.mongo.sync.entity.ClassplanLiveEntity;
import io.renren.mongo.sync.entity.CourseUserPlanClassEntity;
import io.renren.mongo.sync.entity.LearningRecords;
import io.renren.mongo.sync.entity.LiveRoomEntity;
import io.renren.mongo.sync.entity.QuserEntity;
import io.renren.mongo.sync.entity.SyncUsersEntity;
import io.renren.mongo.sync.entity.TeacherEntity;
import io.renren.mongo.sync.entity.UsersEntity;

//@Repository("zkClassplanDao")
public class ClassplanDaoImpl extends MongoBaseDao implements IClassplanDao {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Override
	public List<ClassplanEntity> findClassplanList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0));
		List<ClassplanEntity> list = super.find(query, ClassplanEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public List<ClassplanLiveEntity> findClassplanLiveList(String id,boolean flag) {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0));
		query.addCriteria(Criteria.where("classplan_id").is(id));
		query.addCriteria(Criteria.where("live_start_time").ne(null));
		//根据live_start_time升序排列
		query.with(new Sort(Direction.ASC,"live_start_time"));
		if(!flag){
			//在分页查找第一条记录
			query.skip(0);
			query.limit(1);
		}else{
			//在分页查找第一条记录
			query.skip(0);
			query.limit(100);
		}
		List<ClassplanLiveEntity> list = super.find(query, ClassplanLiveEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public List<TeacherEntity> findTeacherList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0));
		List<TeacherEntity> list = super.find(query, TeacherEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public List<LiveRoomEntity> findLiveRoomList() {
		Query query = new Query();
//		query.addCriteria(Criteria.where("dr").is(0));
		List<LiveRoomEntity> list = super.find(query, LiveRoomEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public List<CourseUserPlanClassEntity> findCourseUserPlanClassList() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0));
		List<CourseUserPlanClassEntity> list = super.find(query, CourseUserPlanClassEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public UsersEntity findUsersTuId(int usersId) {
	   Query query = new Query();
	   query.addCriteria(Criteria.where("_id").is(usersId));
	   UsersEntity users=(UsersEntity) super.findOne(query, UsersEntity.class,zikaoTemplate);
	   return users;
	}

	@Override
	public QuserEntity findQUsersUserName(String tuid) {
		Query query = new Query();
		query.addCriteria(Criteria.where("tuid").is(tuid));
		QuserEntity qUsers=(QuserEntity) super.findOne(query, QuserEntity.class,zikaoTemplate);
		return qUsers;
	}

	@Override
	public ClassplanEntity findCourseId(String classPlanId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("_id").is(classPlanId).and("dr").is(0));
		ClassplanEntity classPlan=(ClassplanEntity) super.findOne(query, ClassplanEntity.class, zikaoTemplate);
		return classPlan;
	}

	@Override
	public List<SyncUsersEntity> findUsers() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0).and("status").is(true));
		List<SyncUsersEntity> syncUsers= super.find(query, SyncUsersEntity.class, zikaoTemplate);
		return syncUsers;
	}

	@Override
	public List<LearningRecords> findLearningRecords(Integer type) {
		logger.info("ClassplanDaoImpl findLearningRecords start type:{}" , type);
		Query query = new Query();
		/*if(null != type){
			query.addCriteria(Criteria.where("type").is(type));
		}*/
		List<LearningRecords> syncLiveLog= super.find(query,LearningRecords.class, zikaoTemplate);
		logger.info("ClassplanDaoImpl findLearningRecords end syncLiveLog.size():{}" , syncLiveLog.size());
		return syncLiveLog;
	}
	@Override
	public List<ClassplanLiveEntity> findAllClassplanLive() {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0));
		List<ClassplanLiveEntity> list = super.find(query, ClassplanLiveEntity.class, zikaoTemplate);
		return list;
	}

	@Override
	public ClassplanLiveEntity findLiveId(String liveId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("dr").is(0).and("_id").is(liveId));
		ClassplanLiveEntity classplanLive = (ClassplanLiveEntity) super.findOne(query, ClassplanLiveEntity.class, zikaoTemplate);
		return classplanLive;
	}
}
