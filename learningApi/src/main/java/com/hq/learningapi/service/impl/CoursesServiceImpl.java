package com.hq.learningapi.service.impl;

import com.hq.learningapi.dao.CoursesDao;
import com.hq.learningapi.dao.MallOrderDao;
import com.hq.learningapi.entity.CoursesEntity;
import com.hq.learningapi.entity.MallOrderEntity;
import com.hq.learningapi.service.CoursesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("coursesService")
public class CoursesServiceImpl implements CoursesService {

	@Autowired
	private MallOrderDao mallOrderDao;

	@Autowired
	private CoursesDao coursesDao;
	
	@Override
	public CoursesEntity queryObject(Long courseId){
		return coursesDao.queryObject(courseId);
	}
	
	@Override
	public List<CoursesEntity> queryList(Map<String, Object> map){
		return coursesDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return coursesDao.queryTotal(map);
	}
	
	@Override
	public void save(CoursesEntity courses){
		coursesDao.save(courses);
	}
	
	@Override
	public void update(CoursesEntity courses){
		coursesDao.update(courses);
	}

	@Override
	public CoursesEntity queryObjectByCourseFk(Long courseId) {
		return coursesDao.queryObjectByCourseFk(courseId);
	}

	@Override
	public Set<String> queryCourseNoList(Long userId, String businessId) {
		try{
			List<MallOrderEntity> orderList =  mallOrderDao.queryOrderByUserId(userId, businessId);
			Set<String> courseNoSet = new HashSet<>();
			if(null != orderList && orderList.size() > 0){

				for(MallOrderEntity order : orderList){
					Set<String> courses = coursesDao.queryCourseByOrder(order.getCommodityId(), order.getAreaId(), 0, 1);
					if(null != courses && courses.size() > 0){
						courseNoSet.addAll(courses);
					}
				}
			}
			return courseNoSet;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
}
