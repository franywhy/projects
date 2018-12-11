package io.renren.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.pojo.log.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.AttendanceStatisticsDao;
import io.renren.service.LogStudentAttenService;
import io.renren.utils.ClassTypeUtils;
@Service("logStudentAttenService")
public class LogStudentAttenServiceImpl implements LogStudentAttenService {
	@Autowired
	private AttendanceStatisticsDao attendanceStatisticsDao;
	
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
	@Override
	public List<LogAttendaceClassTeacherVPOJO> queryLogStudentAttenList(
			Long classTeacherId, String classplanId, 
			Long areaId, Long classId,
			Date startDate, Date endDate, 
			Long userId,String mobile , String userName , Long deptId,List<String> deptIdList
			) {
		List<LogAttendaceClassTeacherVPOJO> logList = null;
		//1.找出时间范围内的 学员规划   查询条件:班主任、排课、省份、班级、入课时间、学员手机号码、学员姓名、排课
		//学员规划ID1,学员规划子表ID11,排课ID1
		//学员规划ID1,学员规划子表ID12,排课ID2
		//学员规划ID1,学员规划子表ID13,排课ID3
		//学员规划ID2,学员规划子表ID21,排课ID4
		List<LogAttendanceClassTeacherPOJO> userClassplanList = this.attendanceStatisticsDao.logStudentAttenUserClassplanList(startDate, endDate, classTeacherId, classplanId, areaId, classId, mobile, userName, userId, deptId,deptIdList);
		Map<String, LogAttendanceClassTeacherFormatPOJO> userplanFormatMap = getUserplanFormatMap(userClassplanList);
		if(null != userplanFormatMap){
			//结果集
			logList = new ArrayList<>();
			for (Map.Entry<String, LogAttendanceClassTeacherFormatPOJO> entry : userplanFormatMap.entrySet()) {  
//				key:学员规划ID ; value:学员规划子表ID+排课ID
				//学员对应的排课计划有的排课
				LogAttendanceClassTeacherFormatPOJO attendanceClassTeacherFormatPOJO = entry.getValue();
				//排课计划ID数组
				List<String> classplanIdList = attendanceClassTeacherFormatPOJO.getClassplanIdList();
				if(null != attendanceClassTeacherFormatPOJO && null != classplanIdList && !classplanIdList.isEmpty()){
					//学员规划ID
					Long userplanId = attendanceClassTeacherFormatPOJO.getUserplanId();
					//学员规划-详情
					//学员规划关联查询用户信息和商品信息(学员ID、姓名、开课时间、手机号、地区、班级、班主任)
					LogAttendaceClassTeacherVPOJO userPlanInfo = this.attendanceStatisticsDao.logStudentAttenUserPlanInfo(userplanId);
					if(null != userPlanInfo){
						//班型ID=> ,id,
						String classTypeIdString = ClassTypeUtils.ins(userPlanInfo.getClassTypeId());
						//第一次入课时间
						userPlanInfo.setStartTimeClassplanLive(this.attendanceStatisticsDao.logStudentAttenFisrtTime(classTypeIdString, userplanId));
						//考勤次数
						int liveNum = this.attendanceStatisticsDao.logStudentAttenClassplanLiveNum(classplanIdList, classTypeIdString,startDate, endDate);
						//考勤率总值
						double logStudentAttenSum = this.attendanceStatisticsDao.logStudentAttenAvg(classplanIdList, classTypeIdString,startDate, endDate, userPlanInfo.getUserId());
						
						//考勤次数
						userPlanInfo.setLiveNum(liveNum);
						//考勤平均值
						double logStudentAttenAvg = liveNum!=0?logStudentAttenSum/liveNum:0;
						userPlanInfo.setLogStudentAttenAvg(logStudentAttenAvg);
						logList.add(userPlanInfo);
					}
				}
			}
		}
		return logList;
	}
	//学员规划ID1,学员规划子表ID11,排课ID1
	//学员规划ID1,学员规划子表ID12,排课ID2
	//学员规划ID1,学员规划子表ID13,排课ID3
	//学员规划ID2,学员规划子表ID21,排课ID4
	public Map<String,LogAttendanceClassTeacherFormatPOJO> getUserplanFormatMap(List<LogAttendanceClassTeacherPOJO> userClassplanList){
		Map<String,LogAttendanceClassTeacherFormatPOJO> formatPOJOMap = null;
		if(null != userClassplanList && !userClassplanList.isEmpty()){
			formatPOJOMap = new HashMap<>();
			for (LogAttendanceClassTeacherPOJO classTeacherPOJO : userClassplanList) {
				//学员规划ID
				Long userplanId = classTeacherPOJO.getUserplanId();
				LogAttendanceClassTeacherFormatPOJO pojo = null;
				pojo = formatPOJOMap.get(String.valueOf(userplanId));
				if(null == pojo){
					pojo = new LogAttendanceClassTeacherFormatPOJO(userplanId);
				}
				pojo.getUserplanDetail().add(classTeacherPOJO);
				formatPOJOMap.put(String.valueOf(userplanId), pojo);
			}
		}
		return formatPOJOMap;
	}
	
	/**
	 * 考勤统计-按学员统计-学员考勤详情
	 * @param userplanId	学员规划ID-必填
	 * @param classplanId	排课ID-非必填
	 * @param attenType		出勤状态-1.已出勤 2.未出勤 其余的没有该查询条件-非必填
	 * @param startTime		时间范围
	 * @param endTime		时间范围
	 * @return				学员考勤详情列表
	 */
	@Override
	public List<LogStudentAttenDetailInfo> queryLogStudentAttenDetail(
			Long userplanId, String classplanId, Integer attenType, String startTime,
			String endTime) {
//		参数
		if(userplanId != null && startTime != null && endTime != null){
			//用户信息:用户ID、用户昵称、班型
			LogStudentAttenDetailUserInfoPOJO userInfoPOJO = this.attendanceStatisticsDao.logStudentAttenDetailUserInfo(userplanId);
			if(null != userInfoPOJO){
				//班型ID=> ,id,
				String classTypeIdString = ClassTypeUtils.ins(userInfoPOJO.getClasstypeId());
				//出勤详情 classplan_live_id 课程章节 直播时间 出勤时长 出勤率
				List<LogStudentAttenDetailInfo> detailClassplanInfoList = this.attendanceStatisticsDao.logStudentAttenDetailClassplanInfo(userInfoPOJO.getUserId(), userplanId, classplanId, classTypeIdString, startTime, endTime);
				if(null != detailClassplanInfoList && !detailClassplanInfoList.isEmpty()){
					for (LogStudentAttenDetailInfo detailClassplanInfo : detailClassplanInfoList) {
//						第一次出勤时间
//						Long logStudentAttenDetailTime = this.attendanceStatisticsDao.logStudentAttenDetailTime(userInfoPOJO.getUserId(), detailClassplanInfo.getLiveId(), detailClassplanInfo.getBackId());
//						//赋值-第一次出勤时间
//						if(null != logStudentAttenDetailTime){
//							detailClassplanInfo.setFirstWatchTime(new Date(logStudentAttenDetailTime));
//						}
						//赋值-学员昵称
						detailClassplanInfo.setUserName(userInfoPOJO.getUserName());
					}
					return detailClassplanInfoList;
				}
			}
		}
		return null;
	}

    @Override
    public List<LogStudentAttentLiveLogDetails> queryLogStudentAttenLiveLogDetails(Long userplanId, String startDateString, String endDateString) {
        return attendanceStatisticsDao.queryLogStudentAttenLiveLogDetails(userplanId,startDateString,endDateString);
    }

}
