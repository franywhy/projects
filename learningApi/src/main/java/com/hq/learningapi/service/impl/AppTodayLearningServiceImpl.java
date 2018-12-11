package com.hq.learningapi.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.ServletRequestUtils;

import com.hq.learningapi.dao.CourseClassplanLivesDao;
import com.hq.learningapi.entity.CourseClassplanLivesEntity;
import com.hq.learningapi.service.AppTodayLearningService;
import com.hq.learningapi.util.DateUtils;

/**
 * Created by Zhaowenwei on 2018/1/22
 */

@Service("appTodayLearningService")
public class AppTodayLearningServiceImpl implements AppTodayLearningService {
	@Value("${tiku.homework-url}")
	private String url;
	
	@Autowired CourseClassplanLivesDao courseClassplanLivesDao;
	
	@Override
	public String jointURL(String token, String phaseId, String type, String courseFk,String classplanLiveId,HttpServletRequest request) {
		
		String businessId = ServletRequestUtils.getStringParameter(request, "businessId", "kuaiji");
		String params = "token="+token+"&phaseId="+phaseId+"&type="+type+"&courseFk="+courseFk+"&classplanLiveId="+classplanLiveId;
		if (type.equals("1") && StringUtils.isNotBlank(classplanLiveId)) {
			CourseClassplanLivesEntity courseClassplanLivesEntity = getCourseClassplanLives(classplanLiveId);
			if (null != courseClassplanLivesEntity) {
				if (DateUtils.getNow().getTime() > courseClassplanLivesEntity.getEndTime().getTime()) {
					String httpgetURL = url + "?" + params;
					return httpgetURL;
				}
			}
		} else if(type.equals("2")){
			String httpgetURL = url + "?" + params;
			return httpgetURL;
		}
		return null;
	}
	
	@Override
	public CourseClassplanLivesEntity getCourseClassplanLives(String classplanLiveId) {
		return courseClassplanLivesDao.queryObject(classplanLiveId);
	}

	

	

}
