package io.renren.service;

import io.renren.entity.CourseOliveEntity;
import io.renren.pojo.CourseOlivePOJO;

import java.util.Date;
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
	
	CourseOliveEntity queryObject(Map<String, Object> map);
	
	List<CourseOliveEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseOliveEntity courseOlive);
	
	void update(CourseOliveEntity courseOlive);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	List<CourseOlivePOJO> queryPojoList(Map<String, Object> map);

	CourseOlivePOJO queryPojoObject(Map<String, Object> map);

	void pause(Long[] oliveIds);

	void resume(Long[] oliveIds);

	void updateAppStatus(Long[] oliveIds, int appStatus);

	void updatePushStatus(Long oliveId, int pushStatus, Date pushTime, String pushMsgId, String pushFindMsgId, int appStatus);

	Map<String, Object> queryMsgMap(Long oliveId);
}
