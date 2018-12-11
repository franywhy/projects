package io.renren.service;

import io.renren.entity.CourseUserplanDetailEntity;

import java.util.List;
import java.util.Map;

/**
 * 学员规划-课程子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:39
 */
public interface CourseUserplanDetailService {
	
	CourseUserplanDetailEntity queryObject(Long userplanDetailId);
	
	List<CourseUserplanDetailEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserplanDetailEntity courseUserplanDetail);
	
	void update(CourseUserplanDetailEntity courseUserplanDetail);
	
	void delete(Long userplanDetailId);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Long[] userplanDetailIds);
	
	void pause(Long[] userplanDetailIds);
	
	void resume(Long[] userplanDetailIds);
	
	/**
	 * 根据商品ID查询课程列表
	 * @param Map key commodityId = 商品ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 * @param Map key examEn2 = true 考英语二  ; false 不考英语二
	 * @param Map key targetGrade = true:专业对口  false:专业不对口
	 */
	List<Map<String , Object>> courseListByCommodityId(Map<String, Object> map);
	/**
	 * 根据商品ID查询课程总数
	 * @param Map key commodityId = 商品ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 * @param Map key examEn2 = true 考英语二  ; false 不考英语二
	 * @param Map key targetGrade = true:专业对口  false:专业不对口
	 */
	int courseTotalByCommodityId(Map<String, Object> map);
	
	/**
	 * 根据学员规划ID查询课程列表
	 * @param Map key userplanId = 学员规划ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 * @param Map key examEn = true 考英语二  ; false 不考英语二
	 * @param Map key targetGrade = true:专业对口  false:专业不对口
	 * @param Map key courseIds = 课程ids(排除已选课程)
	 */
	List<Map<String , Object>> courseListByUserPlanId(Map<String, Object> map);
	/**
	 * 根据学员规划ID查询课程总数
	 * @param Map key userplanId = 学员规划ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 * @param Map key examEn = true 考英语二  ; false 不考英语二
	 * @param Map key targetGrade = true:专业对口  false:专业不对口
	 */
	int courseTotalByUserPlanId(Map<String, Object> map);
	
	List<CourseUserplanDetailEntity> courseUserPlanEntityLisrt(Map<String, Object> map);
	
	int deleteBatch(Map<String, Object> map);

	/**
	 * 根据学员规划id查询子表下拉列表
	 * @param map
	 * @return
	 */
	List<Map<String , Object>> queryListByUserplanId(Map<String, Object> map);

	/**
	 * 根据学员规划子表ID查询课程信息
	 * @param map
	 * @param userplanDetailId	子表ID
	 * @param schoolId			平台ID
	 * @return
	 */
	Map<String, Object> selectCourseNameByUserPlanDetailId(Map<String, Object> map);
	/**
	 * 获得学院规划子表ID数组
	 * @param map
	 * @param userplanDetailId	子表ID
	 * @param schoolId			平台ID
	 * @return
	 */
	Map<String, Object> queryOtherById(long id);
	
	String queryUserplanDetailId(Map<String,Object> map);
	
//	数据同步
	CourseUserplanDetailEntity queryOneByUserPlanId(Map<String,Object> map);
//	检测某区域下是否有相同的课程
	int checkAreaAndCourse(Map<String,Object> map);
	//根据学员规划ID查询课程数量 
	int queryTotalByUserplanId(Map<String, Object> map);
	
	/**
	 * 获取用户名称-因展视互动记录录播播放记录的时候缺少用户ID-此方法是根据用户昵称\观看记录\观看视频ID获取用户的ID
	 * @param backId		视频ID
	 * @param startTime		观看开始时间
	 * @param userName		用户昵称
	 * @return				用户ID
	 */
	List<Long> getUserId(String backId , Long startTime , String userName);
	
	/**
	 * 获取用户名称-因展视互动记直播记录的时候缺少用户ID-此方法是根据用户昵称\排课明细ID获取用户的ID
	 * @param classplanLivesId 排课明细ID
	 * @param userName		用户昵称
	 * @return				用户ID
	 */
	List<Long> getUserIdByClassplanLiveId(String classplanLiveId, String userName);
	
	/**
	 * 展示互动-安卓端-用户观看记录-获取排课信息
	 * @param back_id
	 * @param startTime
	 * @return
	 */
	Map<String , Object> getCourseClassPlanInfoGSync(String backId , Long startTime);
	/**
	 * 展示互动-安卓端-用户观看记录-获取用户ID
	 * @param classplan_id
	 * @param userName
	 * @return
	 */
	List<Long> getUserIdGSync(String classplanId , String userName);
	
	/**
	 * 根据学员规划id更新省份id
	 * @param userPlanId 学员规划id
	 * @param areaId 省份id
	 */
	void updateAreaId(Long userPlanId, Long areaId);
}