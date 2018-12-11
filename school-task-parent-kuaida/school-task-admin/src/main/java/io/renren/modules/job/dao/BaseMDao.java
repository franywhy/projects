package io.renren.modules.job.dao;

import java.util.List;
import java.util.Map;

/**
 * 基础Dao(还需在XML文件里，有对应的SQL语句)
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年9月18日 上午9:31:36
 */
public interface BaseMDao<T> {

	/**
	 * @param map
	 * @param id
	 * @param schoolId
	 * @return
	 */
	T queryObject(Map<String, Object> map);
	
	T queryGoodsInfoId(Map<String, Object> map);
	
	T queryMID(Map<String, Object> map);

	List<Map<String, Object>> queryListMap(Map<String, Object> map);
	List<T> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	void save(T t);

	int update(T t);

	/**
	 * 
	 * @param map
	 * @param id
	 * @param schoolId
	 * @return
	 */
	int delete(Map<String, Object> map);

	/**
	 * @param map
	 * @param ids
	 * @param schoolId
	 * @return
	 */
	int deleteBatch(Map<String, Object> map);

	int liveLogExist(String mId);
	
	int videoLogExist(String mId);	

}
