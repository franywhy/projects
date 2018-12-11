package io.renren.dao;

import io.renren.entity.MallStudioEntity;

import java.util.List;
import java.util.Map;

/**
 * 直播室档案表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-22 11:10:34
 */
public interface MallStudioDao extends BaseDao<MallStudioEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	/**
	 * 直播室列表
	 * @param map
	 * @return
	 */
	List<MallStudioEntity> findStudioList(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的直播室列表
	 * @param map
	 * @return
	 */
	List<MallStudioEntity> queryList1(Map<String, Object> map);
	
	/**
	 * 查询status=1，dr=0的直播室列表总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);

}
