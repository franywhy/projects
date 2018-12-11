package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.CourseClassplanDao;
import io.renren.modules.job.entity.CourseClassplanEntity;
import io.renren.modules.job.service.CourseClassplanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Transactional(readOnly = true)
@Service("courseClassplanService")
public class CourseClassplanServiceImpl implements CourseClassplanService {
	@Autowired
	private CourseClassplanDao courseClassplanDao;

	@Override
	public List<Map<String , Object>> queryClassPlanForQueue(Map<String, Object> map) {
		List<Map<String , Object>> list=courseClassplanDao.queryListMap(map);

//		List<Map<String, Object>> list = this.courseUserplanDetailDao.courseListByUserPlanId(map);
		String schoolId=(String) map.get("schoolId");
		String millisecond=(String) map.get("millisecond");
		if(null != list && list.size() > 0){
			for (Map<String, Object> map2 : list) {
				Map<String, Object> mapp=new HashMap<String, Object>();
				String id = (String) map2.get("id");
				mapp.put("id", id);
				mapp.put("schoolId", schoolId);
				mapp.put("millisecond", millisecond);
				Map<String, Object> map3=this.courseClassplanDao.queryOtherById(mapp);
				if(null != map3){
					map2.putAll(map3);
				}
			}
		}
		return list;
	}
	/**
	 * 查询排课 根据排课ID
	 */
	@Override
	public CourseClassplanEntity queryObjectByClassplanId(String classplanId) {
		return this.courseClassplanDao.queryObjectByClassplanId(classplanId);
	}
	@Override
	public List<Map<String, Object>> querySsCourseClassplanMessage(String millisecond) {
		
		return this.courseClassplanDao.querySsCourseClassplanMessage(millisecond);
	}
}
