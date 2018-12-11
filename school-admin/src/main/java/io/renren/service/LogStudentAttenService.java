package io.renren.service;

import java.util.Date;
import java.util.List;

import io.renren.pojo.log.LogAttendaceClassTeacherVPOJO;
import io.renren.pojo.log.LogStudentAttenDetailInfo;
import io.renren.pojo.log.LogStudentAttentLiveLogDetails;

public interface LogStudentAttenService {
	
	/**
	 * 考勤统计-已班主任角色查询-每个学员某一时间段的信息
	 * @param classTeacherId	班主任ID
	 * @param classPlanId		排课ID
	 * @param areaId			省份ID
	 * @param classId			班级ID
	 * @param startDate			开始时间
	 * @param endDate			结束时间
	 * @param userId			学员ID
	 * @param mobile			学员手机
	 * @param userName			学员用户名称
	 * @return					学员出勤信息
	 */
	List<LogAttendaceClassTeacherVPOJO> queryLogStudentAttenList(
			Long classTeacherId , String classPlanId , 
			Long areaId , Long classId , 
			Date startDate , Date endDate,
			Long userId,String mobile , String userName, Long deptId,List<String> deptIdList
			);
	
	/**
	 * 考勤统计-按学员统计-学员考勤详情
	 * @param userplanId	学员规划ID-必填
	 * @param classplanId	排课ID-非必填
	 * @param attenType		出勤状态-1.已出勤 2.未出勤 其余的没有该查询条件-非必填
	 * @param startTime		时间范围
	 * @param endTime		时间范围
	 * @return				学员考勤详情列表
	 */
	List<LogStudentAttenDetailInfo> queryLogStudentAttenDetail(Long userplanId,String classplanId,Integer attenType , String startTime,String endTime);

    /**
     *考勤统计-按学员统计-查看日志
     * @param userplanId 学员规划ID-必填
     * @param startDateString 直播开始时间
     * @param endDateString 直播结束时间
     * @return
     */
    List<LogStudentAttentLiveLogDetails> queryLogStudentAttenLiveLogDetails(Long userplanId, String startDateString, String endDateString);
}
