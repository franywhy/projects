package com.hq.learningapi.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppTodayTotalCourseNumDao;
import com.hq.learningapi.service.AppTodayTotalCourseNumService;
import com.hq.learningapi.util.DateUtils;

/**
 * Created by LiuHai on 2017/12/29
 */

@Service("appTodayTotalCourseNumService")
public class AppTodayTotalCourseNumServiceImpl implements AppTodayTotalCourseNumService {
	@Autowired AppTodayTotalCourseNumDao appTodayTotalCourseNumDao;

	@Override
	public List<Long> queryUserplanIdListByUserId(Long userId) {
		return this.appTodayTotalCourseNumDao.queryUserplanIdListByUserId(userId);
	}
	
	@Override
	public Long queryClassTypeIdByUserplanId(Long userplanId) {
		return this.appTodayTotalCourseNumDao.queryClassTypeIdByUserplanId(userplanId);
	}
	
	@Override
	public List<Long> queryUserplanDetailIdListByUserplanId(Long userplanId) {
		return this.appTodayTotalCourseNumDao.queryUserplanDetailIdListByUserplanId(userplanId);
	}

	@Override
	public String queryClassplanIdByUserplanDetailId(Long userplanDetailId) {
		return this.appTodayTotalCourseNumDao.queryClassplanIdByUserplanDetailId(userplanDetailId);
	}

	@Override
	public int queryCourseNumByClassplanIdAndToday(String classplanId, String today, Long classTypeId) {
		return this.appTodayTotalCourseNumDao.queryCourseNumByClassplanIdAndToday(classplanId , today , classTypeId);
	}

	@Override
	public List<Map<String, Object>> getTodayTotalCourseNum(Long userId, String start_time, String end_time) {
		List<Map<String, Object>> resultList = new ArrayList<>();
		List<Long> userplanIdList = this.queryUserplanIdListByUserId(userId);
		
		List<String> strDateList = this.strDateList(start_time, end_time);
		if(null != userplanIdList && userplanIdList.size() > 0){
			for (String strDate : strDateList) {
				Map<String , Object> resultMap = new HashMap<>();
				
				int courseNum = 0;
				
				for (Long userplanId : userplanIdList) {
					Long classTypeId = this.queryClassTypeIdByUserplanId(userplanId);
					List<Long> userplanDetailIdList = this.queryUserplanDetailIdListByUserplanId(userplanId);
					for (Long userplanDetailId : userplanDetailIdList) {
						String classplanId = this.queryClassplanIdByUserplanDetailId(userplanDetailId);
						if(null != classplanId){
							int classplanDetailNum = this.queryCourseNumByClassplanIdAndToday(classplanId,strDate,classTypeId);
							courseNum+=classplanDetailNum;
						}
					}
				}
				resultMap.put("time",strDate);
				resultMap.put("num",courseNum);
				resultList.add(resultMap);
			}
			return resultList;
		}else{
			return null;
		}
	}
	
	private List<String> strDateList(String start_time, String end_time){
		List<String> strDateList = new ArrayList<String>();
		Integer startMilliSecond = DateUtils.transForMilliSecond(start_time);
		Integer endMilliSecond = DateUtils.transForMilliSecond(end_time);
		if(startMilliSecond <= endMilliSecond){
			while(startMilliSecond <= endMilliSecond){
				String strDate = DateUtils.transForDate(startMilliSecond);
				strDateList.add(strDate);
				startMilliSecond+=86400;
			}
			
			return strDateList;
		}else{
			String strDate = DateUtils.transForDate(startMilliSecond);
			strDateList.add(strDate);
			
			return strDateList;
		}
		
	}



}
