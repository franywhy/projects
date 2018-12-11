package io.renren.service;

import java.util.List;

/**
 * 角色与产品对应关系
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-03 16:42:59
 */
public interface SysRoleProductService {
	
	/**
	 * 保存角色与产品的对应关系
	 * @param roleId 角色id
	 * @param productIdList 产品id集合
	 */
	void saveOrUpdate(Long roleId, List<Long> productIdList);
	
	/**
	 * 根据角色ID，获取产品线ID列表	
	 */
	List<Long> queryProductIdList(Long roleId);
		
}
