package io.renren.dao;

import java.util.List;
import java.util.Map;

import io.renren.entity.ExaminationResultEntity;
import io.renren.pojo.ExaminationResultPOJO;

/**
 * 统考成绩
 * 
 * @author hq
 * @email hq@hq.com
 * @date 2018-08-07 09:21:13
 */
public interface ExaminationResultDao{
	/**
	 * 批量更新状态
	 */
	int updateBatch(Map<String, Object> map);
	
	public ExaminationResultPOJO queryObject(Map<String, Object> map);
	
	public List<ExaminationResultPOJO> queryList(Map<String, Object> map);
	
	public int queryTotal(Map<String, Object> map);
	
	public void save(ExaminationResultEntity examinationResult);
	
	public void update(ExaminationResultEntity examinationResult);
	
	public void delete(Map<String, Object> map);
	
	public void deleteBatch(Map<String, Object> map);
	
	public int getRegistrationNum(Map<String, Object> map);
}
