package io.renren.service;

import io.renren.entity.MallGoodsDetailsEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品表格详细信息
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-29 10:11:55
 */
public interface MallGoodsDetailsService {
	
//	List<MallGoodsDetailsEntity> queryObject(Long id);
	
	List<MallGoodsDetailsEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallGoodsDetailsEntity mallGoodsDetails);
	
	void update(MallGoodsDetailsEntity mallGoodsDetails);
	
	void delete(Long id);
	
	void deleteBatch(Map<String, Object> map);
	
	void pause(Long[] ids);
	
	void resume(Long[] ids);
	
	/**
	 * 查询商品所销售的地区 
	 * @param map
	 * @return
	 */
	List<Map<String, Object>> queryAreaByGoodId(Map<String, Object> map);
	List<Map<String, Object>> queryListMap(Map<String, Object> map);

	/**
	 * 删除ID不等于ids的数据
	 * @param ids = id数组
	 * @param mallGoodsId = PK
	 */
	void deleteBatchNotIn(Map<String, Object> map);

	/**
	 * 根据课程id确定该课程在哪些商品下
	 * @param courseId 课程id
	 * @param schoolId 平台id
	 * @return
	 */
	List<Long> getGoods(Long courseId, String schoolId);
	
	/**
	 * 根据商品id获取该商品下的课程
	 * @param goodsInfoId
	 * @param schoolId
	 * @return
	 */
	List<Long> getCourses(Long goodsInfoId, String schoolId);
	
	long getAreaIdByGoodsId(Map<String, Object> map);
	
	/**
	 * 查询商品某一个省份下的课程
	 * @param map schoolId:校区PK id:商品ID areaId:省份ID
	 * @return
	 */
	List<MallGoodsDetailsEntity> selectAreaCouresList(Map<String, Object> map);
	
}
