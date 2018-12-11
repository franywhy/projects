package io.renren.service;

import io.renren.entity.AppCourseBannerEntity;
import io.renren.pojo.AppCourseBannerPOJO;

import java.util.List;
import java.util.Map;

/**
 * app课程banner档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-01-18 14:23:20
 */
public interface AppCourseBannerService {
	
		
	AppCourseBannerEntity queryObject(Map<String, Object> map);
	
	List<AppCourseBannerEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(AppCourseBannerEntity appCourseBanner);
	
	void update(AppCourseBannerEntity appCourseBanner);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	AppCourseBannerPOJO queryObjectPOJO(Map<String, Object> map);

	List<AppCourseBannerPOJO> queryListPOJO(Map<String, Object> map);
	
		
		
}
