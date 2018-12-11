package com.hq.learningapi.service.impl;


import com.hq.learningapi.config.LocalConfigEntity;
import com.hq.learningapi.dao.CourseOliveDao;
import com.hq.learningapi.dao.StudentCourseDao;
import com.hq.learningapi.pojo.CourseOlivePOJO;
import com.hq.learningapi.service.CourseOliveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Service("courseOliveService")
public class CourseOliveServiceImpl implements CourseOliveService {

	@Autowired
	private CourseOliveDao courseOliveDao;

	@Autowired
	private StudentCourseDao studentCourseDao;

	@Autowired
	private LocalConfigEntity config;

	@Override
	public List<CourseOlivePOJO> queryPojoList(Map<String, Object> map) {
		return this.courseOliveDao.queryPojoList(map);
	}

	@Override
	public Map<String,Object> queryPojoObject(Long oliveId) {
		return this.courseOliveDao.queryPojoObject(oliveId);
	}

	@Override
	public List<Map> queryMapList(Map<String, Object> map, String token) {
		List<Map> courseOliveList = courseOliveDao.queryMapList(map);
		if (courseOliveList != null && courseOliveList.size() > 0){
			for (Map courseOlive : courseOliveList) {
				//首页公开课点击详情H5
				courseOlive.put("detailsUrl",config.getApph5Url()+"/course?oliveId="+courseOlive.get("oliveId")+"&token="+token);
			}
		}
		return courseOliveList;
	}

	@Override
	public boolean checkAuthority(int authorityId, Long userId) {
		Set<String> ncCommodityIdList = studentCourseDao.queryNcCommodityIdList(userId);
		if(ncCommodityIdList == null || ncCommodityIdList.size() == 0) {
			return false;
		}
		Boolean hasAuthority = courseOliveDao.checkAuthority(authorityId,ncCommodityIdList);
		return hasAuthority == null ? false : hasAuthority;
	}
}
