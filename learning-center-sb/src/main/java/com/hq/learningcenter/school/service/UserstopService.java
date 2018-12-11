package com.hq.learningcenter.school.service;

import java.util.List;
import java.util.Map;

import com.hq.learningcenter.school.entity.UserstopEntity;
import com.hq.learningcenter.school.pojo.UserstopPOJO;

public interface UserstopService {

	List<UserstopPOJO> quseryList(Long userId, List<Long> productIdList);

	void update(Long id);

	void save(UserstopEntity userstopEntity);

	List<Map<String, Object>> queryClasstypeList(Long userId, List<Long> productIdList);

	List<Long> queryProductId(String businessId);

	Long queryProductIdByUserplanId(Long userplanId);

}
