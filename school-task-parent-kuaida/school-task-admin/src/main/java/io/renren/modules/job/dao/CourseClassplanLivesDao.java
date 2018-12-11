package io.renren.modules.job.dao;


import io.renren.modules.job.entity.CourseClassplanLivesEntity;
import io.renren.modules.job.pojo.log.ClassplanLivePOJO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 排课计划直播明细子表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-08 09:55:51
 */
public interface CourseClassplanLivesDao extends BaseDao<CourseClassplanLivesEntity> {
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	/**
	 * 删除ID不等于classplanLiveIds的数据
	 * @param classplanLiveIds = id数组
	 * @param classplanId = 排课计划PK
	 */
	void deleteBatchNotIn(Map<String, Object> map);
	/**
	 * 授课老师校验
	 * @param classplanLivesEntity
	 * @return map
	 */
	Map<String, Object> checkTeacher(CourseClassplanLivesEntity classplanLivesEntity);
	/**
	 * 直播室校验
	 * @param classplanLivesEntity
	 * @return map
	 */
	Map<String, Object> checkStudio(CourseClassplanLivesEntity courseClassplanLives);
	/**
	 * 直播间校验
	 * @param classplanLivesEntity
	 * @return map
	 */
	Map<String, Object> checkLiveRoom(CourseClassplanLivesEntity courseClassplanLives);
	/**
	 * 同一课程多次排课校验
	 * @param courseClassplanLives
	 * @return
	 */
	Map<String, Object> checkCourseLive(CourseClassplanLivesEntity courseClassplanLives);
	/**
	 * 同一商品下的不同课程的课次校验
	 * @param courseClassplanLives
	 * @return
	 */
	Map<String, Object> checkOneCommodityCourseLive(CourseClassplanLivesEntity courseClassplanLives);
	/**
	 * 查询子表
	 */
	List<ClassplanLivePOJO> queryPojoList(Map<String, Object> map);
	//判断是否有班型的引用
	int checkClassType(long id);

	
	/**
	 * 根据课程id获取该课程下的课次
	 * @param courseId 课程id
	 * @param schoolId 平台id
	 * @return
	 */
	List<String> getCourseLives(
            @Param(value = "courseId") Long courseId,
            @Param(value = "schoolId") String schoolId);

	List<Map<String, Object>> queryByDate(Map<String, Object> map);

	Map<String, Object> queryByBackId(Map<String, Object> map);

	/**
	 * 直播明细列表
	 * @param map
	 * @return
	 */
	List<ClassplanLivePOJO> queryPojoList1(Map<String, Object> map);

	/**
	 * 直播明细详情
	 * @param map
	 * @return
	 */
	ClassplanLivePOJO queryObject1(Map<String, Object> map);

	int findMid(String Mid);

	void updateBackUrl(Map<String, Object> map);

	void updateBackUrlByClassplanliveId(Map<String, Object> map);

	String findClassplanliveId(String mId);

	CourseClassplanLivesEntity queryByClassPlanLiveId(Map<String, Object> map);

	/**
	 * 根据时间范围查询直播详情列表
	 * @param map 查询排课子表的参数
	 * @return 直播详情列表
	 */
	List<CourseClassplanLivesEntity> queryCourseClassplanLivesByDate(Map<String, Object> map);
	
	List<Map<String, Object>> queryListByClassplanId(Object classplanId);
	
}
