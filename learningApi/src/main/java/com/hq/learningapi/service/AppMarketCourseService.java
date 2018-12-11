package com.hq.learningapi.service;

import java.util.List;

import com.hq.learningapi.pojo.AppMarketCoursePOJO;
import com.hq.learningapi.pojo.PcMarketParentCoursePOJO;

public interface AppMarketCourseService {

	List<Long> queryProductIdListByBisinessId(String businessId);

	List<AppMarketCoursePOJO> queryCourseList(List<Long> productList);

	List<PcMarketParentCoursePOJO> queryPcCourseList(List<Long> productList);

	List<AppMarketCoursePOJO> queryMostHotCourseList(List<Long> productList);

}
