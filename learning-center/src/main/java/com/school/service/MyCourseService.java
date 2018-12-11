package com.school.service;

import java.util.Map;

import com.school.entity.SysBusinessEntity;
import com.school.pojo.CoursesPOJO;
import com.school.pojo.RatePOJO;
import com.school.pojo.UdeskPOJO;
import com.school.pojo.UserInfoPOJO;
import com.school.utils.ClientEnum;


public interface MyCourseService {
	
	SysBusinessEntity getSysBusiness(String businessId);
	
	Map<String,Object> getCourse(UserInfoPOJO user, String businessId, ClientEnum client);
	
	String getWxCode(Long userplanId, String businessId);
	
	UdeskPOJO getUdesk(UserInfoPOJO userInfo, Long userplanId, String businessId);
	
	RatePOJO getLiveRate(Long userplanId, String businessId, Long userId);
	
	RatePOJO getRecordRate(Long orderId, String businessId, Long userId);

    CoursesPOJO queryObject(Long courseId);
}
