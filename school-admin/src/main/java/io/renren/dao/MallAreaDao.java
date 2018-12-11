package io.renren.dao;

import io.renren.entity.MallAreaEntity;

import java.util.List;
import io.renren.pojo.SelectionItem;

import java.util.List;
import java.util.Map;

/**
 * 省份档案业务表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 16:58:35
 */
public interface MallAreaDao extends BaseDao<MallAreaEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MallAreaEntity> findAreaList();
	
	List<SelectionItem> querySelectionList(Map<String, Object> map);
	
	long getAreaIdByprovinceNane(Map<String, Object> map);
	
	int countAreaIdByprovinceNane(Map<String, Object> map);
	
	/**
	 * 查询状态为上架且审核状态为通过的商品的总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);

	Long queryAreaIdByName(Map<String, Object> areaMap);

    int countAreaIdByExamProvinceName(Map<String, Object> map);

    Long getAreaIdByExamProvinceName(Map<String, Object> map);
}
