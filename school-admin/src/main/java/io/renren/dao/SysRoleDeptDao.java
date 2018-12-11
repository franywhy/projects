package io.renren.dao;

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
public interface SysRoleDeptDao extends BaseMDao<SysRoleDeptEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 根据角色id，删除角色与部门对应关系
	 * @param roleId
	 */
	int delete(Object roleId);
	
	/**
	 * 保存角色与部门的关系
	 * @param map
	 */
	void save(Map<String, Object> map);
	
	/**
	 * 根据角色ID，获取部门ID列表
	 */
	List<Long> queryDeptIdList(Long roleId);
}
