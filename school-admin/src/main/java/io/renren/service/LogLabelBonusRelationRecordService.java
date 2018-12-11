package io.renren.service;


import io.renren.entity.LogLabelBonusRelationRecordEntity;
import io.renren.pojo.LogLabelBonusRelationRecordPOJO;

import java.util.List;
import java.util.Map;

/**
 * 标签对应红包限额关系的修改记录表
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2018-08-29 11:08:11
 */
public interface LogLabelBonusRelationRecordService {
	
	LogLabelBonusRelationRecordEntity queryObject(Long id);
	
	List<LogLabelBonusRelationRecordEntity> queryList(Map<String, Object> map);

	int queryTotal(Map<String, Object> map);
	
	void save(LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord);
	
	void update(LogLabelBonusRelationRecordEntity logLabelBonusRelationRecord);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);

	List<LogLabelBonusRelationRecordPOJO> queryPOJOList(Map<String, Object> map);

	int queryPOJOListTotal(Map<String, Object> map);
}
