package io.renren.service;

import io.renren.entity.SysRoleDeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色与组织对应关系
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-03 17:46:54
 */
public interface SysRoleDeptService {
	
		
	SysRoleDeptEntity queryObject(Map<String, Object> map);
	
	List<SysRoleDeptEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysRoleDeptEntity sysRoleDept);
	
	void update(SysRoleDeptEntity sysRoleDept);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	/**
	 * 保存角色与部门对应的关系
	 * @param roleId 角色id
	 * @param deptIdList 部门id集合
	 */
	void saveOrUpdate(Long roleId, List<Long> deptIdList);
	
	/**
	 * 根据角色ID，获取部门ID列表	
	 */	
	List<Long> queryDeptIdList(Long roleId);	
}
