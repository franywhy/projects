package io.renren.service;

import io.renren.entity.CourseClassplanEntity;
import io.renren.entity.CourseClassplanLivesEntity;
import io.renren.pojo.classplan.ClassplanPOJO;

import java.util.List;
import java.util.Map;

/**
 * 排课计划表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-06 10:45:58
 */
public interface CourseClassplanService {
	
	/**
	 * 查询排课 根据排课ID
	 */
	CourseClassplanEntity queryObjectByClassplanId(String classplanId);
	
	//CourseClassplanEntity queryObject(Map<String, Object> map);
	ClassplanPOJO queryObject1(Map<String, Object> map);
	
	List<CourseClassplanEntity> queryList(Map<String, Object> map);
	
	List<Map<String , Object>> queryListMap(Map<String, Object> map);
	
	List<Map<String , Object>> queryClassPlanForQueue(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(ClassplanPOJO courseClassplan);
	
	void update(ClassplanPOJO courseClassplan);

	void update(CourseClassplanEntity en,List<CourseClassplanLivesEntity> detatilList);
	
	void delete(Long classplanId);
	
	void deleteBatch(Map<String, Object> map);
	
	void pause(String[] classplanIds);
	
	void resume(String[] classplanIds);
	
	void end(String[] classplanIds);
	List<Map<String , Object>> addItem(Map<String, Object> map);
	String checkLive(ClassplanPOJO courseClassplan);

	/**
	 * 根据学员规划子表id查询排课计划下拉列表
	 * @param map
	 * @return
	 */
	List<Map<String , Object>> queryListByUserplanDetailId(Map<String, Object> map);

	/**
	 * 审核通过
	 * */
	void accept(String classplanId,Long userId);

	/**
	 * 审核未过
	 * */
	void reject(String classplanId);

	//判断是否有班型的引用
	int checkClassType(long id);
	
	/**
	 * 查询状态正常并且审核状态为通过并且dr=0的排课
	 * @param map
	 * @return
	 */
	List<CourseClassplanEntity> queryList1(Map<String, Object> map);
	
	/**
	 * 查询状态正常并且审核状态为通过并且dr=0的排课的总数
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
	//修改资料库
	public void updateMaterial(ClassplanPOJO courseClassplan);

	//反审核
    void unAudit(String classplanId);
}
