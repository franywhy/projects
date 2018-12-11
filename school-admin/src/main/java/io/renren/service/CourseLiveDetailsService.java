package io.renren.service;

import io.renren.entity.CourseLiveDetailsEntity;

import java.util.List;
import java.util.Map;

/**
 * 课程直播课子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-30 11:47:50
 */
public interface CourseLiveDetailsService {
	
	CourseLiveDetailsEntity queryObject(Long liveId);
	
	List<CourseLiveDetailsEntity> queryList(Map<String, Object> map);
	
	List<CourseLiveDetailsEntity> queryListByCouresId(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseLiveDetailsEntity courseLiveDetails);
	
	void update(CourseLiveDetailsEntity courseLiveDetails);
	
	void delete(Long liveId);
	
	void deleteBatch(Long[] liveIds);
	
	void pause(Long[] liveIds);
	
	void resume(Long[] liveIds);
	
	/**
	 * 查询子表
	 */
	List<CourseLiveDetailsEntity> queryAll(Map<String , Object> map);
	/**
	 * 分页查询
	 * @param map
	 * @return
	 */
	List<CourseLiveDetailsEntity> queryPageList(Map<String , Object> map);
	/**
	 * 分页查询-总数
	 * @param map
	 * @return
	 */
	int queryPageTotal(Map<String , Object> map);
	/**
	 * 删除ID不等于liveIds的数据
	 * @param liveIds = id数组
	 * @param courseId = 课程PK
	 */
	void deleteBatchNotIn(Map<String , Object> map);
	
	/**
	 * 根据课程ID删除课程直播子表
	 * @param courseId
	 */
	void deleteByCourseId(Long courseId);
	
	//判断是否有班型的引用
		int checkClassType(long id);
}
