package com.school.service;

import java.util.Map;

import com.school.pojo.LiveInfoPOJO;
import com.school.pojo.UserInfoPOJO;

public interface LiveService {
	
	LiveInfoPOJO queryLiveInfo(String classplanLiveId, UserInfoPOJO user);
	
	Map<String,Object> queryReplayInfo(String classplanLiveId,UserInfoPOJO user);
	
	Map<String,Object> queryLiveUrl(String classplanLiveId,UserInfoPOJO user);
	
	Map<String,Object> queryReplayUrl(String classplanLiveId,UserInfoPOJO user);
	
	Map<String,Object> getZegoLiveUrl(String classplanLiveId,UserInfoPOJO user,String businessId);
	
	String verifyGenseeKey(String mapKey);

	String buildKValue(String uid);
}
