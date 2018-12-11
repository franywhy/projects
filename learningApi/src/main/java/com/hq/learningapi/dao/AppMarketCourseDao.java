package com.hq.learningapi.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hq.learningapi.pojo.AppMarketCoursePOJO;
import com.hq.learningapi.pojo.PcMarketCoursePOJO;
import com.hq.learningapi.pojo.PcMarketParentCoursePOJO;

public interface AppMarketCourseDao {

	List<Long> queryProductIdListByBisinessId(@Param("businessId")String businessId);

	List<AppMarketCoursePOJO> queryCourseList(@Param("productList")List<Long> productList);

	List<PcMarketParentCoursePOJO> queryParentCourseList(@Param("productList")List<Long> productList);

	List<PcMarketCoursePOJO> queryCourseListByParentId(@Param("parentId")Long parentId);

	List<AppMarketCoursePOJO> queryMostHotCourseList(@Param("productList")List<Long> productList);

}
