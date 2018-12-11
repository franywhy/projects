package io.renren.dao;

import io.renren.entity.MaterialDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author Yuanjp
 * @date 2017-05-12 11:16:41
 */
public interface MaterialDetailDao extends BaseMDao<MaterialDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	List<MaterialDetailEntity> queryListForLay(Map<String, Object> map);

	int queryTotalForLay(Map<String, Object> map);

	int queryCount(MaterialDetailEntity materialDetail);
	
	List<Integer> queryMaterialDetailYesIds(Map<String, Object> map);
	
	List<MaterialDetailEntity> querymateriDetailYesList(Map<String, Object> map);
	
	List<MaterialDetailEntity> querymateriDetailNoList(Map<String, Object> map);
	
	int judgeIds(Map<String,Object> map);
	
	int queryYesTotal(Map<String, Object> map);
	
	int queryNoTotal();
	//查询已关联的个体
	MaterialDetailEntity queryObjectIsRe(Map<String, Object> map);
	int  queryCount(Map<String, Object> map);

}
