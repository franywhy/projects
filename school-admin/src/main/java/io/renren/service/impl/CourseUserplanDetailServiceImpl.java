package io.renren.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.renren.dao.CourseClassplanLivesDao;
import io.renren.dao.CourseUserplanDao;
import io.renren.dao.CourseUserplanDetailDao;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.entity.CourseUserplanDetailEntity;
import io.renren.service.CourseUserplanDetailService;
import io.renren.utils.Constant;



@Service("courseUserplanDetailService")
public class CourseUserplanDetailServiceImpl implements CourseUserplanDetailService {
	@Autowired
	private CourseUserplanDetailDao courseUserplanDetailDao;
	@Autowired
	private CourseUserplanDao courseUserplanDao;
	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;
	
	@Override
	public CourseUserplanDetailEntity queryObject(Long userplanDetailId){
		return courseUserplanDetailDao.queryObject(userplanDetailId);
	}
	
	@Override
	public List<CourseUserplanDetailEntity> queryList(Map<String, Object> map){
		return courseUserplanDetailDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return courseUserplanDetailDao.queryTotal(map);
	}
	
	@Override
	public void save(CourseUserplanDetailEntity courseUserplanDetail){
		courseUserplanDetailDao.save(courseUserplanDetail);
	}
	
	@Override
	public void update(CourseUserplanDetailEntity courseUserplanDetail){
		courseUserplanDetailDao.update(courseUserplanDetail);
	}
	
	@Override
	public void delete(Long userplanDetailId){
		courseUserplanDetailDao.delete(userplanDetailId);
	}
	
	@Override
	public void deleteBatch(Long[] userplanDetailIds){
		courseUserplanDetailDao.deleteBatch(userplanDetailIds);
	}
	@Override
	public void pause(Long[] userplanDetailIds){
		Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", userplanDetailIds);
    	map.put("status", Constant.Status.PAUSE.getValue());
		courseUserplanDetailDao.updateBatch(map);
	}
		
	@Override
	public void resume(Long[] userplanDetailIds){
	    Map<String, Object> map = new HashMap<String, Object>();
    	map.put("list", userplanDetailIds);
    	map.put("status", Constant.Status.RESUME.getValue());
		courseUserplanDetailDao.updateBatch(map);
	}

	@Override
	public List<Map<String, Object>> courseListByCommodityId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.courseListByCommodityId(map);
	}

	@Override
	public int courseTotalByCommodityId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.courseTotalByCommodityId(map);
	}

	@Override
	public List<Map<String, Object>> courseListByUserPlanId(Map<String, Object> map) {
//		return this.courseUserplanDetailDao.courseListByUserPlanId(map);
		List<Map<String, Object>> list = this.courseUserplanDetailDao.courseListByUserPlanId(map);
		String schoolId=(String) map.get("schoolId");
//		List<Map<String, Object>> list1 = new ArrayList<>();
		if(null != list && list.size() > 0){
			for (Map<String, Object> map2 : list) {
				Map<String, Object> mapp=new HashMap<String, Object>();
				Long id = (Long) map2.get("id");
				mapp.put("id", id);
				mapp.put("schoolId", schoolId);
				Map<String, Object> map3=this.courseUserplanDetailDao.queryOtherById(mapp);
				if(null != map3){
					map2.putAll(map3);
//					list1.add(map3);
				}
			}
		}
		return list;
	}
	
