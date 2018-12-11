package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import io.renren.entity.SysUserLaberEntity;
import io.renren.pojo.SysUserLaberPOJO;

/**
 * 标签管理员表
 * 
 * @author vince
 * @date 2018-05-24 17:01:37
 */
@Mapper
public interface SysUserLaberDao extends BaseDao<SysUserLaberEntity> {
	
	/**
	 * 根据用户ID查询标签
	 */
	String queryLaberByUserId(Long userId);
	
	/**
	 * 查询列表数据
	 */
	List<SysUserLaberPOJO> queryUserLaberList(Map<String, Object> map);
	
	/**
	 * 根据用户ID实体
	 */
	SysUserLaberPOJO queryPOJO(Long id);
	
	/**
	 * 批量更新
	 */
	void updateBatch(Map<String, Object> map);
	
	/**
	 * 批量删除
	 */
	void del(Map<String, Object> map);
}
