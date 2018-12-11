package com.hq.learningapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hq.learningapi.dao.AppMarketCourseDao;
import com.hq.learningapi.pojo.AppMarketCoursePOJO;
import com.hq.learningapi.pojo.PcMarketCoursePOJO;
import com.hq.learningapi.pojo.PcMarketParentCoursePOJO;
import com.hq.learningapi.service.AppMarketCourseService;

@Service("appMarketCourseService")
public class AppMarketCourseServiceImpl implements AppMarketCourseService {
	@Autowired
	private AppMarketCourseDao appMarketCourseDao;
	
	@Override
	public List<Long> queryProductIdListByBisinessId(String businessId) {
		return this.appMarketCourseDao.queryProductIdListByBisinessId(businessId);
	}

	@Override
	public List<AppMarketCoursePOJO> queryCourseList(List<Long> productList) {
		return this.appMarketCourseDao.queryCourseList(productList);
	}

	@Override
	public List<PcMarketParentCoursePOJO> queryPcCourseList(List<Long> productList) {
		List<PcMarketParentCoursePOJO> parentCourseList = this.appMarketCourseDao.queryParentCourseList(productList);
		if(null != parentCourseList && parentCourseList.size() > 0){ 
			for (PcMarketParentCoursePOJO pcMarketParentCoursePOJO : parentCourseList) {
				List<PcMarketCoursePOJO> courseList = this.appMarketCourseDao.queryCourseListByParentId(pcMarketParentCoursePOJO.getId());
				if(null != courseList && courseList.size() > 0){
					pcMarketParentCoursePOJO.setList(courseList);
				}
			}
			return parentCourseList;
		}else{
			return null;
		}
	}

	@Override
	public List<AppMarketCoursePOJO> queryMostHotCourseList(List<Long> productList) {
		return this.appMarketCourseDao.queryMostHotCourseList(productList);
	}

}
