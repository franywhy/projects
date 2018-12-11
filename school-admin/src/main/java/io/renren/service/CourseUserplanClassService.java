package io.renren.service;

import io.renren.entity.CourseUserplanClassEntity;
import io.renren.pojo.CourseUserplanClassPOJO;
import io.renren.utils.UserPlanClassDetailException;

import java.util.List;
import java.util.Map;

/**
 * 学习计划
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-27 09:55:36
 */
public interface CourseUserplanClassService {
	String getCode();
	/**
	 * 批量生成学习计划-会计(通过排课查询课程,按照课程查询学员规划下该课程没有排过该课的学员全部排入该排课下)
	 * 2017-10月数学排课  查询数学课没有排过课程的学员规划,把这部分学员全部排入到该排课下
	 * @param classplanId	排课ID
	 * @param classId 
	 * @return	批量处理信息
	 */
	String addBatchByClassplanId(String classplanId , String classId, String schoolId,Long userId);
	/**
	 * 批量生成学习计划-自考(通过排课查询课程,按照课程和班级查询学员规划下该课程没有排过该课的学员全部排入该排课下)
	 * 如2017-10月数学排课  查询数学课没有排过课程的，并且在这个班级下的学员规划,把这部分学员全部排入到该排课下
	 * @param classId 班级id
	 * @param classplanId 排课id
	 * @param schoolId
	 * @param userId
	 * @return
	 */
	String addBatchByClassplanIdAndClassId(Long examScheduleId, String classId, String classplanId, String schoolId, Long userId);

	
	CourseUserplanClassEntity queryObject(Map<String, Object> map);
	
	List<CourseUserplanClassEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserplanClassPOJO courseUserplanClass) throws UserPlanClassDetailException;
	
	void update(CourseUserplanClassPOJO courseUserplanClass) throws UserPlanClassDetailException;
	
	void update(CourseUserplanClassEntity courseUserplanClassEntity) throws UserPlanClassDetailException;
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	/**
	 * 查询列表
	 * @param map
	 * @return
	 */
	List<CourseUserplanClassPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 根据id查询信息
	 * @param map
	 * @return
	 */
	CourseUserplanClassPOJO queryPojoObject(Map<String, Object> map);

	void pause(Long[] userplanClassIds);

	void resume(Long[] userplanClassIds);

	void accept(Long userplanClassId);

	void reject(Long userplanClassId);
	
    Integer queryUserClassPlanMid(String mId);

    void updateChangeByOrderNo(Long userplanId );

    //根据学员规划id获取学习计划id
    List<Long> queryCourseUserplanClass(Long userPlanId);
}
