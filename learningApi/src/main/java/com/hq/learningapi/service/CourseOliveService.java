package com.hq.learningapi.service;


import com.hq.learningapi.pojo.CourseOlivePOJO;

import java.util.List;
import java.util.Map;

/**
 * 公开课
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-05-12 15:01:23
 */
public interface CourseOliveService {
	
	List<CourseOlivePOJO> queryPojoList(Map<String, Object> map);

	Map<String,Object> queryPojoObject(Long oliveId);

	List<Map> queryMapList(Map<String, Object> map, String token);

	boolean checkAuthority(int authorityId, Long userId);

}
