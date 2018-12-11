package com.school.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.school.entity.CoursesEntity;
import com.school.entity.MallGoodsDetailsEntity;

@Repository
public interface MallGoodsDetailsDao {
	
	List<MallGoodsDetailsEntity> queryCourseByCommodityId(@Param("commodityId") Long commodityId,
														  @Param("areaId") Long areaId,
														  @Param("dr") Integer dr,
														  @Param("ccCommodity") String ccCommodity);
}
