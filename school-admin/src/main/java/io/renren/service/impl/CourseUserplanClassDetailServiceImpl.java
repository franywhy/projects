package io.renren.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.CourseUserplanClassDetailDao;
import io.renren.entity.CourseUserplanClassDetailEntity;
import io.renren.pojo.CourseUserplanClassDetailPOJO;
import io.renren.service.CourseUserplanClassDetailService;
import io.renren.service.CourseUserplanDetailService;
import io.renren.utils.UserPlanClassDetailException;



@Service("courseUserplanClassDetailService")
public class CourseUserplanClassDetailServiceImpl implements CourseUserplanClassDetailService {
	@Autowired
	private CourseUserplanClassDetailDao courseUserplanClassDetailDao;
	@Autowired
	private CourseUserplanDetailService courseUserplanDetailService;
	
	@Override
	public CourseUserplanClassDetailEntity queryObject(Map<String, Object> map){
		return courseUserplanClassDetailDao.queryObject(map);
	}
	
	@Override
	public List<CourseUserplanClassDetailEntity> queryList(Map<String, Object> map){
		return courseUserplanClassDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseUserplanClassDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseUserplanClassDetailEntity courseUserplanClassDetail) throws UserPlanClassDetailException{
		checkOld(courseUserplanClassDetail);
		courseUserplanClassDetailDao.save(courseUserplanClassDetail);
	}
	
	@Override
	public void update(CourseUserplanClassDetailEntity courseUserplanClassDetail) throws UserPlanClassDetailException{
		checkOld(courseUserplanClassDetail);
		courseUserplanClassDetailDao.update(courseUserplanClassDetail);
	}
	
	@Override
	public void delete(Map<String, Object> map){
		courseUserplanClassDetailDao.delete(map);
	}
	
	@Override
	public void deleteBatch(Map<String, Object> map){
		courseUserplanClassDetailDao.deleteBatch(map);
	}

	/**
	 * 根据userplanClassId查询子表列表信息
	 */
	@Override
	public List<CourseUserplanClassDetailPOJO> queryListByUserplanClassId(Map<String, Object> map2) {
		return this.courseUserplanClassDetailDao.queryListByUserplanClassId(map2);
	}

	/**
	 * 删除ID不等于userplanClassDetailIds的数据
	 * @param userplanClassDetailIds = id数组
	 * @param userplanClassId = 学习计划PK
	 */
	@Override
	public void deleteBatchNotIn(Map<String, Object> map) {
		this.courseUserplanClassDetailDao.deleteBatchNotIn(map);
	}
	
	private void checkOld(CourseUserplanClassDetailEntity courseUserplanClassDetail) throws UserPlanClassDetailException{
		Map<String , Object> map = selectOld(courseUserplanClassDetail);
		if(map != null){
			StringBuffer errorMessage = new StringBuffer();
			errorMessage.append("学习计划:");
			errorMessage.append(map.get("userplanClassNo"));
			errorMessage.append("中[");
			//TODO 课程信息
			Map<String , Object> courseMap = new HashMap<>();
			courseMap.put("userplanDetailId", courseUserplanClassDetail.getUserplanDetailId());
			courseMap.put("schoolId", courseUserplanClassDetail.getSchoolId());
			Map<String, Object> selectCourse = courseUserplanDetailService.selectCourseNameByUserPlanDetailId(courseMap);
			errorMessage.append(selectCourse.get("courseName"));
			errorMessage.append("]已经有相同的排课!");
			
			//			StringBuffer errorMessage = (String) map.get("userplanClassNo");
			throw new UserPlanClassDetailException(errorMessage.toString());
		}
	}
	/**
	 * 查询学员同一门课程有没有排在同意个排课计划中
	 * @param classplanId			排课主键
	 * @param userplanDetailId		学员规划子表主键
	 * @param schoolId				平台ID
	 * @param userplanClassDetailId	当前学习计划子表主键(非必填)
	 * @return
	 */
	private Map<String , Object> selectOld(CourseUserplanClassDetailEntity courseUserplanClassDetail) {
		Map<String, Object> map = new HashMap<>();;
		map.put("classplanId", courseUserplanClassDetail.getClassplanId());
		map.put("userplanDetailId", courseUserplanClassDetail.getUserplanDetailId());
		map.put("schoolId", courseUserplanClassDetail.getSchoolId());
		map.put("userplanClassDetailId", courseUserplanClassDetail.getUserplanClassDetailId());
		return this.courseUserplanClassDetailDao.selectOld(map );
	}

	@Override
	public List<Map<String, Object>> queryUserplanClassDetailForQueue(Map<String, Object> map) {
		String millisecond=(String) map.get("millisecond");
		map.put("millisecond", millisecond);
		List<Map<String , Object>> list=courseUserplanClassDetailDao.queryUserplanClassDetailForQueue(map);
		return list;
	
	}

    @Override
    public void updateChangeByUserplanClassId(Long courseUserplanClassId) {
        courseUserplanClassDetailDao.updateChangeByUserplanClassId(courseUserplanClassId);
    }


}
