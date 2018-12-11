package io.renren.dao;

import io.renren.entity.MallLevelEntity;
import io.renren.pojo.SelectionItem;

import java.util.List;
import java.util.Map;

/**
 * 学历层次表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-25 16:38:54
 */
public interface MallLevelDao extends BaseDao<MallLevelEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MallLevelEntity> findLevelList(Map<String, Object> map);
	
	List<SelectionItem> querySelectionList(String schoolId);
}
