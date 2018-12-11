package io.renren.dao;

import io.renren.entity.SysDeptEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 部门管理
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-07-25 14:21:58
 */
public interface SysDeptDao extends BaseMDao<SysDeptEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 根据部门id删除部门
	 * @param deptId 部门id
	 */
	void delete(Long deptId);
	
	/**
	 * 根据部门id查询部门信息
	 * @param deptId 部门id
	 * @return
	 */
	SysDeptEntity queryObject(Long deptId);

	/**
	 * 根据ncId查询部门信息
	 * @param ncId
	 * @return
	 */
	SysDeptEntity queryObjectByNcId(@Param("ncId")String ncId);
	
	List queryTotalByNcCode(Map<String, Object> map);

	void deleteByNcId(@Param("ncId")String nc_id);
	
}
