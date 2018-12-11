package com.school.dao;

import com.school.entity.LcOffliveLogEntity;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-04-13 10:48:24
 */
@Repository
public interface LcOffliveLogDao {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);

	void save(LcOffliveLogEntity entity);

    Long queryUserIdByMobile(String userMobile);
}
