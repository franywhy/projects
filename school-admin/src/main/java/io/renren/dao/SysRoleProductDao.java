package io.renren.dao;

import io.renren.entity.SysRoleProductEntity;

import java.util.List;
import java.util.Map;

/**
 * 角色与产品对应关系
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-03 16:42:59
 */
public interface SysRoleProductDao extends BaseMDao<SysRoleProductEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 根据角色id，删除角色与产品对应关系
	 * @param roleId
	 */
	int delete(Object roleId);
	
	/**
	 * 保存角色与产品的关系
	 * @param map
	 */
	void save(Map<String, Object> map);

	/**
	 * 根据角色ID，获取产品ID列表
	 * @param roleId
	 * @return
	 */
	List<Long> queryProductIdList(Long roleId);
}
