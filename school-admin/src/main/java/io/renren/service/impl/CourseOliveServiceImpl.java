package io.renren.service.impl;

import io.renren.dao.CourseOliveAuthorityDao;
import io.renren.dao.CourseOliveDao;
import io.renren.entity.CourseOliveEntity;
import io.renren.pojo.CourseOlivePOJO;
import io.renren.service.CourseOliveService;
import io.renren.utils.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service("courseOliveService")
public class CourseOliveServiceImpl implements CourseOliveService {
	@Autowired
	private CourseOliveDao courseOliveDao;
	@Autowired
	private CourseOliveAuthorityDao courseOliveAuthorityDao;
	
	@Override
	public CourseOliveEntity queryObject(Map<String, Object> map){
		return courseOliveDao.queryObject(map);
	}
	
	@Override
	public List<CourseOliveEntity> queryList(Map<String, Object> map){
		return courseOliveDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseOliveDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseOliveEntity courseOlive){
		//创建时间
		courseOlive.setCreationTime(new Date());
		//修改时间
		courseOlive.setModifiedTime(courseOlive.getCreationTime());
		//初始化上架状态
		courseOlive.setAppStatus(0);
		//初始化推送状态
		courseOlive.setPushStatus(Constant.Push.NOT.getValue());
		courseOlive.setDr(Constant.DR.NORMAL.getValue());
		courseOliveDao.save(courseOlive);
	}
	
	@Override
	public void update(CourseOliveEntity courseOlive){
		courseOlive.setDr(Constant.DR.NORMAL.getValue());
		courseOliveDao.update(courseOlive);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseOliveDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseOliveDao.deleteBatch(map);
	}

	@Override
	public List<CourseOlivePOJO> queryPojoList(Map<String, Object> map) {
		List<CourseOlivePOJO> resultList = this.courseOliveDao.queryPojoList(map);
		for (CourseOlivePOJO entity:resultList) {
			String authorityName = courseOliveAuthorityDao.queryObjectByAuthorityId(entity.getAuthorityId()).getAuthorityName();
			entity.setAuthorityName(authorityName);
		}
		return resultList;
	}

	@Override
	public CourseOlivePOJO queryPojoObject(Map<String, Object> map) {
		CourseOlivePOJO pojo = this.courseOliveDao.queryPojoObject(map);
		String authorityName = courseOliveAuthorityDao.queryObjectByAuthorityId(pojo.getAuthorityId()).getAuthorityName();
		pojo.setAuthorityName(authorityName);
		return pojo;
	}

	@Override
	public void pause(Long[] oliveIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", oliveIds);
    	map.put("appStatus", Constant.Status.PAUSE.getValue());
    	courseOliveDao.updateBatch(map);
	}

	@Override
	public void resume(Long[] oliveIds) {
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", oliveIds);
    	map.put("appStatus", Constant.Status.RESUME.getValue());
    	courseOliveDao.updateBatch(map);
	}

	@Override
	public void updateAppStatus(Long[] oliveIds, int appStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("list", oliveIds);
		map.put("appStatus", appStatus);
		courseOliveDao.updateBatch(map);
	}

	@Override
	public void updatePushStatus(Long oliveId, int pushStatus, Date pushTime, String pushMsgId, String pushFindMsgId, int appStatus) {
		Map<String, Object> map = new HashMap<>();
		map.put("oliveId", oliveId);
		map.put("pushStatus", pushStatus);
		map.put("pushTime", pushTime);
		map.put("pushMsgId", pushMsgId);
		map.put("pushFindMsgId", pushFindMsgId);
		map.put("appStatus", appStatus);
		courseOliveDao.updatePushStatus(map);
	}

	@Override
	public Map<String, Object> queryMsgMap(Long oliveId) {
		return courseOliveDao.queryMsgMap(oliveId);
	}
}
