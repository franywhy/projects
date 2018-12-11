package io.renren.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import io.renren.pojo.log.LogStudentAttentLiveLogDetails;
import org.apache.ibatis.annotations.Param;

import io.renren.pojo.log.LogStudentAttenDetailInfo;

public interface AttendanceStatisticsDao {
	/**
	 * 学员考勤统计
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAttendanceList(Map<String, Object> map);
	/**
	 * 被统计考勤的学员总数
	 * @param map
	 * @return
	 */
	int queryAttendanceTotal(Map<String, Object> map);
	/**
	 * 被统计考勤学员列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryUserList(Map<String, Object> map);
	/**
	 * 单个学员在某个排课下的当前直播课次
	 * @param userId 学员id
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryLivesAndVideosList(Map<String, Object> map);
	/**
	 * 单个学员的直播出勤数
	 * @param object 班型id
	 * @param classplanLiveId 直播课次id
	 * @param userId 学员id
	 * @return
	 */
	int queryLiveLog(
			@Param(value="schoolId")Object schoolId, 
			@Param(value="classplanLiveId")Object classplanLiveId, 
			@Param(value="userId")Object userId);
	/**
	 * 该排课下的直播课次的录播ids
	 * @param map
	 * @return
	 */
	List<String> queryVideoIdsList(Map<String, Object> map);
	/**
	 * 单个学员的录播出勤数
	 * @param classTypeId 班型id
	 * @param classplanLiveId 直播课次id
	 * @param userId 学员id
	 * @return
	 */
	int queryVideoLog(
			@Param(value="schoolId")Object schoolId, 
			@Param(value="classplanLiveId")Object classplanLiveId, 
			@Param(value="userId")Object userId);
	/**
	 * 单个学员对应的班型
	 * @param userId 学员id
	 * @param userPlanId 排课计划id
	 * @param object 
	 * @return
	 */
	List<Long> queryClassTypeId(
			@Param(value="schoolId")Object schoolId,
			@Param(value="userId")Object userId, 
			@Param(value="userPlanId")Object userPlanId);
	/**
	 * 考勤详情
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryDetailsList(Map<String, Object> map);
	/**
	 * 考勤详情统计
	 * @param map
	 * @return
	 */
	int queryDetailsTotal(Map<String, Object> map);
	/**
	 * 学员信息
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryUserInfoList(Map<String, Object> map);
	/**
	 * 某个课次直播是否出勤
	 * @param map
	 * @return
	 */
	int isLiveAttendance(Map<String, Object> map);
	/**
	 * 某个课次录播是否出勤
	 * @param map
	 * @return
	 */
	int isBackAttendance(Map<String, Object> map);
	
	
	
	
	
	
	
	
	
	
	/** ********************************************* start:按照学员查询考勤报表  ********************************************* **/
	
	/**
	 * 按照学员查询考勤报表-查询学员在startTime和endTime有排课的数据 考勤统计报表用
	 * @param startTime			上课开始时间(非空)
	 * @param endTime			上课结束时间(非空)
	 * @param classTeacherId	班主任老师ID
	 * @param classplanId		排课ID
	 * @param areaId			省份ID
	 * @param classId			班级ID
	 * @param mobile			学员手机
	 * @param userName			学员昵称
	 * @param userId			学员ID
	 * @return
	 */
	List<io.renren.pojo.log.LogAttendanceClassTeacherPOJO> logStudentAttenUserClassplanList(
			@Param("startTime")Date startTime , @Param("endTime")Date endTime,
			@Param("classTeacherId")Long classTeacherId,@Param("classplanId")String classplanId,
			@Param("areaId")Long areaId,@Param("classId")Long classId,
			@Param("mobile")String mobile,@Param("userName")String userName,@Param("userId")Long userId,
			@Param("deptId")Long deptId,@Param("deptIdList")List<String> deptIdList);
	/**
	 * 按照学员查询考勤报表-学员规划详情
	 * @param userplanId
	 * @return
	 */
	io.renren.pojo.log.LogAttendaceClassTeacherVPOJO logStudentAttenUserPlanInfo(@Param("userplanId")Long userplanId);
	/**
	 * 按照学员查询考勤报表-考勤次数
	 * @param classplanIds 	学员规划List
	 * @param userplanId	班型ID ,1,
	 * @return
	 */
	int logStudentAttenClassplanLiveNum(
			@Param("classplanIds")List<String> classplanIds , @Param("classTypeId")String classTypeId,
			@Param("startTime")Date startTime , @Param("endTime")Date endTime
			);
	
	/**
	 * 按照学员查询考勤报表-考勤平均值
	 * @param classplanIds 	学员规划List
	 * @param userplanId	班型ID ,1,
	 * @return
	 */
	double logStudentAttenAvg(
			@Param("classplanIds")List<String> classplanIds , @Param("classTypeId")String classTypeId,
			@Param("startTime")Date startTime , @Param("endTime")Date endTime , @Param("userId")Long userId
			);
	/**
	 * 学员第一次开课时间
	 * 注:此处没有考虑学员报名时间
	 * 如果需要学员报名时间,例如 某排课已经开课,学员后续开课,则获取订单的创建时间,在该SQL中加入条件
	 * @param classTypeId	班型ID
	 * @param userplanId	学员规划ID
	 * @return
	 */
	Date logStudentAttenFisrtTime(
			@Param("classTypeId")String classTypeId,
			@Param("userplanId")Long userplanId);
	
	/** ********************************************* ↓按照学员查询考勤报表-学员考勤详情↓  ********************************************* **/
	/**
	 * 考勤报表-按学员统计-出勤详情 classplan_live_id 课程章节 直播时间 出勤时长 出勤率
	 * @param userId		学员ID
	 * @param userplanId	学员规划ID
	 * @param classplanId	排课ID
	 * @param classTypeId	班型ID
	 * @param startTime		开始时间
	 * @param endTime		结束时间
	 * @return 				LogStudentAttenDetailInfo
	 */
	List<LogStudentAttenDetailInfo> logStudentAttenDetailClassplanInfo(
			@Param(value="userId")Long userId ,
			@Param(value="userplanId")Long userplanId,
			@Param(value="classplanId")String classplanId,
			@Param("classTypeId")String classTypeId,
			@Param("startTime")String startTime , @Param("endTime")String endTime
			);
	/**
	 * 考勤报表-按学员统计-出勤详情-第一次出勤时间 
	 * @param userId	学员ID
	 * @param liveId	直播间ID
	 * @param videoId	回放视频ID
	 * @return			第一次出勤时间Long
	 */
	Long logStudentAttenDetailTime(
			@Param(value="userId")Long userId ,
			@Param(value="liveId")String liveId ,
			@Param(value="videoId")String videoId
			);
	/**
	 * 考勤报表-按学员统计-出勤详情-学员昵称
	 * @param userplanId	学员规划ID
	 * @return				学员名称(String)和学员ID
	 */
	io.renren.pojo.log.LogStudentAttenDetailUserInfoPOJO logStudentAttenDetailUserInfo(@Param(value="userplanId")Long userplanId);

    List<LogStudentAttentLiveLogDetails> queryLogStudentAttenLiveLogDetails(@Param(value="userplanId")Long userplanId, @Param(value="startDate")String startDateString, @Param(value="endDate")String endDateString);

    /** ********************************************* end:按照学员查询考勤报表  ********************************************* **/
}
