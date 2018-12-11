package io.renren.service;


import java.util.List;
import java.util.Map;

import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.SysUserLaberPOJO;

/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-24 17:01:37
 */
public interface SysUserLaberService {
	
	SysUserLaberEntity queryObject(Long id);
	
	List<SysUserLaberEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(SysUserLaberEntity sysUserLaber);
	
	void update(SysUserLaberEntity sysUserLaber);
	
	/**
	 * 根据用户ID查询标签
	 */
	String queryLaberByUserId(Long userId);
	
	List<SysUserLaberPOJO> queryUserLaberList(Map<String, Object> map);
	
	/**
	 * 根据用户ID查询实体
	 */
	SysUserLaberPOJO queryPOJO(Long id);
	
	/**
	 * 批量冻结
	 */
	void stopUse(Long[] ids);
	
	/**
	 * 批量启用
	 */
	void startUse(Long[] ids);
	
	/**
	 * 批量删除
	 */
	void del(Long[] ids);
}
