package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.NcCourseClassplanLogDao;
import io.renren.entity.NcCourseClassplanLogEntity;
import io.renren.service.NcCourseClassplanLogService;
import io.renren.utils.Constant;



@Service("ncCourseClassplanLogService")
public class NcCourseClassplanLogServiceImpl implements NcCourseClassplanLogService {
	@Autowired
	private NcCourseClassplanLogDao ncCourseClassplanLogDao;
	
	@Override
	public NcCourseClassplanLogEntity queryObject(Map<String, Object> map){
		return ncCourseClassplanLogDao.queryObject(map);
	}
	
	@Override
	public List<NcCourseClassplanLogEntity> queryList(Map<String, Object> map){
		return ncCourseClassplanLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return ncCourseClassplanLogDao.queryTotal(map);
	}
	
	@Override
	public void save(NcCourseClassplanLogEntity ncCourseClassplanLog){
		ncCourseClassplanLogDao.save(ncCourseClassplanLog);
	}
	
	@Override
	public void update(NcCourseClassplanLogEntity ncCourseClassplanLog){
		ncCourseClassplanLogDao.update(ncCourseClassplanLog);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		ncCourseClassplanLogDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		ncCourseClassplanLogDao.deleteBatch(map);
	}
	
	
}
