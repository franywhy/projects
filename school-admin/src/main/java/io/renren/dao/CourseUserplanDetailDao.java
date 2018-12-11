package io.renren.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.CourseUserplanDetailEntity;

/**
 * 学员规划-课程子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:39
 */
public interface CourseUserplanDetailDao extends BaseDao<CourseUserplanDetailEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
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
	 */
	List<Map<String , Object>> courseListByUserPlanId(Map<String, Object> map);
	/**
	 * 根据学员规划ID查询课程总数
	 * @param Map key userplanId = 学员规划ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 * @param Map key examEn2 = true 考英语二  ; false 不考英语二
	 * @param Map key targetGrade = true:专业对口  false:专业不对口
	 */
	int courseTotalByUserPlanId(Map<String, Object> map);
	/**
	 * 根据商品ID和地区ID 查询某商品下某一地区所有的课程===生成学员规划的子表课程
	 * @param Map key commodityId = 商品ID
	 * @param Map key areaId = 省份ID
	 * @param Map key schoolId = 平台ID
	 */
	List<Map<String , Object>> queryAllcourseListByCommodityId(Map<String, Object> map);
	
	
	List<CourseUserplanDetailEntity> courseUserPlanEntityLisrt(Map<String, Object> map);
	/**
	 * 根据学员规划id查询子表下拉列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryListByUserplanId(Map<String, Object> map);
	/**
	 * 根据学员规划子表ID查询课程信息
	 * @param map
	 * @param userplanDetailId	子表ID
	 * @param schoolId			平台ID
	 * @return
	 */
	Map<String, Object> selectCourseNameByUserPlanDetailId(Map<String, Object> map);
	
	Map<String, Object> queryOtherById(Map<String, Object> map);
	
	String queryUserplanDetailId(Map<String,Object> map);
	
//	数据同步
	CourseUserplanDetailEntity queryOneByUserPlanId(Map<String,Object> map);
//	检测某区域下是否有相同的课程
	int checkAreaAndCourse(Map<String,Object> map);
	//根据学员规划ID查询课程数量 
	int queryTotalByUserplanId(Map<String, Object> map);
	
	/**
	 * 展示互动-安卓端-用户观看记录-获取排课信息
	 * @param back_id
	 * @param startTime
	 * @return
	 */
	Map<String , Object> getCourseClassPlanInfoGSync(@Param("backId")String backId ,@Param("startTime")Long startTime);
	/**
	 * 展示互动-安卓端-用户观看记录-获取用户ID
	 * @param classplan_id
	 * @param userName
	 * @return
	 */
	List<Long> getUserIdGSync(@Param("classplanId")String classplanId ,@Param("userName")String userName);
	
	/**
	 * 根据学员规划id更新省份id
	 * @param userPlanId 学员规划id
	 * @param areaId 省份id
	 * @return
	 */
	int updateAreaId(@Param("userPlanId")Long userPlanId, @Param("areaId")Long areaId);
}
