package io.renren.dao;

import io.renren.entity.MallAdjustAreaEntity;
import io.renren.pojo.MallAdjustAreaPOJO;

import java.util.List;
import java.util.Map;

/**
 * 特殊情况-转省份
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-09 18:00:13
 */
public interface MallAdjustAreaDao extends BaseMDao<MallAdjustAreaEntity> {
	List<MallAdjustAreaPOJO> queryPojoList(Map<String, Object> map);
}
