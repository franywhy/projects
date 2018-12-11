package com.hq.learningapi.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 题库操作
 * @date 2018-02-02 15:37:32
 */
public interface TikuApiService {
	
	List<Map<String, Object>> getPhaseList(HttpServletRequest request,String token,String courseFk);
	
}
