package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysDeptDao;
import io.renren.entity.SysDeptEntity;
import io.renren.service.SysDeptService;
import io.renren.utils.Constant;



@Service("sysDeptService")
public class SysDeptServiceImpl implements SysDeptService {
	@Autowired
	private SysDeptDao sysDeptDao;
	
	@Override
	public SysDeptEntity queryObject(Map<String, Object> map){
		return sysDeptDao.queryObject(map);
	}
	
	@Override
	public List<SysDeptEntity> queryList(Map<String, Object> map){
		return sysDeptDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysDeptDao.queryTotal(map);
	}
	
	@Override
	public void save(SysDeptEntity sysDept){
		sysDeptDao.save(sysDept);
	}
	
	@Override
	public void update(SysDeptEntity sysDept){
		sysDeptDao.update(sysDept);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		sysDeptDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysDeptDao.deleteBatch(map);
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return sysDeptDao.queryDetpIdList(parentId);
	}

	@Override
	public void delete(Long deptId) {
		sysDeptDao.delete(deptId);
	}

	@Override
	public SysDeptEntity queryObject(Long deptId) {
		return sysDeptDao.queryObject(deptId);
	}

	@Override
	public SysDeptEntity queryObjectByNcId(String ncId) {
		return this.sysDeptDao.queryObjectByNcId(ncId);
	}

	@Override
	public List queryTotalByNcCode(Map<String, Object> map) {
		return this.sysDeptDao.queryTotalByNcCode(map);
	}

	@Override
	public void deleteByNcId(String nc_id) {
		this.sysDeptDao.deleteByNcId(nc_id);
		
	}
	
	
}
