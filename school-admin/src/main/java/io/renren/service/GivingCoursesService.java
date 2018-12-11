package io.renren.service;

import io.renren.entity.GivingCoursesEntity;
import io.renren.entity.MallGoodsInfoEntity;
import io.renren.pojo.GivingCoursesPOJO;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-08-31 15:08:46
 */
public interface GivingCoursesService {
	
		
	GivingCoursesEntity queryObject(Map<String, Object> map);
	
	List<GivingCoursesEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(GivingCoursesEntity givingCourses);
	
	void update(GivingCoursesEntity givingCourses);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);
	
	List<GivingCoursesPOJO> queryPojoList(Map<String, Object> map);

	/**
	 * 取得全部可以开通的组合班型
	 *@param map
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */ 
	Map<String, String> SetGroupGoodToRedis();
	/**
	 * 判断这个班型是否是组合班型
	 *@param ncId
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */
	boolean checkNcCommodity(String ncId);
	/**
	 * 从redis中取得可开通的商品id
	 *@return
	 *@param hasDr 是否取dr=1,true 为取dr=1
	 * @author lintf
	 * 2018年9月25日
	 */
	List<Long> getGroupGoodFromRedis(String ncId,boolean hasDr);
	/**
	 * 清空组合班型对照表
	 *@return
	 * @author lintf
	 * 2018年9月25日
	 */
	boolean DeleteGroupGoodFromRedis();
		
}
