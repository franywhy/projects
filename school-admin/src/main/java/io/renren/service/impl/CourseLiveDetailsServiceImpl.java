package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.renren.entity.CourseLiveDetailsEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.CourseLiveDetailsDao;
import io.renren.service.CourseLiveDetailsService;
import io.renren.utils.ClassTypeUtils;
import io.renren.utils.Constant;



@Service("courseLiveDetailsService")
public class CourseLiveDetailsServiceImpl implements CourseLiveDetailsService {
	@Autowired
	private CourseLiveDetailsDao courseLiveDetailsDao;
	
	@Override
	public CourseLiveDetailsEntity queryObject(Long liveId){
		CourseLiveDetailsEntity courseLiveDetailsEntity = courseLiveDetailsDao.queryObject(liveId);
		if(null != courseLiveDetailsEntity){
			courseLiveDetailsEntity.setLiveClassTypeIds(ClassTypeUtils.out(courseLiveDetailsEntity.getLiveClassTypeIds()));
		}
		return courseLiveDetailsEntity;
//		return courseLiveDetailsDao.queryObject(liveId);
	}
	
	@Override
	public List<CourseLiveDetailsEntity> queryList(Map<String, Object> map){
		List<CourseLiveDetailsEntity> courseLiveDetailsEntities = courseLiveDetailsDao.queryList(map);
		if(null != courseLiveDetailsEntities && courseLiveDetailsEntities.size() > 0){
			for (CourseLiveDetailsEntity courseLiveDetailsEntity : courseLiveDetailsEntities) {
				courseLiveDetailsEntity.setLiveClassTypeIds(ClassTypeUtils.out(courseLiveDetailsEntity.getLiveClassTypeIds()));
			}
		}
		return courseLiveDetailsEntities;
//		return courseLiveDetailsDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseLiveDetailsDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseLiveDetailsEntity courseLiveDetails){
		if(null != courseLiveDetails){
			courseLiveDetails.setLiveClassTypeIds(ClassTypeUtils.in(courseLiveDetails.getLiveClassTypeIds()));
		}
		courseLiveDetailsDao.save(courseLiveDetails);
	}
	
	@Override
	public void update(CourseLiveDetailsEntity courseLiveDetails){
		if(null != courseLiveDetails){
			courseLiveDetails.setLiveClassTypeIds(ClassTypeUtils.in(courseLiveDetails.getLiveClassTypeIds()));
		}
		courseLiveDetailsDao.update(courseLiveDetails);
	}
	
	@Override
	public void delete(Long liveId){
		courseLiveDetailsDao.delete(liveId);
	}
	
	@Override
	public void deleteBatch(Long[] liveIds){
		courseLiveDetailsDao.deleteBatch(liveIds);
	}
	@Override
	public void pause(Long[] liveIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", liveIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		courseLiveDetailsDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] liveIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", liveIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		courseLiveDetailsDao.updateBatch(map);
	}
	
	/**
	 * 查询子表
	 * @param course_id	课程ID
	 * @param school_id	校区ID
	 */
	public List<CourseLiveDetailsEntity> queryAll(Map<String , Object> map){
		List<CourseLiveDetailsEntity> courseLiveDetailsEntities = courseLiveDetailsDao.queryAll(map);
		if(null != courseLiveDetailsEntities && courseLiveDetailsEntities.size() > 0){
			for (CourseLiveDetailsEntity courseLiveDetailsEntity : courseLiveDetailsEntities) {
				courseLiveDetailsEntity.setLiveClassTypeIds(ClassTypeUtils.out(courseLiveDetailsEntity.getLiveClassTypeIds()));
			}
		}
		return courseLiveDetailsEntities;
//		return courseLiveDetailsDao.queryAll(map);
	}
	/**
	 * 查询分页
	 * @param course_id	课程ID
	 * @param school_id	校区ID
	 */
	public List<CourseLiveDetailsEntity> queryPageList(Map<String , Object> map){
		List<CourseLiveDetailsEntity> courseLiveDetailsEntities = courseLiveDetailsDao.queryPageList(map);
		if(null != courseLiveDetailsEntities && courseLiveDetailsEntities.size() > 0){
			for (CourseLiveDetailsEntity courseLiveDetailsEntity : courseLiveDetailsEntities) {
				courseLiveDetailsEntity.setLiveClassTypeIds(ClassTypeUtils.out(courseLiveDetailsEntity.getLiveClassTypeIds()));
			}
		}
		return courseLiveDetailsEntities;
	}
	/**
	 * 分页查询-总数
	 * @param course_id	课程ID
	 * @param school_id	校区ID
	 */
	public int queryPageTotal(Map<String , Object> map){
		return courseLiveDetailsDao.queryPageTotal(map);
	}

	/**
	 * 删除ID不等于liveIds的数据
	 * @param liveIds = id数组
	 * @param courseId = 课程PK
	 */
	@Override
	public void deleteBatchNotIn(Map<String , Object> map) {
		courseLiveDetailsDao.deleteBatchNotIn(map);
	}

	@Override
	public void deleteByCourseId(Long courseId) {
		
	}

	@Override
	public List<CourseLiveDetailsEntity> queryListByCouresId(Map<String, Object> map) {
		List<CourseLiveDetailsEntity> courseLiveDetailsEntities = courseLiveDetailsDao.queryListByCouresId(map);
		if(null != courseLiveDetailsEntities && courseLiveDetailsEntities.size() > 0){
			for (CourseLiveDetailsEntity courseLiveDetailsEntity : courseLiveDetailsEntities) {
				courseLiveDetailsEntity.setLiveClassTypeIds(ClassTypeUtils.out(courseLiveDetailsEntity.getLiveClassTypeIds()));
			}
		}
		return courseLiveDetailsEntities;
//		return this.courseLiveDetailsDao.queryListByCouresId(map);
	}

	@Override
	public int checkClassType(long id) {
		return this.courseLiveDetailsDao.checkClassType(id);
	}
}
