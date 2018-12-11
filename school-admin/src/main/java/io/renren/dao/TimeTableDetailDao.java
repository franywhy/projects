package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.TimeTableDetailEntity;

public interface TimeTableDetailDao extends BaseDao<TimeTableDetailEntity>{

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param relatedCommodityId = PK
	 */
	public void deleteBatchNotIn(Map<String, Object> map);

}
