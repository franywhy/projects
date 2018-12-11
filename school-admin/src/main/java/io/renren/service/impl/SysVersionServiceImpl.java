package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysVersionDao;
import io.renren.entity.SysVersionEntity;
import io.renren.service.SysVersionService;
import io.renren.utils.Constant;



@Service("sysVersionService")
public class SysVersionServiceImpl implements SysVersionService {
	@Autowired
	private SysVersionDao sysVersionDao;
	
	@Override
	public SysVersionEntity queryObject(Map<String, Object> map){
		return sysVersionDao.queryObject(map);
	}
	
	@Override
	public List<SysVersionEntity> queryList(Map<String, Object> map){
		return sysVersionDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysVersionDao.queryTotal(map);
	}
	
	@Override
	public void save(SysVersionEntity sysVersion){
		
		sysVersionDao.save(sysVersion);
	}
	
	@Override
	public void update(SysVersionEntity sysVersion){
		
		sysVersionDao.update(sysVersion);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		sysVersionDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysVersionDao.deleteBatch(map);
	}

	@Override
	public boolean checkSysVersion(Map<String, Object> map) {
		return sysVersionDao.checkSysVersion(map)>0;
	}
	
	
}
