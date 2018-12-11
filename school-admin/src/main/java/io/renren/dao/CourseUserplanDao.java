package io.renren.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import io.renren.entity.CourseUserplanEntity;
import io.renren.pojo.CourseUserplanPOJO;

/**
 * 学员规划
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:16
 */
public interface CourseUserplanDao extends BaseDao<CourseUserplanEntity> {
	
	/**
	 * 查询学员规划下courseId课程没有排过课的学员规划信息
	 * @param courseId    课程Id
	 * @param classId
	 * @return
	 */
	List<Map<String,Object>> queryUserplanInfoNotClassplanByCourseId(@Param(value="courseId")Long courseId, @Param(value="classIds")String[] classIds);
	/**
	 * 查询学员规划下courseId课程和选定班级没有排过课的学员规划信息
	 * @param courseId 课程Id
	 * @param classId 班级Id
	 * @return
	 */
	List<Map<String, Object>> queryUserplanInfoNotClassplanByCourseIdAndClassId(@Param(value="courseId")Long courseId, @Param(value="classIds")String[] classIds);
	
	/**
	 * 查询 新的学员规划（会计产品线）
	 * @param ts
	 * @return
	 */
	List<Map<String,Object>> queryKJClassMessage(@Param(value="ts")String ts);
	/**
	 * 查询 新的学员规划（学来学往产品线）
	 * @param ts
	 * @return
	 */
	List<Map<String, Object>> queryXLXWClassMessage(@Param(value="ts")String ts);
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	List<Map<String,Object>> queryListMap(Map<String,Object> map);
	
	/**
	 * 查询某一条数据
	 * @param map
	 * @return
	 */
	Map<String,Object> queryObjectMap(Map<String,Object> map);
	
	
	/**
	 * 修改通用信息
	 * @param map
	 */
	void updateCommon(Map<String, Object> map);

	/**
	 * 弹框列表
	 * @param map
	 * @return
	 */
	List<CourseUserplanPOJO> queryList2(Map<String, Object> map);
	/**
	 * 根据排课计划id和班型ids获取要发送消息的人群
	 * @param classplanId 排课计划id
	 * @param classtypeIds 班型ids
	 * @param schoolId 平台id
	 * @return
	 */
	List<Long> getUsers(
			@Param(value="classplanId")String classplanId, 
			@Param(value="classtypeIds")String[] classtypeIds, 
			@Param(value="schoolId")String schoolId);
	
	//判断是否有班型的引用
		int checkClassType(long id);
		//判断是否有专业的引用
		 int checkProfession(long id);
	/**
	 * 根据订单id修改学员规划班级
	 * @param courseUserplan
	 */
	void updateByOrderId(CourseUserplanEntity courseUserplan);
	
	String queryUserPlanId(String orderId);
	/**
	 * 获取要发送消息的所有用户
	 * @param schoolId 平台id
	 * @return
	 */
	List<Long> getAllUsers(@Param(value="schoolId")String schoolId);
	/**
	 * 弹框总数
	 * @param map
	 * @return
	 */
	int queryTotal2(Map<String, Object> map);

	/**
	 * 根据订单id查学员规划
	 * @param orderId 订单id
	 * @return
	 */
	int queryUserplanByOrderId(@Param("orderId")Long orderId);

	/**
	 * 根据订单id更新学员规划相关字段
	 */
	int updateUserplanByOrderId(CourseUserplanEntity courseUserplan);

	/**
	 * 根据订单id查学员规划详情
	 * @param orderId 订单id
	 * @return
	 */
	CourseUserplanEntity queryUserplanObjectByOrderId(@Param("orderId")Long orderId);

	//修改问题订单的学员规划dr =1
	void deleteByOrderId(@Param("orderId")Long orderId);

	/**
	 * 通过商品id查询题刻课程编号
	 * @param object
	 * @return
	 */
	List<String> queryCodeListByCommodityId(@Param("goodId")Object object);

    List<Long> queryComnodityListByTkCode(String tkCode);
    //查询 一定时间商品新的学员规划（会计产品线）
    List<Map<String,Object>> queryKJClassMessageByGoods(Map<String, Object> mapQuery);

    //根据订单号查询学员规划详情id
    List<Map<String,Object>> queryUserplanDetailIdByOrderId(@Param("orderId")Long orderId, @Param("courseId")Long courseId);
}
