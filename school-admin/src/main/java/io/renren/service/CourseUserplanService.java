package io.renren.service;

import java.util.List;
import java.util.Map;

import io.renren.entity.CourseUserplanEntity;
import io.renren.pojo.CourseUserplanPOJO;

/**
 * 学员规划
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-04-05 12:04:16
 */
public interface CourseUserplanService {
	
	/**
	 * 查询学员规划下courseId课程没有排过课的学员规划信息
	 * @param courseId    课程Id
	 * @param classId
	 * @return
	 */
	List<Map<String,Object>> queryUserplanInfoNotClassplanByCourseId(Long courseId, String[] classIds);
	/**
	 * 查询学员规划下courseId课程和选定班级没有排过课的学员规划信息
	 * @param courseId 课程Id
	 * @param classId 班级Id
	 * @return
	 */
	List<Map<String, Object>> queryUserplanInfoNotClassplanByCourseIdAndClassId(Long courseId, String[] classIds);
	/**
	 * 批量生成某一商品下的所有订单
	 * @param  commodityId 商品ID
	 * @param classId 
	 * @return  操作信息
	 */
	String saveUserplanBatch(Long commodityId, Long classId);
	/**
	 * 通过订单号生成学员规划
	 * @param orderId
	 */
	boolean saveByOrderId(Long orderId);
	
	CourseUserplanEntity queryObject(Long userPlanId);
	
	List<Map<String , Object>> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(CourseUserplanEntity courseUserplan);
	
	void update(CourseUserplanEntity courseUserplan);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	void pause(Long[] userPlanIds);
	
	void resume(Long[] userPlanIds);
	
	String queryUserPlanId(String orderId);
	
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
	public void updateMap(CourseUserplanEntity entity);
	/**
	 * 转省转专业旧方案
	 * 
	 */
	public boolean updateChange(Map<String,Object> map);
	
	/**
	 * 查询学员全部未生成学员规划的订单
	 * @param map
	 * @return
	 */
	List<Map<String , Object>> queryOrderForChangeUserplan(Map<String , Object> map);

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
	List<Long> getUsers(String classplanId, String[] classtypeIds, String schoolId);
	
	//判断是否有班型的引用
	int checkClassType(long id);
	//判断是否有专业的引用
	int checkProfession(long id);
	
	/**
	 * 根据订单id修改学员规划班级
	 * @param courseUserplan
	 */
	void updateByOrderId(CourseUserplanEntity courseUserplan);
	
	/**
	 * 获取要发送消息的所有用户
	 * @param schoolId 平台id
	 * @return
	 */
	List<Long> getAllUsers(String schoolId);
	/**
	 * 弹框总数
	 * @param map
	 * @return
	 */
	int queryTotal2(Map<String, Object> map);
	
	/**
	 * 转省转专业新方案
	 * @param map
	 */
	void updateChange1(Map<String, Object> map);

	/**
	 * 该根据订单id查学员规划
	 * @param orderId 订单id
	 * @return
	 */
	int queryUserplanByOrderId(Long orderId);

	/**
	 * 根据订单id更新学员规划相关字段
	 */
	void updateUserplanByOrderId(CourseUserplanEntity courseUserplan);

	/**
	 * 根据订单id查学员规划详情
	 * @param orderId 订单id
	 * @return
	 */
	CourseUserplanEntity queryUserplanObjectByOrderId(Long orderId);

	/**
	 * 通过订单id对学员规划软删除
	 * @param orderId
	 */
	void deleteByOrderId(Long orderId);
	
	/**
	 * 查询 新的学员规划（会计所有产品线）
	 * @param ts
	 * @return
	 */
	List<Map<String,Object>> queryKJClassMessage(String ts);
	/**
	 * 查询 新的学员规划（学来学往产品线）
	 * @param ts
	 * @return
	 */
	List<Map<String, Object>> queryXLXWClassMessage(String ts);
	
	/**
	 * 通过商品id查询题库课程编号
	 * @param object
	 * @return
	 */
	List<String> queryCodeListByCommodityId(Object object);
    /*
    @Description:修改问题订单的学员规划dr=1
    @Author:DL
    @Date:9:06 2017/12/7
    @params:
     * @param null
    */
    void updateChangeByOrderNo(Long orderId);
    //查询题库课程编号查询对应的商品
    List<Long> queryComnodityListByTkCode(String tkCode);

    //查询一定时间内的商品班级
    List<Map<String,Object>> queryKJClassMessage(Map<String, Object> mapQuery);

    List<Map<String,Object>> queryUserplanDetailIdByOrderId(Long orderId, Long courseId);
}
