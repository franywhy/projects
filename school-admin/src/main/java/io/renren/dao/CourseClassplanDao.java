package io.renren.dao;

import io.renren.entity.CourseClassplanEntity;
import io.renren.pojo.classplan.ClassplanPOJO;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface CourseClassplanDao extends BaseDao<CourseClassplanEntity> {
	/**
	 * 查询排课 根据排课ID
	 */
	CourseClassplanEntity queryObjectByClassplanId(@Param(value="classplanId")String classplanId);
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	ClassplanPOJO queryObject1(Object id);
	
	List<Map<String, Object>> queryListMap(Map<String, Object> map);

	/**
	 * 查询课程
	 */
	CourseClassplanEntity queryCourseForClassplan(Map<String, Object> queryCourse);

	/**
	 * 根据学员规划子表id查询排课计划下拉列表
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryListByUserplanDetailId(Map<String, Object> map);

	/**
	 * 审核
	 * @param map
	 */
	int audited(Map<String, Object> map);
	
	//判断是否有班型的引用
	int checkClassType(long id);
	
	/**
	 * 查询状态为正常并且审核状态为通过并且dr=0的排课
	 * @param map
	 * @return
	 */
	List<CourseClassplanEntity> queryList1(Map<String, Object> map);
	
	/**
	 * 查询状态为正常并且审核状态为通过并且dr=0的排课的总数
	 * @param map
	 * @return
	 */
	int queryTotal1(Map<String, Object> map);
	
	/**
	 * 根据排课id查询学员列表
	 * @param map
	 * @return
	 */
	List<Object> queryUserList(Map<String, Object> map);
	
	/**
	 * 根据排课id查询学员列表总数
	 * @param map
	 * @return
	 */
	int queryUserListTotal(Map<String, Object> map);
	int findCId(Map<String, Object> map);
	/**
	 * 修改资料库关联
	 * @param map
	 */
	void updateMaterial(Map<String, Object> map);
	Map<String, Object> queryOtherById(Map<String, Object> map);
}
