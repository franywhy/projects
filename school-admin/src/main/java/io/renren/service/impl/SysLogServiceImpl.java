package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysLogDao;
import io.renren.entity.SysLogEntity;
import io.renren.service.SysLogService;
import io.renren.utils.Constant;



@Service("sysLogService")
public class SysLogServiceImpl implements SysLogService {
	@Autowired
	private SysLogDao sysLogDao;
	
	@Override
	public SysLogEntity queryObject(Map<String, Object> map){
		return sysLogDao.queryObject(map);
	}
	
	@Override
	public List<SysLogEntity> queryList(Map<String, Object> map){
		return sysLogDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysLogDao.queryTotal(map);
	}
	
	@Override
	public void save(SysLogEntity sysLog){
		sysLogDao.save(sysLog);
	}
	
	@Override
	public void update(SysLogEntity sysLog){
		sysLogDao.update(sysLog);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		sysLogDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysLogDao.deleteBatch(map);
	}
	
	
}
