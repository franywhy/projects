package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.GoodsCoursetkEntity;

public interface GoodsCoursetkDao {
	int queryTotalByCommodityId(@Param("commodityId")Long commodityId);

	List<String> queryCodeListByCommodityId(@Param("commodityId")Object commodityId);

	int queryTotalByMap(Map<String, Object> map);

	void save(GoodsCoursetkEntity goodsCoursetkEntity);

	void deleteByCommodityId(@Param("commodityId")Long commodityId);
}
