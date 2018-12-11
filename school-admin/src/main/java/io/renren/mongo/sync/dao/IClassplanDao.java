package io.renren.mongo.sync.dao;

import java.util.List;

import io.renren.mongo.sync.entity.ClassplanEntity;
import io.renren.mongo.sync.entity.ClassplanLiveEntity;
import io.renren.mongo.sync.entity.CourseUserPlanClassEntity;
import io.renren.mongo.sync.entity.LearningRecords;
import io.renren.mongo.sync.entity.LiveRoomEntity;
import io.renren.mongo.sync.entity.QuserEntity;
import io.renren.mongo.sync.entity.SyncUsersEntity;
import io.renren.mongo.sync.entity.TeacherEntity;
import io.renren.mongo.sync.entity.UsersEntity;

public interface IClassplanDao {
	
	/**
	 * 查询mongodb排课信息
	 * @return
	 */
	public List<ClassplanEntity> findClassplanList();
	
	/**
	 * 查询mongodb排课课次信息
	 * @return
	 */
	public List<ClassplanLiveEntity> findClassplanLiveList(String id,boolean flag);
	
	/**
	 * 查询mongodb教师列表
	 * @return
	 */
	public List<TeacherEntity> findTeacherList();
	
	/**
	 * 查询mongodb直播间列表
	 * @return
	 */
	public List<LiveRoomEntity> findLiveRoomList();
	
	/**
	 * 查询mongodb直播间列表
	 * @return
	 */
	public List<CourseUserPlanClassEntity> findCourseUserPlanClassList();
	
	/**
	 * 查询mongodb用户tuid
	 * @return
	 */
	public UsersEntity findUsersTuId(int usersId);
	/**
	 * 查询mongodb用户username
	 * @return
	 */
	public QuserEntity findQUsersUserName(String tuid);
	/**
	 * 查询mongodb用户username
	 * @return
	 */
	public ClassplanEntity findCourseId(String classPlanId);
	/**
	 * 查询学员
	 * @return
	 */
	public List<SyncUsersEntity> findUsers();
	/**
	 * 查询mongoDB（learning_records）直播信息
	 * @return
	 */
	public List<LearningRecords> findLearningRecords(Integer type);
	/**
	 * 查询mongoDB排课明细所有信息
	 * @return
	 */
	public List<ClassplanLiveEntity> findAllClassplanLive();
	/**
	 * 根据查询mongoDB中learning_records.live_id信息
	 * @return
	 */
	public ClassplanLiveEntity findLiveId(String liveId);
	
}
