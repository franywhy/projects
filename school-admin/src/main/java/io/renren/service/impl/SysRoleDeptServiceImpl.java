package io.renren.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.renren.dao.SysRoleDeptDao;
import io.renren.entity.SysRoleDeptEntity;
import io.renren.service.SysRoleDeptService;
import io.renren.utils.Constant;



@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl implements SysRoleDeptService {
	@Autowired
	private SysRoleDeptDao sysRoleDeptDao;
	
	@Override
	public SysRoleDeptEntity queryObject(Map<String, Object> map){
		return sysRoleDeptDao.queryObject(map);
	}
	
	@Override
	public List<SysRoleDeptEntity> queryList(Map<String, Object> map){
		return sysRoleDeptDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return sysRoleDeptDao.queryTotal(map);
	}
	
	@Override
	public void save(SysRoleDeptEntity sysRoleDept){
		sysRoleDeptDao.save(sysRoleDept);
	}
	
	@Override
	public void update(SysRoleDeptEntity sysRoleDept){
		sysRoleDeptDao.update(sysRoleDept);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		sysRoleDeptDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		sysRoleDeptDao.deleteBatch(map);
	}

	@Override
	public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
		if(deptIdList.size() == 0){
			return ;
		}
		
		//先删除角色与部门的关系
		sysRoleDeptDao.delete(roleId);
		
		//保存角色与部门的关系
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("roleId", roleId);
		map.put("deptIdList", deptIdList);
		sysRoleDeptDao.save(map);
	}

	@Override
	public List<Long> queryDeptIdList(Long roleId) {
		return sysRoleDeptDao.queryDeptIdList(roleId);
	}
	
	
}
