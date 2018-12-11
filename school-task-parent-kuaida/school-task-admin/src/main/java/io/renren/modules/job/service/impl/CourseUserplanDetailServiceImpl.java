package io.renren.modules.job.service.impl;

import io.renren.modules.job.dao.CourseClassplanLivesDao;
import io.renren.modules.job.dao.CourseUserplanDetailDao;
import io.renren.modules.job.entity.CourseClassplanLivesEntity;
import io.renren.modules.job.service.CourseUserplanDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service("courseUserplanDetailService")
public class CourseUserplanDetailServiceImpl implements CourseUserplanDetailService {
	@Autowired
	private CourseUserplanDetailDao courseUserplanDetailDao;
	@Autowired
	private CourseClassplanLivesDao courseClassplanLivesDao;

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
}
