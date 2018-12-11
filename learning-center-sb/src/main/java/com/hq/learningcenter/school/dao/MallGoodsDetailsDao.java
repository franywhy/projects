package com.hq.learningcenter.school.dao;

import java.util.List;

import com.hq.learningcenter.school.entity.MallGoodsDetailsEntity;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MallGoodsDetailsDao {
	
	List<MallGoodsDetailsEntity> queryCourseByCommodityId(@Param("commodityId") Long commodityId,
                                                          @Param("areaId") Long areaId,
                                                          @Param("dr") Integer dr,
                                                          @Param("ccCommodity") String ccCommodity);
}
