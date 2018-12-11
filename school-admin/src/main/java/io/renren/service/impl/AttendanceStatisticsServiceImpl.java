package io.renren.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.AttendanceStatisticsDao;
import io.renren.service.AttendanceStatisticsService;
@Service("attendanceStatisticsService")
public class AttendanceStatisticsServiceImpl implements AttendanceStatisticsService {
	@Autowired
	private AttendanceStatisticsDao attendanceStatisticsDao;
	
	@Override
	public List<Map<String, Object>> queryAttendanceList(Map<String, Object> map) {
		//被统计考勤的学员列表
		List<Map<String, Object>> userList =  attendanceStatisticsDao.queryUserList(map);
		for (Map<String, Object> user : userList) {
			//通过学员id和学员规划id获取单个学员对应的唯一班型
			List<Long> classTypeIds = attendanceStatisticsDao.queryClassTypeId(map.get("schoolId") , user.get("userId") , user.get("userPlanId"));
			Long classTypeId = (Long) classTypeIds.toArray()[0];
			map.put("classTypeId", classTypeId);
			map.put("dateTime", new Date());//当前时间
			
			//通过排课id和班型id获取该排课下的当前直播课次id
			List<Map<String, Object>> classplanLiveMap = attendanceStatisticsDao.queryLivesAndVideosList(map);
			int mustNum = classplanLiveMap.size();//当前应出勤数
			
			int liveNum = 0;//直播出勤数初始化
			int backNum = 0;//录播出勤数初始化
			for (Map<String, Object> classplanLive : classplanLiveMap) {
				//该课次的直播是否出勤
				if(attendanceStatisticsDao.queryLiveLog(map.get("schoolId") , classplanLive.get("classplanLiveId") , user.get("userId")) > 0){
					//单个学员的当前直播出勤数
					liveNum++;
				}
				else{//该课次的回放是否出勤
					if(attendanceStatisticsDao.queryVideoLog(map.get("schoolId") , classplanLive.get("classplanLiveId") , user.get("userId")) > 0){
						//单个学员的当前录播出勤数
						backNum++;
					}
					
				}
			}
			//缺勤数
			int absenteeismNum = mustNum - liveNum - backNum;
			if(mustNum > 0){
				//出勤率
				double attendanceRate1 = (double)(liveNum + backNum)/mustNum * 100;
				String attendanceRate = String.format("%.0f" , attendanceRate1) + "%";
				user.put("attendanceRate", attendanceRate);//出勤率
			}
			else{//如果应出勤为0，出勤率也直接为0
				String attendanceRate = "0%";
				user.put("attendanceRate", attendanceRate);//出勤率
			}
	
			user.put("mustNum", mustNum);//应出勤
			user.put("liveNum", liveNum);//直播出勤
			user.put("backNum", backNum);//录播出勤
			user.put("absenteeismNum", absenteeismNum);//缺勤
		}
		return userList;
	}

	@Override
	public int queryAttendanceTotal(Map<String, Object> map) {
		return this.attendanceStatisticsDao.queryAttendanceTotal(map);
	}

	@Override
	public List<Map<String, Object>> queryDetailsList(Map<String, Object> map) {
		//根据排课计划id获得该排课下的课次
		List<Map<String, Object>> DetailsList = this.attendanceStatisticsDao.queryDetailsList(map);
		//根据学员规划id获取该学员唯一的基本信息
		List<Map<String, Object>> userInfoList = this.attendanceStatisticsDao.queryUserInfoList(map);
		Map<String, Object> userInfo = (Map<String, Object>) userInfoList.toArray()[0];
		for (Map<String, Object> Details : DetailsList) {
			Details.put("registrationTime", userInfo.get("registrationTime"));//报名时间（即订单支付时间）
			Details.put("areaName", userInfo.get("areaName"));//省份
			Details.put("userName", userInfo.get("userName"));//学员姓名
			Details.put("mobile", userInfo.get("mobile"));//学员电话
			Details.put("levelName", userInfo.get("levelName"));//学员层次
			Details.put("professionName", userInfo.get("professionName"));//学员专业
			Details.put("classTypeName", userInfo.get("classTypeName"));//学员班型
			Details.put("teacherName", userInfo.get("teacherName"));//班主任
			int liveAttendance = 0;//直播出勤初始化
			int backAttendance = 0;//回放出勤初始化
			int isAttendance = 0;//是否出勤初始化
			map.put("classplanLiveId", Details.get("classplanLiveId"));
			//map.put("videoId", Details.get("videoId"));
			//根据课次id和学员id获取该节课直播是否出勤
			liveAttendance = this.attendanceStatisticsDao.isLiveAttendance(map);
			//根据课次的录播id和学员id获取该节课录播是否出勤
			backAttendance = this.attendanceStatisticsDao.isBackAttendance(map);
			Details.put("liveAttendance", liveAttendance);
			Details.put("backAttendance", backAttendance);
			Details.put("isAttendance", liveAttendance + backAttendance);//该课次是否出勤（直播或者录播有一个出勤即视为出勤）
			
		}
		return DetailsList;
	}

	@Override
	public int queryDetailsTotal(Map<String, Object> map) {
		return this.attendanceStatisticsDao.queryDetailsTotal(map);
	}
}