//	@Override
//	public List<Map<String, Object>> courseListByUserPlanId(Map<String, Object> map) {
////		return this.courseUserplanDetailDao.courseListByUserPlanId(map);
//		List<Map<String, Object>> list = this.courseUserplanDetailDao.courseListByUserPlanId(map);
//		List<Map<String, Object>> list1 = new ArrayList<>();
//		if(null != list && list.size() > 0){
//			for (Map<String, Object> map2 : list) {
//				Long id = (Long) map2.get("id");
//				Map<String, Object> map3=this.courseUserplanDetailDao.queryOtherById(id);
//				if(null != map3 && map.size() > 0){
//					list1.add(map3);
//				}
//			}
//		}
//		return list1;
//	}

	@Override
	public int courseTotalByUserPlanId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.courseTotalByUserPlanId(map);
	}

	@Override
	public List<CourseUserplanDetailEntity> courseUserPlanEntityLisrt(Map<String, Object> map) {
		return this.courseUserplanDetailDao.courseUserPlanEntityLisrt(map);
	}

	@Override
	public int deleteBatch(Map<String, Object> map) {
		return this.courseUserplanDetailDao.deleteBatch(map);
	}

	@Override
	public List<Map<String, Object>> queryListByUserplanId(Map<String, Object> map) {
		Map<String, Object> courseUserplanMap = courseUserplanDao.queryObjectMap(map);
		if(null != courseUserplanMap){
			Integer isRep = (Integer) courseUserplanMap.get("isRep");
			Integer isMatch = (Integer) courseUserplanMap.get("isMatch");
			//考英语二
			boolean examEn2  = true;
			//专业对口
			boolean targetGrade  = true;
			if(1 == isRep){
				examEn2 = false;
			}
			if(1 == isMatch){
				targetGrade = false;
			}
			map.put("examEn2", examEn2);
			map.put("targetGrade", targetGrade);
		}
		return this.courseUserplanDetailDao.queryListByUserplanId(map);
	}

	@Override
	public Map<String, Object> selectCourseNameByUserPlanDetailId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.selectCourseNameByUserPlanDetailId(map);
	}

	@Override
	public Map<String, Object> queryOtherById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String queryUserplanDetailId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.queryUserplanDetailId(map);
	}

	@Override
	public CourseUserplanDetailEntity queryOneByUserPlanId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.queryOneByUserPlanId(map);
	}

	@Override
	public void delete(Map<String, Object> map) {
		this.courseUserplanDetailDao.delete(map);
	}

	@Override
	public int checkAreaAndCourse(Map<String,Object> map) {
		return this.courseUserplanDetailDao.checkAreaAndCourse(map);
	}

	@Override
	public int queryTotalByUserplanId(Map<String, Object> map) {
		return this.courseUserplanDetailDao.queryTotalByUserplanId(map);
	}

	/**
	 * 获取用户名称-因展视互动记录录播播放记录的时候缺少用户ID-此方法是根据用户昵称\观看记录\观看视频ID获取用户的ID
	 * @param backId		视频ID
	 * @param startTime		观看开始时间
	 * @param userName		用户昵称
	 * @return				用户ID
	 */
	@Override
	public List<Long> getUserId(String backId, Long startTime, String userName) {
		List<Long> list = null;
		Map<String, Object> coursePlanMap = getCourseClassPlanInfoGSync(backId, startTime);
		if(null != coursePlanMap){
			//排课
			String classplanId = (String) coursePlanMap.get("classplanId");
			list = this.getUserIdGSync(classplanId, userName);
		}
		return list;
	}
	
	/**
	 * 获取用户名称-因展视互动记直播记录的时候缺少用户ID-此方法是根据用户昵称\排课明细ID获取用户的ID
	 * @param classplanLivesId 排课明细ID
	 * @param userName		用户昵称
	 * @return				用户ID
	 */
	@Override
	public List<Long> getUserIdByClassplanLiveId(String classplanLiveId, String userName) {
		List<Long> list = null;
		//Map<String, Object> coursePlanMap = getCourseClassPlanInfoGSync(backId, startTime);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("classplanLiveId", classplanLiveId);
		paramMap.put("dr", 0);
		CourseClassplanLivesEntity entity = courseClassplanLivesDao.queryByClassPlanLiveId(paramMap);
		if(null != entity){
			//排课
			String classplanId = entity.getClassplanId();
			list = this.getUserIdGSync(classplanId, userName);
		}
		return list;
	}

	/**
	 * 展示互动-安卓端-用户观看记录-获取排课信息
	 * @param back_id
	 * @param startTime
	 * @return
	 */
	@Override
	public Map<String, Object> getCourseClassPlanInfoGSync(String backId, Long startTime) {
		return this.courseUserplanDetailDao.getCourseClassPlanInfoGSync(backId, startTime);
	}

	/**
	 * 展示互动-安卓端-用户观看记录-获取用户ID
	 * @param classplan_id
	 * @param userName
	 * @return
	 */
	@Override
	public List<Long> getUserIdGSync(String classplanId, String userName) {
		return this.courseUserplanDetailDao.getUserIdGSync(classplanId, userName);
	}

	@Override
	public void updateAreaId(Long userPlanId, Long areaId) {
		this.courseUserplanDetailDao.updateAreaId(userPlanId, areaId);
	}

	
}
