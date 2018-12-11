package io.renren.dao;

import io.renren.entity.MallClassEntity;
import io.renren.entity.MallProfessionEntity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 专业档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 16:15:19
 */
public interface MallProfessionDao extends BaseMDao<MallProfessionEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	void deleteClassType(Map<String, Object> map);
	/**
	 * 查询status=1 && dr = 0 的专业
	 * @param map
	 * @return
	 */
	List<MallProfessionEntity> queryList1(Map<String, Object> map);
	MallProfessionEntity queryObjectById(@Param("professionId")Long professionId);	
}
