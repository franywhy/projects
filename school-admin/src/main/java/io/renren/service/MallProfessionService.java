package io.renren.service;

import io.renren.entity.MallClassEntity;
import io.renren.entity.MallProfessionEntity;

import java.util.List;
import java.util.Map;

/**
 * 专业档案
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2017-04-21 16:15:19
 */
public interface MallProfessionService {
	
		
	MallProfessionEntity queryObject(Map<String, Object> map);
	MallProfessionEntity queryMID(Map<String, Object> map);
	
	List<MallProfessionEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(MallProfessionEntity mallProfession);
	
	void update(MallProfessionEntity mallProfession);
	
	void delete(Map<String, Object> map);
	
	void deleteBatch(Map<String, Object> map);

	void pause(Long[] professionIds);

	void resume(Long[] professionIds);
	
	void deleteClassType(Map<String, Object> map);

	/**
	 * 查询status=1 && dr = 0 的专业
	 * @param map
	 * @return
	 */
	List<MallProfessionEntity> queryList1(Map<String, Object> map);
	MallProfessionEntity queryObjectById(Long professionId);	
		
}
