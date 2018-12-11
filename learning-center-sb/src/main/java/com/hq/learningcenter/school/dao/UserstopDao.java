package com.hq.learningcenter.school.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hq.learningcenter.school.entity.UserstopEntity;
import com.hq.learningcenter.school.pojo.UserstopPOJO;

/**
 * 休学档案表
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-25 15:34:34
 */
@Repository
public interface UserstopDao {

	List<UserstopPOJO> quserList(@Param("userId")Long userId, @Param("productIdList")List<Long> productIdList);

	void update(@Param("id")Long id);

	void save(UserstopEntity userstopEntity);

	List<Map<String, Object>> queryClasstypeList(@Param("userId")Long userId, @Param("productIdList")List<Long> productIdList);

	List<Long> queryProductId(@Param("businessId")String businessId);

	Long queryProductIdByUserplanId(@Param("userplanId")Long userplanId);

}
