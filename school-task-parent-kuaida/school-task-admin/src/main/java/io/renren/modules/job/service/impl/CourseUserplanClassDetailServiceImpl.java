package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.CourseUserplanClassDetailDao;
import io.renren.modules.job.service.CourseUserplanClassDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("courseUserplanClassDetailService")
public class CourseUserplanClassDetailServiceImpl implements CourseUserplanClassDetailService {
	@Autowired
	private CourseUserplanClassDetailDao courseUserplanClassDetailDao;
	


	@Override
	public List<Map<String, Object>> queryUserplanClassDetailForQueue(Map<String, Object> map) {
		String millisecond=(String) map.get("millisecond");
		map.put("millisecond", millisecond);
		List<Map<String , Object>> list=courseUserplanClassDetailDao.queryUserplanClassDetailForQueue(map);
		return list;
	
	}
	
	
}
