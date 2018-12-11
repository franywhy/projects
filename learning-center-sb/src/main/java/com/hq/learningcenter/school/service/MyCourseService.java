package com.hq.learningcenter.school.service;

import java.util.Map;

import com.hq.learningcenter.school.pojo.UdeskPOJO;
import com.hq.learningcenter.school.pojo.UserInfoPOJO;
import com.hq.learningcenter.utils.ClientEnum;
import com.hq.learningcenter.school.entity.SysBusinessEntity;
import com.hq.learningcenter.school.pojo.CoursesPOJO;
import com.hq.learningcenter.school.pojo.RatePOJO;


public interface MyCourseService {
	
	SysBusinessEntity getSysBusiness(String businessId);
	
	Map<String,Object> getCourse(UserInfoPOJO user, String businessId, ClientEnum client);
	
	String getWxCode(Long userplanId, String businessId);
	
	UdeskPOJO getUdesk(UserInfoPOJO userInfo, Long userplanId, String businessId);
	
	RatePOJO getLiveRate(Long userplanId, String businessId, Long userId);
	
	RatePOJO getRecordRate(Long orderId, String businessId, Long userId);

    CoursesPOJO queryObject(Long courseId);
}
