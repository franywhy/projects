package io.renren.dao;

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
public interface CourseLiveDetailsDao extends BaseDao<CourseLiveDetailsEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 查询子表
	 */
	public List<CourseLiveDetailsEntity> queryAll(Map<String , Object> map);
	/**
	 * 查询分页
	 */
	public List<CourseLiveDetailsEntity> queryPageList(Map<String , Object> map);
	public int queryPageTotal(Map<String , Object> map);
	
	/**
	 * 删除ID不等于liveIds的数据
	 * @param liveIds = id数组
	 * @param courseId = 课程PK
	 */
	public void deleteBatchNotIn(Map<String , Object> map);
	
	/**
	 * 根据课程ID删除课程直播子表
	 * @param courseId
	 */
	public void deleteByCourseId(Long courseId);
	
	List<CourseLiveDetailsEntity> queryListByCouresId(Map<String , Object> map);
	
	//判断是否有班型的引用
			int checkClassType(long id);
}
